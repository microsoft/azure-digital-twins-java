/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.telemetry.ingress;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.DevicesApi.DevicesRetrieveQueryParams;
import com.microsoft.twins.api.EndpointsApi;
import com.microsoft.twins.api.EndpointsApi.EndpointsRetrieveQueryParams;
import com.microsoft.twins.api.ResourcesApi;
import com.microsoft.twins.api.ResourcesApi.ResourcesRetrieveQueryParams;
import com.microsoft.twins.api.SpacesApi;
import com.microsoft.twins.api.SpacesApi.SpacesRetrieveQueryParams;
import com.microsoft.twins.api.TypesApi;
import com.microsoft.twins.model.DeviceCreate;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.EndpointCreate;
import com.microsoft.twins.model.EndpointCreate.EventTypesEnum;
import com.microsoft.twins.model.EndpointCreate.TypeEnum;
import com.microsoft.twins.model.EndpointRetrieve;
import com.microsoft.twins.model.ExtendedTypeCreate;
import com.microsoft.twins.model.ExtendedTypeCreate.CategoryEnum;
import com.microsoft.twins.model.SpaceCreate;
import com.microsoft.twins.model.SpaceResourceCreate;
import com.microsoft.twins.model.SpaceResourceRetrieve;
import com.microsoft.twins.model.SpaceRetrieveWithChildren;
import com.microsoft.twins.spring.configuration.DigitalTwinClientAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes = {DigitalTwinClientAutoConfiguration.class, TelemetryIngressAutoConfiguration.class,
    TestConfiguration.class})
@TestPropertySource(locations = "classpath:/test.properties")
@EnableBinding(Sink.class)
public abstract class AbstractTest {
  private static final String TEST_SPACE_TYPE = "TestSpaces";
  private static final String TEST_DEVICE_TYPE = "TestDevices";

  @Autowired
  protected TwinsApiClient twinsApiClient;

  @Autowired
  private TestConfigurationProperties testConfigurationProperties;

  protected UUID tenant;

  @Before
  public void setup() {
    createTestTenantSetup();
    cleanTestSpaces();
    cleanTestDevices();
  }

  @After
  public void cleanup() {
    cleanTestSpaces();
    cleanTestDevices();
  }

  private void cleanTestSpaces() {
    final SpacesApi spacesApi = twinsApiClient.getSpacesApi();

    final List<SpaceRetrieveWithChildren> existing = spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams()
        .types(TEST_SPACE_TYPE).useParentSpace(true).spaceId(tenant.toString()).traverse("Down"));

    existing.forEach(space -> spacesApi.spacesDelete(space.getId().toString()));
  }

  private void cleanTestDevices() {
    final DevicesApi devicesApi = twinsApiClient.getDevicesApi();

    final List<DeviceRetrieve> existing =
        devicesApi.devicesRetrieve(new DevicesRetrieveQueryParams().types(TEST_DEVICE_TYPE));

    existing.forEach(device -> devicesApi.devicesDelete(device.getId().toString()));

  }

  private void createTestTenantSetup() {
    final SpacesApi spacesApi = twinsApiClient.getSpacesApi();

    // Check for existing setup
    final List<SpaceRetrieveWithChildren> found =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().name("TEST_TENANT"));
    if (!found.isEmpty()) {
      tenant = found.get(0).getId();

      assertThat(twinsApiClient.getResourcesApi()
          .resourcesRetrieve(new ResourcesRetrieveQueryParams().spaceId(tenant.toString()))).hasSize(1);
      assertThat(twinsApiClient.getEndpointsApi().endpointsRetrieve(new EndpointsRetrieveQueryParams()
          .eventTypes(EventTypesEnum.DEVICEMESSAGE.toString()).types(TypeEnum.EVENTHUB.toString()))).hasSize(1);

      return;
    }

    final SpaceCreate tenantCreate = new SpaceCreate();
    tenantCreate.setName("TEST_TENANT");
    tenantCreate.setFriendlyName("My test tenant");
    tenantCreate.setDescription("Test tenant");
    tenantCreate.setType("Tenant");

    // TODO try to add gateway to tenant

    tenant = spacesApi.spacesCreate(tenantCreate);

    createTypes();
    createResources();
    addDeviceEventEndPoint(testConfigurationProperties.getConnectionString(),
        testConfigurationProperties.getSecondaryConnectionString(), testConfigurationProperties.getHubname());
  }


  private void addDeviceEventEndPoint(final String connectionString, final String secondaryConnectionString,
      final String hubName) {
    final EndpointsApi endpoints = twinsApiClient.getEndpointsApi();


    final EndpointCreate eventHub = new EndpointCreate();
    eventHub.addEventTypesItem(EventTypesEnum.DEVICEMESSAGE);
    eventHub.setType(TypeEnum.EVENTHUB);
    eventHub.setConnectionString(connectionString + ";EntityPath=" + hubName);
    eventHub.setSecondaryConnectionString(secondaryConnectionString + ";EntityPath=" + hubName);
    eventHub.setPath(hubName);

    final UUID created = endpoints.endpointsCreate(eventHub);

    Awaitility.await().atMost(15, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS).until(
            () -> endpoints.endpointsRetrieveById(created.toString()).getStatus() == EndpointRetrieve.StatusEnum.READY);
  }

  private void createTypes() {
    final TypesApi typesApi = twinsApiClient.getTypesApi();
    final ExtendedTypeCreate type = new ExtendedTypeCreate();
    type.setCategory(CategoryEnum.SPACETYPE);
    type.setSpaceId(tenant);
    type.setName(TEST_SPACE_TYPE);
    typesApi.typesCreate(type);

    type.setCategory(CategoryEnum.DEVICETYPE);
    type.setName(TEST_DEVICE_TYPE);
    typesApi.typesCreate(type);
  }

  private void createResources() {
    final ResourcesApi resourcesApi = twinsApiClient.getResourcesApi();
    final SpaceResourceCreate iotHub = new SpaceResourceCreate();
    iotHub.setSpaceId(tenant);
    iotHub.setType(SpaceResourceCreate.TypeEnum.IOTHUB);
    final UUID created = resourcesApi.resourcesCreate(iotHub);

    Awaitility.await().atMost(15, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS).until(() -> resourcesApi.resourcesRetrieveById(created.toString(), "")
            .getStatus() == SpaceResourceRetrieve.StatusEnum.RUNNING);
  }

  protected UUID createDevice(final String deviceName) {
    final SpacesApi spacesApi = twinsApiClient.getSpacesApi();

    final SpaceCreate deviceSpaceCreate = new SpaceCreate();
    deviceSpaceCreate.setName(deviceName + "space");
    deviceSpaceCreate.setFriendlyName("Space for " + deviceName);
    deviceSpaceCreate.setDescription("Space for " + deviceName);
    deviceSpaceCreate.setType(TEST_SPACE_TYPE);
    deviceSpaceCreate.setParentSpaceId(tenant);

    return createDevice(deviceName, spacesApi.spacesCreate(deviceSpaceCreate));
  }

  protected UUID createDevice(final String deviceName, final UUID spaceId) {
    //TODO switch to sensors
    final DevicesApi devicesApi = twinsApiClient.getDevicesApi();

    // Add new vehicle to line
    final DeviceCreate device = new DeviceCreate();
    device.setName(deviceName);
    device.setHardwareId(deviceName);
    device.setType(TEST_DEVICE_TYPE);
    device.setSpaceId(spaceId);
    //TODO - switch to gateway device
    //device.createIoTHubDevice(false);
    final UUID createdDevice = devicesApi.devicesCreate(device);
    assertThat(createdDevice).isNotNull();

    return createdDevice;
  }

}
