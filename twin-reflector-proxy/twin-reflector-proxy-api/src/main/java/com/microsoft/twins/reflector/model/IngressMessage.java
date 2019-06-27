/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

import java.util.Map;
import java.util.Set;
import javax.validation.constraints.NotBlank;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Setter
public class IngressMessage {
  @JsonProperty(value = "id", required = true)
  @NotBlank
  private String id;

  @JsonProperty("entity-type")
  private String entityType;

  @JsonProperty("attributes")
  private Map<String, String> attributes;

  @JsonProperty("properties")
  private Set<Property> properties;

  @JsonProperty("relationships")
  private Set<Relationship> relationships;

  @JsonProperty(value = "telemetry")
  private Object telemetry;

}
