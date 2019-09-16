/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

import javax.validation.constraints.NotEmpty;
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
 * ADT properties. The name field is the ExtendedPropertyKey.
 *
 */
@ToString
@EqualsAndHashCode
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Property {
  @JsonProperty("entity-type")
  private String entityType;

  @JsonProperty("name")
  @NotEmpty
  @Size(min = 5, max = 50)
  private String name;

  @JsonProperty("value")
  private String value;
}
