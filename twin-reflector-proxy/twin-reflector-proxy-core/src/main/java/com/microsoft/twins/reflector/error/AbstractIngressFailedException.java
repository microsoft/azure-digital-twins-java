/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.error;

import java.util.UUID;
import com.microsoft.twins.reflector.model.ErrorCode;

public abstract class AbstractIngressFailedException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  private final ErrorCode errorCode;
  private final UUID correlationID;

  AbstractIngressFailedException(final String message, final UUID correlationID,
      final ErrorCode errorCode) {
    super(message + "[errorCode: " + errorCode + " | correlationID: " + correlationID + "]");
    this.errorCode = errorCode;
    this.correlationID = correlationID;
  }

  AbstractIngressFailedException(final String message, final UUID correlationID,
      final Throwable cause, final ErrorCode errorCode) {
    super(message + "[errorCode: " + errorCode + " | correlationID: " + correlationID + "]", cause);
    this.errorCode = errorCode;
    this.correlationID = correlationID;
  }

  public ErrorCode getErrorCode() {
    return errorCode;
  }

  public UUID getCorrelationID() {
    return correlationID;
  }



}
