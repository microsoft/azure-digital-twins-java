/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.ingress;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import com.microsoft.twins.reflector.model.MessageType;

/**
 * Spring cloud stream {@link Input} for reflector ingress.
 *
 */
public interface ReflectorIngressSink {


  /**
   * Header field for {@link MessageType}.
   */
  String HEADER_MESSAGE_TYPE = "TwinReflectorProxy-MessageType";

  /**
   * Header field for Correlation ID..
   */
  String HEADER_CORRELATION_ID = "TwinReflectorProxy-CorrelationId";

  /**
   * Input channel name.
   */
  String INPUT = "reflectoringress";

  /**
   * @return input channel.
   */
  @Input(ReflectorIngressSink.INPUT)
  SubscribableChannel inputChannel();

}
