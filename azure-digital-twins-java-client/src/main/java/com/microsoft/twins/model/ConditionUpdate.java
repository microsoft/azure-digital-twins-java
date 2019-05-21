/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import java.util.Objects;
import java.util.UUID;
import javax.validation.Valid;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * ConditionUpdate
 */
public class ConditionUpdate {
  @JsonProperty("id")
  private UUID id;

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

  public ConditionUpdate id(final UUID id) {
    this.id = id;
    return this;
  }

  /**
   * If specified, Id of the condition to update or delete. If not specified, condition is created
   *
   * @return id
   **/
  @Valid
  public UUID getId() {
    return id;
  }

  public void setId(final UUID id) {
    this.id = id;
  }

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

  @Override
  public boolean equals(final java.lang.Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    final ConditionUpdate conditionUpdate = (ConditionUpdate) o;
    return Objects.equals(this.id, conditionUpdate.id)
        && Objects.equals(this.target, conditionUpdate.target)
        && Objects.equals(this.path, conditionUpdate.path)
        && Objects.equals(this.value, conditionUpdate.value)
        && Objects.equals(this.comparison, conditionUpdate.comparison);
  }

  @Override
  public int hashCode() {
    return java.util.Objects.hash(id, target, path, value, comparison);
  }

  @Override
  public String toString() {
    final StringBuilder sb = new StringBuilder();
    sb.append("class ConditionUpdate {\n");
    sb.append("    id: ").append(toIndentedString(id)).append("\n");
    sb.append("    target: ").append(toIndentedString(target)).append("\n");
    sb.append("    path: ").append(toIndentedString(path)).append("\n");
    sb.append("    value: ").append(toIndentedString(value)).append("\n");
    sb.append("    comparison: ").append(toIndentedString(comparison)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces (except the first line).
   */
  private String toIndentedString(final java.lang.Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }
}
