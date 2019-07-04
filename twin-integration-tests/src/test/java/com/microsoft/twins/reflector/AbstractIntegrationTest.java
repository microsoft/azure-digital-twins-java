/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.DevicesApi.DevicesRetrieveQueryParams;
import com.microsoft.twins.api.EndpointsApi;
import com.microsoft.twins.api.EndpointsApi.EndpointsRetrieveQueryParams;
import com.microsoft.twins.api.ResourcesApi;
import com.microsoft.twins.api.ResourcesApi.ResourcesRetrieveQueryParams;
import com.microsoft.twins.api.SensorsApi;
import com.microsoft.twins.api.SensorsApi.SensorsRetrieveQueryParams;
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
import com.microsoft.twins.model.SensorCreate;
import com.microsoft.twins.model.SensorRetrieve;
import com.microsoft.twins.model.SpaceCreate;
import com.microsoft.twins.model.SpaceResourceCreate;
import com.microsoft.twins.model.SpaceResourceRetrieve;
import com.microsoft.twins.model.SpaceRetrieveWithChildren;
import com.microsoft.twins.spring.configuration.DigitalTwinClientAutoConfiguration;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@ContextConfiguration(classes = {DigitalTwinClientAutoConfiguration.class,
    TwinReflectorProxyAutoConfiguration.class, TestConfiguration.class})
@EnableBinding(Sink.class)
public abstract class AbstractIntegrationTest {
  private static final String TEST_SPACE_TYPE = "TestSpaces";
  private static final String TEST_DEVICE_TYPE = "TestDevices";
  private static final String TEST_SENSOR_TYPE = "TestSensors";

  @Autowired
  protected SpacesApi spacesApi;

  @Autowired
  protected DevicesApi devicesApi;

  @Autowired
  protected SensorsApi sensorsApi;

  @Autowired
  protected ResourcesApi resourcesApi;

  @Autowired
  protected EndpointsApi endpointsApi;

  @Autowired
  protected TypesApi typesApi;

  @Autowired
  private TestConfigurationProperties testConfigurationProperties;

  protected UUID tenant;

  protected UUID testGateway;

  @BeforeEach
  public void setup() {
    createTestTenantSetup();
    cleanTestSensors();
    cleanTestDevices();
    cleanTestSpaces();

    final String gatewayName = UUID.randomUUID().toString();
    testGateway = createGateway(gatewayName, tenant);
  }

  @AfterEach
  public void cleanup() {
    cleanTestSensors();
    cleanTestDevices();
    cleanTestSpaces();
  }

  private void cleanTestSpaces() {
    final List<SpaceRetrieveWithChildren> existing =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().types(TEST_SPACE_TYPE)
            .useParentSpace(true).spaceId(tenant.toString()).traverse("Down"));

    existing.forEach(space -> spacesApi.spacesDelete(space.getId().toString()));
  }

  private void cleanTestDevices() {
    final List<DeviceRetrieve> existing =
        devicesApi.devicesRetrieve(new DevicesRetrieveQueryParams().types(TEST_DEVICE_TYPE));

    existing.forEach(device -> devicesApi.devicesDelete(device.getId().toString()));

  }

  private void cleanTestSensors() {
    final List<SensorRetrieve> existing =
        sensorsApi.sensorsRetrieve(new SensorsRetrieveQueryParams().types(TEST_SENSOR_TYPE));

    existing.forEach(sensor -> sensorsApi.sensorsDelete(sensor.getId().toString()));

  }

  private void createTestTenantSetup() {
    // Check for existing setup
    final List<SpaceRetrieveWithChildren> found =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().name("TEST_TENANT"));
    if (!found.isEmpty()) {
      tenant = found.get(0).getId();

      assertThat(resourcesApi
          .resourcesRetrieve(new ResourcesRetrieveQueryParams().spaceId(tenant.toString())))
              .hasSize(1);
      assertThat(endpointsApi.endpointsRetrieve(new EndpointsRetrieveQueryParams()
          .eventTypes(EventTypesEnum.DEVICEMESSAGE.toString()).types(TypeEnum.EVENTHUB.toString())))
              .hasSize(1);

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
        testConfigurationProperties.getSecondaryConnectionString(),
        testConfigurationProperties.getDevicesHubname());
  }


  private void addDeviceEventEndPoint(final String connectionString,
      final String secondaryConnectionString, final String hubName) {

    final EndpointCreate eventHub = new EndpointCreate();
    eventHub.addEventTypesItem(EventTypesEnum.DEVICEMESSAGE);
    eventHub.setType(TypeEnum.EVENTHUB);
    eventHub.setConnectionString(connectionString + ";EntityPath=" + hubName);
    eventHub.setSecondaryConnectionString(secondaryConnectionString + ";EntityPath=" + hubName);
    eventHub.setPath(hubName);

    final UUID created = endpointsApi.endpointsCreate(eventHub);

    Awaitility.await().atMost(15, TimeUnit.MINUTES).pollDelay(10, TimeUnit.SECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .until(() -> endpointsApi.endpointsRetrieveById(created.toString())
            .getStatus() == EndpointRetrieve.StatusEnum.READY);
  }

  private void createTypes() {
    final ExtendedTypeCreate type = new ExtendedTypeCreate();
    type.setCategory(CategoryEnum.SPACETYPE);
    type.setSpaceId(tenant);
    type.setName(TEST_SPACE_TYPE);
    typesApi.typesCreate(type);

    type.setCategory(CategoryEnum.DEVICETYPE);
    type.setName(TEST_DEVICE_TYPE);
    typesApi.typesCreate(type);

    type.setCategory(CategoryEnum.SENSORTYPE);
    type.setName(TEST_SENSOR_TYPE);
    typesApi.typesCreate(type);
  }

  private void createResources() {
    final SpaceResourceCreate iotHub = new SpaceResourceCreate();
    iotHub.setSpaceId(tenant);
    iotHub.setType(SpaceResourceCreate.TypeEnum.IOTHUB);
    final UUID created = resourcesApi.resourcesCreate(iotHub);

    Awaitility.await().atMost(15, TimeUnit.MINUTES).pollDelay(10, TimeUnit.SECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .until(() -> resourcesApi.resourcesRetrieveById(created.toString())
            .getStatus() == SpaceResourceRetrieve.StatusEnum.RUNNING);
  }

  protected UUID createSpace(final String spaceName) {
    final SpaceCreate deviceSpaceCreate = new SpaceCreate();
    deviceSpaceCreate.setName(spaceName);
    deviceSpaceCreate.setFriendlyName("Space " + spaceName);
    deviceSpaceCreate.setDescription("Space " + spaceName);
    deviceSpaceCreate.setType(TEST_SPACE_TYPE);
    deviceSpaceCreate.setParentSpaceId(tenant);

    return spacesApi.spacesCreate(deviceSpaceCreate);
  }

  protected UUID createGateway(final String deviceName, final UUID spaceId) {
    // Add new vehicle to line
    final DeviceCreate device = new DeviceCreate();
    device.setName(deviceName);
    device.setType(TEST_DEVICE_TYPE);
    device.setSpaceId(spaceId);
    device.setHardwareId(deviceName);
    device.setGatewayId(deviceName);

    final UUID createdDevice = devicesApi.devicesCreate(device);
    assertThat(createdDevice).isNotNull();

    return createdDevice;
  }

  protected UUID createDevice(final String deviceName, final UUID gatewayId, final UUID spaceId) {
    final DeviceCreate device = new DeviceCreate();
    device.setName(deviceName);
    device.setType(TEST_DEVICE_TYPE);
    device.setSpaceId(spaceId);
    device.setHardwareId(deviceName);
    device.setGatewayId(gatewayId.toString());
    device.setCreateIoTHubDevice(false);

    final UUID createdDevice = devicesApi.devicesCreate(device);
    assertThat(createdDevice).isNotNull();

    return createdDevice;
  }

  protected UUID createSensor(final String deviceName, final UUID deviceId, final UUID spaceId) {
    final SensorCreate device = new SensorCreate();
    device.setType(TEST_SENSOR_TYPE);
    device.setDeviceId(deviceId);
    device.setHardwareId(deviceName);
    device.setSpaceId(spaceId);

    final UUID createdDevice = sensorsApi.sensorsCreate(device);
    assertThat(createdDevice).isNotNull();

    return createdDevice;
  }

}
