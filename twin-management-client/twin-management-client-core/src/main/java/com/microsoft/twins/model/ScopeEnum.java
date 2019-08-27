/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * The scope the property key applies to
 */
public enum ScopeEnum {
  SPACES("Spaces"), SENSORS("Sensors"), USERS("Users"), DEVICES("Devices");
  private final String value;

  ScopeEnum(final String value) {
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
  public static ScopeEnum fromValue(final String text) {
    for (final ScopeEnum b : ScopeEnum.values()) {
      if (String.valueOf(b.value).equalsIgnoreCase(text)) {
        return b;
      }
    }
    return null;
  }
}