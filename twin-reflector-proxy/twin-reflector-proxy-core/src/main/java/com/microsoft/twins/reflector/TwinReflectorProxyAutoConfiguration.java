/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.reflector.proxy.CachedDigitalTwinProxy;
import com.microsoft.twins.reflector.telemetry.TelemetryForwarder;
import com.microsoft.twins.reflector.topology.TopologyUpdater;

@Configuration
@EnableBinding({ReflectorIngressSink.class, TopologyOperationSink.class})
@EnableCaching
@EnableConfigurationProperties(TwinReflectorProxyProperties.class)
@PropertySource("classpath:/digitial-twins-reflector-proxy-defaults.properties")
public class TwinReflectorProxyAutoConfiguration {


  @Bean
  @ConditionalOnMissingBean
  TelemetryForwarder telemetryForwarder(final CachedDigitalTwinProxy cachedDigitalTwinProxy) {
    return new TelemetryForwarder(cachedDigitalTwinProxy);
  }

  @Bean
  @ConditionalOnMissingBean
  TopologyUpdater topologyUpdater(final CachedDigitalTwinProxy cachedDigitalTwinProxy) {
    return new TopologyUpdater(cachedDigitalTwinProxy);
  }

  @Bean
  @ConditionalOnMissingBean
  CachedDigitalTwinProxy cachedDigitalTwinProxy(final TwinsApiClient client,
      final TwinReflectorProxyProperties properties, final CacheManager cacheManager) {
    return new CachedDigitalTwinProxy(client, properties, cacheManager);
  }

  @Bean
  @ConditionalOnMissingBean
  IngressMessageListener ingressMessageListener(final TopologyUpdater topologyUpdater,
      final TelemetryForwarder telemetryForwarder) {
    return new IngressMessageListener(topologyUpdater, telemetryForwarder);
  }

}
