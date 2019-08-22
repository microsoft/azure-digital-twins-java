/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.cache.Cache.ValueWrapper;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.DevicesApi.DevicesRetrieveQueryParams;
import com.microsoft.twins.api.EndpointsApi;
import com.microsoft.twins.api.SensorsApi;
import com.microsoft.twins.event.model.TopologyOperationEvent;
import com.microsoft.twins.model.DeviceCreate;
import com.microsoft.twins.model.DeviceCreate.StatusEnum;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.EndpointCreate;
import com.microsoft.twins.model.EndpointCreate.EventTypesEnum;
import com.microsoft.twins.model.EndpointCreate.TypeEnum;
import com.microsoft.twins.model.EndpointRetrieve;
import com.microsoft.twins.model.ExtendedPropertyCreate;
import com.microsoft.twins.model.SensorRetrieve;
import com.microsoft.twins.reflector.TwinReflectorProxyProperties;
import com.microsoft.twins.reflector.model.Property;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Validated
public class CachedDigitalTwinTopologyProxy {

  private static final String CACHE_GATEWAY_ID_BY_HARDWARE_ID = "gatewayIdByHardwareId";
  private static final String CACHE_DEVICE_BY_ID = "deviceById";
  private static final String CACHE_DEVICE_BY_NAME = "deviceByName";


  private final CachedDigitalTwinMetadataProxy metadataProxy;
  private final SensorsApi sensorsApi;
  private final DevicesApi devicesApi;
  private final EndpointsApi endpointsApi;
  private final TwinReflectorProxyProperties properties;
  private final CacheManager cacheManager;

  public UUID createDevice(@NotEmpty final String name, @NotNull final UUID parent,
      @NotNull final UUID gateway, final List<Property> properties,
      final Map<String, String> attributes) {
    final DeviceCreate device = new DeviceCreate();
    device.setName(name);
    device.setSpaceId(parent);
    device.setHardwareId(name);
    device.setGatewayId(gateway.toString());
    device.setCreateIoTHubDevice(false);

    if (!CollectionUtils.isEmpty(properties)) {
      properties.stream()
          .map(p -> new ExtendedPropertyCreate()
              .name(metadataProxy.getPropertykey(p.getName(), "devices")).value(p.getValue()))
          .forEach(device::addPropertiesItem);
    }

    if (!CollectionUtils.isEmpty(attributes)) {
      attributes.entrySet().forEach(e -> setAttribute(device, e));
    }

    return devicesApi.devicesCreate(device);
  }



  private void setAttribute(final DeviceCreate device, final Map.Entry<String, String> attribute) {

    switch (attribute.getKey()) {
      case "description":
        device.setDescription(attribute.getValue());
        break;
      case "friendlyName":
        device.setFriendlyName(attribute.getValue());
        break;
      case "status":
        device.setStatus(StatusEnum.fromValue(attribute.getValue()));
        break;
      default:
        log.error("Attribute [{}] not supported", attribute.getKey());
        break;
    }
    // TODO support device location
    // device.setLocation(location);

    // TODO support device types
    // device.setType(type);
    // device.setSubtype(subtype);
  }



  @Caching(cacheable = {@Cacheable(cacheNames = CACHE_DEVICE_BY_NAME)},
      put = {@CachePut(cacheNames = CACHE_DEVICE_BY_ID, key = "#result.id",
          condition = "#result != null")})
  public Optional<DeviceRetrieve> getDeviceByName(@NotEmpty final String name) {
    final List<DeviceRetrieve> devices = devicesApi
        .devicesRetrieve(new DevicesRetrieveQueryParams().names(name).includes("ConnectionString"));

    if (devices.isEmpty()) {
      return Optional.empty();
    }

    return Optional.ofNullable(devices.get(0));
  }

  @Caching(cacheable = {@Cacheable(cacheNames = CACHE_DEVICE_BY_ID)},
      put = {@CachePut(cacheNames = CACHE_DEVICE_BY_NAME, key = "#result.name",
          condition = "#result != null")})
  public Optional<DeviceRetrieve> getDeviceByDeviceId(@NotNull final UUID deviceId) {
    final List<DeviceRetrieve> devices = devicesApi.devicesRetrieve(
        new DevicesRetrieveQueryParams().ids(deviceId.toString()).includes("ConnectionString"));

    if (devices.isEmpty()) {
      return Optional.empty();
    }

    return Optional.ofNullable(devices.get(0));
  }

  @CacheEvict(cacheNames = CACHE_DEVICE_BY_NAME)
  public void deleteDeviceByName(@NotEmpty final String name) {
    final Optional<DeviceRetrieve> device = getDeviceByName(name);

    if (device.isEmpty()) {
      log.warn("Device with [{}] does not exist. Will silently ignore delition.");
      return;
    }

    devicesApi.devicesDelete(device.get().getId().toString());
    cacheManager.getCache(CACHE_DEVICE_BY_ID).evict(device.get().getId());
  }


  @Cacheable(cacheNames = CACHE_GATEWAY_ID_BY_HARDWARE_ID)
  public Optional<UUID> getGatewayIdByHardwareId(@NotEmpty final String hardwareId) {
    // Check first if hardware ID belongs to sensor
    final List<SensorRetrieve> sensors = sensorsApi.sensorsRetrieve(
        new SensorsApi.SensorsRetrieveQueryParams().hardwareIds(hardwareId).includes("device"));

    if (!CollectionUtils.isEmpty(sensors)) {
      if (StringUtils.isEmpty(sensors.get(0).getDevice().getGatewayId())) {
        return Optional.of(sensors.get(0).getDevice().getId());
      }
      return Optional.of(UUID.fromString(sensors.get(0).getDevice().getGatewayId()));
    }

    // Check next if hardware ID belongs to device
    final List<DeviceRetrieve> devices =
        devicesApi.devicesRetrieve(new DevicesRetrieveQueryParams().hardwareIds(hardwareId));

    if (CollectionUtils.isEmpty(devices)) {
      return Optional.empty();
    }

    if (StringUtils.isEmpty(devices.get(0).getGatewayId())) {
      return Optional.of(devices.get(0).getId());
    }

    return Optional.of(UUID.fromString(devices.get(0).getGatewayId()));
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


  @StreamListener(target = TopologyOperationSink.INPUT)
  void getTopologyUpdate(final TopologyOperationEvent topologyOperationEvent) {
    if (TopologyOperationEvent.AccessType.UPDATE == topologyOperationEvent.getAccessType()
        || TopologyOperationEvent.AccessType.DELETE == topologyOperationEvent.getAccessType()) {

      switch (topologyOperationEvent.getType()) {
        case DEVICE:
          evictDeviceCache(topologyOperationEvent.getId());
          break;
        default:
          break;
      }
    }
  }

  private void evictDeviceCache(final UUID deviceId) {
    final ValueWrapper inCache = cacheManager.getCache(CACHE_DEVICE_BY_ID).get(deviceId);

    if (inCache != null) {
      cacheManager.getCache(CACHE_DEVICE_BY_NAME).evict(((DeviceRetrieve) inCache.get()).getName());
      cacheManager.getCache(CACHE_GATEWAY_ID_BY_HARDWARE_ID)
          .evict(((DeviceRetrieve) inCache.get()).getHardwareId());
      cacheManager.getCache(CACHE_DEVICE_BY_ID).evict(deviceId);
    }
  }
}
