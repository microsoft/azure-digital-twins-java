/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.EqualsAndHashCode;
import lombok.ToString;

/**
 * ConditionUpdate
 */
@ToString
@EqualsAndHashCode
public class ConditionUpdate extends AbstractRetrieve<ConditionUpdate> {


  /**
   * What object the condition applies to: the sensor, its parent device or its parent space
   */
  public enum TargetEnum {
    SENSOR("Sensor"), SENSORDEVICE("SensorDevice"), SENSORSPACE("SensorSpace");
    private final String value;

    TargetEnum(final String value) {
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
    public static TargetEnum fromValue(final String text) {
      for (final TargetEnum b : TargetEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("target")
  private TargetEnum target;
  @JsonProperty("path")
  private String path;
  @JsonProperty("value")
  private String value;

  /**
   * Type of comparison to perform
   */
  public enum ComparisonEnum {
    EQUALS("Equals"), NOTEQUALS("NotEquals"), CONTAINS("Contains");
    private final String value;

    ComparisonEnum(final String value) {
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
    public static ComparisonEnum fromValue(final String text) {
      for (final ComparisonEnum b : ComparisonEnum.values()) {
        if (String.valueOf(b.value).equals(text)) {
          return b;
        }
      }
      return null;
    }
  }

  @JsonProperty("comparison")
  private ComparisonEnum comparison;

  public ConditionUpdate target(final TargetEnum target) {
    this.target = target;
    return this;
  }

  /**
   * What object the condition applies to: the sensor, its parent device or its parent space
   *
   * @return target
   **/
  public TargetEnum getTarget() {
    return target;
  }

  public void setTarget(final TargetEnum target) {
    this.target = target;
  }

  public ConditionUpdate path(final String path) {
    this.path = path;
    return this;
  }

  /**
   * If specified and not empty, case sensitive JSON path which is evaluated against the target. If
   * specified and empty, the corresponding Condition is deleted
   *
   * @return path
   **/
  public String getPath() {
    return path;
  }

  public void setPath(final String path) {
    this.path = path;
  }

  public ConditionUpdate value(final String value) {
    this.value = value;
    return this;
  }

  /**
   * Value to compare against
   *
   * @return value
   **/
  public String getValue() {
    return value;
  }

  public void setValue(final String value) {
    this.value = value;
  }

  public ConditionUpdate comparison(final ComparisonEnum comparison) {
    this.comparison = comparison;
    return this;
  }

  /**
   * Type of comparison to perform
   *
   * @return comparison
   **/
  public ComparisonEnum getComparison() {
    return comparison;
  }

  public void setComparison(final ComparisonEnum comparison) {
    this.comparison = comparison;
  }
}
