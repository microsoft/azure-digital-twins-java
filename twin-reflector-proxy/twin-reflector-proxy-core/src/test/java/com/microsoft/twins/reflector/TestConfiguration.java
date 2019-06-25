/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.microsoft.twins.reflector.telemetry.TelemetryForwarder;
import com.microsoft.twins.reflector.topology.TopologyUpdater;
import com.microsoft.twins.spring.configuration.DigitalTwinClientAutoConfiguration;
import lombok.Getter;

@EnableAutoConfiguration(
    exclude = {DigitalTwinClientAutoConfiguration.class, TwinReflectorProxyAutoConfiguration.class})
@Configuration
@EnableBinding(Sink.class)
@Getter
public class TestConfiguration {

  @MockBean
  private TopologyUpdater topologyUpdater;

  @MockBean
  private TelemetryForwarder telemetryForwarder;

  @Bean
  IngressMessageListener ingressMessageListener() {
    return new IngressMessageListener(topologyUpdater, telemetryForwarder);
  }

}
