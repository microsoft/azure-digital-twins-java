/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Property {
  @JsonProperty("entity-type")
  private String entityType;

  @JsonProperty("name")
  private String name;

  @JsonProperty("value")
  private String value;
}
