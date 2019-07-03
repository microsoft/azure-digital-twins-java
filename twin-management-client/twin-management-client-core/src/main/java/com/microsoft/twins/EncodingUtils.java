/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

/**
 * Utilities to support Swagger encoding formats in Feign.
 */
public final class EncodingUtils {
  private static final int NOT_SINGLE = 2;

  /**
   * Private constructor. Do not construct this class.
   */
  private EncodingUtils() {}

  /**
   * <p>
   * Encodes a collection of query parameters according to the Swagger collection format.
   * </p>
   *
   * <p>
   * Of the various collection formats defined by Swagger ("csv", "tsv", etc), Feign only natively
   * supports "multi". This utility generates the other format types so it will be properly processed
   * by Feign.
   * </p>
   *
   * <p>
   * Note, as part of reformatting, it URL encodes the parameters as well.
   * </p>
   *
   * @param parameters The collection object to be formatted. This object will not be changed.
   * @param collectionFormat The Swagger collection format (eg, "csv", "tsv", "pipes"). See the
   *        <a href= "http://swagger.io/specification/#parameter-object-44"> Swagger Spec</a> for more
   *        details.
   * @return An object that will be correctly formatted by Feign.
   */
  public static Object encodeCollection(final Collection<?> parameters, final String collectionFormat) {
    if (parameters == null) {
      return parameters;
    }
    final List<String> stringValues = new ArrayList<>(parameters.size());
    for (final Object parameter : parameters) {
      // ignore null values (same behavior as Feign)
      if (parameter != null) {
        stringValues.add(encode(parameter));
      }
    }
    // Feign natively handles single-element lists and the "multi" format.
    if (stringValues.size() < NOT_SINGLE || "multi".equals(collectionFormat)) {
      return stringValues;
    }
    // Otherwise return a formatted String
    final String[] stringArray = stringValues.toArray(new String[0]);
    switch (collectionFormat) {
      case "ssv":
        return StringUtils.join(stringArray, " ");
      case "tsv":
        return StringUtils.join(stringArray, "\t");
      case "pipes":
        return StringUtils.join(stringArray, "|");
      case "csv":
      default:
        return StringUtils.join(stringArray, ",");
    }
  }

  /**
   * URL encode a single query parameter.
   *
   * @param parameter The query parameter to encode. This object will not be changed.
   * @return The URL encoded string representation of the parameter. If the parameter is null, returns
   *         null.
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
