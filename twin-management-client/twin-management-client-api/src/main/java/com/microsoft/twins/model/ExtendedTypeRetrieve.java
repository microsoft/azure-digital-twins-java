/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import java.util.List;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * ExtendedTypeRetrieve
 */
@ToString
@EqualsAndHashCode
public class ExtendedTypeRetrieve {
  @JsonProperty("id")
  private Integer id;
  @JsonProperty("spaceId")
  private UUID spaceId;
  @JsonProperty("space")
  private SpaceRetrieve space;

  @JsonProperty("category")
  private CategoryEnum category;
  @JsonProperty("name")
  private String name;
  @JsonProperty("disabled")
  private Boolean disabled;
  @JsonProperty("logicalOrder")
  private Integer logicalOrder;
  @JsonProperty("friendlyName")
  private String friendlyName;
  @JsonProperty("description")
  private String description;
  @JsonProperty("fullName")
  private String fullName;
  @JsonProperty("spacePaths")
  private List<String> spacePaths;
  @JsonProperty("ontologies")
  private List<OntologyRetrieve> ontologies;

  public ExtendedTypeRetrieve id(final Integer id) {
    this.id = id;
    return this;
  }

  /**
   * Type identifier
   *
   * @return id
   **/
  @NotNull
  public Integer getId() {
    return id;
  }

  public void setId(final Integer id) {
    this.id = id;
  }

  public ExtendedTypeRetrieve spaceId(final UUID spaceId) {
    this.spaceId = spaceId;
    return this;
  }

  /**
   * Parent space Id. If specified, the type is available to this space topology (custom type). If
   * not, the type is available to all spaces (system type).
   *
   * @return spaceId
   **/
  @Valid
  public UUID getSpaceId() {
    return spaceId;
  }

  public void setSpaceId(final UUID spaceId) {
    this.spaceId = spaceId;
  }

  public ExtendedTypeRetrieve space(final SpaceRetrieve space) {
    this.space = space;
    return this;
  }

  /**
   * Get space
   *
   * @return space
   **/
  @Valid
  public SpaceRetrieve getSpace() {
    return space;
  }

  public void setSpace(final SpaceRetrieve space) {
    this.space = space;
  }

  public ExtendedTypeRetrieve category(final CategoryEnum category) {
    this.category = category;
    return this;
  }

  /**
   * The type&#x27;s category, for example SensorPortType
   *
   * @return category
   **/
  @NotNull
  public CategoryEnum getCategory() {
    return category;
  }

  public void setCategory(final CategoryEnum category) {
    this.category = category;
  }

  public ExtendedTypeRetrieve name(final String name) {
    this.name = name;
    return this;
  }

  /**
   * Get name
   *
   * @return name
   **/
  @NotNull
  public String getName() {
    return name;
  }

  public void setName(final String name) {
    this.name = name;
  }

  public ExtendedTypeRetrieve disabled(final Boolean disabled) {
    this.disabled = disabled;
    return this;
  }

  /**
   * If disabled, a type cannot be be referenced. This can be used to remove types from loaded
   * ontologies or to prevent \&quot;wrong\&quot; type names from being created. For example,
   * creating a disabled type called &#x27;Temp&#x27; will enforce using Temperature instead
   *
   * @return disabled
   **/
  @NotNull
  public Boolean isDisabled() {
    return disabled;
  }

  public void setDisabled(final Boolean disabled) {
    this.disabled = disabled;
  }

  public ExtendedTypeRetrieve logicalOrder(final Integer logicalOrder) {
    this.logicalOrder = logicalOrder;
    return this;
  }

  /**
   * Display logical ordering
   *
   * @return logicalOrder
   **/
  @NotNull
  public Integer getLogicalOrder() {
    return logicalOrder;
  }

  public void setLogicalOrder(final Integer logicalOrder) {
    this.logicalOrder = logicalOrder;
  }

  public ExtendedTypeRetrieve friendlyName(final String friendlyName) {
    this.friendlyName = friendlyName;
    return this;
  }

  /**
   * Optional friendly name
   *
   * @return friendlyName
   **/
  public String getFriendlyName() {
    return friendlyName;
  }

  public void setFriendlyName(final String friendlyName) {
    this.friendlyName = friendlyName;
  }

  public ExtendedTypeRetrieve description(final String description) {
    this.description = description;
    return this;
  }

  /**
   * Get description
   *
   * @return description
   **/
  public String getDescription() {
    return description;
  }

  public void setDescription(final String description) {
    this.description = description;
  }

  public ExtendedTypeRetrieve fullName(final String fullName) {
    this.fullName = fullName;
    return this;
  }

  /**
   * Get fullName
   *
   * @return fullName
   **/
  public String getFullName() {
    return fullName;
  }

  public void setFullName(final String fullName) {
    this.fullName = fullName;
  }

  public ExtendedTypeRetrieve spacePaths(final List<String> spacePaths) {
    this.spacePaths = spacePaths;
    return this;
  }

  public ExtendedTypeRetrieve addSpacePathsItem(final String spacePathsItem) {
    if (this.spacePaths == null) {
      this.spacePaths = new java.util.ArrayList<>();
    }
    this.spacePaths.add(spacePathsItem);
    return this;
  }

  /**
   * Get spacePaths
   *
   * @return spacePaths
   **/
  public List<String> getSpacePaths() {
    return spacePaths;
  }

  public void setSpacePaths(final List<String> spacePaths) {
    this.spacePaths = spacePaths;
  }

  public ExtendedTypeRetrieve ontologies(final List<OntologyRetrieve> ontologies) {
    this.ontologies = ontologies;
    return this;
  }

  public ExtendedTypeRetrieve addOntologiesItem(final OntologyRetrieve ontologiesItem) {
    if (this.ontologies == null) {
      this.ontologies = new java.util.ArrayList<>();
    }
    this.ontologies.add(ontologiesItem);
    return this;
  }

  /**
   * List of ontologies this type belongs to
   *
   * @return ontologies
   **/
  @Valid
  public List<OntologyRetrieve> getOntologies() {
    return ontologies;
  }

  public void setOntologies(final List<OntologyRetrieve> ontologies) {
    this.ontologies = ontologies;
  }
}
