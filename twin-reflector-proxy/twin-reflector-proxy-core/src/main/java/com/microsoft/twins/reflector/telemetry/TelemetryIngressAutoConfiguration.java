/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.telemetry;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.microsoft.twins.TwinsApiClient;

@Configuration
@PropertySource("classpath:/digitial-twins-telemetry-ingress-defaults.properties")
public class TelemetryIngressAutoConfiguration {


  @Bean
  @ConditionalOnMissingBean
  DeviceConnectionStringResolver deviceConnectionStringResolver(final TwinsApiClient twinsApiClient) {
    return new DeviceConnectionStringResolver(twinsApiClient);
  }

  @Bean
  @ConditionalOnMissingBean
  TelemetryForwarder telemetryForwarder(final DeviceConnectionStringResolver deviceConnectionStringResolver) {
    return new TelemetryForwarder(deviceConnectionStringResolver);
  }

}
