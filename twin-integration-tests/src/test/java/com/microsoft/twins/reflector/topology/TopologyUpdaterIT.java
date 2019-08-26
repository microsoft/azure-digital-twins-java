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
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.SpacesApi;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.ExtendedPropertyRetrieve;
import com.microsoft.twins.model.SpaceRetrieve;
import com.microsoft.twins.reflector.AbstractIntegrationTest;
import com.microsoft.twins.reflector.ingress.ReflectorIngressSink;
import com.microsoft.twins.reflector.model.IngressMessage;
import com.microsoft.twins.reflector.model.MessageType;
import com.microsoft.twins.reflector.model.Property;
import com.microsoft.twins.reflector.model.Relationship;

public class TopologyUpdaterIT extends AbstractIntegrationTest {

  private static final String TEST_PROP_VALUE = "testValue1";
  private static final String TEST_PROP_KEY = "testName1";
  @Autowired
  private ReflectorIngressSink sink;

  @Test
  public void deleteSpace() {
    final String spaceName = UUID.randomUUID().toString();
    final UUID testSpace = createSpace(spaceName);

    final UUID correlationId = UUID.randomUUID();

    assertThat(spacesApi.spacesRetrieve(new SpacesApi.SpacesRetrieveQueryParams().ids(testSpace)))
        .hasSize(1);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceName);
    testMessage.setEntityType("spaces");

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.DELETE.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(
            spacesApi.spacesRetrieve(new SpacesApi.SpacesRetrieveQueryParams().ids(testSpace)))
                .isEmpty());
  }

  @Test
  public void deleteDevice() {
    final UUID testSpace = createSpace("My Reflector Proxy delete test space");

    final UUID correlationId = UUID.randomUUID();

    final String deviceId = UUID.randomUUID().toString();
    final UUID device = createDevice(deviceId, testGateway, testSpace);

    assertThat(devicesApi.devicesRetrieve(new DevicesApi.DevicesRetrieveQueryParams().ids(device)))
        .hasSize(1);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.DELETE.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(
            devicesApi.devicesRetrieve(new DevicesApi.DevicesRetrieveQueryParams().ids(device)))
                .isEmpty());
  }

  @Test
  public void createDeviceFull() {
    final String spaceName = UUID.randomUUID().toString();
    createDeviceWithGivenParent(spaceName, createSpace(spaceName));
  }

  @Test
  public void createDeviceFullNoParent() {
    createDeviceWithGivenParent(null, null);
  }

  private void createDeviceWithGivenParent(final String parentName, final UUID parent) {
    final UUID correlationId = UUID.randomUUID();
    final String deviceId = UUID.randomUUID().toString();
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
    testMessage.setAttributes(
        Map.of("status", "Provisioned", "friendlyName", friendlyName, "description", description));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.FULL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

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
    final String updatedParentName = UUID.randomUUID().toString();
    final UUID updatedParent = createSpace(updatedParentName);
    final UUID correlationId = UUID.randomUUID();

    final String updatedGatewayName = UUID.randomUUID().toString();
    final UUID updatedGateway = createGateway(updatedGatewayName, tenant);

    final String deviceId = UUID.randomUUID().toString();
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
    testMessage.setAttributes(
        Map.of("status", "Active", "friendlyName", friendlyName, "description", description));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.FULL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

    final DeviceRetrieve updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(deviceId);
    assertThat(updated.getHardwareId()).isEqualTo(deviceId);
    assertThat(updated.getGatewayId()).isEqualTo(updatedGateway);
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
  public void updateDeviceGateway() {
    final UUID correlationId = UUID.randomUUID();
    final String deviceId = UUID.randomUUID().toString();

    final String updatedGatewayName = UUID.randomUUID().toString();
    final UUID updatedGateway = createGateway(updatedGatewayName, tenant);

    final UUID device = createDevice(deviceId);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");

    testMessage.setRelationships(List.of(Relationship.builder().entityType("devices")
        .name("gateway").targetId(updatedGatewayName).build()));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.PARTIAL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

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
  public void updateDeviceUpdateProperty() {
    final UUID correlationId = UUID.randomUUID();
    final String deviceId = UUID.randomUUID().toString();


    final UUID device = createDevice(deviceId);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.PARTIAL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();
    testMessage
        .setProperties(List.of(Property.builder().name(TEST_PROP_KEY).value("newValue").build()));

    sink.inputChannel().send(hubMessage);

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

    sink.inputChannel().send(hubMessage);

    updated = devicesApi.devicesRetrieve(
        new DevicesApi.DevicesRetrieveQueryParams().ids(device).includes("properties,description"))
        .get(0);
    assertThat(updated.getProperties()).containsOnly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value("updatedValue").dataType("string"));


    // Add another property
    testMessage.setProperties(
        List.of(Property.builder().name(TEST_PROP_KEY + "_2").value("newValue").build()));

    sink.inputChannel().send(hubMessage);

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
    final UUID correlationId = UUID.randomUUID();
    final String deviceId = UUID.randomUUID().toString();

    final String updatedParentName = UUID.randomUUID().toString();
    final UUID updatedParent = createSpace(updatedParentName);

    final UUID device = createDevice(deviceId);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");

    testMessage.setRelationships(List.of(Relationship.builder().entityType("spaces").name("parent")
        .targetId(updatedParentName).build()));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.PARTIAL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);


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
    final UUID correlationId = UUID.randomUUID();
    final String deviceId = UUID.randomUUID().toString();
    final UUID device = createDevice(deviceId);

    final String description = "updated description";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");
    testMessage.setAttributes(Map.of("description", description));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.PARTIAL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);


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
  public void updateSpaceFull() {
    final String spaceId = UUID.randomUUID().toString();
    final UUID space = createSpace(spaceId);
    final UUID correlationId = UUID.randomUUID();
    final String updateParentSpaceName = UUID.randomUUID().toString();
    final UUID updateParentSpace = createSpace(updateParentSpaceName);

    final String updateChildSpaceName = UUID.randomUUID().toString();
    final UUID updateChildSpaceId = createSpace(updateChildSpaceName);

    final String updateChildDeviceName = UUID.randomUUID().toString();
    final UUID updateChildDeviceId = createDevice(updateChildDeviceName, testGateway,
        createSpace("My Reflector Proxy original parent space"));

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
    testMessage.setAttributes(Map.of("friendlyName", friendlyName, "description", description));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.FULL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

    final SpaceRetrieve updated = spacesApi
        .spacesRetrieve(
            new SpacesApi.SpacesRetrieveQueryParams().ids(space).includes("properties,description"))
        .get(0);
    assertThat(updated.getName()).isEqualTo(spaceId);
    assertThat(updated.getParentSpaceId()).isEqualTo(updateParentSpace);
    assertThat(updated.getFriendlyName()).isEqualTo(friendlyName);
    assertThat(updated.getDescription()).isEqualTo(description);
    assertThat(updated.getProperties()).containsOnly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value("updatedValue").dataType("string"));

    assertThat(getDevice(updateChildDeviceId)).isNotEmpty()
        .hasValueSatisfying(child -> child.getSpaceId().equals(space));

    assertThat(getSpace(updateChildSpaceId)).isNotEmpty()
        .hasValueSatisfying(child -> child.getParentSpaceId().equals(space));
  }

  @Test
  public void updateSpaceFriendlyName() {
    final String spaceId = UUID.randomUUID().toString();
    final UUID space = createSpace(spaceId);
    final UUID correlationId = UUID.randomUUID();

    final String friendlyName = "updated name";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setAttributes(Map.of("friendlyName", friendlyName));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.PARTIAL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

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
  public void updateSpaceProperty() {
    final String spaceId = UUID.randomUUID().toString();
    final UUID space = createSpace(spaceId);
    final UUID correlationId = UUID.randomUUID();

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");

    testMessage
        .setProperties(List.of(Property.builder().name(TEST_PROP_KEY).value("newValue").build()));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.PARTIAL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

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
    sink.inputChannel().send(hubMessage);

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
    sink.inputChannel().send(hubMessage);

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
    final String spaceId = UUID.randomUUID().toString();
    final UUID space = createSpace(spaceId);
    final UUID correlationId = UUID.randomUUID();

    final String updateChildSpaceName = UUID.randomUUID().toString();
    final UUID updateChildSpaceId = createSpace(updateChildSpaceName);


    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setRelationships(List.of(Relationship.builder().entityType("spaces").name("child")
        .targetId(updateChildSpaceName).build()));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.FULL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

    final SpaceRetrieve updated = spacesApi
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
  }

  @Test
  public void updateSpaceChildDevice() {
    final String spaceId = UUID.randomUUID().toString();
    final UUID space = createSpace(spaceId);
    final UUID correlationId = UUID.randomUUID();

    final String updateChildDeviceName = UUID.randomUUID().toString();
    final UUID updateChildDeviceId = createDevice(updateChildDeviceName, testGateway,
        createSpace("My Reflector Proxy original parent space"));


    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setRelationships(List.of(Relationship.builder().entityType("devices").name("child")
        .targetId(updateChildDeviceName).build()));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.FULL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

    final SpaceRetrieve updated = spacesApi
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
  }

  @Test
  public void updateSpaceParent() {
    final String spaceId = UUID.randomUUID().toString();
    final UUID space = createSpace(spaceId);
    final UUID correlationId = UUID.randomUUID();

    final String updateParentSpaceName = UUID.randomUUID().toString();
    final UUID updateParentSpace = createSpace(updateParentSpaceName);


    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(spaceId);
    testMessage.setEntityType("spaces");
    testMessage.setRelationships(List.of(Relationship.builder().entityType("spaces").name("parent")
        .targetId(updateParentSpaceName).build()));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.FULL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

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
  public void createSpaceFull() {
    final String spaceName = UUID.randomUUID().toString();

    testSpaceCreationWithParent(spaceName, createSpace(spaceName));
  }

  @Test
  public void createSpaceFullNoParent() {
    testSpaceCreationWithParent(null, null);
  }

  private void testSpaceCreationWithParent(final String parentSpaceName, final UUID parentSpaceId) {
    final UUID correlationId = UUID.randomUUID();
    final String spaceId = UUID.randomUUID().toString();
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
    testMessage.setAttributes(Map.of("friendlyName", friendlyName, "description", description));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.FULL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

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
    assertThat(created.getFriendlyName()).isEqualTo(friendlyName);
    assertThat(created.getDescription()).isEqualTo(description);
    assertThat(created.getProperties()).containsOnly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value(TEST_PROP_VALUE).dataType("string"));
  }


}
