/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.reflector.telemetry.DeviceConnectionStringResolver;
import com.microsoft.twins.reflector.telemetry.TelemetryForwarder;
import com.microsoft.twins.reflector.topology.CachedDigitalTwinProxy;
import com.microsoft.twins.reflector.topology.TopologyUpdater;

@Configuration
@EnableBinding(Sink.class)
@EnableCaching
@PropertySource("classpath:/digitial-twins-reflector-proxy-defaults.properties")
public class TwinReflectorProxyAutoConfiguration {


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

  @Bean
  @ConditionalOnMissingBean
  TopologyUpdater topologyUpdater(final CachedDigitalTwinProxy cachedDigitalTwinProxy) {
    return new TopologyUpdater(cachedDigitalTwinProxy);
  }

  @Bean
  @ConditionalOnMissingBean
  CachedDigitalTwinProxy cachedDigitalTwinProxy(final TwinsApiClient client) {
    return new CachedDigitalTwinProxy(client);
  }

  @Bean
  @ConditionalOnMissingBean
  IngressMessageListener ingressMessageListener(final TopologyUpdater topologyUpdater,
      final TelemetryForwarder telemetryForwarder) {
    return new IngressMessageListener(topologyUpdater, telemetryForwarder);
  }

}
