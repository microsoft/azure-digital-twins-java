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
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import com.microsoft.twins.reflector.TwinReflectorProxyAutoConfiguration;
import com.microsoft.twins.spring.configuration.DigitalTwinClientAutoConfiguration;

@ContextConfiguration(classes = {DigitalTwinClientAutoConfiguration.class, TwinReflectorProxyAutoConfiguration.class,
    TestConfiguration.class})
public class TelemetryForwarderTest extends AbstractTest {

  @Autowired
  private TelemetryForwarder telemetryForwarderUnderTest;

  @Autowired
  private ListenToIngressSampler listToIngress;

  private UUID testGateway;

  @Before
  public void createGateway() {
    final String gatewayName = UUID.randomUUID().toString();
    testGateway = createGateway(gatewayName, tenant);
  }

  @Test
  public void testSendMessages()
      throws IllegalArgumentException, IOException, URISyntaxException, InterruptedException {

    final UUID testSpace = createSpace("My test space");
    final Set<TestMessage> sendMessages = new HashSet<>();

    for (int i = 0; i < 10; i++) {
      final String message = RandomStringUtils.randomAlphanumeric(20);
      final UUID correlationId = UUID.randomUUID();

      final String deviceId = UUID.randomUUID().toString();
      final UUID device = createDevice(deviceId, testGateway, testSpace);

      final String hardwareId = UUID.randomUUID().toString();
      createSensor(hardwareId, device, testSpace);

      telemetryForwarderUnderTest.sendMessage(message, testGateway, correlationId, hardwareId);
      sendMessages.add(new TestMessage(hardwareId, message));
    }

    Awaitility.await().atMost(1, TimeUnit.MINUTES).pollDelay(50, TimeUnit.MILLISECONDS)
        .pollInterval(1, TimeUnit.SECONDS)
        .until(() -> listToIngress.getReceivedDeviceMessages().containsAll(sendMessages));

  }
}
