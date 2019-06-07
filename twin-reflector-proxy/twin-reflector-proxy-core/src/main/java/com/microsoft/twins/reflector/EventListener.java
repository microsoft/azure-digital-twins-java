/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;

public class EventListener {
  @StreamListener(Sink.INPUT)
  void getEvent(final String event) {


  }
}
