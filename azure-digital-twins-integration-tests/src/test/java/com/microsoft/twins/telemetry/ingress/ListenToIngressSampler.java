/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.telemetry.ingress;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
public class ListenToIngressSampler {
  private final Set<String> receivedDeviceMessages = Collections.synchronizedSet(new HashSet<>());

  @StreamListener(Sink.INPUT)
  void getEvent(final String event) {
    receivedDeviceMessages.add(event);
  }


  Set<String> getReceivedDeviceMessages() {
    return receivedDeviceMessages;
  }



}
