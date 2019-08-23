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
import com.microsoft.twins.reflector.proxy.CachedDigitalTwinTopologyProxy;
import com.microsoft.twins.reflector.proxy.TenantResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;


// TODO handle child relationship, i.e. update parent field of specified child element
@RequiredArgsConstructor
@Slf4j
@Validated
public class TopologyUpdater {

  private final TenantResolver tenantResolver;
  private final CachedDigitalTwinTopologyProxy cachedDigitalTwinProxy;


  // Note: for the time being we assume the ID is stored in the name fields and we only handle the
  // topology element device for now.
  public void updateTopologyElementPartial(@Valid final IngressMessage update,
      final UUID correlationId) {
    log.trace("Got partial topology update: [{}] with correlation ID: [{}]", update, correlationId);



    // TODO implement
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
      cachedDigitalTwinProxy.updateSpace(existing.get().getId(),
          getParent(update.getRelationships()).orElseGet(() -> tenantResolver.getTenant()),
          update.getProperties(), update.getAttributes());
    } else {
      cachedDigitalTwinProxy.createSpace(update.getId(),
          getParent(update.getRelationships()).orElseGet(() -> tenantResolver.getTenant()),
          update.getProperties(), update.getAttributes());
    }
  }


  private void updateDeviceComplete(final IngressMessage update, final UUID correlationId) {
    final Optional<DeviceRetrieve> existing =
        cachedDigitalTwinProxy.getDeviceByName(update.getId());

    if (existing.isPresent()) {
      cachedDigitalTwinProxy.updateDevice(existing.get().getId(),
          getParent(update.getRelationships()).orElseGet(() -> tenantResolver.getTenant()),
          getGateway(update.getRelationships()).orElseThrow(
              () -> new InconsistentTopologyException(update.getId() + " lacks a gateway",
                  correlationId)),
          update.getProperties(), update.getAttributes());
    } else {
      cachedDigitalTwinProxy.createDevice(update.getId(),
          getParent(update.getRelationships()).orElseGet(() -> tenantResolver.getTenant()),
          getGateway(update.getRelationships()).orElseThrow(
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


  private static Optional<UUID> getParent(final List<Relationship> relationShips) {
    if (CollectionUtils.isEmpty(relationShips)) {
      return Optional.empty();
    }

    return relationShips.stream()
        .filter(relationShip -> "spaces".equalsIgnoreCase(relationShip.getEntityType())
            && "parent".equalsIgnoreCase(relationShip.getName()))
        .findAny().map(Relationship::getTargetId);
  }

  private static Optional<UUID> getGateway(final List<Relationship> relationShips) {
    if (CollectionUtils.isEmpty(relationShips)) {
      return Optional.empty();
    }

    return relationShips.stream()
        .filter(relationShip -> "devices".equalsIgnoreCase(relationShip.getEntityType())
            && "gateway".equalsIgnoreCase(relationShip.getName()))
        .findAny().map(Relationship::getTargetId);
  }

}
