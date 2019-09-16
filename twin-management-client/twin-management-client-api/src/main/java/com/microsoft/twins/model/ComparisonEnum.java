/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * Type of comparison to perform
 */
public enum ComparisonEnum {
  EQUALS("Equals"), NOTEQUALS("NotEquals"), CONTAINS("Contains");
  private final String value;

  ComparisonEnum(final String value) {
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
  public static ComparisonEnum fromValue(final String text) {
    for (final ComparisonEnum b : ComparisonEnum.values()) {
      if (String.valueOf(b.value).equalsIgnoreCase(text)) {
        return b;
      }
    }
    return null;
  }
}
