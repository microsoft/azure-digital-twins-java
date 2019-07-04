/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.topology;

import static org.assertj.core.api.Assertions.assertThat;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.reflector.AbstractIntegrationTest;
import com.microsoft.twins.reflector.ingress.ReflectorIngressSink;
import com.microsoft.twins.reflector.model.IngressMessage;
import com.microsoft.twins.reflector.model.MessageType;
import com.microsoft.twins.reflector.model.Relationship;

public class TopologyUpdaterIT extends AbstractIntegrationTest {

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

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(deviceId);
    testMessage.setEntityType("device");
    testMessage.setRelationships(List.of(
        Relationship.builder().entityType("space").name("parent").targetId(testSpace.toString())
            .build(),
        Relationship.builder().entityType("device").name("gateway").targetId(testGateway.toString())
            .build()));

    final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.FULL.toString().toLowerCase())
        .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

    sink.inputChannel().send(hubMessage);

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .untilAsserted(() -> assertThat(devicesApi.devicesRetrieve(
            new DevicesApi.DevicesRetrieveQueryParams().names(deviceId.toString()))).hasSize(1));

    final DeviceRetrieve created = devicesApi
        .devicesRetrieve(new DevicesApi.DevicesRetrieveQueryParams().names(deviceId.toString()))
        .get(0);
    assertThat(created.getName()).isEqualTo(deviceId);
    assertThat(created.getHardwareId()).isEqualTo(deviceId);
    assertThat(created.getGatewayId()).isEqualTo(testGateway.toString());
    assertThat(created.getSpaceId()).isEqualTo(testSpace);
    assertThat(created.getIoTHubUrl()).isNullOrEmpty();
    assertThat(created.getConnectionString()).isNullOrEmpty();
  }

}
