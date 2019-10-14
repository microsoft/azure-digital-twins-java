/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.topology;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.SpaceRetrieve;
import com.microsoft.twins.reflector.error.EntityTypeNotSupportedException;
import com.microsoft.twins.reflector.error.TopologyElementDoesNotExistException;
import com.microsoft.twins.reflector.model.IngressMessage;
import com.microsoft.twins.reflector.model.Relationship;
import com.microsoft.twins.reflector.proxy.DigitalTwinTopologyProxy;
import com.microsoft.twins.reflector.proxy.TenantResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Validated
public class TopologyUpdater {

  private final TenantResolver tenantResolver;
  private final DigitalTwinTopologyProxy cachedDigitalTwinProxy;


  // Note: for the time being we assume the ID is stored in the name fields and we only handle the
  // topology element device for now.
  public void updateTopologyElementPartial(@Valid final IngressMessage update,
      final UUID correlationId) {
    log.trace("Got partial topology update: [{}] with correlation ID: [{}]", update, correlationId);


    if (IngressMessage.ENTITY_V1_DEVICE.equalsIgnoreCase(update.getEntityType())) {
      updateDevicePartial(update, correlationId);
    } else if (IngressMessage.ENTITY_V1_SPACE.equalsIgnoreCase(update.getEntityType())) {
      updateSpacePartial(update, correlationId);
    } else {
      throw new EntityTypeNotSupportedException(update.getEntityType(), correlationId);
    }
  }


  public void updateTopologyElementComplete(@Valid final IngressMessage update,
      final UUID correlationId) {
    log.trace("Got complete topology update: [{}] with correlation ID: [{}]", update,
        correlationId);

    if (IngressMessage.ENTITY_V1_DEVICE.equalsIgnoreCase(update.getEntityType())) {
      updateDeviceComplete(update, correlationId);
    } else if (IngressMessage.ENTITY_V1_SPACE.equalsIgnoreCase(update.getEntityType())) {
      updateSpaceComplete(update, correlationId);
    } else {
      throw new EntityTypeNotSupportedException(update.getEntityType(), correlationId);
    }
  }


  private void updateSpaceComplete(final IngressMessage update, final UUID correlationId) {
    final Optional<SpaceRetrieve> existing = cachedDigitalTwinProxy.getSpaceByName(update.getId());

    if (existing.isPresent()) {
      cachedDigitalTwinProxy.updateSpaceComplete(existing.get().getId(),
          getParent(update.getRelationships()).orElseGet(tenantResolver::getTenant),
          update.getProperties(), update.getAttributes());

      updateChildSpaces(existing.get().getId(), update.getRelationships(), correlationId, true);
      updateChildDevices(existing.get().getId(), update.getRelationships(), correlationId, true);
    } else {
      final UUID created = cachedDigitalTwinProxy.createSpace(update.getId(),
          getParent(update.getRelationships()).orElseGet(tenantResolver::getTenant),
          update.getProperties(), update.getAttributes());

      updateChildSpaces(created, update.getRelationships(), correlationId, false);
      updateChildDevices(created, update.getRelationships(), correlationId, false);

    }
  }

  private void updateSpacePartial(final IngressMessage update, final UUID correlationId) {
    final Optional<SpaceRetrieve> existing = cachedDigitalTwinProxy.getSpaceByName(update.getId());

    if (existing.isPresent()) {
      cachedDigitalTwinProxy.updateSpacePartial(existing.get().getId(),
          getParent(update.getRelationships()).orElse(null), update.getProperties(),
          update.getAttributes());

      updateChildSpaces(existing.get().getId(), update.getRelationships(), correlationId, false);
      updateChildDevices(existing.get().getId(), update.getRelationships(), correlationId, false);
    } else {
      final UUID created = cachedDigitalTwinProxy.createSpace(update.getId(),
          getParent(update.getRelationships()).orElseGet(tenantResolver::getTenant),
          update.getProperties(), update.getAttributes());

      updateChildSpaces(created, update.getRelationships(), correlationId, false);
      updateChildDevices(created, update.getRelationships(), correlationId, false);
    }
  }


  private void updateDevicePartial(final IngressMessage update, final UUID correlationId) {
    final Optional<DeviceRetrieve> existing =
        cachedDigitalTwinProxy.getDeviceByName(update.getId());

    if (existing.isPresent()) {
      cachedDigitalTwinProxy.updateDevicePartial(existing.get(),
          getParent(update.getRelationships()).orElse(null),
          getGateway(update.getRelationships(), correlationId).orElse(null), update.getProperties(),
          update.getAttributes());
    } else {
      cachedDigitalTwinProxy.createDevice(update.getId(),
          getParent(update.getRelationships()).orElseGet(tenantResolver::getTenant),
          getGateway(update.getRelationships(), correlationId).orElseGet(
              tenantResolver::getGateway),
          update.getProperties(), update.getAttributes());
    }
  }

  private void updateDeviceComplete(final IngressMessage update, final UUID correlationId) {
    final Optional<DeviceRetrieve> existing =
        cachedDigitalTwinProxy.getDeviceByName(update.getId());

    if (existing.isPresent()) {
      cachedDigitalTwinProxy.updateDeviceComplete(existing.get(),
          getParent(update.getRelationships()).orElseGet(tenantResolver::getTenant),
          getGateway(update.getRelationships(), correlationId).orElseGet(
              tenantResolver::getGateway),
          update.getProperties(), update.getAttributes());
    } else {
      cachedDigitalTwinProxy.createDevice(update.getId(),
          getParent(update.getRelationships()).orElseGet(tenantResolver::getTenant),
          getGateway(update.getRelationships(), correlationId).orElseGet(
              tenantResolver::getGateway),
          update.getProperties(), update.getAttributes());
    }
  }

  public void deleteTopologyElement(@NotBlank final String id, final UUID correlationId,
      final String entityType) {
    log.trace("Got delete topology element with ID: [{}] with correlation ID: [{}]", id,
        correlationId);

    if (IngressMessage.ENTITY_V1_DEVICE.equalsIgnoreCase(entityType)) {
      cachedDigitalTwinProxy.deleteDeviceByName(id);
    } else if (IngressMessage.ENTITY_V1_SPACE.equalsIgnoreCase(entityType)) {
      cachedDigitalTwinProxy.deleteSpaceByName(id);
    } else {
      throw new EntityTypeNotSupportedException(entityType, correlationId);
    }
  }


  private Optional<UUID> getParent(final Collection<Relationship> relationShips) {
    if (CollectionUtils.isEmpty(relationShips)) {
      return Optional.empty();
    }

    return relationShips.stream()
        .filter(relationShip -> IngressMessage.ENTITY_V1_SPACE
            .equalsIgnoreCase(relationShip.getEntityType()))
        .filter(relationShip -> Relationship.RELATIONSHIP_PARENT
            .equalsIgnoreCase(relationShip.getName()))
        .findAny().map(relationShip -> cachedDigitalTwinProxy
            .getSpaceByName(relationShip.getTargetId()).map(SpaceRetrieve::getId).orElse(null));
  }

  private void updateChildSpaces(final UUID parent, final Collection<Relationship> relationShips,
      final UUID correlationId, final boolean removeOrphans) {
    if (CollectionUtils.isEmpty(relationShips)) {
      if (removeOrphans) {
        cachedDigitalTwinProxy.getSpaceChildrenOf(parent).forEach(orphan -> cachedDigitalTwinProxy
            .updateSpaceParent(orphan.getId(), tenantResolver.getTenant()));
      }

      return;
    }

    final List<SpaceRetrieve> children = relationShips.stream()
        .filter(relationShip -> IngressMessage.ENTITY_V1_SPACE
            .equalsIgnoreCase(relationShip.getEntityType()))
        .filter(relationShip -> Relationship.RELATIONSHIP_CHILD
            .equalsIgnoreCase(relationShip.getName()))
        .map(relationShip -> cachedDigitalTwinProxy.getSpaceByName(relationShip.getTargetId())
            .orElse(null))
        .collect(Collectors.toList());

    if (removeOrphans) {
      final List<SpaceRetrieve> existing = cachedDigitalTwinProxy.getSpaceChildrenOf(parent);
      final Set<UUID> childIds =
          children.stream().map(SpaceRetrieve::getId).collect(Collectors.toSet());
      existing.removeIf(child -> childIds.contains(child.getId()));

      if (!CollectionUtils.isEmpty(existing)) {
        existing.forEach(orphan -> cachedDigitalTwinProxy.updateSpaceParent(orphan.getId(),
            tenantResolver.getTenant()));
      }
    }

    children.forEach(child -> cachedDigitalTwinProxy.updateSpaceParent(child.getId(), parent));
  }

  private void updateChildDevices(final UUID parent, final Collection<Relationship> relationShips,
      final UUID correlationId, final boolean removeOrphans) {
    if (CollectionUtils.isEmpty(relationShips)) {
      if (removeOrphans) {
        cachedDigitalTwinProxy.getDeviceChildrenOf(parent).forEach(orphan -> cachedDigitalTwinProxy
            .updateDeviceParent(orphan, tenantResolver.getTenant()));
      }

      return;
    }

    final List<DeviceRetrieve> children = relationShips.stream()
        .filter(relationShip -> IngressMessage.ENTITY_V1_DEVICE
            .equalsIgnoreCase(relationShip.getEntityType()))
        .filter(relationShip -> Relationship.RELATIONSHIP_CHILD
            .equalsIgnoreCase(relationShip.getName()))
        .map(relationShip -> cachedDigitalTwinProxy.getDeviceByName(relationShip.getTargetId())
            .orElse(null))
        .collect(Collectors.toList());

    if (removeOrphans) {
      final List<DeviceRetrieve> existing = cachedDigitalTwinProxy.getDeviceChildrenOf(parent);
      final Set<UUID> childIds =
          children.stream().map(DeviceRetrieve::getId).collect(Collectors.toSet());
      existing.removeIf(child -> childIds.contains(child.getId()));

      if (!CollectionUtils.isEmpty(existing)) {
        existing.forEach(orphan -> cachedDigitalTwinProxy.updateDeviceParent(orphan,
            tenantResolver.getTenant()));
      }
    }

    children.forEach(child -> cachedDigitalTwinProxy.updateDeviceParent(child, parent));



  }

  private Optional<UUID> getGateway(final Collection<Relationship> relationShips,
      final UUID correlationId) {
    if (CollectionUtils.isEmpty(relationShips)) {
      return Optional.empty();
    }

    return relationShips.stream()
        .filter(relationShip -> IngressMessage.ENTITY_V1_DEVICE
            .equalsIgnoreCase(relationShip.getEntityType()))
        .filter(relationShip -> Relationship.RELATIONSHIP_GATEWAY
            .equalsIgnoreCase(relationShip.getName()))
        .findAny()
        .map(relationShip -> cachedDigitalTwinProxy
            .getGatewayIdByHardwareId(relationShip.getTargetId())
            .orElseThrow(() -> new TopologyElementDoesNotExistException(relationShip.getTargetId(),
                correlationId)));
  }

}
