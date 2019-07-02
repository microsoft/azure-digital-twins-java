/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.error;

public class FailedToAcquireBearerTokenException extends RuntimeException {

  private static final long serialVersionUID = 1L;

  public FailedToAcquireBearerTokenException(final String message, final Throwable cause) {
    super(message, cause);
  }


}
