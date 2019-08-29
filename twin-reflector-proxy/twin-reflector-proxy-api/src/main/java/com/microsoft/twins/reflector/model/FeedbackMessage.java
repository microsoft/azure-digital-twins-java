/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

import java.util.UUID;
import javax.validation.constraints.NotNull;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackMessage {
  @JsonProperty(value = "correlationId", required = true)
  @NotNull
  private UUID correlationId;

  @JsonProperty(value = "status")
  private Status status;

  @JsonProperty(value = "error-code")
  private ErrorCode errorCode;

  @JsonProperty(value = "error-message")
  private String errorMessage;



}
