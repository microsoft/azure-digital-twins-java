/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.model;

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public abstract class AbstractRetrieve<T> {
  @JsonProperty("id")
  private UUID id;


  public T id(final UUID id) {
    this.id = id;
    return (T) this;
  }

  /**
   * Get id
   *
   * @return id
   **/
  @NotNull
  @Valid
  public UUID getId() {
    return id;
  }

  public void setId(final UUID id) {
    this.id = id;
  }
}
