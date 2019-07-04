/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.microsoft.twins.EncodingUtils;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.model.UserDefinedFunctionRetrieve;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface UserDefinedFunctionsApi extends TwinsApiClient.Api {
  /**
   * Creates a udf This is a multi-part request. For more information, see doc examples. Key value
   * pairs specified in the Content-Disposition header in the udf-chunk of the multipart request
   * will be preserved as meta-data on the stored udf.
   *
   * @param metadata (optional)
   * @param contents (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/userdefinedfunctions")
  @Headers({"Accept: */*",})
  UUID userDefinedFunctionsCreate(@Param("metadata") String metadata,
      @Param("contents") File contents);

  /**
   * Deletes a udf
   *
   * @param id Udf Id (required)
   */
  @RequestLine("DELETE /api/v1.0/userdefinedfunctions/{id}")
  @Headers({"Accept: */*",})
  void userDefinedFunctionsDelete(@Param("id") String id);

  /**
   * Gets the contents of a udf
   *
   * @param id Udf id (required)
   * @return File
   */
  @RequestLine("GET /api/v1.0/userdefinedfunctions/{id}/contents")
  @Headers({"Accept: */*",})
  File userDefinedFunctionsGetBlobContents(@Param("id") String id);

  /**
   * Gets a list of udfs
   *
   * @param names Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of udf names to filter by
   *        (optional)
   * @param ids Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;ContentInfo,Description\&quot;. Defaults to None (optional)
   * @param spaceId Optionally filter on objects based on their location in the topology relative to
   *        the specified spaceId (optional)
   * @param traverse None (the default) for the specified spaceId only, Down for space and
   *        descendants, Up for spaceId and ancestors, Any for both (optional)
   * @param minLevel Optional filter on minimum level, inclusive (optional)
   * @param maxLevel Optional filter on maximum level, inclusive (optional)
   * @param minRelative Specify if min level is absolute or relative (optional)
   * @param maxRelative Specify if max level is absolute or relative (optional)
   * @return java.util.List&lt;UserDefinedFunctionRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/userdefinedfunctions?names={names}&ids={ids}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  List<UserDefinedFunctionRetrieve> userDefinedFunctionsRetrieve(@Param("names") String names,
      @Param("ids") String ids, @Param("includes") String includes,
      @Param("spaceId") String spaceId, @Param("traverse") String traverse,
      @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel,
      @Param("minRelative") Boolean minRelative, @Param("maxRelative") Boolean maxRelative);

  /**
   * Gets a list of udfs
   *
   * Note, this is equivalent to the other <code>userDefinedFunctionsRetrieve</code> method, but
   * with the query parameters collected into a single Map parameter. This is convenient for
   * services with optional query parameters, especially when used with the
   * {@link UserDefinedFunctionsRetrieveQueryParams} class that allows for building up this map in a
   * fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>names - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of udf names to
   *        filter by (optional)</li>
   *        <li>ids - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)</li>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;ContentInfo,Description\&quot;. Defaults to None (optional)</li>
   *        <li>spaceId - Optionally filter on objects based on their location in the topology
   *        relative to the specified spaceId (optional)</li>
   *        <li>traverse - None (the default) for the specified spaceId only, Down for space and
   *        descendants, Up for spaceId and ancestors, Any for both (optional)</li>
   *        <li>minLevel - Optional filter on minimum level, inclusive (optional)</li>
   *        <li>maxLevel - Optional filter on maximum level, inclusive (optional)</li>
   *        <li>minRelative - Specify if min level is absolute or relative (optional)</li>
   *        <li>maxRelative - Specify if max level is absolute or relative (optional)</li>
   *        </ul>
   * @return java.util.List&lt;UserDefinedFunctionRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/userdefinedfunctions?names={names}&ids={ids}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  List<UserDefinedFunctionRetrieve> userDefinedFunctionsRetrieve(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>userDefinedFunctionsRetrieve</code> method in a fluent style.
   */
  public static class UserDefinedFunctionsRetrieveQueryParams extends HashMap<String, Object> {
    public UserDefinedFunctionsRetrieveQueryParams names(final String value) {
      put("names", EncodingUtils.encode(value));
      return this;
    }

    public UserDefinedFunctionsRetrieveQueryParams ids(final String value) {
      put("ids", EncodingUtils.encode(value));
      return this;
    }

    public UserDefinedFunctionsRetrieveQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public UserDefinedFunctionsRetrieveQueryParams spaceId(final String value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public UserDefinedFunctionsRetrieveQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public UserDefinedFunctionsRetrieveQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public UserDefinedFunctionsRetrieveQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public UserDefinedFunctionsRetrieveQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public UserDefinedFunctionsRetrieveQueryParams maxRelative(final Boolean value) {
      put("maxRelative", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a udf
   *
   * @param id Udf Id (required)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;ContentInfo,Description\&quot;. Defaults to None (optional)
   * @return UserDefinedFunctionRetrieve
   */
  @RequestLine("GET /api/v1.0/userdefinedfunctions/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  UserDefinedFunctionRetrieve userDefinedFunctionsRetrieveById(@Param("id") String id,
      @Param("includes") String includes);

  /**
   * Gets a udf
   *
   * Note, this is equivalent to the other <code>userDefinedFunctionsRetrieveById</code> method, but
   * with the query parameters collected into a single Map parameter. This is convenient for
   * services with optional query parameters, especially when used with the
   * {@link UserDefinedFunctionsRetrieveByIdQueryParams} class that allows for building up this map
   * in a fluent style.
   *
   * @param id Udf Id (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;ContentInfo,Description\&quot;. Defaults to None (optional)</li>
   *        </ul>
   * @return UserDefinedFunctionRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/userdefinedfunctions/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  UserDefinedFunctionRetrieve userDefinedFunctionsRetrieveById(@Param("id") String id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>userDefinedFunctionsRetrieveById</code> method in a fluent style.
   */
  public static class UserDefinedFunctionsRetrieveByIdQueryParams extends HashMap<String, Object> {
    public UserDefinedFunctionsRetrieveByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Updates a udf This is a multi-part request. For more information, see doc examples.
   *
   * @param id Udf Id (required)
   * @param metadata (optional)
   * @param contents (optional)
   */
  @RequestLine("PATCH /api/v1.0/userdefinedfunctions/{id}")
  @Headers({"Accept: */*",})
  void userDefinedFunctionsUpdate(@Param("id") String id, @Param("metadata") String metadata,
      @Param("contents") File contents);
}
