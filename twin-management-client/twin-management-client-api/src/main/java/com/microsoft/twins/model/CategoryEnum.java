/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The type&#x27;s category, for example SensorPortType
 */
public enum CategoryEnum {
  DEVICESUBTYPE("DeviceSubtype"), DEVICETYPE("DeviceType"), DEVICEBLOBSUBTYPE(
      "DeviceBlobSubtype"), DEVICEBLOBTYPE("DeviceBlobType"), SENSORDATASUBTYPE(
          "SensorDataSubtype"), SENSORDATATYPE("SensorDataType"), SENSORDATAUNITTYPE(
              "SensorDataUnitType"), SENSORPORTTYPE("SensorPortType"), SENSORTYPE(
                  "SensorType"), SPACEBLOBSUBTYPE("SpaceBlobSubtype"), SPACEBLOBTYPE(
                      "SpaceBlobType"), SPACESTATUS("SpaceStatus"), SPACESUBTYPE(
                          "SpaceSubtype"), SPACETYPE("SpaceType"), USERBLOBSUBTYPE(
                              "UserBlobSubtype"), USERBLOBTYPE("UserBlobType");
  private final String value;

  CategoryEnum(final String value) {
    this.value = value;
  }

  @JsonValue
  public String getValue() {
    return value;
  }

  @Override
  public String toString() {
    return String.valueOf(value);
  }

  @JsonCreator
  public static CategoryEnum fromValue(final String text) {
    for (final CategoryEnum b : CategoryEnum.values()) {
      if (String.valueOf(b.value).equalsIgnoreCase(text)) {
        return b;
      }
    }
    return null;
  }
}
