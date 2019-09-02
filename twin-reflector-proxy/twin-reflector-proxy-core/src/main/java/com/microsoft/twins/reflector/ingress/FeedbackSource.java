/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.ingress;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface FeedbackSource {
  String OUTPUT = "feedback";

  @Output(FeedbackSource.OUTPUT)
  MessageChannel feedback();
}
