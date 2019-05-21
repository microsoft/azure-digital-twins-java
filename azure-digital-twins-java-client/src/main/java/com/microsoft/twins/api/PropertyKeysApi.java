/**
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.microsoft.twins.EncodingUtils;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.model.ExtendedPropertyKeyCreate;
import com.microsoft.twins.model.ExtendedPropertyKeyRetrieve;
import com.microsoft.twins.model.ExtendedPropertyKeyUpdate;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface PropertyKeysApi extends TwinsApiClient.Api {
  /**
   * Creates a property key
   *
   * @param body The property key information (required)
   * @return Integer
   */
  @RequestLine("POST /api/v1.0/propertykeys")
  @Headers({"Accept: */*",})
  Integer propertyKeysCreate(ExtendedPropertyKeyCreate body);

  /**
   * Creates a property key
   *
   * @param name (optional)
   * @param primitiveDataType (optional)
   * @param category (optional)
   * @param description (optional)
   * @param spaceId (optional)
   * @param scope (optional)
   * @param validationData (optional)
   * @param min (optional)
   * @param max (optional)
   * @return Integer
   */
  @RequestLine("POST /api/v1.0/propertykeys")
  @Headers({"Accept: */*",})
  Integer propertyKeysCreate(@Param("name") String name,
      @Param("primitiveDataType") String primitiveDataType, @Param("category") String category,
      @Param("description") String description, @Param("spaceId") UUID spaceId,
      @Param("scope") String scope, @Param("validationData") String validationData,
      @Param("min") String min, @Param("max") String max);

  /**
   * Adds or updates a property key using SpaceId, Name and Scope as lookup keys
   *
   * @param body The property key information (required)
   * @return Integer
   */
  @RequestLine("PUT /api/v1.0/propertykeys")
  @Headers({"Accept: */*",})
  Integer propertyKeysCreateOrUpdate(ExtendedPropertyKeyCreate body);

  /**
   * Adds or updates a property key using SpaceId, Name and Scope as lookup keys
   *
   * @param name (optional)
   * @param primitiveDataType (optional)
   * @param category (optional)
   * @param description (optional)
   * @param spaceId (optional)
   * @param scope (optional)
   * @param validationData (optional)
   * @param min (optional)
   * @param max (optional)
   * @return Integer
   */
  @RequestLine("PUT /api/v1.0/propertykeys")
  @Headers({"Accept: */*",})
  Integer propertyKeysCreateOrUpdate(@Param("name") String name,
      @Param("primitiveDataType") String primitiveDataType, @Param("category") String category,
      @Param("description") String description, @Param("spaceId") UUID spaceId,
      @Param("scope") String scope, @Param("validationData") String validationData,
      @Param("min") String min, @Param("max") String max);

  /**
   * Deletes the given property key
   *
   * @param id Key identifier (required)
   */
  @RequestLine("DELETE /api/v1.0/propertykeys/{id}")
  @Headers({"Accept: */*",})
  void propertyKeysDelete(@Param("id") Integer id);

  /**
   * Deletes property keys for child objects of the given space
   *
   * @param spaceId Space Id (required)
   * @param scope Scope for the property keys (required)
   * @param keys &#x27;;&#x27; delimited list of names of property keys to delete (required)
   */
  @RequestLine("DELETE /api/v1.0/propertykeys?spaceId={spaceId}&scope={scope}&keys={keys}")
  @Headers({"Accept: */*",})
  void propertyKeysDeleteBySpace(@Param("spaceId") String spaceId, @Param("scope") String scope,
      @Param("keys") String keys);

  /**
   * Deletes property keys for child objects of the given space
   *
   * Note, this is equivalent to the other <code>propertyKeysDeleteBySpace</code> method, but with
   * the query parameters collected into a single Map parameter. This is convenient for services
   * with optional query parameters, especially when used with the
   * {@link PropertyKeysDeleteBySpaceQueryParams} class that allows for building up this map in a
   * fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>spaceId - Space Id (required)</li>
   *        <li>scope - Scope for the property keys (required)</li>
   *        <li>keys - &#x27;;&#x27; delimited list of names of property keys to delete
   *        (required)</li>
   *        </ul>
   *
   */
  @RequestLine("DELETE /api/v1.0/propertykeys?spaceId={spaceId}&scope={scope}&keys={keys}")
  @Headers({"Content-Type: */*",})
  void propertyKeysDeleteBySpace(@QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>propertyKeysDeleteBySpace</code> method in a fluent style.
   */
  public static class PropertyKeysDeleteBySpaceQueryParams extends HashMap<String, Object> {
    public PropertyKeysDeleteBySpaceQueryParams spaceId(final String value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public PropertyKeysDeleteBySpaceQueryParams scope(final String value) {
      put("scope", EncodingUtils.encode(value));
      return this;
    }

    public PropertyKeysDeleteBySpaceQueryParams keys(final String value) {
      put("keys", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Retrieves property keys
   *
   * @param scope Optional scope filter (optional)
   * @param category Optional category filter (optional)
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
   * @return java.util.List&lt;ExtendedPropertyKeyRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/propertykeys?scope={scope}&category={category}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  java.util.List<ExtendedPropertyKeyRetrieve> propertyKeysRetrieve(@Param("scope") String scope,
      @Param("category") String category, @Param("includes") String includes,
      @Param("spaceId") String spaceId, @Param("traverse") String traverse,
      @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel,
      @Param("minRelative") Boolean minRelative, @Param("maxRelative") Boolean maxRelative);

  /**
   * Retrieves property keys
   *
   * Note, this is equivalent to the other <code>propertyKeysRetrieve</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link PropertyKeysRetrieveQueryParams} class that allows for building up this map in a fluent
   * style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>scope - Optional scope filter (optional)</li>
   *        <li>category - Optional category filter (optional)</li>
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
   * @return java.util.List&lt;ExtendedPropertyKeyRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/propertykeys?scope={scope}&category={category}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  java.util.List<ExtendedPropertyKeyRetrieve> propertyKeysRetrieve(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>propertyKeysRetrieve</code>
   * method in a fluent style.
   */
  public static class PropertyKeysRetrieveQueryParams extends HashMap<String, Object> {
    public PropertyKeysRetrieveQueryParams scope(final String value) {
      put("scope", EncodingUtils.encode(value));
      return this;
    }

    public PropertyKeysRetrieveQueryParams category(final String value) {
      put("category", EncodingUtils.encode(value));
      return this;
    }

    public PropertyKeysRetrieveQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public PropertyKeysRetrieveQueryParams spaceId(final String value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public PropertyKeysRetrieveQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public PropertyKeysRetrieveQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public PropertyKeysRetrieveQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public PropertyKeysRetrieveQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public PropertyKeysRetrieveQueryParams maxRelative(final Boolean value) {
      put("maxRelative", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Retrieves a property key
   *
   * @param id Key identifier (required)
   * @param includes Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None (optional)
   * @return ExtendedPropertyKeyRetrieve
   */
  @RequestLine("GET /api/v1.0/propertykeys/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  ExtendedPropertyKeyRetrieve propertyKeysRetrieveById(@Param("id") Integer id,
      @Param("includes") String includes);

  /**
   * Retrieves a property key
   *
   * Note, this is equivalent to the other <code>propertyKeysRetrieveById</code> method, but with
   * the query parameters collected into a single Map parameter. This is convenient for services
   * with optional query parameters, especially when used with the
   * {@link PropertyKeysRetrieveByIdQueryParams} class that allows for building up this map in a
   * fluent style.
   *
   * @param id Key identifier (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None (optional)</li>
   *        </ul>
   * @return ExtendedPropertyKeyRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/propertykeys/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  ExtendedPropertyKeyRetrieve propertyKeysRetrieveById(@Param("id") Integer id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>propertyKeysRetrieveById</code> method in a fluent style.
   */
  public static class PropertyKeysRetrieveByIdQueryParams extends HashMap<String, Object> {
    public PropertyKeysRetrieveByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Updates a property key
   *
   * @param body The property key information (required)
   * @param id The property key Id (required)
   */
  @RequestLine("PATCH /api/v1.0/propertykeys/{id}")
  @Headers({"Accept: */*",})
  void propertyKeysUpdate(ExtendedPropertyKeyUpdate body, @Param("id") Integer id);

  /**
   * Updates a property key
   *
   * @param id The property key Id (required)
   * @param name (optional)
   * @param category (optional)
   * @param description (optional)
   * @param spaceId (optional)
   * @param validationData (optional)
   * @param min (optional)
   * @param max (optional)
   */
  @RequestLine("PATCH /api/v1.0/propertykeys/{id}")
  @Headers({"Accept: */*",})
  void propertyKeysUpdate(@Param("id") Integer id, @Param("name") String name,
      @Param("category") String category, @Param("description") String description,
      @Param("spaceId") UUID spaceId, @Param("validationData") String validationData,
      @Param("min") String min, @Param("max") String max);
}
