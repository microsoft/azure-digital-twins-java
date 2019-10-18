/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The status
 */
public enum DeviceStatusEnum {
  PROVISIONED("Provisioned"), ACTIVE("Active"), OFFLINE("Offline"), DISABLED("Disabled");
  private final String value;

  DeviceStatusEnum(final String value) {
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
  public static DeviceStatusEnum fromValue(final String text) {
    for (final DeviceStatusEnum b : DeviceStatusEnum.values()) {
      if (String.valueOf(b.value).equalsIgnoreCase(text)) {
        return b;
      }
    }
    return null;
  }
}