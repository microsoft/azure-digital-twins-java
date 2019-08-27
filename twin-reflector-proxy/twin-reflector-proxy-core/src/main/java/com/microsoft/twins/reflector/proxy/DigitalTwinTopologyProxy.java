/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.SpaceRetrieve;
import com.microsoft.twins.reflector.model.Property;


public interface DigitalTwinTopologyProxy {

  UUID createDevice(@NotEmpty String name, @NotNull UUID parent, @NotNull UUID gateway,
      List<Property> properties, Map<String, String> attributes);

  void updateDeviceComplete(@NotNull UUID id, @NotNull UUID parent, @NotNull UUID gateway,
      List<Property> properties, Map<String, String> attributes);

  void updateDevicePartial(@NotNull UUID id, UUID parent, UUID gateway, List<Property> properties,
      Map<String, String> attributes);

  default void updateDeviceParent(@NotNull final UUID id, @NotNull final UUID parent) {
    updateDevicePartial(id, parent, null, null, null);
  }


  UUID createSpace(@NotEmpty String name, @NotNull UUID parent, List<Property> properties,
      Map<String, String> attributes);

  void updateSpaceComplete(@NotNull UUID id, @NotNull UUID parent, List<Property> properties,
      Map<String, String> attributes);

  void updateSpacePartial(@NotNull UUID id, UUID parent, List<Property> properties,
      Map<String, String> attributes);

  default void updateSpaceParent(@NotNull final UUID id, final UUID parent) {
    updateSpaceComplete(id, parent, null, null);
  }


  Optional<DeviceRetrieve> getDeviceByName(@NotEmpty String name);

  Optional<SpaceRetrieve> getSpaceByName(@NotEmpty String name);

  Optional<DeviceRetrieve> getDeviceByDeviceId(@NotNull UUID deviceId);

  Optional<SpaceRetrieve> getSpaceBySpaceId(@NotNull UUID deviceId);

  Optional<UUID> getGatewayIdByHardwareId(@NotEmpty String hardwareId);

  void deleteDeviceByName(@NotEmpty String name);

  void deleteSpaceByName(@NotEmpty String name);

}
