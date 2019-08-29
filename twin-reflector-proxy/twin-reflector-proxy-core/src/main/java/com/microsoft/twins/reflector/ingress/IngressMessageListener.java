/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.ingress;

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.MDC;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.validation.annotation.Validated;
import com.microsoft.twins.event.model.TopologyOperationEvent;
import com.microsoft.twins.reflector.error.AbstractIngressFailedException;
import com.microsoft.twins.reflector.model.FeedbackMessage;
import com.microsoft.twins.reflector.model.FeedbackMessage.FeedbackMessageBuilder;
import com.microsoft.twins.reflector.model.IngressMessage;
import com.microsoft.twins.reflector.model.Status;
import com.microsoft.twins.reflector.telemetry.TelemetryForwarder;
import com.microsoft.twins.reflector.topology.TopologyUpdater;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Validated
public class IngressMessageListener {
  private static final String LOG_CORRELATION_ID = "correlationId";
  private final TopologyUpdater topologyUpdater;
  private final TelemetryForwarder telemetryForwarder;

  @StreamListener(ReflectorIngressSink.INPUT)
  @SendTo(FeedbackSource.FEEDBACK)
  FeedbackMessage getIngress(@NotNull @Valid @Payload final IngressMessage message,
      @Header(name = ReflectorIngressSink.HEADER_MESSAGE_TYPE,
          required = true) final String messageType,
      @Header(name = ReflectorIngressSink.HEADER_CORRELATION_ID,
          required = false) final UUID correlationId) {

    if (correlationId != null) {
      MDC.put(LOG_CORRELATION_ID, correlationId.toString());
    }

    log.trace("Got ingress message {}", message);

    try {
      if (messageType.equalsIgnoreCase("full")) {
        return getCompleteTopologyUpdate(message, correlationId);
      } else if (messageType.equalsIgnoreCase("partial")) {
        return getPartialTopologyUpdate(message, correlationId);
      } else if (messageType.equalsIgnoreCase("delete")) {
        return getDeleteTopologyElement(message, correlationId);
      } else {
        log.error("Got message with unknown messageType [{}]", messageType);
        return null;
      }
    } finally {
      MDC.clear();
    }
  }

  private FeedbackMessage getPartialTopologyUpdate(final IngressMessage message,
      final UUID correlationId) {

    topologyUpdater.updateTopologyElementPartial(message, correlationId);

    if (message.getTelemetry() != null) {
      telemetryForwarder.sendMessage(String.valueOf(message.getTelemetry()), correlationId,
          message.getId());
    }

    return FeedbackMessage.builder().correlationId(correlationId).status(Status.PROCESSED).build();
  }

  private FeedbackMessage getCompleteTopologyUpdate(final IngressMessage message,
      final UUID correlationId) {

    if (correlationId != null) {
      MDC.put(LOG_CORRELATION_ID, correlationId.toString());
    }

    log.trace("Got complete update ingress message {}", message);

    topologyUpdater.updateTopologyElementComplete(message, correlationId);

    if (message.getTelemetry() != null) {
      telemetryForwarder.sendMessage(String.valueOf(message.getTelemetry()), correlationId,
          message.getId());
    }

    MDC.clear();

    return FeedbackMessage.builder().correlationId(correlationId).status(Status.PROCESSED).build();
  }


  private FeedbackMessage getDeleteTopologyElement(final IngressMessage message,
      final UUID correlationId) {

    topologyUpdater.deleteTopologyElement(message.getId(), correlationId, message.getEntityType());

    return FeedbackMessage.builder().correlationId(correlationId).status(Status.PROCESSED).build();
  }


  @StreamListener("errorChannel")
  @SendTo(FeedbackSource.FEEDBACK)
  FeedbackMessage error(final ErrorMessage message) {
    if (message.getOriginalMessage().getPayload().getClass() == IngressMessage.class) {
      final FeedbackMessageBuilder response = FeedbackMessage.builder()
          .correlationId(UUID.fromString(
              (String) message.getHeaders().get(ReflectorIngressSink.HEADER_CORRELATION_ID)))
          .status(Status.ERROR);

      if (message.getPayload() instanceof AbstractIngressFailedException) {
        response.errorCode(((AbstractIngressFailedException) message.getPayload()).getErrorCode());
      }

      response.errorMessage(message.getPayload().getMessage());

      return response.build();
    } else if (message.getOriginalMessage().getPayload()
        .getClass() == TopologyOperationEvent.class) {
      log.error("Failed to invalidate cache for topology item [{}]",
          ((TopologyOperationEvent) message.getOriginalMessage().getPayload()).getId());
    }

    return null;

  }
}
