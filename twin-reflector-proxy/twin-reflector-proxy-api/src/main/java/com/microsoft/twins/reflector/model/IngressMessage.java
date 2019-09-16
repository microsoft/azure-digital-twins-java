/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

import java.util.List;
import java.util.Map;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Ingress message that may contains topology changes as well telemetry data. Contains mandatory as
 * well as optional fields. Message are of a defined {@link MessageType}.
 *
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class IngressMessage {
  @JsonProperty(value = "id", required = true)
  @NotNull
  @Size(min = 3, max = 64)
  private String id;

  // Supported ADT V1 entity types
  public static final String ENTITY_V1_SPACE = "spaces";
  public static final String ENTITY_V1_DEVICE = "devices";

  @JsonProperty(value = "entity-type", required = true)
  @NotEmpty
  private String entityType;

  // Supported ADT V1 attributes
  public static final String ATTRIBUTE_V1_STATUS = "status";
  public static final String ATTRIBUTE_V1_TYPE = "type";
  public static final String ATTRIBUTE_V1_SUB_TYPE = "subType";
  public static final String ATTRIBUTE_V1_FRIENDLY_NAME = "friendlyName";
  public static final String ATTRIBUTE_V1_DESCRIPTION = "description";

  @JsonProperty("attributes")
  private Map<String, String> attributes;

  @JsonProperty("properties")
  private List<Property> properties;

  @JsonProperty("relationships")
  private List<Relationship> relationships;

  @JsonProperty(value = "telemetry")
  private Object telemetry;

}
