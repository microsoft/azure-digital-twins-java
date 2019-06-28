/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.telemetry;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.microsoft.twins.reflector.ReflectorIngressSink;

@Configuration
@EnableAutoConfiguration
@EnableBinding({DeviceMessageSink.class, ReflectorIngressSink.class})
@EnableConfigurationProperties(TestConfigurationProperties.class)
public class TestConfiguration {

  @Bean
  ListenToIngressSampler listenToIngressSampler() {
    return new ListenToIngressSampler();
  }


}
