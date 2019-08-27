/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Relationship {
  /**
   * Supported values are
   *
   * - {@link IngressMessage#ENTITY_V1_DEVICE}<br/>
   * - {@link IngressMessage#ENTITY_V1_SPACE}.
   */
  @JsonProperty("entity-type")
  private String entityType;

  // Supported ADT V1 relationships
  public static final String RELATIONSHIP_CHILD = "child";
  public static final String RELATIONSHIP_PARENT = "parent";
  public static final String RELATIONSHIP_GATEWAY = "gateway";

  @JsonProperty("name")
  private String name;

  @JsonProperty("target-id")
  private String targetId;
}
