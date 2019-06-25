/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.telemetry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ListenToIngressSampler {
  private final Set<TestMessage> receivedDeviceMessages = Collections.synchronizedSet(new HashSet<>());

  private final AmqpDeserializer amqpDeserializer = new AmqpDeserializer();

  @StreamListener(Sink.INPUT)
  void getEvent(final Message<String> event) {
    final String hardwareId =
        amqpDeserializer.deserializeString((byte[]) event.getHeaders().get("DigitalTwins-SensorHardwareId"));

    log.debug("Got event {} for sensor {}", event.getPayload(), hardwareId);

    receivedDeviceMessages.add(new TestMessage(hardwareId, event.getPayload()));
  }

  public Set<TestMessage> getReceivedDeviceMessages() {
    return receivedDeviceMessages;
  }

}
