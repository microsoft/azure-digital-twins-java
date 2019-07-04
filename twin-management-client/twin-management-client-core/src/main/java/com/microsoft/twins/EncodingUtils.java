/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * Utilities to support Swagger encoding formats in Feign.
 */
public final class EncodingUtils {
  /**
   * Private constructor. Do not construct this class.
   */
  private EncodingUtils() {
  }

  /**
   * URL encode a single query parameter.
   *
   * @param parameter The query parameter to encode. This object will not be changed.
   * @return The URL encoded string representation of the parameter. If the parameter is null,
   *         returns null.
   */
  public static String encode(final Object parameter) {
    if (parameter == null) {
      return null;
    }
    try {
      return URLEncoder.encode(parameter.toString(), "UTF-8");
    } catch (final UnsupportedEncodingException e) {
      // Should never happen, UTF-8 is always supported
      throw new RuntimeException(e);
    }
  }
}
