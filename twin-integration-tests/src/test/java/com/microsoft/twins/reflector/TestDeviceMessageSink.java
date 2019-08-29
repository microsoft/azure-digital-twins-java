/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface TestDeviceMessageSink {
  /**
   * Input channel name.
   */
  String INPUT = "devices";

  /**
   * @return input channel.
   */
  @Input(TestDeviceMessageSink.INPUT)
  SubscribableChannel inputChannel();

}
