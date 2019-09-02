/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins;

import java.util.UUID;

public final class CorrelationIdContext {
  private static final ThreadLocal<UUID> CORRELATION_ID = new ThreadLocal<>();

  private CorrelationIdContext() {
    // Utility class
  }

  public static void setCorrelationId(final UUID correlationId) {
    CORRELATION_ID.set(correlationId);
  }

  public static UUID getCorrelationId() {
    return CORRELATION_ID.get();
  }

  public static void clear() {
    CORRELATION_ID.remove();
  }
}
