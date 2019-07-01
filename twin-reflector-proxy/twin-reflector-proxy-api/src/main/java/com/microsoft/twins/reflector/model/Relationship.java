/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Relationship {
  @JsonProperty("entity-type")
  private String entityType;

  @JsonProperty("name")
  private String name;

  @JsonProperty("target-id")
  private String targetId;
}
