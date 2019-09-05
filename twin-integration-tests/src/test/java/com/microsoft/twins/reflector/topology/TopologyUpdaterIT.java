/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.topology;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomStringUtils;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.SpacesApi;
import com.microsoft.twins.model.CategoryEnum;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.ExtendedPropertyRetrieve;
import com.microsoft.twins.model.SpaceRetrieve;
import com.microsoft.twins.reflector.AbstractIntegrationTest;
import com.microsoft.twins.reflector.model.ErrorCode;
import com.microsoft.twins.reflector.model.IngressMessage;
import com.microsoft.twins.reflector.model.MessageType;
import com.microsoft.twins.reflector.model.Property;
import com.microsoft.twins.reflector.model.Relationship;

public class TopologyUpdaterIT extends AbstractIntegrationTest {

  private static final String TEST_PROP_VALUE = "testValue1";
  private static final String TEST_PROP_KEY = "testName1";


  @Test
  public void deleteSpace() {
    final String spaceName = RandomStringUtils.randomAlphanumeric(20);
    final UUID testSpace = createSpace(spaceName);

    assertThat(spacesApi.spacesRetrieve(new SpacesApi.SpacesRetrieveQueryParams().ids(testSpace)))
        .hasSize(1);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceName);
    testMessage.setEntityType("spaces");

    sendAndAwaitFeedback(testMessage, MessageType.DELETE);

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(
            spacesApi.spacesRetrieve(new SpacesApi.SpacesRetrieveQueryParams().ids(testSpace)))
                .isEmpty());
  }



  @Test
  public void deleteDevice() {
    final UUID testSpace = createSpace("My Reflector Proxy delete test space");

    final String deviceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID device = createDevice(deviceId, testGateway, testSpace);

    assertThat(devicesApi.devicesRetrieve(new DevicesApi.DevicesRetrieveQueryParams().ids(device)))
        .hasSize(1);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");

    sendAndAwaitFeedback(testMessage, MessageType.DELETE);

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(
            devicesApi.devicesRetrieve(new DevicesApi.DevicesRetrieveQueryParams().ids(device)))
                .isEmpty());
  }

  @Test
  public void createDeviceMinimal() {
    final String deviceId = RandomStringUtils.randomAlphanumeric(20);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");

    sendAndAwaitFeedback(testMessage, MessageType.FULL);

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(devicesApi.devicesRetrieve(
            new DevicesApi.DevicesRetrieveQueryParams().names(deviceId.toString()))).hasSize(1));

    final DeviceRetrieve created =
        devicesApi.devicesRetrieve(new DevicesApi.DevicesRetrieveQueryParams()
            .names(deviceId.toString()).includes("properties,description")).get(0);
    assertThat(created.getName()).isEqualTo(deviceId);
    assertThat(created.getHardwareId()).isEqualTo(deviceId);
    assertThat(created.getGatewayId()).isEqualTo(testGateway);
    assertThat(created.getSpaceId()).isEqualTo(tenant);
    assertThat(created.getTypeId()).isEqualTo(getType("None", CategoryEnum.DEVICETYPE));
    assertThat(created.getIoTHubUrl()).isNullOrEmpty();
    assertThat(created.getConnectionString()).isNullOrEmpty();
    assertThat(created.getProperties()).isEmpty();
    assertThat(created.getFriendlyName()).isNullOrEmpty();
    assertThat(created.getDescription()).isNullOrEmpty();
    assertThat(created.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.PROVISIONED);
  }

  @Test
  public void createDeviceFull() {
    final String spaceName = RandomStringUtils.randomAlphanumeric(20);
    createDeviceWithGivenParent(spaceName, createSpace(spaceName));
  }

  @Test
  public void createDeviceFullNoParent() {
    createDeviceWithGivenParent(null, null);
  }

  private void createDeviceWithGivenParent(final String parentName, final UUID parent) {
    final String deviceId = RandomStringUtils.randomAlphanumeric(20);
    final String friendlyName = "a test device";
    final String description = "a test descrption";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");

    final List<Relationship> relationShips = new ArrayList<>();
    relationShips.add(Relationship.builder().entityType("devices").name("gateway")
        .targetId(testGatewayName).build());

    if (parentName != null) {
      relationShips.add(
          Relationship.builder().entityType("spaces").name("parent").targetId(parentName).build());
    }

    testMessage.setRelationships(relationShips);
    testMessage.setProperties(
        List.of(Property.builder().name(TEST_PROP_KEY).value(TEST_PROP_VALUE).build()));
    testMessage.setAttributes(Map.of("status", "Provisioned", "friendlyName", friendlyName,
        "description", description, "type", TEST_DEVICE_TYPE));

    sendAndAwaitFeedback(testMessage, MessageType.FULL);

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(devicesApi.devicesRetrieve(
            new DevicesApi.DevicesRetrieveQueryParams().names(deviceId.toString()))).hasSize(1));

    final DeviceRetrieve created =
        devicesApi.devicesRetrieve(new DevicesApi.DevicesRetrieveQueryParams()
            .names(deviceId.toString()).includes("properties,description")).get(0);
    assertThat(created.getName()).isEqualTo(deviceId);
    assertThat(created.getHardwareId()).isEqualTo(deviceId);
    assertThat(created.getGatewayId()).isEqualTo(testGateway);
    if (parent != null) {
      assertThat(created.getSpaceId()).isEqualTo(parent);
    } else {
      assertThat(created.getSpaceId()).isEqualTo(tenant);
    }
    assertThat(created.getTypeId()).isEqualTo(deviceTypeId);
    assertThat(created.getIoTHubUrl()).isNullOrEmpty();
    assertThat(created.getConnectionString()).isNullOrEmpty();
    assertThat(created.getProperties()).containsOnly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value(TEST_PROP_VALUE).dataType("string"));
    assertThat(created.getFriendlyName()).isEqualTo(friendlyName);
    assertThat(created.getDescription()).isEqualTo(description);
    assertThat(created.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.PROVISIONED);
  }

  @Test
  public void updateDeviceFull() {
    final String updatedParentName = RandomStringUtils.randomAlphanumeric(20);
    final UUID updatedParent = createSpace(updatedParentName);

    final String updatedGatewayName = RandomStringUtils.randomAlphanumeric(20);
    final UUID updatedGateway = createGateway(updatedGatewayName, tenant);

    final String updatedType = "updatedTestType";
    final int updatedTypeId = getType(updatedType, CategoryEnum.DEVICETYPE);

    final String deviceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID device =
        createDevice(deviceId, testGateway, createSpace("My Reflector Proxyoriginal space"));

    final String friendlyName = "updated name";
    final String description = "updated description";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");
    testMessage.setRelationships(List.of(
        Relationship.builder().entityType("spaces").name("parent").targetId(updatedParentName)
            .build(),
        Relationship.builder().entityType("devices").name("gateway").targetId(updatedGatewayName)
            .build()));
    testMessage.setProperties(
        List.of(Property.builder().name(TEST_PROP_KEY).value("updatedValue").build()));
    testMessage.setAttributes(Map.of("status", "Active", "friendlyName", friendlyName,
        "description", description, "type", updatedType));

    sendAndAwaitFeedback(testMessage, MessageType.FULL);

    final DeviceRetrieve updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(deviceId);
    assertThat(updated.getHardwareId()).isEqualTo(deviceId);
    assertThat(updated.getGatewayId()).isEqualTo(updatedGateway);
    assertThat(updated.getTypeId()).isEqualTo(updatedTypeId);
    assertThat(updated.getSpaceId()).isEqualTo(updatedParent);
    assertThat(updated.getIoTHubUrl()).isNullOrEmpty();
    assertThat(updated.getConnectionString()).isNullOrEmpty();
    assertThat(updated.getProperties()).containsOnly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value("updatedValue").dataType("string"));
    assertThat(updated.getFriendlyName()).isEqualTo(friendlyName);
    assertThat(updated.getDescription()).isEqualTo(description);
    assertThat(updated.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.ACTIVE);
  }

  @Test
  public void updateDeviceFullResetValues() {
    final String deviceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID device = createDevice(deviceId, createGateway("AnotherGateway", tenant),
        createSpace("AnotherSpace"));

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");

    sendAndAwaitFeedback(testMessage, MessageType.FULL);

    final DeviceRetrieve updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(deviceId);
    assertThat(updated.getHardwareId()).isEqualTo(deviceId);
    assertThat(updated.getGatewayId()).isEqualTo(testGateway);
    assertThat(updated.getTypeId()).isEqualTo(getType("None", CategoryEnum.DEVICETYPE));
    assertThat(updated.getSpaceId()).isEqualTo(tenant);
    assertThat(updated.getIoTHubUrl()).isNullOrEmpty();
    assertThat(updated.getConnectionString()).isNullOrEmpty();
    assertThat(updated.getProperties()).isEmpty();
    assertThat(updated.getFriendlyName()).isNullOrEmpty();
    assertThat(updated.getDescription()).isNullOrEmpty();
    assertThat(updated.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.PROVISIONED);
  }


  @Test
  public void updateDeviceGateway() {
    final String deviceId = RandomStringUtils.randomAlphanumeric(20);

    final String updatedGatewayName = RandomStringUtils.randomAlphanumeric(20);
    final UUID updatedGateway = createGateway(updatedGatewayName, tenant);

    final UUID device = createDevice(deviceId);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");

    testMessage.setRelationships(List.of(Relationship.builder().entityType("devices")
        .name("gateway").targetId(updatedGatewayName).build()));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    final DeviceRetrieve updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(deviceId);
    assertThat(updated.getHardwareId()).isEqualTo(deviceId);
    assertThat(updated.getGatewayId()).isEqualTo(updatedGateway);
    assertThat(updated.getSpaceId()).isEqualTo(tenant);
    assertThat(updated.getIoTHubUrl()).isNullOrEmpty();
    assertThat(updated.getConnectionString()).isNullOrEmpty();
    assertThat(updated.getProperties()).isEmpty();
    assertThat(updated.getFriendlyName()).isEqualTo(deviceId);
    assertThat(updated.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.PROVISIONED);
    assertThat(updated.getDescription()).isEqualTo(deviceId);
  }

  @Test
  public void updateDeviceGatewayFailsIfGatewayIdDoesNotExist() {
    final String deviceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID device = createDevice(deviceId);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");

    testMessage.setRelationships(List.of(Relationship.builder().entityType("devices")
        .name("gateway").targetId("doesNotExist").build()));

    sendAndAwaitErrorFeedback(testMessage, MessageType.PARTIAL, ErrorCode.ELEMENT_DOES_NOT_EXIST);

    final DeviceRetrieve updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(deviceId);
    assertThat(updated.getHardwareId()).isEqualTo(deviceId);
    assertThat(updated.getGatewayId()).isEqualTo(testGateway);
    assertThat(updated.getSpaceId()).isEqualTo(tenant);
    assertThat(updated.getIoTHubUrl()).isNullOrEmpty();
    assertThat(updated.getConnectionString()).isNullOrEmpty();
    assertThat(updated.getProperties()).isEmpty();
    assertThat(updated.getFriendlyName()).isEqualTo(deviceId);
    assertThat(updated.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.PROVISIONED);
    assertThat(updated.getDescription()).isEqualTo(deviceId);
  }

  @Test
  public void updateDeviceUpdateProperty() {
    final String deviceId = RandomStringUtils.randomAlphanumeric(20);

    final UUID device = createDevice(deviceId);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");
    testMessage
        .setProperties(List.of(Property.builder().name(TEST_PROP_KEY).value("newValue").build()));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    // Create property
    DeviceRetrieve updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(deviceId);
    assertThat(updated.getHardwareId()).isEqualTo(deviceId);
    assertThat(updated.getGatewayId()).isEqualTo(testGateway);
    assertThat(updated.getSpaceId()).isEqualTo(tenant);
    assertThat(updated.getIoTHubUrl()).isNullOrEmpty();
    assertThat(updated.getConnectionString()).isNullOrEmpty();
    assertThat(updated.getProperties()).containsOnly(
        new ExtendedPropertyRetrieve().name(TEST_PROP_KEY).value("newValue").dataType("string"));
    assertThat(updated.getFriendlyName()).isEqualTo(deviceId);
    assertThat(updated.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.PROVISIONED);
    assertThat(updated.getDescription()).isEqualTo(deviceId);

    // Update same property
    testMessage.setProperties(
        List.of(Property.builder().name(TEST_PROP_KEY).value("updatedValue").build()));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getProperties()).containsOnly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value("updatedValue").dataType("string"));


    // Add another property
    testMessage.setProperties(
        List.of(Property.builder().name(TEST_PROP_KEY + "_2").value("newValue").build()));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getProperties()).containsOnly(
        new ExtendedPropertyRetrieve().name(TEST_PROP_KEY).value("updatedValue").dataType("string"),
        new ExtendedPropertyRetrieve().name(TEST_PROP_KEY + "_2").value("newValue")
            .dataType("string"));
  }

  @Test
  public void updateDeviceParent() {

    final String deviceId = RandomStringUtils.randomAlphanumeric(20);

    final String updatedParentName = RandomStringUtils.randomAlphanumeric(20);
    final UUID updatedParent = createSpace(updatedParentName);

    final UUID device = createDevice(deviceId);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");

    testMessage.setRelationships(List.of(Relationship.builder().entityType("spaces").name("parent")
        .targetId(updatedParentName).build()));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    final DeviceRetrieve updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(deviceId);
    assertThat(updated.getHardwareId()).isEqualTo(deviceId);
    assertThat(updated.getGatewayId()).isEqualTo(testGateway);
    assertThat(updated.getSpaceId()).isEqualTo(updatedParent);
    assertThat(updated.getIoTHubUrl()).isNullOrEmpty();
    assertThat(updated.getConnectionString()).isNullOrEmpty();
    assertThat(updated.getProperties()).isEmpty();
    assertThat(updated.getFriendlyName()).isEqualTo(deviceId);
    assertThat(updated.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.PROVISIONED);
    assertThat(updated.getDescription()).isEqualTo(deviceId);
  }



  @Test
  public void updateDeviceDescription() {

    final String deviceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID device = createDevice(deviceId);

    final String description = "updated description";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");
    testMessage.setAttributes(Map.of("description", description));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    final DeviceRetrieve updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(deviceId);
    assertThat(updated.getHardwareId()).isEqualTo(deviceId);
    assertThat(updated.getGatewayId()).isEqualTo(testGateway);
    assertThat(updated.getSpaceId()).isEqualTo(tenant);
    assertThat(updated.getIoTHubUrl()).isNullOrEmpty();
    assertThat(updated.getConnectionString()).isNullOrEmpty();
    assertThat(updated.getProperties()).isEmpty();
    assertThat(updated.getFriendlyName()).isEqualTo(deviceId);
    assertThat(updated.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.PROVISIONED);
    assertThat(updated.getDescription()).isEqualTo(description);
  }

  @Test
  public void ingressFailsIfEntityTypeNotSupported() {

    final String deviceId = RandomStringUtils.randomAlphanumeric(20);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("not supported");

    sendAndAwaitErrorFeedback(testMessage, MessageType.PARTIAL, ErrorCode.ENTITY_NOT_SUPPORTED);

    assertThat(devicesApi.devicesRetrieve(new DevicesApi.DevicesRetrieveQueryParams()
        .names(deviceId).includes("properties,description"))).isEmpty();

  }

  @Test
  public void updateDeviceAttributeFailsIfKeyNotSupported() {

    final String deviceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID device = createDevice(deviceId);


    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");
    testMessage.setAttributes(Map.of("doesNotExist", "willNotBeSet"));

    sendAndAwaitErrorFeedback(testMessage, MessageType.PARTIAL, ErrorCode.ATTRIBUTE_NOT_SUPPORTED);

    final DeviceRetrieve updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(deviceId);
    assertThat(updated.getHardwareId()).isEqualTo(deviceId);
    assertThat(updated.getGatewayId()).isEqualTo(testGateway);
    assertThat(updated.getSpaceId()).isEqualTo(tenant);
    assertThat(updated.getTypeId()).isEqualTo(deviceTypeId);
    assertThat(updated.getIoTHubUrl()).isNullOrEmpty();
    assertThat(updated.getConnectionString()).isNullOrEmpty();
    assertThat(updated.getProperties()).isEmpty();
    assertThat(updated.getFriendlyName()).isEqualTo(deviceId);
    assertThat(updated.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.PROVISIONED);
  }

  @Test
  public void updateDeviceType() {

    final String deviceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID device = createDevice(deviceId);

    final String updatedType = "updatedTestType";
    final int updatedTypeId = getType(updatedType, CategoryEnum.DEVICETYPE);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");
    testMessage.setAttributes(Map.of("type", updatedType));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    final DeviceRetrieve updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(deviceId);
    assertThat(updated.getHardwareId()).isEqualTo(deviceId);
    assertThat(updated.getGatewayId()).isEqualTo(testGateway);
    assertThat(updated.getSpaceId()).isEqualTo(tenant);
    assertThat(updated.getTypeId()).isEqualTo(updatedTypeId);
    assertThat(updated.getIoTHubUrl()).isNullOrEmpty();
    assertThat(updated.getConnectionString()).isNullOrEmpty();
    assertThat(updated.getProperties()).isEmpty();
    assertThat(updated.getFriendlyName()).isEqualTo(deviceId);
    assertThat(updated.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.PROVISIONED);
  }



  @Test
  public void updateSpaceFull() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID space = createSpace(spaceId);

    final String updateParentSpaceName = RandomStringUtils.randomAlphanumeric(20);
    final UUID updateParentSpace = createSpace(updateParentSpaceName);

    final String updateChildSpaceName = RandomStringUtils.randomAlphanumeric(20);
    final UUID updateChildSpaceId = createSpace(updateChildSpaceName);

    final String updateChildDeviceName = RandomStringUtils.randomAlphanumeric(20);
    final UUID updateChildDeviceId = createDevice(updateChildDeviceName, testGateway,
        createSpace("My Reflector Proxy original parent space"));

    final String updatedType = "updatedTestSpaceType";
    final int updatedTypeId = getType(updatedType, CategoryEnum.SPACETYPE);

    final String updatedStatus = "updatedTestSpaceStatus";
    final int updatedStatusId = getType(updatedStatus, CategoryEnum.SPACESTATUS);

    final String friendlyName = "updated name";
    final String description = "updated description";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setRelationships(List.of(
        Relationship.builder().entityType("spaces").name("parent").targetId(updateParentSpaceName)
            .build(),
        Relationship.builder().entityType("spaces").name("child").targetId(updateChildSpaceName)
            .build(),
        Relationship.builder().entityType("devices").name("child").targetId(updateChildDeviceName)
            .build()));
    testMessage.setProperties(
        List.of(Property.builder().name(TEST_PROP_KEY).value("updatedValue").build()));
    testMessage.setAttributes(Map.of("friendlyName", friendlyName, "description", description,
        "type", updatedType, "status", updatedStatus));

    sendAndAwaitFeedback(testMessage, MessageType.FULL);

    final SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(updateParentSpace);
    assertThat(updated.getFriendlyName()).isEqualTo(friendlyName);
    assertThat(updated.getDescription()).isEqualTo(description);
    assertThat(updated.getTypeId()).isEqualTo(updatedTypeId);
    assertThat(updated.getStatusId()).isEqualTo(updatedStatusId);
    assertThat(updated.getProperties()).containsOnly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value("updatedValue").dataType("string"));

    assertThat(getDevice(updateChildDeviceId)).isNotEmpty()
        .hasValueSatisfying(child -> child.getSpaceId().equals(space));

    assertThat(getSpace(updateChildSpaceId)).isNotEmpty()
        .hasValueSatisfying(child -> child.getParentSpaceId().equals(space));
  }

  @Test
  public void updateSpaceFullResetValue() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID space = createSpace(spaceId);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");

    sendAndAwaitFeedback(testMessage, MessageType.FULL);

    final SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);
    assertThat(updated.getFriendlyName()).isNullOrEmpty();
    assertThat(updated.getDescription()).isNullOrEmpty();
    assertThat(updated.getTypeId()).isEqualTo(getType("None", CategoryEnum.SPACETYPE));
    assertThat(updated.getProperties()).isEmpty();
    assertThat(updated.getStatusId()).isEqualTo(getType("None", CategoryEnum.SPACESTATUS));
  }

  @Test
  public void updateSpaceFriendlyName() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID space = createSpace(spaceId);


    final String friendlyName = "updated name";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setAttributes(Map.of("friendlyName", friendlyName));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    final SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);
    assertThat(updated.getFriendlyName()).isEqualTo(friendlyName);
    assertThat(updated.getDescription()).isEqualTo(spaceId);
    assertThat(updated.getProperties()).isEmpty();
  }

  @Test
  public void updateSpaceType() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID space = createSpace(spaceId);

    final String updatedType = "updatedTestSpaceType";
    final int updatedTypeId = getType(updatedType, CategoryEnum.SPACETYPE);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setAttributes(Map.of("type", updatedType));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    final SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);
    assertThat(updated.getDescription()).isEqualTo(spaceId);
    assertThat(updated.getProperties()).isEmpty();
    assertThat(updated.getTypeId()).isEqualTo(updatedTypeId);
  }

  @Test
  public void updateSpaceAttributeFailsIfKeyNotSupported() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID space = createSpace(spaceId);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setAttributes(Map.of("doesNotexist", "newValue"));

    sendAndAwaitErrorFeedback(testMessage, MessageType.PARTIAL, ErrorCode.ATTRIBUTE_NOT_SUPPORTED);

    final SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);
    assertThat(updated.getDescription()).isEqualTo(spaceId);
    assertThat(updated.getProperties()).isEmpty();
    assertThat(updated.getTypeId()).isEqualTo(spaceTypeId);
  }

  @Test
  public void updateSpaceStatus() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID space = createSpace(spaceId);

    final String updatedStatus = "updatedTestSpaceStatus";
    final int updatedStatusId = getType(updatedStatus, CategoryEnum.SPACESTATUS);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setAttributes(Map.of("status", updatedStatus));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    final SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);
    assertThat(updated.getDescription()).isEqualTo(spaceId);
    assertThat(updated.getProperties()).isEmpty();
    assertThat(updated.getStatusId()).isEqualTo(updatedStatusId);
  }

  @Test
  public void updateSpaceProperty() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID space = createSpace(spaceId);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");

    testMessage
        .setProperties(List.of(Property.builder().name(TEST_PROP_KEY).value("newValue").build()));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    // Add property
    SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);
    assertThat(updated.getFriendlyName()).isEqualTo(spaceId);
    assertThat(updated.getDescription()).isEqualTo(spaceId);
    assertThat(updated.getProperties()).containsOnly(
        new ExtendedPropertyRetrieve().name(TEST_PROP_KEY).value("newValue").dataType("string"));

    // Update property
    testMessage.setProperties(
        List.of(Property.builder().name(TEST_PROP_KEY).value("updatedValue").build()));
    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);
    assertThat(updated.getFriendlyName()).isEqualTo(spaceId);
    assertThat(updated.getDescription()).isEqualTo(spaceId);
    assertThat(updated.getProperties()).containsOnly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value("updatedValue").dataType("string"));

    // Add another property
    testMessage.setProperties(
        List.of(Property.builder().name(TEST_PROP_KEY + "_2").value("newValue").build()));
    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);
    assertThat(updated.getFriendlyName()).isEqualTo(spaceId);
    assertThat(updated.getDescription()).isEqualTo(spaceId);
    assertThat(updated.getProperties()).containsOnly(
        new ExtendedPropertyRetrieve().name(TEST_PROP_KEY).value("updatedValue").dataType("string"),
        new ExtendedPropertyRetrieve().name(TEST_PROP_KEY + "_2").value("newValue")
            .dataType("string"));
  }

  @Test
  public void updateSpaceChildSpace() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID space = createSpace(spaceId);

    final String updateChildSpaceName = RandomStringUtils.randomAlphanumeric(20);
    final UUID updateChildSpaceId = createSpace(updateChildSpaceName);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setRelationships(List.of(Relationship.builder().entityType("spaces").name("child")
        .targetId(updateChildSpaceName).build()));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    // Add child
    SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);
    assertThat(updated.getFriendlyName()).isEqualTo(spaceId);
    assertThat(updated.getDescription()).isEqualTo(spaceId);
    assertThat(updated.getProperties()).isEmpty();

    assertThat(getSpace(updateChildSpaceId)).isNotEmpty()
        .hasValueSatisfying(child -> child.getParentSpaceId().equals(space));

    // Remove child
    testMessage.setRelationships(null);
    sendAndAwaitFeedback(testMessage, MessageType.FULL);

    updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);

    assertThat(getSpace(updateChildSpaceId)).isNotEmpty()
        .hasValueSatisfying(child -> child.getParentSpaceId().equals(tenant));

  }

  @Test
  public void updateSpaceChildDevice() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID space = createSpace(spaceId);

    final String updateChildDeviceName = RandomStringUtils.randomAlphanumeric(20);
    final UUID updateChildDeviceId = createDevice(updateChildDeviceName, testGateway,
        createSpace("My Reflector Proxy original parent space"));

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setRelationships(List.of(Relationship.builder().entityType("devices").name("child")
        .targetId(updateChildDeviceName).build()));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    // Add child
    SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);
    assertThat(updated.getFriendlyName()).isEqualTo(spaceId);
    assertThat(updated.getDescription()).isEqualTo(spaceId);
    assertThat(updated.getProperties()).isEmpty();


    assertThat(getDevice(updateChildDeviceId)).isNotEmpty()
        .hasValueSatisfying(child -> child.getSpaceId().equals(space));

    // Remove child
    testMessage.setRelationships(null);
    sendAndAwaitFeedback(testMessage, MessageType.FULL);

    updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);

    assertThat(getDevice(updateChildDeviceId)).isNotEmpty()
        .hasValueSatisfying(child -> child.getSpaceId().equals(tenant));
  }

  @Test
  public void updateSpaceParent() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID space = createSpace(spaceId);


    final String updateParentSpaceName = RandomStringUtils.randomAlphanumeric(20);
    final UUID updateParentSpace = createSpace(updateParentSpaceName);


    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setRelationships(List.of(Relationship.builder().entityType("spaces").name("parent")
        .targetId(updateParentSpaceName).build()));

    sendAndAwaitFeedback(testMessage, MessageType.PARTIAL);

    final SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(updateParentSpace);
    assertThat(updated.getFriendlyName()).isEqualTo(spaceId);
    assertThat(updated.getDescription()).isEqualTo(spaceId);
    assertThat(updated.getProperties()).isEmpty();
  }

  @Test
  public void updateSpaceParentFailsIfParentIdDoesNotExist() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final UUID space = createSpace(spaceId);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setRelationships(List.of(Relationship.builder().entityType("spaces").name("parent")
        .targetId("DoesNotExist").build()));

    sendAndAwaitErrorFeedback(testMessage, MessageType.PARTIAL, ErrorCode.ELEMENT_DOES_NOT_EXIST);

    final SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(tenant);
    assertThat(updated.getFriendlyName()).isEqualTo(spaceId);
    assertThat(updated.getDescription()).isEqualTo(spaceId);
    assertThat(updated.getProperties()).isEmpty();
  }

  @Test
  public void createSpaceFull() {
    final String spaceName = RandomStringUtils.randomAlphanumeric(20);

    testSpaceCreationWithParent(spaceName, createSpace(spaceName));
  }

  @Test
  public void createSpaceFullNoParent() {
    testSpaceCreationWithParent(null, null);
  }

  private void testSpaceCreationWithParent(final String parentSpaceName, final UUID parentSpaceId) {

    final String spaceId = RandomStringUtils.randomAlphanumeric(20);
    final String friendlyName = "a test device";
    final String description = "a test descrption";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");

    if (parentSpaceName != null) {
      testMessage.setRelationships(List.of(Relationship.builder().entityType("spaces")
          .name("parent").targetId(parentSpaceName).build()));
    }

    testMessage.setProperties(
        List.of(Property.builder().name(TEST_PROP_KEY).value(TEST_PROP_VALUE).build()));
    testMessage.setAttributes(Map.of("friendlyName", friendlyName, "description", description,
        "type", TEST_SPACE_TYPE, "status", TEST_SPACE_STATUS));

    sendAndAwaitFeedback(testMessage, MessageType.FULL);

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(spacesApi
            .spacesRetrieve(new SpacesApi.SpacesRetrieveQueryParams().name(spaceId.toString())))
                .hasSize(1));

    final SpaceRetrieve created = spacesApi.spacesRetrieve(new SpacesApi.SpacesRetrieveQueryParams()
        .name(spaceId.toString()).includes("properties,description")).get(0);
    assertThat(created.getName()).isEqualTo(spaceId);
    if (parentSpaceId != null) {
      assertThat(created.getParentSpaceId()).isEqualTo(parentSpaceId);
    } else {
      assertThat(created.getParentSpaceId()).isEqualTo(tenant);
    }
    assertThat(created.getTypeId()).isEqualTo(spaceTypeId);
    assertThat(created.getFriendlyName()).isEqualTo(friendlyName);
    assertThat(created.getDescription()).isEqualTo(description);
    assertThat(created.getStatusId()).isEqualTo(spaceStatusId);
    assertThat(created.getProperties()).containsOnly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value(TEST_PROP_VALUE).dataType("string"));
  }

  @Test
  public void creationSpaceMinimal() {
    final String spaceId = RandomStringUtils.randomAlphanumeric(20);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");

    sendAndAwaitFeedback(testMessage, MessageType.FULL);

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(spacesApi
            .spacesRetrieve(new SpacesApi.SpacesRetrieveQueryParams().name(spaceId.toString())))
                .hasSize(1));

    final SpaceRetrieve created = spacesApi.spacesRetrieve(new SpacesApi.SpacesRetrieveQueryParams()
        .name(spaceId.toString()).includes("properties,description")).get(0);
    assertThat(created.getName()).isEqualTo(spaceId);
    assertThat(created.getParentSpaceId()).isEqualTo(tenant);
    assertThat(created.getTypeId()).isEqualTo(getType("None", CategoryEnum.SPACETYPE));
    assertThat(created.getStatusId()).isEqualTo(getType("None", CategoryEnum.SPACESTATUS));
    assertThat(created.getFriendlyName()).isNullOrEmpty();
    assertThat(created.getDescription()).isNullOrEmpty();
    assertThat(created.getProperties()).isEmpty();
  }


}
