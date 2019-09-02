/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.error;

import java.util.UUID;
import com.microsoft.twins.reflector.model.ErrorCode;

public class EntityTypeNotSupportedException extends AbstractIngressFailedException {
  private static final long serialVersionUID = 1L;

  private static final ErrorCode ERROR_CODE = ErrorCode.ENTITY_NOT_SUPPORTED;

  public EntityTypeNotSupportedException(final String id, final UUID correlationId) {
    super("Entity type " + id + " not supported", correlationId, ERROR_CODE);
  }

  public EntityTypeNotSupportedException(final String id, final UUID correlationId,
      final Throwable cause) {
    super("Entity type " + id + " not supported", correlationId, cause, ERROR_CODE);
  }
}
