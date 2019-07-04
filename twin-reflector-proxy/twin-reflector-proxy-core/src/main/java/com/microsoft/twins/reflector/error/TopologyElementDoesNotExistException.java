/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.error;

import java.util.UUID;
import com.microsoft.twins.reflector.model.ErrorCode;

public class TopologyElementDoesNotExistException extends AbstractIngressFailedException {
  private static final long serialVersionUID = 1L;

  private static final ErrorCode ERROR_CODE = ErrorCode.ELEMENT_DOES_NOT_EXIST;

  public TopologyElementDoesNotExistException(final String id, final UUID correlationId) {
    super("Topology element with id " + id + " does not exist", correlationId, ERROR_CODE);
  }

  public TopologyElementDoesNotExistException(final String id, final UUID correlationId,
      final Throwable cause) {
    super("Topology element with id " + id + " does not exist", correlationId, cause, ERROR_CODE);
  }
}
