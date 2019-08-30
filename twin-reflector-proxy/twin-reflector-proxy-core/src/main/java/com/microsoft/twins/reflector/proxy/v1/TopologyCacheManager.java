/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy.v1;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.util.CollectionUtils;
import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.twins.api.EndpointsApi;
import com.microsoft.twins.event.model.TopologyOperationEvent;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.EndpointCreate;
import com.microsoft.twins.model.EndpointRetrieve;
import com.microsoft.twins.model.EventTypesEnum;
import com.microsoft.twins.model.SpaceRetrieve;
import com.microsoft.twins.model.TypeEnum;
import com.microsoft.twins.reflector.TwinReflectorProxyProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class TopologyCacheManager {
  private static final String LOG_TOPO_TYPE = "type";
  private static final String LOG_TOPO_ACCESS = "access";
  private static final String LOG_TOPO_ID = "id";

  private final EndpointsApi endpointsApi;
  private final TwinReflectorProxyProperties properties;
  private final CacheManager cacheManager;

  @Autowired(required = false)
  private TelemetryClient telemetryClient;

  @StreamListener(target = TopologyOperationSink.INPUT)
  void getTopologyUpdate(final TopologyOperationEvent topologyOperationEvent) {
    log.trace("Got TopologyOperationEvent [{}] ", topologyOperationEvent);

    trackTelemetry(topologyOperationEvent);

    if (TopologyOperationEvent.AccessType.UPDATE == topologyOperationEvent.getAccessType()
        || TopologyOperationEvent.AccessType.DELETE == topologyOperationEvent.getAccessType()) {

      switch (topologyOperationEvent.getType()) {
        case DEVICE:
          evictDeviceCache(topologyOperationEvent.getId());
          break;
        case SPACE:
          evictSpaceCache(topologyOperationEvent.getId());
          break;
        default:
          break;
      }
    }
  }

  private void trackTelemetry(final TopologyOperationEvent topologyOperationEvent) {
    if (telemetryClient == null) {
      return;
    }
    telemetryClient.trackEvent("topologyChange",
        Map.of(LOG_TOPO_TYPE, topologyOperationEvent.getType().toString(), LOG_TOPO_ACCESS,
            topologyOperationEvent.getAccessType().toString(), LOG_TOPO_ID,
            topologyOperationEvent.getId().toString()),
        null);

  }

  @PostConstruct
  void registerForTopologyChanges() {
    final List<EndpointRetrieve> existing = endpointsApi.endpointsRetrieve(
        new EndpointsApi.EndpointsRetrieveQueryParams().types(TypeEnum.EVENTHUB.toString())
            .eventTypes(EventTypesEnum.TOPOLOGYOPERATION.toString()));

    if (!CollectionUtils.isEmpty(existing)
        && existing.stream().anyMatch(endpoint -> endpoint.getPath()
            .equalsIgnoreCase(properties.getEventHubs().getTopologyOperations().getHubname()))) {
      return;
    }


    final EndpointCreate eventHub = new EndpointCreate();
    eventHub.addEventTypesItem(EventTypesEnum.TOPOLOGYOPERATION);
    eventHub.setType(TypeEnum.EVENTHUB);
    eventHub.setConnectionString(properties.getEventHubs().getConnectionString() + ";EntityPath="
        + properties.getEventHubs().getTopologyOperations().getHubname());
    eventHub.setSecondaryConnectionString(properties.getEventHubs().getSecondaryConnectionString()
        + ";EntityPath=" + properties.getEventHubs().getTopologyOperations().getHubname());
    eventHub.setPath(properties.getEventHubs().getTopologyOperations().getHubname());

    endpointsApi.endpointsCreate(eventHub);
  }



  private void evictDeviceCache(final UUID deviceId) {
    final ValueWrapper inCache =
        cacheManager.getCache(Cachedv1DigitalTwinTopologyProxy.CACHE_DEVICE_BY_ID).get(deviceId);

    if (inCache != null) {
      cacheManager.getCache(Cachedv1DigitalTwinTopologyProxy.CACHE_DEVICE_BY_NAME)
          .evict(((DeviceRetrieve) inCache.get()).getName());
      cacheManager.getCache(Cachedv1DigitalTwinTopologyProxy.CACHE_GATEWAY_ID_BY_HARDWARE_ID)
          .evict(((DeviceRetrieve) inCache.get()).getHardwareId());
      cacheManager.getCache(Cachedv1DigitalTwinTopologyProxy.CACHE_DEVICE_BY_ID).evict(deviceId);
    }
  }

  private void evictSpaceCache(final UUID spaceId) {
    final ValueWrapper inCache =
        cacheManager.getCache(Cachedv1DigitalTwinTopologyProxy.CACHE_SPACE_BY_ID).get(spaceId);

    if (inCache != null) {
      cacheManager.getCache(Cachedv1DigitalTwinTopologyProxy.CACHE_SPACE_BY_NAME)
          .evict(((SpaceRetrieve) inCache.get()).getName());
      cacheManager.getCache(Cachedv1DigitalTwinTopologyProxy.CACHE_SPACE_BY_ID).evict(spaceId);
    }
  }

}
