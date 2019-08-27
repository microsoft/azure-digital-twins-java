/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Endpoint status
 */
public enum StatusEnum {
  PROVISIONING("Provisioning"), READY("Ready"), STOPPED("Stopped"), FAILED("Failed"), DELETING(
      "Deleting");
  private final String value;

  StatusEnum(final String value) {
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
  public static StatusEnum fromValue(final String text) {
    for (final StatusEnum b : StatusEnum.values()) {
      if (String.valueOf(b.value).equalsIgnoreCase(text)) {
        return b;
      }
    }
    return null;
  }
}