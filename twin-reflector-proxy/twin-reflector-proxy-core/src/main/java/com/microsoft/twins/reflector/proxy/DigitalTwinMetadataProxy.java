/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import com.microsoft.twins.model.CategoryEnum;
import com.microsoft.twins.model.ScopeEnum;

/**
 * Proxy service for twin metadata, e.g. types and property keys.
 *
 */
public interface DigitalTwinMetadataProxy {

  /**
   * Retrieve property key if it exists or create otherwise.
   *
   * @param name of the property key
   * @param scope of the property
   * @return property key.
   */
  String getOrCreatePropertykey(@NotEmpty String name, @NotNull ScopeEnum scope);

  default int getDeviceType(@NotEmpty final String name) {
    return getOrCreateType(name, CategoryEnum.DEVICETYPE);
  }

  default int getDeviceSubType(@NotEmpty final String name) {
    return getOrCreateType(name, CategoryEnum.DEVICESUBTYPE);
  }

  default int getSpaceType(@NotEmpty final String name) {
    return getOrCreateType(name, CategoryEnum.SPACETYPE);
  }

  default int getSpaceSubType(@NotEmpty final String name) {
    return getOrCreateType(name, CategoryEnum.SPACESUBTYPE);
  }

  default int getSpaceStatus(@NotEmpty final String name) {
    return getOrCreateType(name, CategoryEnum.SPACESTATUS);
  }

  /**
   * Retrieve topology element type if it exists or create otherwise.
   *
   * @param name of the type
   * @param category of the type
   * @return ID of the type
   */
  int getOrCreateType(@NotEmpty String name, @NotNull CategoryEnum category);
}
