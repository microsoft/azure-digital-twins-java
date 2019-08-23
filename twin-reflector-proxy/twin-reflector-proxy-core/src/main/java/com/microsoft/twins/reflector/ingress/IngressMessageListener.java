/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.ingress;

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.validation.annotation.Validated;
import com.microsoft.twins.reflector.model.IngressMessage;
import com.microsoft.twins.reflector.telemetry.TelemetryForwarder;
import com.microsoft.twins.reflector.topology.TopologyUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Validated
public class IngressMessageListener {
  private static final String HEADER_CORRELATION_ID = "TwinReflectorProxy-CorrelationId";
  private final TopologyUpdater topologyUpdater;
  private final TelemetryForwarder telemetryForwarder;

  @StreamListener(target = ReflectorIngressSink.INPUT,
      condition = "headers['TwinReflectorProxy-MessageType']=='partial'")
  void getPartialTopologyUpdate(@NotNull @Valid @Payload final IngressMessage message,
      @Header(name = HEADER_CORRELATION_ID, required = false) final UUID correlationId) {
    topologyUpdater.updateTopologyElementPartial(message, correlationId);

    if (message.getTelemetry() != null) {
      telemetryForwarder.sendMessage(String.valueOf(message.getTelemetry()), correlationId,
          message.getId());
    }
  }

  @StreamListener(target = ReflectorIngressSink.INPUT,
      condition = "headers['TwinReflectorProxy-MessageType']=='full'")
  void getCompleteTopologyUpdate(@NotNull @Valid @Payload final IngressMessage message,
      @Header(name = HEADER_CORRELATION_ID, required = false) final UUID correlationId) {
    topologyUpdater.updateTopologyElementComplete(message, correlationId);

    if (message.getTelemetry() != null) {
      telemetryForwarder.sendMessage(String.valueOf(message.getTelemetry()), correlationId,
          message.getId());
    }
  }

  @StreamListener(target = ReflectorIngressSink.INPUT,
      condition = "headers['TwinReflectorProxy-MessageType']=='delete'")
  void getDeleteTopologyElement(@NotNull @Valid @Payload final IngressMessage message,
      @Header(name = HEADER_CORRELATION_ID, required = false) final UUID correlationId) {
    topologyUpdater.deleteTopologyElement(message.getId(), correlationId, message.getEntityType());
  }

}
