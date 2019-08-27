/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.Optional;
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
import org.springframework.util.CollectionUtils;
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
import com.microsoft.twins.api.TypesApi.TypesRetrieveQueryParams;
import com.microsoft.twins.model.CategoryEnum;
import com.microsoft.twins.model.DeviceCreate;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.EndpointCreate;
import com.microsoft.twins.model.EventTypesEnum;
import com.microsoft.twins.model.ExtendedTypeCreate;
import com.microsoft.twins.model.ExtendedTypeRetrieve;
import com.microsoft.twins.model.SensorCreate;
import com.microsoft.twins.model.SensorRetrieve;
import com.microsoft.twins.model.SpaceCreate;
import com.microsoft.twins.model.SpaceResourceCreate;
import com.microsoft.twins.model.SpaceResourceRetrieve;
import com.microsoft.twins.model.SpaceRetrieveWithChildren;
import com.microsoft.twins.model.SpaceTypeEnum;
import com.microsoft.twins.model.StatusEnum;
import com.microsoft.twins.model.TypeEnum;
import com.microsoft.twins.reflector.TestConfiguration.TestTenantResolver;
import com.microsoft.twins.reflector.proxy.TenantResolver;
import com.microsoft.twins.spring.configuration.DigitalTwinClientAutoConfiguration;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@ActiveProfiles({"test"})
@ContextConfiguration(classes = {DigitalTwinClientAutoConfiguration.class,
    TwinReflectorProxyAutoConfiguration.class, TestConfiguration.class})
@EnableBinding(Sink.class)
public abstract class AbstractIntegrationTest {
  protected static final String TEST_DEVICE_SUBTYPE = "IntegrationTestDevice";
  protected static final String TEST_SPACE_TYPE = "TestSpaces";
  protected static final String TEST_SPACE_SUBTYPE = "integrationTestSpaces";
  protected static final String TEST_DEVICE_TYPE = "TestDevices";
  protected static final String TEST_SENSOR_TYPE = "TestSensors";
  protected int sensorTypeId;
  protected int deviceTypeId;
  protected int deviceSubTypeId;
  protected int spaceTypeId;
  protected int spaceSubTypeId;

  @Autowired
  private TenantResolver tenantResolver;

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
  private TwinReflectorProxyProperties twinReflectorProxyProperties;

  @Autowired
  private TestConfigurationProperties testConfigurationProperties;

  protected UUID tenant;

  protected UUID testGateway;

  protected String testGatewayName = UUID.randomUUID().toString();

  @BeforeEach
  public void setup() {
    createTestTenantSetup();
    cleanTestSensors();
    cleanTestDevices();
    cleanTestSpaces();


    createTypes();
    testGateway = createGateway(testGatewayName, tenant);
  }

  @AfterEach
  public void cleanup() {
    cleanTestSensors();
    cleanTestDevices();
    cleanTestSpaces();
  }

  protected Optional<SpaceRetrieveWithChildren> getSpace(final UUID id) {
    final List<SpaceRetrieveWithChildren> existing =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().ids(id));

    if (CollectionUtils.isEmpty(existing)) {
      return Optional.empty();
    }

    return Optional.ofNullable(existing.get(0));
  }

  protected Optional<DeviceRetrieve> getDevice(final UUID id) {
    final List<DeviceRetrieve> existing =
        devicesApi.devicesRetrieve(new DevicesRetrieveQueryParams().ids(id));

    if (CollectionUtils.isEmpty(existing)) {
      return Optional.empty();
    }

    return Optional.ofNullable(existing.get(0));
  }

  private void cleanTestSpaces() {
    final List<SpaceRetrieveWithChildren> existing =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().types(TEST_SPACE_TYPE)
            .useParentSpace(true).spaceId(tenant).traverse("Down").maxLevel(1));

    existing.forEach(space -> spacesApi.spacesDelete(space.getId()));
  }

  private void cleanTestDevices() {
    final List<DeviceRetrieve> existing =
        devicesApi.devicesRetrieve(new DevicesRetrieveQueryParams().types(TEST_DEVICE_TYPE));

    existing.forEach(device -> devicesApi.devicesDelete(device.getId()));

  }

  private void cleanTestSensors() {
    final List<SensorRetrieve> existing =
        sensorsApi.sensorsRetrieve(new SensorsRetrieveQueryParams().types(TEST_SENSOR_TYPE));

    existing.forEach(sensor -> sensorsApi.sensorsDelete(sensor.getId()));

  }

  private void createTestTenantSetup() {
    // Check for existing setup
    final List<SpaceRetrieveWithChildren> found =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().name("TEST_TENANT"));
    if (!found.isEmpty()) {
      tenant = found.get(0).getId();
      ((TestTenantResolver) tenantResolver).setTenant(tenant);

      assertThat(resourcesApi.resourcesRetrieve(new ResourcesRetrieveQueryParams().spaceId(tenant)))
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

    ((TestTenantResolver) tenantResolver).setTenant(tenant);


    createResources();
    addDeviceEventEndPoint(twinReflectorProxyProperties.getEventHubs().getConnectionString(),
        twinReflectorProxyProperties.getEventHubs().getSecondaryConnectionString(),
        testConfigurationProperties.getDevicesHubname());
  }


  private void createTypes() {
    sensorTypeId = getType(TEST_SENSOR_TYPE, CategoryEnum.SENSORTYPE);
    deviceTypeId = getType(TEST_DEVICE_TYPE, CategoryEnum.DEVICETYPE);
    deviceSubTypeId = getType(TEST_DEVICE_SUBTYPE, CategoryEnum.DEVICESUBTYPE);
    spaceTypeId = getType(TEST_SPACE_TYPE, CategoryEnum.SPACETYPE);
    spaceSubTypeId = getType(TEST_SPACE_SUBTYPE, CategoryEnum.SPACESUBTYPE);

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
        .until(() -> endpointsApi.endpointsRetrieveById(created).getStatus() == StatusEnum.READY);
  }

  private void createResources() {
    final SpaceResourceCreate iotHub = new SpaceResourceCreate();
    iotHub.setSpaceId(tenant);
    iotHub.setType(SpaceTypeEnum.IOTHUB);
    final UUID created = resourcesApi.resourcesCreate(iotHub);

    Awaitility.await().atMost(15, TimeUnit.MINUTES).pollDelay(10, TimeUnit.SECONDS)
        .pollInterval(1, TimeUnit.SECONDS).until(() -> resourcesApi.resourcesRetrieveById(created)
            .getStatus() == SpaceResourceRetrieve.StatusEnum.RUNNING);
  }

  protected UUID createSpace(final String spaceName, final UUID parent) {
    final SpaceCreate deviceSpaceCreate = new SpaceCreate();
    deviceSpaceCreate.setName(spaceName);
    deviceSpaceCreate.setFriendlyName(spaceName);
    deviceSpaceCreate.setDescription(spaceName);
    deviceSpaceCreate.setTypeId(spaceTypeId);
    deviceSpaceCreate.setParentSpaceId(parent);

    return spacesApi.spacesCreate(deviceSpaceCreate);
  }

  protected UUID createSpace(final String spaceName) {
    return createSpace(spaceName, tenant);
  }

  protected UUID createGateway(final String deviceName, final UUID spaceId) {
    // Add new vehicle to line
    final DeviceCreate device = new DeviceCreate();
    device.setName(deviceName);
    device.setTypeId(deviceTypeId);
    device.setSpaceId(spaceId);
    device.setHardwareId(deviceName);
    device.setDescription(deviceName);
    device.setFriendlyName(deviceName);

    final UUID createdDevice = devicesApi.devicesCreate(device);
    assertThat(createdDevice).isNotNull();

    return createdDevice;
  }

  protected UUID createDevice(final String deviceName) {
    return createDevice(deviceName, testGateway, tenant);
  }

  protected UUID createDevice(final String deviceName, final UUID gatewayId, final UUID spaceId) {
    final DeviceCreate device = new DeviceCreate();
    device.setName(deviceName);
    device.setTypeId(deviceTypeId);
    device.setSpaceId(spaceId);
    device.setHardwareId(deviceName);
    device.setGatewayId(gatewayId);
    device.setCreateIoTHubDevice(false);
    device.setFriendlyName(deviceName);
    device.setDescription(deviceName);

    final UUID createdDevice = devicesApi.devicesCreate(device);
    assertThat(createdDevice).isNotNull();

    return createdDevice;
  }



  protected int getType(final String name, final CategoryEnum category) {
    final List<ExtendedTypeRetrieve> found = typesApi.typesRetrieve(
        new TypesRetrieveQueryParams().spaceId(tenant).names(name).categories(category));

    if (CollectionUtils.isEmpty(found)) {
      return typesApi
          .typesCreate(new ExtendedTypeCreate().name(name).category(category).spaceId(tenant));
    }

    return found.get(0).getId();
  }



  protected UUID createSensor(final String deviceName, final UUID deviceId, final UUID spaceId) {
    final SensorCreate device = new SensorCreate();
    device.setTypeId(sensorTypeId);
    device.setDeviceId(deviceId);
    device.setHardwareId(deviceName);
    device.setSpaceId(spaceId);

    final UUID createdDevice = sensorsApi.sensorsCreate(device);
    assertThat(createdDevice).isNotNull();

    return createdDevice;
  }

}
