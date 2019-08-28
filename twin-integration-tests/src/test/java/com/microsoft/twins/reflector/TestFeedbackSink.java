/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;

public interface TestFeedbackSink {
  String FEEDBACK = "testfeedback";

  @Input(TestFeedbackSink.FEEDBACK)
  SubscribableChannel inputChannel();
}
