/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

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
      if (String.valueOf(b.value).equalsIgnoreCase(text)) {
        return b;
      }
    }
    return null;
  }
}