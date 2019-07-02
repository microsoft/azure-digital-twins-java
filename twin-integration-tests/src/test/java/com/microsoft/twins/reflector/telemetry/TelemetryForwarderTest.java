/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.telemetry;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomStringUtils;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.Message;
import com.microsoft.twins.reflector.AbstractTest;
import com.microsoft.twins.reflector.ListenToIngressSampler;
import com.microsoft.twins.reflector.TestMessage;
import com.microsoft.twins.reflector.ingress.ReflectorIngressSink;
import com.microsoft.twins.reflector.model.IngressMessage;
import com.microsoft.twins.reflector.model.MessageType;

public class TelemetryForwarderTest extends AbstractTest {

  @Autowired
  private TelemetryForwarder telemetryForwarderUnderTest;

  @Autowired
  private ListenToIngressSampler listToIngress;

  @Autowired
  private ReflectorIngressSink sink;

  @Test
  public void sendTelemetryMessagesThroughIoTHub()
      throws IllegalArgumentException, IOException, URISyntaxException, InterruptedException {

    final UUID testSpace = createSpace("My IoT Hub test space");
    final Set<TestMessage> sendMessages = new HashSet<>();

    for (int i = 0; i < 10; i++) {
      final String payload = RandomStringUtils.randomAlphanumeric(20);
      final UUID correlationId = UUID.randomUUID();

      final String deviceId = UUID.randomUUID().toString();
      final UUID device = createDevice(deviceId, testGateway, testSpace);

      final String hardwareId = UUID.randomUUID().toString();
      createSensor(hardwareId, device, testSpace);

      telemetryForwarderUnderTest.sendMessage(payload, correlationId, hardwareId);
      sendMessages.add(new TestMessage(hardwareId, payload, testGateway));
    }


    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .until(() -> listToIngress.getReceivedDeviceMessages().containsAll(sendMessages));

  }

  @Test
  public void sendTelemetryMessagesThroughReflectorProxy()
      throws IllegalArgumentException, IOException, URISyntaxException, InterruptedException {

    final UUID testSpace = createSpace("My Reflector Proxy test space");
    final Set<TestMessage> sendMessages = new HashSet<>();

    for (int i = 0; i < 10; i++) {


      final String payload = RandomStringUtils.randomAlphanumeric(20);
      final UUID correlationId = UUID.randomUUID();

      final String deviceId = UUID.randomUUID().toString();
      final UUID device = createDevice(deviceId, testGateway, testSpace);

      final String hardwareId = UUID.randomUUID().toString();
      createSensor(hardwareId, device, testSpace);


      final IngressMessage testMessage = new IngressMessage();
      testMessage.setId(hardwareId);
      testMessage.setTelemetry(payload);

      final Message<IngressMessage> hubMessage = MessageBuilder.withPayload(testMessage)
          .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE, MessageType.FULL.toString().toLowerCase())
          .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build();

      sink.inputChannel().send(hubMessage);

      sendMessages.add(new TestMessage(hardwareId, payload, testGateway));
    }

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .until(() -> listToIngress.getReceivedDeviceMessages().containsAll(sendMessages));

  }
}
