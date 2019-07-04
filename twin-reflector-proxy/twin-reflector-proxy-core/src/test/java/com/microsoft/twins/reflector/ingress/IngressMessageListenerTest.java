/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.ingress;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.timeout;
import static org.mockito.Mockito.verify;
import java.util.UUID;
import com.microsoft.twins.reflector.AbstractTest;
import com.microsoft.twins.reflector.ingress.IngressMessageListenerTest.IngressMessageListenerTestConfiguration;
import com.microsoft.twins.reflector.model.IngressMessage;
import com.microsoft.twins.reflector.model.MessageType;
import com.microsoft.twins.reflector.telemetry.TelemetryForwarder;
import com.microsoft.twins.reflector.topology.TopologyUpdater;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.messaging.handler.annotation.support.MethodArgumentNotValidException;
import org.springframework.test.context.ContextConfiguration;
import lombok.Getter;

@ContextConfiguration(classes = {IngressMessageListenerTestConfiguration.class})
public class IngressMessageListenerTest extends AbstractTest {

  @Autowired
  private ReflectorIngressSink sink;

  @Autowired
  private IngressMessageListenerTestConfiguration testConfiguration;

  @Configuration
  @EnableBinding(ReflectorIngressSink.class)
  @Getter
  static class IngressMessageListenerTestConfiguration {
    @MockBean
    private TopologyUpdater topologyUpdater;

    @MockBean
    private TelemetryForwarder telemetryForwarder;

    @Bean
    IngressMessageListener ingressMessageListener() {
      return new IngressMessageListener(topologyUpdater, telemetryForwarder);
    }

  }

  @Test
  public void partialUpdateCalled() {
    final String testId = UUID.randomUUID().toString();

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(testId);

    sink.inputChannel().send(
        MessageBuilder.withPayload(testMessage).setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.PARTIAL.toString().toLowerCase()).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000))
        .updateTopologyElementPartial(testMessage, null);

    final UUID correlationId = UUID.randomUUID();
    sink.inputChannel()
        .send(MessageBuilder.withPayload(testMessage)
            .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
                MessageType.PARTIAL.toString().toLowerCase())
            .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000))
        .updateTopologyElementPartial(testMessage, correlationId);
  }

  @Test
  public void completeUpdateCalled() {
    final String testId = UUID.randomUUID().toString();

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(testId);

    sink.inputChannel().send(
        MessageBuilder.withPayload(testMessage).setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.FULL.toString().toLowerCase()).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000))
        .updateTopologyElementComplete(testMessage, null);

    final UUID correlationId = UUID.randomUUID();
    sink.inputChannel()
        .send(MessageBuilder.withPayload(testMessage)
            .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
                MessageType.FULL.toString().toLowerCase())
            .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000))
        .updateTopologyElementComplete(testMessage, correlationId);
  }

  @Test
  public void deleteElementCalled() {
    final String testId = UUID.randomUUID().toString();

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(testId);

    sink.inputChannel().send(
        MessageBuilder.withPayload(testMessage).setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.DELETE.toString().toLowerCase()).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000)).deleteTopologyElement(testId,
        null);

    final UUID correlationId = UUID.randomUUID();
    sink.inputChannel()
        .send(MessageBuilder.withPayload(testMessage)
            .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
                MessageType.DELETE.toString().toLowerCase())
            .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000)).deleteTopologyElement(testId,
        correlationId);
  }

  @Test
  public void deleteElementFailsIfIdNotProvided() {
    final IngressMessage testMessage = new IngressMessage();

    assertThatExceptionOfType(MethodArgumentNotValidException.class)
        .isThrownBy(() -> sink.inputChannel()
            .send(MessageBuilder.withPayload(testMessage)
                .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
                    MessageType.DELETE.toString().toLowerCase())
                .build()));
    verify(testConfiguration.getTopologyUpdater(), never()).deleteTopologyElement(any(), any());
  }

  @Test
  public void completeUpdateWithTelemetryIngressCalled() {
    final String testId = UUID.randomUUID().toString();
    final String testTelemetry = "test";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(testId);
    testMessage.setTelemetry(testTelemetry);

    sink.inputChannel().send(
        MessageBuilder.withPayload(testMessage).setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.FULL.toString().toLowerCase()).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000))
        .updateTopologyElementComplete(testMessage, null);

    verify(testConfiguration.getTelemetryForwarder(), timeout(2_000)).sendMessage(eq(testTelemetry),
        isNull(), eq(testId));

    final UUID correlationId = UUID.randomUUID();
    sink.inputChannel()
        .send(MessageBuilder.withPayload(testMessage)
            .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
                MessageType.FULL.toString().toLowerCase())
            .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000))
        .updateTopologyElementComplete(testMessage, correlationId);
    verify(testConfiguration.getTelemetryForwarder(), timeout(2_000)).sendMessage(eq(testTelemetry),
        eq(correlationId), eq(testId));
  }

  @Test
  public void partialUpdateWithTelemetryIngressCalled() {
    final String testId = UUID.randomUUID().toString();
    final String testTelemetry = "test";

    final IngressMessage testMessage = new IngressMessage();
    testMessage.setId(testId);
    testMessage.setTelemetry(testTelemetry);

    sink.inputChannel().send(
        MessageBuilder.withPayload(testMessage).setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
            MessageType.PARTIAL.toString().toLowerCase()).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000))
        .updateTopologyElementPartial(testMessage, null);

    verify(testConfiguration.getTelemetryForwarder(), timeout(2_000)).sendMessage(eq(testTelemetry),
        isNull(), eq(testId));

    final UUID correlationId = UUID.randomUUID();
    sink.inputChannel()
        .send(MessageBuilder.withPayload(testMessage)
            .setHeader(ReflectorIngressSink.HEADER_MESSAGE_TYPE,
                MessageType.PARTIAL.toString().toLowerCase())
            .setHeader(ReflectorIngressSink.HEADER_CORRELATION_ID, correlationId).build());

    verify(testConfiguration.getTopologyUpdater(), timeout(2_000))
        .updateTopologyElementPartial(testMessage, correlationId);
    verify(testConfiguration.getTelemetryForwarder(), timeout(2_000)).sendMessage(eq(testTelemetry),
        eq(correlationId), eq(testId));
  }

}
