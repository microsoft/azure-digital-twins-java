/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * SpaceResourceCreate
 */
@ToString
@EqualsAndHashCode
public class SpaceResourceCreate {
  @JsonProperty("spaceId")
  private UUID spaceId;

  @JsonProperty("type")
  private SpaceTypeEnum type;

  @JsonProperty("size")
  private SizeEnum size;

  @JsonProperty("region")
  private RegionEnum region;
  @JsonProperty("isExternallyCreated")
  private Boolean isExternallyCreated;
  @JsonProperty("parameters")
  private Map<String, String> parameters;
  @JsonProperty("resourceDependencies")
  private List<UUID> resourceDependencies;

  @JsonProperty("status")
  private StatusEnum status;

  public SpaceResourceCreate spaceId(final UUID spaceId) {
    this.spaceId = spaceId;
    return this;
  }

  /**
   * Parent space
   *
   * @return spaceId
   **/
  @NotNull
  @Valid
  public UUID getSpaceId() {
    return spaceId;
  }

  public void setSpaceId(final UUID spaceId) {
    this.spaceId = spaceId;
  }

  public SpaceResourceCreate type(final SpaceTypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Resource type
   *
   * @return type
   **/
  @NotNull
  public SpaceTypeEnum getType() {
    return type;
  }

  public void setType(final SpaceTypeEnum type) {
    this.type = type;
  }

  public SpaceResourceCreate size(final SizeEnum size) {
    this.size = size;
    return this;
  }

  /**
   * Resource size
   *
   * @return size
   **/
  public SizeEnum getSize() {
    return size;
  }

  public void setSize(final SizeEnum size) {
    this.size = size;
  }

  public SpaceResourceCreate region(final RegionEnum region) {
    this.region = region;
    return this;
  }

  /**
   * Resource region
   *
   * @return region
   **/
  public RegionEnum getRegion() {
    return region;
  }

  public void setRegion(final RegionEnum region) {
    this.region = region;
  }

  public SpaceResourceCreate isExternallyCreated(final Boolean isExternallyCreated) {
    this.isExternallyCreated = isExternallyCreated;
    return this;
  }

  /**
   * If the resource was created externally
   *
   * @return isExternallyCreated
   **/
  public Boolean isIsExternallyCreated() {
    return isExternallyCreated;
  }

  public void setIsExternallyCreated(final Boolean isExternallyCreated) {
    this.isExternallyCreated = isExternallyCreated;
  }

  public SpaceResourceCreate parameters(final Map<String, String> parameters) {
    this.parameters = parameters;
    return this;
  }

  public SpaceResourceCreate putParametersItem(final String key, final String parametersItem) {
    if (this.parameters == null) {
      this.parameters = new java.util.HashMap<>();
    }
    this.parameters.put(key, parametersItem);
    return this;
  }

  /**
   * Resource-type-dependent template parameters
   *
   * @return parameters
   **/
  public Map<String, String> getParameters() {
    return parameters;
  }

  public void setParameters(final Map<String, String> parameters) {
    this.parameters = parameters;
  }

  public SpaceResourceCreate resourceDependencies(final List<UUID> resourceDependencies) {
    this.resourceDependencies = resourceDependencies;
    return this;
  }

  public SpaceResourceCreate addResourceDependenciesItem(final UUID resourceDependenciesItem) {
    if (this.resourceDependencies == null) {
      this.resourceDependencies = new java.util.ArrayList<>();
    }
    this.resourceDependencies.add(resourceDependenciesItem);
    return this;
  }

  /**
   * List of resources the current resource is depending on
   *
   * @return resourceDependencies
   **/
  @Valid
  public List<UUID> getResourceDependencies() {
    return resourceDependencies;
  }

  public void setResourceDependencies(final List<UUID> resourceDependencies) {
    this.resourceDependencies = resourceDependencies;
  }

  public SpaceResourceCreate status(final StatusEnum status) {
    this.status = status;
    return this;
  }

  /**
   * Resource status
   *
   * @return status
   **/
  public StatusEnum getStatus() {
    return status;
  }

  public void setStatus(final StatusEnum status) {
    this.status = status;
  }
}
