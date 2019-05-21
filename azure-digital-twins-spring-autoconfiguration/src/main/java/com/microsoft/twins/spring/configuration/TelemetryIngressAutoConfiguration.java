/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.spring.configuration;

import java.io.IOException;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.telemetry.ingress.TelemetryForwarder;

@Configuration
@EnableConfigurationProperties(DigitalTwinsClientProperties.class)
@PropertySource("classpath:/digitial-twins-defaults.properties")
public class TelemetryIngressAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnClass(TwinsApiClient.class)
  TelemetryForwarder telemetryForwarder(final DigitalTwinsClientProperties properties)
      throws IOException {

    return new TelemetryForwarder();
  }

}
