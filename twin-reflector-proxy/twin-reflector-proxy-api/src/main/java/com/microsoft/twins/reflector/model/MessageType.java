/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * type of the {@link IngressMessage} provides as header field.
 *
 */
public enum MessageType {

  /**
   * Full update, all attributes or properties that are not part of the {@link IngressMessage} are
   * deleted.
   */
  FULL,

  /**
   * Partial update. Attributes or properties that are not already part of the topology are added,
   * others updated.
   */
  PARTIAL,

  /**
   * Topology element delete.
   */
  DELETE;

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
