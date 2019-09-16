/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Primitive data type used for validation.
 */
public enum PrimitiveDataTypeEnum {
  NONE("None"), STRING("String"), BOOL("Bool"), INT("Int"), UINT("UInt"), LONG("Long"), DATETIME(
      "DateTime"), JSON("Json"), SET("Set"), ENUM("Enum");
  private final String value;

  PrimitiveDataTypeEnum(final String value) {
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
  public static PrimitiveDataTypeEnum fromValue(final String text) {
    for (final PrimitiveDataTypeEnum b : PrimitiveDataTypeEnum.values()) {
      if (String.valueOf(b.value).equalsIgnoreCase(text)) {
        return b;
      }
    }
    return null;
  }
}