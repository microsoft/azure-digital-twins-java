/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.error;

import java.util.UUID;
import com.microsoft.twins.reflector.model.ErrorCode;

public class InconsistentTopologyException extends AbstractIngressFailedException {
  private static final long serialVersionUID = 1L;

  private static final ErrorCode ERROR_CODE = ErrorCode.INCONSISTENT_TOPOLOGY;

  public InconsistentTopologyException(final String message, final UUID correlationId) {
    super(message, correlationId, ERROR_CODE);
  }

  public InconsistentTopologyException(final String message, final UUID correlationId, final Throwable cause) {
    super(message, correlationId, cause, ERROR_CODE);
  }
}
