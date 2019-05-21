/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.spring.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.microsoft.twins.TwinsApiClient;

@Configuration
@EnableConfigurationProperties(DigitalTwinsClientProperties.class)
@PropertySource("classpath:/digitial-twins-defaults.properties")
public class DigitalTwinAutoClientConfiguration {

  @Bean
  @ConditionalOnMissingBean
  TwinsApiClient twinsApiClient(final DigitalTwinsClientProperties properties) {

    return new TwinsApiClient(properties.getAad().getAuthorityHost(),
        properties.getAad().getTenant(), properties.getAad().getClientId(),
        properties.getAad().getClientSecret(), properties.getAad().getTimeout(),
        properties.getTwinsUrl().toString());
  }
}
