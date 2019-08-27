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
import com.microsoft.twins.api.SpacesApi;
import com.microsoft.twins.api.SpacesApi.SpacesRetrieveQueryParams;
import com.microsoft.twins.event.model.TopologyOperationEvent;
import com.microsoft.twins.event.model.TopologyOperationEvent.AccessType;
import com.microsoft.twins.event.model.TopologyOperationEvent.Type;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.EndpointCreate;
import com.microsoft.twins.model.EndpointRetrieve;
import com.microsoft.twins.model.EventTypesEnum;
import com.microsoft.twins.model.SpaceRetrieveWithChildren;
import com.microsoft.twins.reflector.AbstractTest;
import com.microsoft.twins.reflector.TwinReflectorProxyProperties;
import com.microsoft.twins.reflector.proxy.CachedDigitalTwinProxyTest.CachedDigitalTwinProxyTestConfiguration;
import lombok.Getter;

@ContextConfiguration(classes = {CachedDigitalTwinProxyTestConfiguration.class})
public class CachedDigitalTwinProxyTest extends AbstractTest {
  private static final String CACHE_GATEWAY_ID_BY_HARDWARE_ID = "gatewayIdByHardwareId";
  private static final String CACHE_DEVICE_BY_ID = "deviceById";
  private static final String CACHE_DEVICE_BY_NAME = "deviceByName";
  private static final String CACHE_SPACE_BY_NAME = "spaceByName";
  private static final String CACHE_SPACE_BY_ID = "spaceById";

  @Autowired
  private CacheManager cacheManager;

  @Autowired
  private EndpointsApi endpointsApi;

  @Autowired
  private DigitalTwinTopologyProxy cachedDigitalTwinProxy;

  @Autowired
  private TopologyCacheManager topologyCacheManager;

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
    private SpacesApi spacesApi;

    @MockBean
    private SensorsApi sensorsApi;

    @MockBean
    private DevicesApi devicesApi;

    @MockBean
    private DigitalTwinMetadataProxy cachedDigitalTwinMetadataProxy;

    private final TwinReflectorProxyProperties properties = new TwinReflectorProxyProperties();

    @Bean
    CacheManager cacheManager() {
      final SimpleCacheManager cacheManager = new SimpleCacheManager();
      cacheManager.setCaches(Arrays.asList(new ConcurrentMapCache(CACHE_GATEWAY_ID_BY_HARDWARE_ID),
          new ConcurrentMapCache(CACHE_DEVICE_BY_ID), new ConcurrentMapCache(CACHE_DEVICE_BY_NAME),
          new ConcurrentMapCache(CACHE_SPACE_BY_ID), new ConcurrentMapCache(CACHE_SPACE_BY_NAME)));
      return cacheManager;
    }

    @Bean
    EndpointsApi endpointsApi() {
      final EndpointsApi endpointsApi = mock(EndpointsApi.class);

      final EndpointRetrieve existing = new EndpointRetrieve();
      properties.getEventHubs().getTopologyOperations()
          .setHubname(RandomStringUtils.randomAscii(10));
      existing.setPath(properties.getEventHubs().getTopologyOperations().getHubname());


      when(endpointsApi.endpointsRetrieve(any(EndpointsApi.EndpointsRetrieveQueryParams.class)))
          .thenReturn(List.of(existing));

      return endpointsApi;
    }

    @Bean
    DigitalTwinTopologyProxy cachedDigitalTwinProxy(final CacheManager cacheManager) {
      return new Cachedv1DigitalTwinTopologyProxy(cachedDigitalTwinMetadataProxy, spacesApi,
          sensorsApi, devicesApi, cacheManager);
    }

    @Bean
    TopologyCacheManager topologyCacheManager(final EndpointsApi endpointsApi,
        final CacheManager cacheManager) {
      return new TopologyCacheManager(endpointsApi, properties, cacheManager);
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

    testConfiguration.getProperties().getEventHubs().setConnectionString(testConnectionString);
    testConfiguration.getProperties().getEventHubs().getTopologyOperations()
        .setHubname(testHubname);
    testConfiguration.getProperties().getEventHubs()
        .setSecondaryConnectionString(testSecondaryConnectionString);

    topologyCacheManager.registerForTopologyChanges();
    verify(endpointsApi, times(2))
        .endpointsRetrieve(any(EndpointsApi.EndpointsRetrieveQueryParams.class));
    final ArgumentCaptor<EndpointCreate> endpointCreateCaptor =
        ArgumentCaptor.forClass(EndpointCreate.class);
    verify(endpointsApi).endpointsCreate(endpointCreateCaptor.capture());

    assertThat(endpointCreateCaptor.getValue().getPath()).isEqualTo(testHubname);
    assertThat(endpointCreateCaptor.getValue().getConnectionString())
        .contains(testConnectionString);
    assertThat(endpointCreateCaptor.getValue().getSecondaryConnectionString())
        .contains(testSecondaryConnectionString);
    assertThat(endpointCreateCaptor.getValue().getEventTypes())
        .containsExactly(EventTypesEnum.TOPOLOGYOPERATION);
  }

  @Test
  public void deviceCacheIsFilledAsPartOfGetCall() {
    final ConcurrentHashMap<Object, Object> idCache =
        (ConcurrentHashMap<Object, Object>) cacheManager.getCache(CACHE_DEVICE_BY_ID)
            .getNativeCache();
    final ConcurrentHashMap<Object, Object> nameCache =
        (ConcurrentHashMap<Object, Object>) cacheManager.getCache(CACHE_DEVICE_BY_NAME)
            .getNativeCache();

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

  @Test
  public void spaceCacheIsFilledAsPartOfGetCall() {
    final ConcurrentHashMap<Object, Object> idCache =
        (ConcurrentHashMap<Object, Object>) cacheManager.getCache(CACHE_SPACE_BY_ID)
            .getNativeCache();
    final ConcurrentHashMap<Object, Object> nameCache =
        (ConcurrentHashMap<Object, Object>) cacheManager.getCache(CACHE_SPACE_BY_NAME)
            .getNativeCache();


    final UUID testSpaceId = UUID.randomUUID();
    assertThat(idCache).isEmpty();
    assertThat(nameCache).isEmpty();


    final String testSpaceName = "testSpaceName";
    final SpaceRetrieveWithChildren testSpaceRetrieve = new SpaceRetrieveWithChildren();
    testSpaceRetrieve.setId(testSpaceId);
    testSpaceRetrieve.setName(testSpaceName);

    when(testConfiguration.getSpacesApi().spacesRetrieve(any(SpacesRetrieveQueryParams.class)))
        .thenReturn(List.of(testSpaceRetrieve));

    cachedDigitalTwinProxy.getSpaceBySpaceId(testSpaceId);

    assertThat(idCache).containsKey(testSpaceId).hasSize(1);
    assertThat(nameCache).containsKey(testSpaceName).hasSize(1);

    final TopologyOperationEvent cleanCacheEvent = new TopologyOperationEvent();
    cleanCacheEvent.setAccessType(AccessType.DELETE);
    cleanCacheEvent.setType(Type.SPACE);
    cleanCacheEvent.setId(testSpaceId);

    topologyOperationSink.inputChannel().send(MessageBuilder.withPayload(cleanCacheEvent).build());

    assertThat(idCache).isEmpty();
    assertThat(nameCache).isEmpty();
  }

}
