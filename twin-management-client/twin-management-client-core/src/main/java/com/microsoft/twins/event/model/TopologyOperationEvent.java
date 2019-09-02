/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.event.model;

import java.util.List;
import java.util.UUID;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@JsonInclude(Include.NON_NULL)
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class TopologyOperationEvent {

  @JsonProperty("Id")
  private UUID id;
  @JsonProperty("SpacesToNotify")
  private List<String> spacesToNotify;
  @JsonProperty("Type")
  private Type type;
  @JsonProperty("AccessType")
  private AccessType accessType;
  @JsonProperty("CorrelationId")
  private String correlationId;


  public enum AccessType {
    CREATE, DELETE, UPDATE;

    @JsonCreator
    public static AccessType fromString(final String key) {
      for (final AccessType type : AccessType.values()) {
        if (type.name().equalsIgnoreCase(key)) {
          return type;
        }
      }
      return null;
    }
  }

  public enum Type {
    DEVICE, DEVICEBLOBMETADATA, DEVICEEXTENDEDPROPERTY, EXTENDEDPROPERTYKEY, EXTENDEDTYPE, KEYSTORE, REPORT, ROLEDEFINITION, SENSOR, SENSORBLOBMETADATA, SENSOREXTENDEDPROPERTY, SPACE, SPACEBLOBMETADATA, SPACEEXTENDEDPROPERTY, SPACERESOURCE, SPACEROLEASSIGNMENT, SYSTEM, USER, USERBLOBMETADATA, USEREXTENDEDPROPERTY;

    @JsonCreator
    public static Type fromString(final String key) {
      for (final Type type : Type.values()) {
        if (type.name().equalsIgnoreCase(key)) {
          return type;
        }
      }
      return null;
    }
  }
}
