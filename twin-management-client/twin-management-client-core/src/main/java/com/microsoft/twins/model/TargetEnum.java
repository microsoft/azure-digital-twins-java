/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * What object the condition applies to: the sensor, its parent device or its parent space
 */
public enum TargetEnum {
  SENSOR("Sensor"), SENSORDEVICE("SensorDevice"), SENSORSPACE("SensorSpace");
  private final String value;

  TargetEnum(final String value) {
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
  public static TargetEnum fromValue(final String text) {
    for (final TargetEnum b : TargetEnum.values()) {
      if (String.valueOf(b.value).equalsIgnoreCase(text)) {
        return b;
      }
    }
    return null;
  }
}