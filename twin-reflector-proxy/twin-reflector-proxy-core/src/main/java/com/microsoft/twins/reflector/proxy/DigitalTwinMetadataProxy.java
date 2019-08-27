/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.microsoft.twins.model.CategoryEnum;

/**
 * Proxy service for twin metadata, e.g. types and property keys.
 *
 */
public interface DigitalTwinMetadataProxy {

  String getPropertykey(@NotEmpty String name, @NotEmpty String scope);

  default int getDeviceType(@NotEmpty final String name) {
    return getType(name, CategoryEnum.DEVICETYPE);
  }

  default int getDeviceSubType(@NotEmpty final String name) {
    return getType(name, CategoryEnum.DEVICESUBTYPE);
  }

  default int getSpaceType(@NotEmpty final String name) {
    return getType(name, CategoryEnum.SPACETYPE);
  }

  default int getSpaceSubType(@NotEmpty final String name) {
    return getType(name, CategoryEnum.SPACESUBTYPE);
  }

  default int getSpaceStatus(@NotEmpty final String name) {
    return getType(name, CategoryEnum.SPACESTATUS);
  }

  int getType(@NotEmpty String name, @NotNull CategoryEnum category);
}
