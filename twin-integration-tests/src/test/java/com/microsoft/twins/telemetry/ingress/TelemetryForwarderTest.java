/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.telemetry.ingress;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.microsoft.twins.spring.configuration.DigitalTwinClientAutoConfiguration;

@ContextConfiguration(classes = {DigitalTwinClientAutoConfiguration.class, TelemetryIngressAutoConfiguration.class,
    TestConfiguration.class})
public class TelemetryForwarderTest extends AbstractTest {

  @Autowired
  private TelemetryForwarder telemetryForwarderUnderTest;

  @Autowired
  private ListenToIngressSampler listToIngress;

  @Test
  public void testSendMessages()
      throws IllegalArgumentException, IOException, URISyntaxException, InterruptedException {

    // TODO use/test device-gateway connection string with sensor

    // TODO test correlationId
    final Set<String> sendMessages = new HashSet<>();
    for (int i = 0; i < 10; i++) {
      final String deviceName = UUID.randomUUID().toString();

      final UUID testDevice = createDevice(deviceName);

      for (int x = 0; x < 10; x++) {
        final String message = UUID.randomUUID().toString();

        telemetryForwarderUnderTest.sendMessage(message, testDevice, message);
        sendMessages.add(message);
      }
    }

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .until(() -> listToIngress.getReceivedDeviceMessages().containsAll(sendMessages));

  }
}
