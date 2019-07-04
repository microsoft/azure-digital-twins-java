/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
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

  /**
   * Resource type
   */
  public enum TypeEnum {
    IOTHUB("IotHub");
    private final String value;

    TypeEnum(final String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static TypeEnum fromValue(final String text) {
      for (final TypeEnum b : TypeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("type")
  private TypeEnum type;

  /**
   * Resource size
   */
  public enum SizeEnum {
    XS("XS"), S("S"), M("M"), L("L"), XL("XL"), XXL("XXL");
    private final String value;

    SizeEnum(final String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static SizeEnum fromValue(final String text) {
      for (final SizeEnum b : SizeEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("size")
  private SizeEnum size;

  /**
   * Resource region
   */
  public enum RegionEnum {
    WESTUS("WestUS"), WESTUS2("WestUS2"), CENTRALUSEUAP("CentralUSEUAP"), EASTUS("EastUS"), EASTUS2(
        "EastUS2"), CENTRALUS("CentralUS"), SOUTHCENTRALUS("SouthCentralUS"), WESTCENTRALUS(
            "WestCentralUS"), CANADAEAST("CanadaEast"), CANADACENTRAL("CanadaCentral"), BRAZILSOUTH(
                "BrazilSouth"), AUSTRALIAEAST("AustraliaEast"), AUSTRALIASOUTHEAST(
                    "AustraliaSoutheast"), SOUTHEASTASIA("SoutheastAsia"), EASTASIA(
                        "EastAsia"), CENTRALINDIA(
                            "CentralIndia"), SOUTHINDIA("SouthIndia"), JAPANEAST(
                                "JapanEast"), JAPANWEST("JapanWest"), KOREACENTRAL(
                                    "KoreaCentral"), KOREASOUTH("KoreaSouth"), NORTHEUROPE(
                                        "NorthEurope"), WESTEUROPE("WestEurope"), UKWEST(
                                            "UKWest"), UKSOUTH("UKSouth"), GERMANYCENTRAL(
                                                "GermanyCentral"), GERMANYNORTHEAST(
                                                    "GermanyNortheast");
    private final String value;

    RegionEnum(final String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static RegionEnum fromValue(final String text) {
      for (final RegionEnum b : RegionEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("region")
  private RegionEnum region;
  @JsonProperty("isExternallyCreated")
  private Boolean isExternallyCreated;
  @JsonProperty("parameters")
  private Map<String, String> parameters;
  @JsonProperty("resourceDependencies")
  private List<UUID> resourceDependencies;

  /**
   * Resource status
   */
  public enum StatusEnum {
    NONE("None"), PROVISIONING("Provisioning"), RUNNING("Running"), STOPPED("Stopped"), FAILED(
        "Failed"), DELETING("Deleting");
    private final String value;

    StatusEnum(final String value) {
      this.value = value;
    }

    @JsonValue
    public String getValue() {
      return value;
    }

    @Override
    public String toString() {
      return String.valueOf(value);
    }

    @JsonCreator
    public static StatusEnum fromValue(final String text) {
      for (final StatusEnum b : StatusEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

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

  public SpaceResourceCreate type(final TypeEnum type) {
    this.type = type;
    return this;
  }

  /**
   * Resource type
   *
   * @return type
   **/
  @NotNull
  public TypeEnum getType() {
    return type;
  }

  public void setType(final TypeEnum type) {
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
