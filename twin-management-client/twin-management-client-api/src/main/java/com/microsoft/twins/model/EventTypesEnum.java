/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Gets or Sets eventTypes
 */
public enum EventTypesEnum {
  SENSORCHANGE("SensorChange"), SPACECHANGE("SpaceChange"), TOPOLOGYOPERATION(
      "TopologyOperation"), DEVICEMESSAGE("DeviceMessage"), UDFCUSTOM("UdfCustom");
  private final String value;

  EventTypesEnum(final String value) {
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
  public static EventTypesEnum fromValue(final String text) {
    for (final EventTypesEnum b : EventTypesEnum.values()) {
      if (String.valueOf(b.value).equalsIgnoreCase(text)) {
        return b;
      }
    }
    return null;
  }
}