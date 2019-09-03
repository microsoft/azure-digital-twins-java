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
import com.microsoft.twins.api.TypesApi;
import com.microsoft.twins.reflector.ingress.IngressMessageListener;
import com.microsoft.twins.reflector.ingress.ReflectorIngressSink;
import com.microsoft.twins.reflector.proxy.DigitalTwinMetadataProxy;
import com.microsoft.twins.reflector.proxy.DigitalTwinTopologyProxy;
import com.microsoft.twins.reflector.proxy.TenantResolver;
import com.microsoft.twins.reflector.proxy.v1.Cachedv1DigitalTwinMetadataProxy;
import com.microsoft.twins.reflector.proxy.v1.Cachedv1DigitalTwinTopologyProxy;
import com.microsoft.twins.reflector.proxy.v1.PropertBackedV1TenantResolver;
import com.microsoft.twins.reflector.proxy.v1.TopologyCacheManager;
import com.microsoft.twins.reflector.proxy.v1.TopologyOperationSink;
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
  TelemetryForwarder telemetryForwarder(final DigitalTwinTopologyProxy cachedDigitalTwinProxy) {
    return new TelemetryForwarder(cachedDigitalTwinProxy);
  }

  @Bean
  @ConditionalOnMissingBean
  TopologyUpdater topologyUpdater(final TenantResolver tenantResolver,
      final DigitalTwinTopologyProxy cachedDigitalTwinProxy) {
    return new TopologyUpdater(tenantResolver, cachedDigitalTwinProxy);
  }

  @Bean
  @ConditionalOnMissingBean
  DigitalTwinMetadataProxy cachedDigitalTwinMetadataProxy(final TenantResolver tenantResolver,
      final PropertyKeysApi propertyKeysApi, final TypesApi typesApi) {

    return new Cachedv1DigitalTwinMetadataProxy(tenantResolver, propertyKeysApi, typesApi);
  }

  @Bean
  @ConditionalOnMissingBean
  DigitalTwinTopologyProxy cachedDigitalTwinProxy(
      final DigitalTwinMetadataProxy cachedDigitalTwinMetadataProxy, final SpacesApi spacesApi,
      final SensorsApi sensorsApi, final DevicesApi devicesApi, final CacheManager cacheManager) {
    return new Cachedv1DigitalTwinTopologyProxy(cachedDigitalTwinMetadataProxy, spacesApi,
        sensorsApi, devicesApi, cacheManager);
  }

  @Bean
  @ConditionalOnMissingBean
  TopologyCacheManager topologyCacheManager(final EndpointsApi endpointsApi,
      final TwinReflectorProxyProperties properties, final CacheManager cacheManager) {
    return new TopologyCacheManager(endpointsApi, properties, cacheManager);
  }

  @Bean
  @ConditionalOnMissingBean
  IngressMessageListener ingressMessageListener(final TopologyUpdater topologyUpdater,
      final TelemetryForwarder telemetryForwarder, final TwinReflectorProxyProperties properties) {
    return new IngressMessageListener(topologyUpdater, telemetryForwarder, properties);
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

  @Bean
  @ConditionalOnMissingBean
  TenantResolver tenantResolver(final TwinReflectorProxyProperties properties,
      final DevicesApi devicesApi) {
    return new PropertBackedV1TenantResolver(properties, devicesApi);
  }

}
