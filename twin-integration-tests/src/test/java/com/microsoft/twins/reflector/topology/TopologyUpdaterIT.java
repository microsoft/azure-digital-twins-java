/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.topology;

import static org.assertj.core.api.Assertions.assertThat;
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
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.ExtendedPropertyRetrieve;
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
  public void deleteDevice() {
    final UUID testSpace = createSpace("My Reflector Proxy delete test space");

    final UUID correlationId = UUID.randomUUID();

    final String deviceId = UUID.randomUUID().toString();
    final UUID device = createDevice(deviceId, testGateway, testSpace);

    assertThat(devicesApi
        .devicesRetrieve(new DevicesApi.DevicesRetrieveQueryParams().ids(device.toString())))
            .hasSize(1);

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.DELETE.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(devicesApi
            .devicesRetrieve(new DevicesApi.DevicesRetrieveQueryParams().ids(device.toString())))
                .isEmpty());
  }

  @Test
  public void createDeviceFull() {
    final UUID testSpace = createSpace("My Reflector Proxy create test space");

    final UUID correlationId = UUID.randomUUID();

    final String deviceId = UUID.randomUUID().toString();

    final String friendlyName = "a test device";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("devices");
    testMessage.setRelationships(List.of(
        Relationship.builder().entityType("spaces").name("parent").targetId(testSpace.toString())
            .build(),
        Relationship.builder().entityType("devices").name("gateway")
            .targetId(testGateway.toString()).build()));
    testMessage.setProperties(
        List.of(Property.builder().name(TEST_PROP_KEY).value(TEST_PROP_VALUE).build()));

    testMessage.setAttributes(Map.of("status", "Provisioned", "friendlyName", friendlyName));

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
            .names(deviceId.toString()).includes("properties")).get(0);
    assertThat(created.getName()).isEqualTo(deviceId);
    assertThat(created.getHardwareId()).isEqualTo(deviceId);
    assertThat(created.getGatewayId()).isEqualTo(testGateway.toString());
    assertThat(created.getSpaceId()).isEqualTo(testSpace);
    assertThat(created.getIoTHubUrl()).isNullOrEmpty();
    assertThat(created.getConnectionString()).isNullOrEmpty();
    assertThat(created.getProperties()).containsExactly(new ExtendedPropertyRetrieve()
        .name(TEST_PROP_KEY).value(TEST_PROP_VALUE).dataType("string"));
    assertThat(created.getFriendlyName()).isEqualTo(friendlyName);
    assertThat(created.getStatus()).isEqualTo(DeviceRetrieve.StatusEnum.PROVISIONED);

  }

}
