/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.error;

import java.util.UUID;
import com.microsoft.twins.reflector.model.ErrorCode;

public class AttributeNotSupportedException extends AbstractIngressFailedException {
  private static final long serialVersionUID = 1L;

  private static final ErrorCode ERROR_CODE = ErrorCode.ATTRIBUTE_NOT_SUPPORTED;

  public AttributeNotSupportedException(final String id, final UUID correlationId) {
    super("Attribute " + id + " not supported", correlationId, ERROR_CODE);
  }

  public AttributeNotSupportedException(final String id, final UUID correlationId,
      final Throwable cause) {
    super("Attribute " + id + " not supported", correlationId, cause, ERROR_CODE);
  }
}
