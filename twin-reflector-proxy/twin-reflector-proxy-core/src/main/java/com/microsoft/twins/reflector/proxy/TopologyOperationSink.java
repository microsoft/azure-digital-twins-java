/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy;

import org.springframework.cloud.stream.annotation.Input;
import org.springframework.messaging.SubscribableChannel;
import com.microsoft.twins.model.EndpointCreate.EventTypesEnum;

/**
 * Spring cloud stream {@link Input} for {@link EventTypesEnum#TOPOLOGYOPERATION}s.
 *
 */
public interface TopologyOperationSink {

  /**
   * Input channel name.
   */
  String INPUT = "topologyoperations";

  /**
   * @return input channel.
   */
  @Input(TopologyOperationSink.INPUT)
  SubscribableChannel inputChannel();

}
