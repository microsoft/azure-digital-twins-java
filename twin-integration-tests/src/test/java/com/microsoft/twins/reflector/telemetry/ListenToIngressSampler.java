/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.telemetry;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.Message;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ListenToIngressSampler {
  private final Set<TestMessage> receivedDeviceMessages = Collections.synchronizedSet(new HashSet<>());

  private final AmqpDeserializer amqpDeserializer = new AmqpDeserializer();

  @StreamListener(DeviceMessageSink.INPUT)
  void getEvent(final Message<String> event) {
    final String hardwareId =
        amqpDeserializer.deserializeString((byte[]) event.getHeaders().get("DigitalTwins-SensorHardwareId"));
    final String deviceId =
        amqpDeserializer.deserializeString((byte[]) event.getHeaders().get("iothub-connection-device-id"));
    final String source = amqpDeserializer.deserializeString((byte[]) event.getHeaders().get("iothub-message-source"));
    final String adtIsTelemetry =
        amqpDeserializer.deserializeString((byte[]) event.getHeaders().get("DigitalTwins-Telemetry"));


    log.info("Got testevent {} for sensor {} through device {} by source {} and marked by ADT as telemetry: {}",
        event.getPayload(), hardwareId, deviceId, source, adtIsTelemetry);

    receivedDeviceMessages.add(new TestMessage(hardwareId, event.getPayload(), UUID.fromString(deviceId)));
  }

  public Set<TestMessage> getReceivedDeviceMessages() {
    return receivedDeviceMessages;
  }

}
