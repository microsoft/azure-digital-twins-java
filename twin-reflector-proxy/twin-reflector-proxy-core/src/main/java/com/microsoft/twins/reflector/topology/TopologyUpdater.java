/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.topology;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.SpaceRetrieve;
import com.microsoft.twins.reflector.error.InconsistentTopologyException;
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


    if ("devices".equalsIgnoreCase(update.getEntityType())) {
      updateDevicePartial(update, correlationId);
    } else if ("spaces".equalsIgnoreCase(update.getEntityType())) {
      updateSpacePartial(update, correlationId);
    }
  }


  public void updateTopologyElementComplete(@Valid final IngressMessage update,
      final UUID correlationId) {
    log.trace("Got complete topology update: [{}] with correlation ID: [{}]", update,
        correlationId);

    if ("devices".equalsIgnoreCase(update.getEntityType())) {
      updateDeviceComplete(update, correlationId);
    } else if ("spaces".equalsIgnoreCase(update.getEntityType())) {
      updateSpaceComplete(update, correlationId);
    }
  }


  private void updateSpaceComplete(final IngressMessage update, final UUID correlationId) {
    final Optional<SpaceRetrieve> existing = cachedDigitalTwinProxy.getSpaceByName(update.getId());

    if (existing.isPresent()) {
      cachedDigitalTwinProxy
          .updateSpaceComplete(existing.get().getId(),
              getParent(update.getRelationships(), correlationId)
                  .orElseGet(() -> tenantResolver.getTenant()),
              update.getProperties(), update.getAttributes());

      // TODO support removing childs as well
      updateChildSpaces(existing.get().getId(), update.getRelationships(), correlationId);
      updateChildDevices(existing.get().getId(), update.getRelationships(), correlationId);
    } else {
      final UUID created =
          cachedDigitalTwinProxy.createSpace(update.getId(),
              getParent(update.getRelationships(), correlationId)
                  .orElseGet(() -> tenantResolver.getTenant()),
              update.getProperties(), update.getAttributes());

      updateChildSpaces(created, update.getRelationships(), correlationId);
      updateChildDevices(created, update.getRelationships(), correlationId);
    }
  }

  private void updateSpacePartial(final IngressMessage update, final UUID correlationId) {
    final Optional<SpaceRetrieve> existing = cachedDigitalTwinProxy.getSpaceByName(update.getId());

    if (existing.isPresent()) {
      cachedDigitalTwinProxy.updateSpacePartial(existing.get().getId(),
          getParent(update.getRelationships(), correlationId).orElse(null), update.getProperties(),
          update.getAttributes());

      // TODO support removing childs as well
      updateChildSpaces(existing.get().getId(), update.getRelationships(), correlationId);
      updateChildDevices(existing.get().getId(), update.getRelationships(), correlationId);
    } else {
      final UUID created =
          cachedDigitalTwinProxy.createSpace(update.getId(),
              getParent(update.getRelationships(), correlationId)
                  .orElseGet(() -> tenantResolver.getTenant()),
              update.getProperties(), update.getAttributes());

      updateChildSpaces(created, update.getRelationships(), correlationId);
      updateChildDevices(created, update.getRelationships(), correlationId);
    }
  }


  private void updateDevicePartial(final IngressMessage update, final UUID correlationId) {
    final Optional<DeviceRetrieve> existing =
        cachedDigitalTwinProxy.getDeviceByName(update.getId());

    if (existing.isPresent()) {
      cachedDigitalTwinProxy.updateDevicePartial(existing.get().getId(),
          getParent(update.getRelationships(), correlationId).orElse(null),
          getGateway(update.getRelationships(), correlationId).orElse(null), update.getProperties(),
          update.getAttributes());
    } else {
      cachedDigitalTwinProxy.createDevice(update.getId(),
          getParent(update.getRelationships(), correlationId)
              .orElseGet(() -> tenantResolver.getTenant()),
          getGateway(update.getRelationships(), correlationId).orElseThrow(
              () -> new InconsistentTopologyException(update.getId() + " lacks a gateway",
                  correlationId)),
          update.getProperties(), update.getAttributes());
    }
  }

  private void updateDeviceComplete(final IngressMessage update, final UUID correlationId) {
    final Optional<DeviceRetrieve> existing =
        cachedDigitalTwinProxy.getDeviceByName(update.getId());

    if (existing.isPresent()) {
      cachedDigitalTwinProxy.updateDeviceComplete(existing.get().getId(),
          getParent(update.getRelationships(), correlationId)
              .orElseGet(() -> tenantResolver.getTenant()),
          getGateway(update.getRelationships(), correlationId).orElseThrow(
              () -> new InconsistentTopologyException(update.getId() + " lacks a gateway",
                  correlationId)),
          update.getProperties(), update.getAttributes());
    } else {
      cachedDigitalTwinProxy.createDevice(update.getId(),
          getParent(update.getRelationships(), correlationId)
              .orElseGet(() -> tenantResolver.getTenant()),
          getGateway(update.getRelationships(), correlationId).orElseThrow(
              () -> new InconsistentTopologyException(update.getId() + " lacks a gateway",
                  correlationId)),
          update.getProperties(), update.getAttributes());
    }
  }

  public void deleteTopologyElement(@NotBlank final String id, final UUID correlationId,
      final String entityType) {
    log.trace("Got delete topology element with ID: [{}] with correlation ID: [{}]", id,
        correlationId);

    if ("devices".equalsIgnoreCase(entityType)) {
      cachedDigitalTwinProxy.deleteDeviceByName(id);
    } else if ("spaces".equalsIgnoreCase(entityType)) {
      cachedDigitalTwinProxy.deleteSpaceByName(id);
    }
  }


  private Optional<UUID> getParent(final List<Relationship> relationShips,
      final UUID correlationId) {
    if (CollectionUtils.isEmpty(relationShips)) {
      return Optional.empty();
    }

    return relationShips.stream()
        .filter(
            relationShip -> "spaces".equalsIgnoreCase(relationShip.getEntityType()))
        .filter(relationShip -> "parent".equalsIgnoreCase(relationShip.getName())).findAny()
        .map(relationShip -> cachedDigitalTwinProxy.getSpaceByName(relationShip.getTargetId())
            .orElseThrow(() -> new InconsistentTopologyException(
                relationShip.getTargetId() + " does not exist", correlationId))
            .getId());
  }

  private void updateChildSpaces(final UUID parent, final List<Relationship> relationShips,
      final UUID correlationId) {
    if (CollectionUtils.isEmpty(relationShips)) {
      return;
    }

    relationShips.stream()
        .filter(relationShip -> "spaces".equalsIgnoreCase(relationShip.getEntityType()))
        .filter(relationShip -> "child".equalsIgnoreCase(relationShip.getName()))
        .map(relationShip -> cachedDigitalTwinProxy.getSpaceByName(relationShip.getTargetId())
            .orElseThrow(() -> new InconsistentTopologyException(
                relationShip.getTargetId() + " does not exist", correlationId)))
        .forEach(
            childSpace -> cachedDigitalTwinProxy.updateSpaceParent(childSpace.getId(), parent));
  }

  private void updateChildDevices(final UUID parent, final List<Relationship> relationShips,
      final UUID correlationId) {
    if (CollectionUtils.isEmpty(relationShips)) {
      return;
    }

    relationShips.stream()
        .filter(relationShip -> "devices".equalsIgnoreCase(relationShip.getEntityType()))
        .filter(relationShip -> "child".equalsIgnoreCase(relationShip.getName()))
        .map(relationShip -> cachedDigitalTwinProxy.getDeviceByName(relationShip.getTargetId())
            .orElseThrow(() -> new InconsistentTopologyException(
                relationShip.getTargetId() + " does not exist", correlationId)))
        .forEach(
            childSpace -> cachedDigitalTwinProxy.updateDeviceParent(childSpace.getId(), parent));
  }

  private Optional<UUID> getGateway(final List<Relationship> relationShips,
      final UUID correlationId) {
    if (CollectionUtils.isEmpty(relationShips)) {
      return Optional.empty();
    }

    return relationShips.stream()
        .filter(relationShip -> "devices".equalsIgnoreCase(relationShip.getEntityType()))
        .filter(relationShip -> "gateway".equalsIgnoreCase(relationShip.getName())).findAny()
        .map(relationShip -> cachedDigitalTwinProxy
            .getGatewayIdByHardwareId(relationShip.getTargetId())
            .orElseThrow(() -> new InconsistentTopologyException(
                relationShip.getTargetId() + " does not exist", correlationId)));
  }

}
