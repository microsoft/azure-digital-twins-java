/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface TestIngressSource {
  String INGRESS = "testingress";

  @Output(TestIngressSource.INGRESS)
  MessageChannel ingress();
}
