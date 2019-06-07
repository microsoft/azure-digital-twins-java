/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.event.model;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

// FIXME other events
@JsonInclude(Include.NON_NULL)
public class TopologyOperationEvent {

  @JsonProperty("Id")
  private String id;
  @JsonProperty("SpacesToNotify")
  private List<String> spacesToNotify;
  @JsonProperty("Type")
  private Type type;
  @JsonProperty("AccessType")
  private AccessType accessType;
  @JsonProperty("CorrelationId")
  private String correlationId;

  public TopologyOperationEvent() {
    // Jackson
  }

  public TopologyOperationEvent(final String id, final List<String> spacesToNotify, final Type type,
      final AccessType accessType, final String correlationId) {
    this.id = id;
    this.spacesToNotify = spacesToNotify;
    this.type = type;
    this.accessType = accessType;
    this.correlationId = correlationId;
  }

  public String getId() {
    return id;
  }

  public void setId(final String id) {
    this.id = id;
  }

  public List<String> getSpacesToNotify() {
    return spacesToNotify;
  }

  public void setSpacesToNotify(final List<String> spacesToNotify) {
    this.spacesToNotify = spacesToNotify;
  }

  public Type getType() {
    return type;
  }

  public void setType(final Type type) {
    this.type = type;
  }

  public AccessType getAccessType() {
    return accessType;
  }

  public void setAccessType(final AccessType accessType) {
    this.accessType = accessType;
  }

  public String getCorrelationId() {
    return correlationId;
  }

  public void setCorrelationId(final String correlationId) {
    this.correlationId = correlationId;
  }

  @Override
  public String toString() {
    return "TopologyOperationEvent [id=" + id + ", spacesToNotify=" + spacesToNotify + ", type="
        + type + ", accessType=" + accessType + ", correlationId=" + correlationId + "]";
  }

  enum AccessType {
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

  enum Type {
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
