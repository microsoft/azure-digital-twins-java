/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import java.util.UUID;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import com.microsoft.twins.reflector.model.IngressMessage;
import com.microsoft.twins.reflector.model.MessageType;

public class IngressMessageListenerTest extends AbstractTest {

  @Autowired
  private ReflectorIngressSink sink;

  @Autowired
  private TestConfiguration testConfiguration;

  @Test
  public void partialUpdateCalled() {
    final String testId = UUID.randomUUID().toString();

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(testId);

    sink.input().send(MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE, MessageType.partial.toString()).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000)).updateTopologyElementPartial(testMessage, null);

    final UUID correlationId = UUID.randomUUID();
    sink.input()
        .send(MessageBuilder.withPayload(testMessage)
            .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE, MessageType.partial.toString())
            .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000)).updateTopologyElementPartial(testMessage,
        correlationId);
  }

  @Test
  public void completeUpdateCalled() {
    final String testId = UUID.randomUUID().toString();

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(testId);

    sink.input().send(MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE, MessageType.full.toString()).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000)).updateTopologyElementComplete(testMessage, null);

    final UUID correlationId = UUID.randomUUID();
    sink.input()
        .send(MessageBuilder.withPayload(testMessage)
            .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE, MessageType.full.toString())
            .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000)).updateTopologyElementComplete(testMessage,
        correlationId);
  }

  @Test
  public void deleteElementCalled() {
    final String testId = UUID.randomUUID().toString();

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(testId);

    sink.input().send(MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE, MessageType.delete.toString()).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000)).deleteTopologyElement(testId, null);

    final UUID correlationId = UUID.randomUUID();
    sink.input()
        .send(MessageBuilder.withPayload(testMessage)
            .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE, MessageType.delete.toString())
            .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000)).deleteTopologyElement(testId, correlationId);
  }

  @Test
  public void deleteElementFailsIfIdNotProvided() {
    final IngressMessage testMessage = new IngressMessage();

    assertThatExceptionOfType(MethodArgumentNotValidException.class)
        .isThrownBy(() -> sink.input().send(MessageBuilder.withPayload(testMessage)
            .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE, MessageType.delete.toString()).build()));
    verify(testConfiguration.getTopologyUpdater(), never()).deleteTopologyElement(any(), any());
  }

  @Test
  public void completeUpdateWithTelemetryIngressCalled() {
    final String testId = UUID.randomUUID().toString();
    final String testTelemetry = "test";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(testId);
    testMessage.setTelemetry(testTelemetry);

    sink.input().send(MessageBuilder.withPayload(testMessage)
        .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE, MessageType.full.toString()).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000)).updateTopologyElementComplete(testMessage, null);

    verify(testConfiguration.getTelemetryForwarder(), timeout(2_000)).sendMessage(eq(testTelemetry), isNull(),
        eq(testId));

    final UUID correlationId = UUID.randomUUID();
    sink.input()
        .send(MessageBuilder.withPayload(testMessage)
            .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE, MessageType.full.toString())
            .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000)).updateTopologyElementComplete(testMessage,
        correlationId);
    verify(testConfiguration.getTelemetryForwarder(), timeout(2_000)).sendMessage(eq(testTelemetry), eq(correlationId),
        eq(testId));
  }

}
