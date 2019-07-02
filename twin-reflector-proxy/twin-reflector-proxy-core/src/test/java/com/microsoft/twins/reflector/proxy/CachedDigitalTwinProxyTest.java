/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.test.context.ContextConfiguration;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.DevicesApi.DevicesRetrieveQueryParams;
import com.microsoft.twins.api.EndpointsApi;
import com.microsoft.twins.api.SensorsApi;
import com.microsoft.twins.event.model.TopologyOperationEvent;
import com.microsoft.twins.event.model.TopologyOperationEvent.AccessType;
import com.microsoft.twins.event.model.TopologyOperationEvent.Type;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.EndpointCreate;
import com.microsoft.twins.model.EndpointCreate.EventTypesEnum;
import com.microsoft.twins.model.EndpointRetrieve;
import com.microsoft.twins.reflector.AbstractTest;
import com.microsoft.twins.reflector.TwinReflectorProxyProperties;
import com.microsoft.twins.reflector.proxy.CachedDigitalTwinProxyTest.CachedDigitalTwinProxyTestConfiguration;
import lombok.Getter;

@ContextConfiguration(classes = {CachedDigitalTwinProxyTestConfiguration.class})
public class CachedDigitalTwinProxyTest extends AbstractTest {
  private static final String CACHE_GATEWAY_ID_BY_HARDWARE_ID = "gatewayIdByHardwareId";
  private static final String CACHE_DEVICE_BY_ID = "deviceById";
  private static final String CACHE_DEVICE_BY_NAME = "deviceByName";

  @Autowired
  private CacheManager cacheManager;

  @Autowired
  private EndpointsApi endpointsApi;

  @Autowired
  private CachedDigitalTwinProxy cachedDigitalTwinProxy;

  @Autowired
  private CachedDigitalTwinProxyTestConfiguration testConfiguration;

  @Autowired
  private TopologyOperationSink topologyOperationSink;

  @Configuration
  @EnableCaching
  @EnableBinding(TopologyOperationSink.class)
  @Getter
  static class CachedDigitalTwinProxyTestConfiguration {

    @MockBean
    private SensorsApi sensorsApi;

    @MockBean
    private DevicesApi devicesApi;

    private final TwinReflectorProxyProperties properties = new TwinReflectorProxyProperties();

    @Bean
    CacheManager cacheManager() {
      final SimpleCacheManager cacheManager = new SimpleCacheManager();
      cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache(CACHE_GATEWAY_ID_BY_HARDWARE_ID),
          new ConcurrentMapCache(CACHE_DEVICE_BY_ID), new ConcurrentMapCache(CACHE_DEVICE_BY_NAME)));
      return cacheManager;
    }

    @Bean
    EndpointsApi endpointsApi() {
      final EndpointsApi endpointsApi = mock(EndpointsApi.class);

      final EndpointRetrieve existing = new EndpointRetrieve();
      properties.getTopologyChangeRegistration().setHubname(RandomStringUtils.randomAscii(10));
      existing.setPath(properties.getTopologyChangeRegistration().getHubname());


      when(endpointsApi.endpointsRetrieve(any(EndpointsApi.EndpointsRetrieveQueryParams.class)))
          .thenReturn(List.of(existing));

      return endpointsApi;
    }

    @Bean
    CachedDigitalTwinProxy cachedDigitalTwinProxy(final CacheManager cacheManager, final EndpointsApi endpointsApi) {
      return new CachedDigitalTwinProxy(sensorsApi, devicesApi, endpointsApi, properties, cacheManager);
    }

  }

  @Test
  public void proxyDoesNotCreateEndpointIfExists() {
    verify(endpointsApi).endpointsRetrieve(any(EndpointsApi.EndpointsRetrieveQueryParams.class));
    verify(endpointsApi, never()).endpointsCreate(any());
  }

  @Test
  public void proxyCreatesEndpointIfNotExists() {
    verify(endpointsApi).endpointsRetrieve(any(EndpointsApi.EndpointsRetrieveQueryParams.class));
    verify(endpointsApi, never()).endpointsCreate(any());


    final String testHubname = "testHub";
    final String testConnectionString =
        "Endpoint=sb://testHub.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=foobar";
    final String testSecondaryConnectionString =
        "Endpoint=sb://testHub.servicebus.windows.net/;SharedAccessKeyName=RootManageSharedAccessKey;SharedAccessKey=foobar2";

    testConfiguration.getProperties().getTopologyChangeRegistration().setConnectionString(testConnectionString);
    testConfiguration.getProperties().getTopologyChangeRegistration().setHubname(testHubname);
    testConfiguration.getProperties().getTopologyChangeRegistration()
        .setSecondaryConnectionString(testSecondaryConnectionString);

    cachedDigitalTwinProxy.registerForTopologyChanges();
    verify(endpointsApi, times(2)).endpointsRetrieve(any(EndpointsApi.EndpointsRetrieveQueryParams.class));
    final ArgumentCaptor<EndpointCreate> endpointCreateCaptor = ArgumentCaptor.forClass(EndpointCreate.class);
    verify(endpointsApi).endpointsCreate(endpointCreateCaptor.capture());

    assertThat(endpointCreateCaptor.getValue().getPath()).isEqualTo(testHubname);
    assertThat(endpointCreateCaptor.getValue().getConnectionString()).contains(testConnectionString);
    assertThat(endpointCreateCaptor.getValue().getSecondaryConnectionString()).contains(testSecondaryConnectionString);
    assertThat(endpointCreateCaptor.getValue().getEventTypes()).containsExactly(EventTypesEnum.TOPOLOGYOPERATION);
  }

  @Test
  public void cacheIsFilledAsPartOfGetCall() {
    final ConcurrentHashMap<Object, Object> idCache =
        (ConcurrentHashMap<Object, Object>) cacheManager.getCache(CACHE_DEVICE_BY_ID).getNativeCache();
    final ConcurrentHashMap<Object, Object> nameCache =
        (ConcurrentHashMap<Object, Object>) cacheManager.getCache(CACHE_DEVICE_BY_NAME).getNativeCache();

    assertThat(idCache).isEmpty();
    assertThat(nameCache).isEmpty();

    final UUID testDeviceId = UUID.randomUUID();
    final String testDeviceName = "testName";
    final DeviceRetrieve testDeviceRetrieve = new DeviceRetrieve();
    testDeviceRetrieve.setId(testDeviceId);
    testDeviceRetrieve.setName(testDeviceName);
    testDeviceRetrieve.setHardwareId(testDeviceName);

    when(testConfiguration.getDevicesApi().devicesRetrieve(any(DevicesRetrieveQueryParams.class)))
        .thenReturn(List.of(testDeviceRetrieve));

    cachedDigitalTwinProxy.getDeviceByDeviceId(testDeviceId);

    assertThat(idCache).containsKey(testDeviceId).hasSize(1);
    assertThat(nameCache).containsKey(testDeviceName).hasSize(1);

    final TopologyOperationEvent cleanCacheEvent = new TopologyOperationEvent();
    cleanCacheEvent.setAccessType(AccessType.DELETE);
    cleanCacheEvent.setType(Type.DEVICE);
    cleanCacheEvent.setId(testDeviceId);

    topologyOperationSink.inputChannel().send(MessageBuilder.withPayload(cleanCacheEvent).build());

    assertThat(idCache).isEmpty();
    assertThat(nameCache).isEmpty();
  }

}
