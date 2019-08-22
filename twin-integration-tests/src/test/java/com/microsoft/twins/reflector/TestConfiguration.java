/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import java.util.UUID;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.microsoft.twins.reflector.ingress.ReflectorIngressSink;
import com.microsoft.twins.reflector.proxy.TenantResolver;
import com.microsoft.twins.reflector.proxy.TopologyOperationSink;

@Configuration
@EnableAutoConfiguration
@EnableBinding({DeviceMessageSink.class, ReflectorIngressSink.class, TopologyOperationSink.class})
@EnableConfigurationProperties(TestConfigurationProperties.class)
public class TestConfiguration {

  @Bean
  ListenToIngressSampler listenToIngressSampler() {
    return new ListenToIngressSampler();
  }

  @Bean
  TenantResolver tenantResolver() {
    return new TestTenantResolver();
  }


  public class TestTenantResolver implements TenantResolver {
    private UUID tenant;

    public void setTenant(final UUID tenant) {
      this.tenant = tenant;
    }

    @Override
    public UUID getTenant() {
      return tenant;
    }


  }



}
