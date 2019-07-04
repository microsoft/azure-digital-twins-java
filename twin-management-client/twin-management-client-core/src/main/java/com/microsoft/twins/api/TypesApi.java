/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.microsoft.twins.EncodingUtils;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.model.ExtendedTypeCreate;
import com.microsoft.twins.model.ExtendedTypeRetrieve;
import com.microsoft.twins.model.ExtendedTypeUpdate;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface TypesApi extends TwinsApiClient.Api {
  /**
   * Creates an extended type
   *
   * @param body The type information (required)
   * @return Integer
   */
  @RequestLine("POST /api/v1.0/types")
  @Headers({"Accept: */*",})
  Integer typesCreate(ExtendedTypeCreate body);

  /**
   * Creates an extended type
   *
   * @param category (optional)
   * @param spaceId (optional)
   * @param name (optional)
   * @param friendlyName (optional)
   * @param description (optional)
   * @param disabled (optional)
   * @param logicalOrder (optional)
   * @return Integer
   */
  @RequestLine("POST /api/v1.0/types")
  @Headers({"Accept: */*",})
  Integer typesCreate(@Param("category") String category, @Param("spaceId") UUID spaceId,
      @Param("name") String name, @Param("friendlyName") String friendlyName,
      @Param("description") String description, @Param("disabled") Boolean disabled,
      @Param("logicalOrder") Integer logicalOrder);

  /**
   * Deletes an extended type
   *
   * @param id Extended type id (required)
   */
  @RequestLine("DELETE /api/v1.0/types/{id}")
  @Headers({"Accept: */*",})
  void typesDelete(@Param("id") Integer id);

  /**
   * Gets a list of extended types
   *
   * @param ids Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)
   * @param categories Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of categories to
   *        filter by (optional)
   * @param names Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of names to filter by
   *        (optional)
   * @param system Optionally filter on objects that have a parent space. If true, only return
   *        unparented (system) types. If false, only return parented (custom) types. (optional)
   * @param disabled Optional filter on the disabled flag (optional)
   * @param includes Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None (optional)
   * @param spaceId Optionally filter on objects based on their location in the topology relative to
   *        the specified spaceId (optional)
   * @param traverse None (the default) for the specified spaceId only, Down for space and
   *        descendants, Up for spaceId and ancestors, Any for both (optional)
   * @param minLevel Optional filter on minimum level, inclusive (optional)
   * @param maxLevel Optional filter on maximum level, inclusive (optional)
   * @param minRelative Specify if min level is absolute or relative (optional)
   * @param maxRelative Specify if max level is absolute or relative (optional)
   * @return java.util.List&lt;ExtendedTypeRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/types?ids={ids}&categories={categories}&names={names}&system={system}&disabled={disabled}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  List<ExtendedTypeRetrieve> typesRetrieve(@Param("ids") String ids,
      @Param("categories") String categories, @Param("names") String names,
      @Param("system") Boolean system, @Param("disabled") Boolean disabled,
      @Param("includes") String includes, @Param("spaceId") String spaceId,
      @Param("traverse") String traverse, @Param("minLevel") Integer minLevel,
      @Param("maxLevel") Integer maxLevel, @Param("minRelative") Boolean minRelative,
      @Param("maxRelative") Boolean maxRelative);

  /**
   * Gets a list of extended types
   *
   * Note, this is equivalent to the other <code>typesRetrieve</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link TypesRetrieveQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>ids - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)</li>
   *        <li>categories - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of categories to
   *        filter by (optional)</li>
   *        <li>names - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of names to filter by
   *        (optional)</li>
   *        <li>system - Optionally filter on objects that have a parent space. If true, only return
   *        unparented (system) types. If false, only return parented (custom) types.
   *        (optional)</li>
   *        <li>disabled - Optional filter on the disabled flag (optional)</li>
   *        <li>includes - Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None (optional)</li>
   *        <li>spaceId - Optionally filter on objects based on their location in the topology
   *        relative to the specified spaceId (optional)</li>
   *        <li>traverse - None (the default) for the specified spaceId only, Down for space and
   *        descendants, Up for spaceId and ancestors, Any for both (optional)</li>
   *        <li>minLevel - Optional filter on minimum level, inclusive (optional)</li>
   *        <li>maxLevel - Optional filter on maximum level, inclusive (optional)</li>
   *        <li>minRelative - Specify if min level is absolute or relative (optional)</li>
   *        <li>maxRelative - Specify if max level is absolute or relative (optional)</li>
   *        </ul>
   * @return java.util.List&lt;ExtendedTypeRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/types?ids={ids}&categories={categories}&names={names}&system={system}&disabled={disabled}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  List<ExtendedTypeRetrieve> typesRetrieve(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>typesRetrieve</code> method
   * in a fluent style.
   */
  public static class TypesRetrieveQueryParams extends HashMap<String, Object> {
    public TypesRetrieveQueryParams ids(final String value) {
      put("ids", EncodingUtils.encode(value));
      return this;
    }

    public TypesRetrieveQueryParams categories(final String value) {
      put("categories", EncodingUtils.encode(value));
      return this;
    }

    public TypesRetrieveQueryParams names(final String value) {
      put("names", EncodingUtils.encode(value));
      return this;
    }

    public TypesRetrieveQueryParams system(final Boolean value) {
      put("system", EncodingUtils.encode(value));
      return this;
    }

    public TypesRetrieveQueryParams disabled(final Boolean value) {
      put("disabled", EncodingUtils.encode(value));
      return this;
    }

    public TypesRetrieveQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public TypesRetrieveQueryParams spaceId(final String value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public TypesRetrieveQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public TypesRetrieveQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public TypesRetrieveQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public TypesRetrieveQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public TypesRetrieveQueryParams maxRelative(final Boolean value) {
      put("maxRelative", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a specific extended type
   *
   * @param id Extended type id (required)
   * @param includes Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None (optional)
   * @return ExtendedTypeRetrieve
   */
  @RequestLine("GET /api/v1.0/types/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  ExtendedTypeRetrieve typesRetrieveById(@Param("id") Integer id,
      @Param("includes") String includes);

  /**
   * Gets a specific extended type
   *
   * Note, this is equivalent to the other <code>typesRetrieveById</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link TypesRetrieveByIdQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param id Extended type id (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None (optional)</li>
   *        </ul>
   * @return ExtendedTypeRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/types/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  ExtendedTypeRetrieve typesRetrieveById(@Param("id") Integer id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>typesRetrieveById</code>
   * method in a fluent style.
   */
  public static class TypesRetrieveByIdQueryParams extends HashMap<String, Object> {
    public TypesRetrieveByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Updates an extended type
   *
   * @param body Update payload (required)
   * @param id Extended type id (required)
   */
  @RequestLine("PATCH /api/v1.0/types/{id}")
  @Headers({"Accept: */*",})
  void typesUpdate(ExtendedTypeUpdate body, @Param("id") Integer id);

  /**
   * Updates an extended type
   *
   * @param id Extended type id (required)
   * @param spaceId (optional)
   * @param name (optional)
   * @param friendlyName (optional)
   * @param description (optional)
   * @param disabled (optional)
   * @param logicalOrder (optional)
   */
  @RequestLine("PATCH /api/v1.0/types/{id}")
  @Headers({"Accept: */*",})
  void typesUpdate(@Param("id") Integer id, @Param("spaceId") UUID spaceId,
      @Param("name") String name, @Param("friendlyName") String friendlyName,
      @Param("description") String description, @Param("disabled") Boolean disabled,
      @Param("logicalOrder") Integer logicalOrder);
}
