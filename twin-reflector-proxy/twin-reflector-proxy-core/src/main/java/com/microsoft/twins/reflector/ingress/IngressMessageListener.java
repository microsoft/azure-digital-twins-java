/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.ingress;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.support.ErrorMessage;
import org.springframework.validation.annotation.Validated;
import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.core.dependencies.google.common.collect.Maps;
import com.microsoft.twins.CorrelationIdContext;
import com.microsoft.twins.reflector.TwinReflectorProxyProperties;
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
  private static final String LOG_TYPE = "type";
  private static final String DEVICE_ID = "deviceId";

  private final TopologyUpdater topologyUpdater;
  private final TelemetryForwarder telemetryForwarder;
  private final TwinReflectorProxyProperties properties;

  @Autowired(required = false)
  private TelemetryClient telemetryClient;

  @StreamListener(ReflectorIngressSink.INPUT)
  @SendTo(FeedbackSource.OUTPUT)
  FeedbackMessage getIngress(@NotNull @Valid @Payload final IngressMessage message,
      @Header(name = ReflectorIngressSink.HEADER_MESSAGE_TYPE,
          required = true) final String messageType,
      @Header(name = ReflectorIngressSink.HEADER_CORRELATION_ID,
          required = false) final UUID correlationId) {

    trackIngress(messageType, correlationId, message.getId());

    if (correlationId != null) {
      MDC.put(LOG_CORRELATION_ID, correlationId.toString());
      CorrelationIdContext.setCorrelationId(correlationId);
    }

    log.trace("Got ingress message {}", message);

    try {

      FeedbackMessage response = null;
      if ("full".equalsIgnoreCase(messageType)) {
        response = getCompleteTopologyUpdate(message, correlationId);
      } else if ("partial".equalsIgnoreCase(messageType)) {
        response = getPartialTopologyUpdate(message, correlationId);
      } else if ("delete".equalsIgnoreCase(messageType)) {
        response = getDeleteTopologyElement(message, correlationId);
      } else {
        log.error("Got message with unknown messageType [{}]", messageType);
      }

      if (properties.getEventHubs().getFeedback().isEnabled()) {
        return response;
      }

    } finally {
      MDC.clear();
      CorrelationIdContext.clear();
    }

    return null;
  }

  private void trackIngress(final String messageType, final UUID correlationId,
      final String deviceID) {
    if (telemetryClient == null) {
      return;
    }

    final Map<String, String> eventProperties = Maps.newHashMapWithExpectedSize(3);
    eventProperties.put(LOG_TYPE, messageType);
    eventProperties.put(DEVICE_ID, deviceID);
    if (correlationId != null) {
      eventProperties.put(LOG_CORRELATION_ID, correlationId.toString());
    }

    telemetryClient.trackEvent("ingress", eventProperties, null);
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
  @SendTo(FeedbackSource.OUTPUT)
  FeedbackMessage error(final ErrorMessage message) {
    if (properties.getEventHubs().getFeedback().isEnabled() && message.getOriginalMessage()
        .getHeaders().containsKey(ReflectorIngressSink.HEADER_CORRELATION_ID)) {

      final UUID correlationId = UUID.fromString((String) message.getOriginalMessage().getHeaders()
          .get(ReflectorIngressSink.HEADER_CORRELATION_ID));

      final FeedbackMessageBuilder response =
          FeedbackMessage.builder().correlationId(correlationId).status(Status.ERROR);

      if (message.getPayload() instanceof MessagingException
          && ((MessagingException) message.getPayload())
              .getCause() instanceof AbstractIngressFailedException) {

        final AbstractIngressFailedException exception =
            (AbstractIngressFailedException) ((MessagingException) message.getPayload()).getCause();

        trackException(exception, correlationId);

        response.errorCode(exception.getErrorCode());
        response.errorMessage(exception.getMessage());
      }

      return response.build();
    }

    return null;
  }

  private void trackException(final AbstractIngressFailedException exception,
      final UUID correlationId) {
    if (telemetryClient == null) {
      return;
    }

    final Map<String, String> properties = new HashMap<>();
    if (correlationId != null) {
      properties.put(LOG_CORRELATION_ID, correlationId.toString());
    }

    telemetryClient.trackException(exception, properties, null);

  }
}
