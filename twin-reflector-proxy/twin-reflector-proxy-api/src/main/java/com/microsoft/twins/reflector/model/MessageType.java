/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

import com.fasterxml.jackson.annotation.JsonCreator;

public enum MessageType {
  FULL, PARTIAL, DELETE;

  @JsonCreator
  public static MessageType fromString(final String key) {
    for (final MessageType type : MessageType.values()) {
      if (type.name().equalsIgnoreCase(key)) {
        return type;
      }
    }
    return null;
  }
}
