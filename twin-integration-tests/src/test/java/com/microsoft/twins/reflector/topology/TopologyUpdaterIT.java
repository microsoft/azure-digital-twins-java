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
    assertThat(created.getProperties()).containsExactly(new ExtendedPropertyRetrieve()
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

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(devicesApi.devicesRetrieve(
            new DevicesApi.DevicesRetrieveQueryParams().names(deviceId.toString()))).hasSize(1));

    final DeviceRetrieve created =
        devicesApi.devicesRetrieve(new DevicesApi.DevicesRetrieveQueryParams()
            .names(deviceId.toString()).includes("properties,description")).get(0);
    assertThat(created.getName()).isEqualTo(deviceId);
    assertThat(created.getHardwareId()).isEqualTo(deviceId);
    assertThat(created.getGatewayId()).isEqualTo(updatedGateway);
    assertThat(created.getSpaceId()).isEqualTo(updatedParent);
    assertThat(created.getIoTHubUrl()).isNullOrEmpty();
    assertThat(created.getConnectionString()).isNullOrEmpty();
    assertThat(created.getProperties()).containsExactly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value("updatedValue").dataType("string"));
    assertThat(created.getFriendlyName()).isEqualTo(friendlyName);
    assertThat(created.getDescription()).isEqualTo(description);
    assertThat(created.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.ACTIVE);

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

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(spacesApi
            .spacesRetrieve(new SpacesApi.SpacesRetrieveQueryParams().name(spaceId.toString())))
                .hasSize(1));

    final SpaceRetrieve created = spacesApi.spacesRetrieve(new SpacesApi.SpacesRetrieveQueryParams()
        .name(spaceId.toString()).includes("properties,description")).get(0);
    assertThat(created.getName()).isEqualTo(spaceId);
    assertThat(created.getParentSpaceId()).isEqualTo(updateParentSpace);
    assertThat(created.getFriendlyName()).isEqualTo(friendlyName);
    assertThat(created.getDescription()).isEqualTo(description);
    assertThat(created.getProperties()).containsExactly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value("updatedValue").dataType("string"));

    assertThat(getDevice(updateChildDeviceId)).isNotEmpty()
        .hasValueSatisfying(child -> child.getSpaceId().equals(space));

    assertThat(getSpace(updateChildSpaceId)).isNotEmpty()
        .hasValueSatisfying(child -> child.getParentSpaceId().equals(space));
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
    assertThat(created.getProperties()).containsExactly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value(TEST_PROP_VALUE).dataType("string"));
  }


}
