/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import org.springframework.boot.actuate.autoconfigure.health.ConditionalOnEnabledHealthIndicator;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.EndpointsApi;
import com.microsoft.twins.api.PropertyKeysApi;
import com.microsoft.twins.api.SensorsApi;
import com.microsoft.twins.api.SpacesApi;
import com.microsoft.twins.reflector.ingress.IngressMessageListener;
import com.microsoft.twins.reflector.ingress.ReflectorIngressSink;
import com.microsoft.twins.reflector.proxy.CachedDigitalTwinMetadataProxy;
import com.microsoft.twins.reflector.proxy.CachedDigitalTwinTopologyProxy;
import com.microsoft.twins.reflector.proxy.TenantResolver;
import com.microsoft.twins.reflector.proxy.TopologyOperationSink;
import com.microsoft.twins.reflector.telemetry.TelemetryForwarder;
import com.microsoft.twins.reflector.topology.TopologyUpdater;

@Configuration
@EnableBinding({ReflectorIngressSink.class, TopologyOperationSink.class})
@EnableCaching
@EnableConfigurationProperties(TwinReflectorProxyProperties.class)
@PropertySource("classpath:/twins-reflector-proxy-defaults.properties")
public class TwinReflectorProxyAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  TelemetryForwarder telemetryForwarder(
      final CachedDigitalTwinTopologyProxy cachedDigitalTwinProxy) {
    return new TelemetryForwarder(cachedDigitalTwinProxy);
  }

  @Bean
  @ConditionalOnMissingBean
  TopologyUpdater topologyUpdater(final TenantResolver tenantResolver,
      final CachedDigitalTwinTopologyProxy cachedDigitalTwinProxy) {
    return new TopologyUpdater(tenantResolver, cachedDigitalTwinProxy);
  }

  @Bean
  @ConditionalOnMissingBean
  CachedDigitalTwinMetadataProxy cachedDigitalTwinMetadataProxy(final TenantResolver tenantResolver,
      final PropertyKeysApi propertyKeysApi) {

    return new CachedDigitalTwinMetadataProxy(tenantResolver, propertyKeysApi);
  }

  @Bean
  @ConditionalOnMissingBean
  CachedDigitalTwinTopologyProxy cachedDigitalTwinProxy(
      final CachedDigitalTwinMetadataProxy cachedDigitalTwinMetadataProxy,
      final SpacesApi spacesApi, final SensorsApi sensorsApi, final DevicesApi devicesApi,
      final EndpointsApi endpointsApi, final TwinReflectorProxyProperties properties,
      final CacheManager cacheManager) {
    return new CachedDigitalTwinTopologyProxy(cachedDigitalTwinMetadataProxy, spacesApi, sensorsApi,
        devicesApi, endpointsApi, properties, cacheManager);
  }

  @Bean
  @ConditionalOnMissingBean
  IngressMessageListener ingressMessageListener(final TopologyUpdater topologyUpdater,
      final TelemetryForwarder telemetryForwarder) {
    return new IngressMessageListener(topologyUpdater, telemetryForwarder);
  }

  @Bean
  @ConditionalOnEnabledHealthIndicator("aad")
  AadHealthIndicator aadHealthIndicator(final TwinsApiClient twinsApiClient) {
    return new AadHealthIndicator(twinsApiClient);
  }

  @Bean
  @ConditionalOnEnabledHealthIndicator("twins")
  TwinsHealthIndicator twinsHealthIndicator(final TwinsApiClient twinsApiClient) {
    return new TwinsHealthIndicator(twinsApiClient);
  }

  // TODO verify tenant exists?
  @Bean
  @ConditionalOnMissingBean
  TenantResolver tenantResolver(final TwinReflectorProxyProperties properties) {
    return properties::getTenant;
  }

}
