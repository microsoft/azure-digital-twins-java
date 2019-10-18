/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.SpaceRetrieve;
import com.microsoft.twins.reflector.model.MessageType;
import com.microsoft.twins.reflector.model.Property;


/**
 * Service to manage the twin topology, e.g. devices and spaces.
 *
 */
public interface DigitalTwinTopologyProxy {

  /**
   * Creates a new device topology element in ADT.
   *
   * @param name of the device
   * @param parent of the device, e.g. space.
   * @param gateway of the device for Azure IoT Hub operations.
   * @param properties of the device
   * @param attributes of the device
   * @return the ID of the created device.
   */
  UUID createDevice(@NotEmpty String name, @NotNull UUID parent, @NotNull UUID gateway,
      Collection<Property> properties, Map<String, String> attributes);

  /**
   * Updates device complete, i.e. all properties or attributes not provided by the caller are
   * removed from the device.
   *
   * @param existing device element to update
   * @param parent of the device, e.g. space.
   * @param gateway of the device for Azure IoT Hub operations.
   * @param properties of the device
   * @param attributes of the device
   *
   * @see MessageType#FULL
   */
  void updateDeviceComplete(@NotNull DeviceRetrieve existing, @NotNull UUID parent, @NotNull UUID gateway,
      Collection<Property> properties, Map<String, String> attributes);

  /**
   * Updates device partial, i.e. all properties or attributes not provided by the caller are kept
   * on the device and only the provided ones are set.
   *
   * @param existing device element to update
   * @param parent of the device, e.g. space.
   * @param gateway of the device for Azure IoT Hub operations.
   * @param properties of the device
   * @param attributes of the device
   *
   * @see MessageType#PARTIAL
   */
  void updateDevicePartial(@NotNull DeviceRetrieve existing, UUID parent, UUID gateway,
      Collection<Property> properties, Map<String, String> attributes);

  default void updateDeviceParent(@NotNull final DeviceRetrieve existing, @NotNull final UUID parent) {
    updateDevicePartial(existing, parent, null, null, null);
  }

  /**
   * Creates a new space topology element in ADT.
   *
   * @param name of the device
   * @param parent of the device, e.g. space.
   * @param properties of the device
   * @param attributes of the device
   *
   * @return the ID of the created space.
   */
  UUID createSpace(@NotEmpty String name, @NotNull UUID parent, Collection<Property> properties,
      Map<String, String> attributes);

  /**
   * Updates space complete, i.e. all properties or attributes not provided by the caller are
   * removed from the space.
   *
   * @param id of the device
   * @param parent of the device, e.g. space.
   * @param properties of the device
   * @param attributes of the device
   *
   * @see MessageType#FULL
   */
  void updateSpaceComplete(@NotNull UUID id, @NotNull UUID parent, Collection<Property> properties,
      Map<String, String> attributes);

  /**
   * Updates space partial, i.e. all properties or attributes not provided by the caller are kept on
   * the space and only the provided ones are set.
   *
   * @param id of the device
   * @param parent of the device, e.g. space.
   * @param properties of the device
   * @param attributes of the device
   *
   * @see MessageType#PARTIAL
   */
  void updateSpacePartial(@NotNull UUID id, UUID parent, Collection<Property> properties,
      Map<String, String> attributes);

  default void updateSpaceParent(@NotNull final UUID id, final UUID parent) {
    updateSpaceComplete(id, parent, null, null);
  }


  Optional<DeviceRetrieve> getDeviceByName(@NotEmpty String name);

  List<DeviceRetrieve> getDeviceChildrenOf(@NotNull UUID space);

  Optional<SpaceRetrieve> getSpaceByName(@NotEmpty String name);

  List<SpaceRetrieve> getSpaceChildrenOf(@NotNull UUID space);

  Optional<DeviceRetrieve> getDeviceByDeviceId(@NotNull UUID deviceId);

  Optional<SpaceRetrieve> getSpaceBySpaceId(@NotNull UUID deviceId);

  Optional<UUID> getGatewayIdByHardwareId(@NotEmpty String hardwareId);

  void deleteDeviceByName(@NotEmpty String name);

  void deleteSpaceByName(@NotEmpty String name);

}
