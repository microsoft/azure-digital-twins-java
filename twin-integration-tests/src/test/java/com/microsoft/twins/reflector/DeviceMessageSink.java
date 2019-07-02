/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface DeviceMessageSink {
  /**
   * Input channel name.
   */
  String INPUT = "devices";

  /**
   * @return input channel.
   */
  @Input(DeviceMessageSink.INPUT)
  SubscribableChannel inputChannel();

}
