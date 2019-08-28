/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy.v1;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.DevicesApi.DevicesRetrieveQueryParams;
import com.microsoft.twins.api.SensorsApi;
import com.microsoft.twins.api.SpacesApi;
import com.microsoft.twins.api.SpacesApi.SpacesRetrieveQueryParams;
import com.microsoft.twins.model.DeviceCreate;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.DeviceUpdate;
import com.microsoft.twins.model.DeviceUpdate.StatusEnum;
import com.microsoft.twins.model.ExtendedPropertyCreate;
import com.microsoft.twins.model.SensorRetrieve;
import com.microsoft.twins.model.SpaceCreate;
import com.microsoft.twins.model.SpaceRetrieve;
import com.microsoft.twins.model.SpaceUpdate;
import com.microsoft.twins.reflector.model.IngressMessage;
import com.microsoft.twins.reflector.model.Property;
import com.microsoft.twins.reflector.proxy.DigitalTwinMetadataProxy;
import com.microsoft.twins.reflector.proxy.DigitalTwinTopologyProxy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Validated
public class Cachedv1DigitalTwinTopologyProxy implements DigitalTwinTopologyProxy {

  private static final String UNKOWN_TYPE = "None";
  private static final String UNKOWN_SPACE_STATUS = "None";
  private static final String ENTITY_TYPE_SPACE = "spaces";
  private static final String ENTITY_TYPE_DEVICE = "devices";

  static final String CACHE_GATEWAY_ID_BY_HARDWARE_ID = "gatewayIdByHardwareId";
  static final String CACHE_DEVICE_BY_ID = "deviceById";
  static final String CACHE_DEVICE_BY_NAME = "deviceByName";
  static final String CACHE_SPACE_BY_NAME = "spaceByName";
  static final String CACHE_SPACE_BY_ID = "spaceById";


  private final DigitalTwinMetadataProxy metadataProxy;
  private final SpacesApi spacesApi;
  private final SensorsApi sensorsApi;
  private final DevicesApi devicesApi;
  private final CacheManager cacheManager;

  @Override
  public UUID createDevice(final String name, final UUID parent, final UUID gateway,
      final Collection<Property> properties, final Map<String, String> attributes) {
    final DeviceCreate device = new DeviceCreate();
    device.setName(name);
    device.setSpaceId(parent);
    device.setHardwareId(name);
    device.setGatewayId(gateway);
    device.setCreateIoTHubDevice(false);

    if (!CollectionUtils.isEmpty(properties)) {
      properties.stream()
          .map(p -> new ExtendedPropertyCreate()
              .name(metadataProxy.getPropertykey(p.getName(), ENTITY_TYPE_DEVICE))
              .value(p.getValue()))
          .forEach(device::addPropertiesItem);
    }

    if (!CollectionUtils.isEmpty(attributes)) {
      attributes.entrySet().forEach(e -> setDeviceAttribute(device, e));
    }

    return devicesApi.devicesCreate(device);
  }

  @Override
  public void updateDeviceComplete(final UUID id, final UUID parent, final UUID gateway,
      final Collection<Property> properties, final Map<String, String> attributes) {
    final DeviceUpdate device = new DeviceUpdate();
    device.setSpaceId(parent);
    device.setGatewayId(gateway);

    if (!CollectionUtils.isEmpty(properties)) {
      devicesApi.devicesUpdateProperties(properties.stream()
          .map(p -> new ExtendedPropertyCreate()
              .name(metadataProxy.getPropertykey(p.getName(), ENTITY_TYPE_DEVICE))
              .value(p.getValue()))
          .collect(Collectors.toList()), id);
    }

    setAllDeviceAttributes(device, attributes);


    devicesApi.devicesUpdate(device, id);
  }

  private void setAllDeviceAttributes(final DeviceUpdate device,
      final Map<String, String> attributes) {

    if (CollectionUtils.isEmpty(attributes)) {
      device.setDescription("");
      device.setFriendlyName("");
      device.setStatus(StatusEnum.PROVISIONED);
      device.setTypeId(metadataProxy.getDeviceType(UNKOWN_TYPE));
      device.setSubtypeId(metadataProxy.getDeviceSubType(UNKOWN_TYPE));
      return;
    }

    device.setDescription(attributes.getOrDefault(IngressMessage.ATTRIBUTE_V1_DESCRIPTION, ""));
    device.setFriendlyName(attributes.getOrDefault(IngressMessage.ATTRIBUTE_V1_FRIENDLY_NAME, ""));

    device.setTypeId(metadataProxy
        .getDeviceType(attributes.getOrDefault(IngressMessage.ATTRIBUTE_V1_TYPE, UNKOWN_TYPE)));
    device.setSubtypeId(metadataProxy.getDeviceSubType(
        attributes.getOrDefault(IngressMessage.ATTRIBUTE_V1_SUB_TYPE, UNKOWN_TYPE)));

    final StatusEnum status =
        StatusEnum.fromValue(attributes.get(IngressMessage.ATTRIBUTE_V1_STATUS));
    device.setStatus(status);
  }

  private void setAllSpaceAttributes(final SpaceUpdate space,
      final Map<String, String> attributes) {

    if (CollectionUtils.isEmpty(attributes)) {
      space.setDescription("");
      space.setFriendlyName("");
      space.setTypeId(metadataProxy.getSpaceType(UNKOWN_TYPE));
      space.setSubtypeId(metadataProxy.getSpaceSubType(UNKOWN_TYPE));
      space.setStatusId(metadataProxy.getSpaceStatus(UNKOWN_SPACE_STATUS));
      return;
    }

    space.setDescription(attributes.getOrDefault(IngressMessage.ATTRIBUTE_V1_DESCRIPTION, ""));
    space.setFriendlyName(attributes.getOrDefault(IngressMessage.ATTRIBUTE_V1_FRIENDLY_NAME, ""));
    space.setTypeId(metadataProxy
        .getSpaceType(attributes.getOrDefault(IngressMessage.ATTRIBUTE_V1_TYPE, UNKOWN_TYPE)));
    space.setSubtypeId(metadataProxy.getSpaceSubType(
        attributes.getOrDefault(IngressMessage.ATTRIBUTE_V1_SUB_TYPE, UNKOWN_TYPE)));
    space.setStatusId(metadataProxy.getSpaceStatus(
        attributes.getOrDefault(IngressMessage.ATTRIBUTE_V1_STATUS, UNKOWN_SPACE_STATUS)));
  }

  @Override
  public void updateDevicePartial(final UUID id, final UUID parent, final UUID gateway,
      final Collection<Property> properties, final Map<String, String> attributes) {
    final DeviceUpdate device = new DeviceUpdate();

    if (parent != null) {
      device.setSpaceId(parent);
    }

    if (gateway != null) {
      device.setGatewayId(gateway);
    }

    if (!CollectionUtils.isEmpty(properties)) {
      devicesApi.devicesUpdateProperties(properties.stream()
          .map(p -> new ExtendedPropertyCreate()
              .name(metadataProxy.getPropertykey(p.getName(), ENTITY_TYPE_DEVICE))
              .value(p.getValue()))
          .collect(Collectors.toList()), id);
    }

    if (!CollectionUtils.isEmpty(attributes)) {
      attributes.entrySet().forEach(e -> setDeviceAttribute(device, e));
    }

    devicesApi.devicesUpdate(device, id);
  }



  @Override
  public UUID createSpace(final String name, final UUID parent,
      final Collection<Property> properties, final Map<String, String> attributes) {

    final SpaceCreate space = new SpaceCreate();
    space.setName(name);
    space.setParentSpaceId(parent);

    if (!CollectionUtils.isEmpty(properties)) {
      properties.stream()
          .map(p -> new ExtendedPropertyCreate()
              .name(metadataProxy.getPropertykey(p.getName(), ENTITY_TYPE_SPACE))
              .value(p.getValue()))
          .forEach(space::addPropertiesItem);
    }

    if (!CollectionUtils.isEmpty(attributes)) {
      attributes.entrySet().forEach(e -> setSpaceAttribute(space, e));
    }

    return spacesApi.spacesCreate(space);

  }

  @Override
  public void updateSpaceComplete(final UUID id, final UUID parent,
      final Collection<Property> properties, final Map<String, String> attributes) {

    final SpaceUpdate space = new SpaceUpdate();
    space.setParentSpaceId(parent);

    if (!CollectionUtils.isEmpty(properties)) {
      spacesApi.spacesUpdateProperties(properties.stream()
          .map(p -> new ExtendedPropertyCreate()
              .name(metadataProxy.getPropertykey(p.getName(), ENTITY_TYPE_SPACE))
              .value(p.getValue()))
          .collect(Collectors.toList()), id);
    }

    setAllSpaceAttributes(space, attributes);

    spacesApi.spacesUpdate(space, id);
  }

  @Override
  public void updateSpacePartial(final UUID id, final UUID parent,
      final Collection<Property> properties, final Map<String, String> attributes) {

    final SpaceUpdate space = new SpaceUpdate();

    if (parent != null) {
      space.setParentSpaceId(parent);
    }

    if (!CollectionUtils.isEmpty(properties)) {
      spacesApi.spacesUpdateProperties(properties.stream()
          .map(p -> new ExtendedPropertyCreate()
              .name(metadataProxy.getPropertykey(p.getName(), ENTITY_TYPE_SPACE))
              .value(p.getValue()))
          .collect(Collectors.toList()), id);
    }

    if (!CollectionUtils.isEmpty(attributes)) {
      attributes.entrySet().forEach(e -> setSpaceAttribute(space, e));
    }

    spacesApi.spacesUpdate(space, id);
  }


  private void setDeviceAttribute(final DeviceUpdate device,
      final Map.Entry<String, String> attribute) {

    switch (attribute.getKey()) {
      case IngressMessage.ATTRIBUTE_V1_DESCRIPTION:
        device.setDescription(attribute.getValue());
        break;
      case IngressMessage.ATTRIBUTE_V1_FRIENDLY_NAME:
        device.setFriendlyName(attribute.getValue());
        break;
      case IngressMessage.ATTRIBUTE_V1_STATUS:
        device.setStatus(StatusEnum.fromValue(attribute.getValue()));
        break;
      case IngressMessage.ATTRIBUTE_V1_TYPE:
        device.setTypeId(metadataProxy.getDeviceType(attribute.getValue()));
        break;
      case IngressMessage.ATTRIBUTE_V1_SUB_TYPE:
        device.setSubtypeId(metadataProxy.getDeviceSubType(attribute.getValue()));
        break;
      default:
        log.error("Attribute [{}] not supported", attribute.getKey());
        break;
    }
    // TODO support device location
    // device.setLocation(location);

  }

  private void setSpaceAttribute(final SpaceUpdate space,
      final Map.Entry<String, String> attribute) {

    switch (attribute.getKey()) {
      case IngressMessage.ATTRIBUTE_V1_DESCRIPTION:
        space.setDescription(attribute.getValue());
        break;
      case IngressMessage.ATTRIBUTE_V1_FRIENDLY_NAME:
        space.setFriendlyName(attribute.getValue());
        break;
      case IngressMessage.ATTRIBUTE_V1_STATUS:
        space.setStatusId(metadataProxy.getSpaceStatus(attribute.getValue()));
        break;
      case IngressMessage.ATTRIBUTE_V1_TYPE:
        space.setTypeId(metadataProxy.getSpaceType(attribute.getValue()));
        break;
      case IngressMessage.ATTRIBUTE_V1_SUB_TYPE:
        space.setSubtypeId(metadataProxy.getSpaceSubType(attribute.getValue()));
        break;
      default:
        log.error("Attribute [{}] not supported", attribute.getKey());
        break;
    }

    // TODO support space location
    // device.setLocation(location);
  }



  @Override
  @Caching(cacheable = {@Cacheable(cacheNames = CACHE_DEVICE_BY_NAME)},
      put = {@CachePut(cacheNames = CACHE_DEVICE_BY_ID, key = "#result.id",
          condition = "#result != null")})
  public Optional<DeviceRetrieve> getDeviceByName(final String name) {
    return devicesApi
        .devicesRetrieve(new DevicesRetrieveQueryParams().names(name).includes("ConnectionString"))
        .stream().findAny();
  }

  @Override
  @Caching(cacheable = {@Cacheable(cacheNames = CACHE_SPACE_BY_NAME)}, put = {
      @CachePut(cacheNames = CACHE_SPACE_BY_ID, key = "#result.id", condition = "#result != null")})
  public Optional<SpaceRetrieve> getSpaceByName(final String name) {
    return spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().name(name)).stream()
        .map(srwc -> (SpaceRetrieve) srwc).findAny();
  }

  @Override
  @Caching(cacheable = {@Cacheable(cacheNames = CACHE_DEVICE_BY_ID)},
      put = {@CachePut(cacheNames = CACHE_DEVICE_BY_NAME, key = "#result.name",
          condition = "#result != null")})
  public Optional<DeviceRetrieve> getDeviceByDeviceId(final UUID deviceId) {
    return devicesApi
        .devicesRetrieve(
            new DevicesRetrieveQueryParams().ids(deviceId).includes("ConnectionString"))
        .stream().findAny();
  }

  @Override
  @Caching(cacheable = {@Cacheable(cacheNames = CACHE_SPACE_BY_ID)},
      put = {@CachePut(cacheNames = CACHE_SPACE_BY_NAME, key = "#result.name",
          condition = "#result != null")})
  public Optional<SpaceRetrieve> getSpaceBySpaceId(final UUID deviceId) {
    return spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().ids(deviceId)).stream()
        .map(srwc -> (SpaceRetrieve) srwc).findAny();
  }

  @Override
  @CacheEvict(cacheNames = CACHE_DEVICE_BY_NAME)
  public void deleteDeviceByName(final String name) {
    final Optional<DeviceRetrieve> device = getDeviceByName(name);

    if (device.isEmpty()) {
      log.warn("Device with [{}] does not exist. Will silently ignore delition.");
      return;
    }

    devicesApi.devicesDelete(device.get().getId());
    cacheManager.getCache(CACHE_DEVICE_BY_ID).evict(device.get().getId());
  }

  @Override
  @CacheEvict(cacheNames = CACHE_SPACE_BY_NAME)
  public void deleteSpaceByName(final String name) {
    final Optional<SpaceRetrieve> space = getSpaceByName(name);

    if (space.isEmpty()) {
      log.warn("Space with [{}] does not exist. Will silently ignore delition.");
      return;
    }

    spacesApi.spacesDelete(space.get().getId());
    cacheManager.getCache(CACHE_SPACE_BY_ID).evict(space.get().getId());
  }


  @Override
  @Cacheable(cacheNames = CACHE_GATEWAY_ID_BY_HARDWARE_ID)
  public Optional<UUID> getGatewayIdByHardwareId(final String hardwareId) {
    // Check first if hardware ID belongs to sensor
    final List<SensorRetrieve> sensors = sensorsApi.sensorsRetrieve(
        new SensorsApi.SensorsRetrieveQueryParams().hardwareIds(hardwareId).includes("device"));

    if (!CollectionUtils.isEmpty(sensors)) {
      if (StringUtils.isEmpty(sensors.get(0).getDevice().getGatewayId())) {
        return Optional.of(sensors.get(0).getDevice().getId());
      }
      return Optional.of(sensors.get(0).getDevice().getGatewayId());
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

    return Optional.of(devices.get(0).getGatewayId());
  }

  @Override
  public List<DeviceRetrieve> getDeviceChildrenOf(@NotNull final UUID space) {
    return devicesApi.devicesRetrieve(new DevicesRetrieveQueryParams().spaceId(space));
  }

  @Override
  public List<SpaceRetrieve> getSpaceChildrenOf(@NotNull final UUID space) {
    return spacesApi
        .spacesRetrieve(new SpacesRetrieveQueryParams().spaceId(space).useParentSpace(true))
        .stream().map(s -> (SpaceRetrieve) s).collect(Collectors.toList());
  }



}
