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
import com.microsoft.twins.model.SpaceResourceCreate;
import com.microsoft.twins.model.SpaceResourceRetrieve;
import com.microsoft.twins.model.SpaceResourceUpdate;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface ResourcesApi extends TwinsApiClient.Api {
  /**
   * Creates a resource
   *
   * @param body The resource information (required)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/resources")
  @Headers({"Accept: */*",})
  UUID resourcesCreate(SpaceResourceCreate body);

  /**
   * Creates a resource
   *
   * @param spaceId (optional)
   * @param type (optional)
   * @param size (optional)
   * @param region (optional)
   * @param isExternallyCreated (optional)
   * @param parameters (optional)
   * @param resourceDependencies (optional)
   * @param status (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/resources")
  @Headers({"Accept: */*",})
  UUID resourcesCreate(@Param("spaceId") UUID spaceId, @Param("type") String type, @Param("size") String size,
      @Param("region") String region, @Param("isExternallyCreated") Boolean isExternallyCreated,
      @Param("parameters") Map<String, String> parameters,
      @Param("resourceDependencies") List<UUID> resourceDependencies, @Param("status") String status);

  /**
   * Deletes the specified resource
   *
   * @param id Resource Id (required)
   */
  @RequestLine("DELETE /api/v1.0/resources/{id}")
  @Headers({"Accept: */*",})
  void resourcesDelete(@Param("id") String id);

  /**
   * Retrieves resources
   *
   * @param type Optional resource type filter (optional)
   * @param isExternallyCreated Optional externally created filter (optional)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;Space,DependentChildren\&quot;. Defaults to None (optional)
   * @param spaceId Optionally filter on objects based on their location in the topology relative to
   *        the specified spaceId (optional)
   * @param traverse None (the default) for the specified spaceId only, Down for space and
   *        descendants, Up for spaceId and ancestors, Any for both (optional)
   * @param minLevel Optional filter on minimum level, inclusive (optional)
   * @param maxLevel Optional filter on maximum level, inclusive (optional)
   * @param minRelative Specify if min level is absolute or relative (optional)
   * @param maxRelative Specify if max level is absolute or relative (optional)
   * @return java.util.List&lt;SpaceResourceRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/resources?type={type}&isExternallyCreated={isExternallyCreated}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  List<SpaceResourceRetrieve> resourcesRetrieve(@Param("type") String type,
      @Param("isExternallyCreated") Boolean isExternallyCreated, @Param("includes") String includes,
      @Param("spaceId") String spaceId, @Param("traverse") String traverse, @Param("minLevel") Integer minLevel,
      @Param("maxLevel") Integer maxLevel, @Param("minRelative") Boolean minRelative,
      @Param("maxRelative") Boolean maxRelative);

  /**
   * Retrieves resources
   *
   * Note, this is equivalent to the other <code>resourcesRetrieve</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link ResourcesRetrieveQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>type - Optional resource type filter (optional)</li>
   *        <li>isExternallyCreated - Optional externally created filter (optional)</li>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;Space,DependentChildren\&quot;. Defaults to None (optional)</li>
   *        <li>spaceId - Optionally filter on objects based on their location in the topology
   *        relative to the specified spaceId (optional)</li>
   *        <li>traverse - None (the default) for the specified spaceId only, Down for space and
   *        descendants, Up for spaceId and ancestors, Any for both (optional)</li>
   *        <li>minLevel - Optional filter on minimum level, inclusive (optional)</li>
   *        <li>maxLevel - Optional filter on maximum level, inclusive (optional)</li>
   *        <li>minRelative - Specify if min level is absolute or relative (optional)</li>
   *        <li>maxRelative - Specify if max level is absolute or relative (optional)</li>
   *        </ul>
   * @return java.util.List&lt;SpaceResourceRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/resources?type={type}&isExternallyCreated={isExternallyCreated}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  List<SpaceResourceRetrieve> resourcesRetrieve(@QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>resourcesRetrieve</code> method
   * in a fluent style.
   */
  public static class ResourcesRetrieveQueryParams extends HashMap<String, Object> {
    public ResourcesRetrieveQueryParams type(final String value) {
      put("type", EncodingUtils.encode(value));
      return this;
    }

    public ResourcesRetrieveQueryParams isExternallyCreated(final Boolean value) {
      put("isExternallyCreated", EncodingUtils.encode(value));
      return this;
    }

    public ResourcesRetrieveQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public ResourcesRetrieveQueryParams spaceId(final String value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public ResourcesRetrieveQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public ResourcesRetrieveQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public ResourcesRetrieveQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public ResourcesRetrieveQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public ResourcesRetrieveQueryParams maxRelative(final Boolean value) {
      put("maxRelative", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Retrieves the specified resource
   *
   * @param id Resource Id (required)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;Space,DependentChildren\&quot;. Defaults to None (optional)
   * @return SpaceResourceRetrieve
   */
  @RequestLine("GET /api/v1.0/resources/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  SpaceResourceRetrieve resourcesRetrieveById(@Param("id") String id, @Param("includes") String includes);

  /**
   * Retrieves the specified resource
   *
   * Note, this is equivalent to the other <code>resourcesRetrieveById</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the {@link ResourcesRetrieveByIdQueryParams}
   * class that allows for building up this map in a fluent style.
   *
   * @param id Resource Id (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;Space,DependentChildren\&quot;. Defaults to None (optional)</li>
   *        </ul>
   * @return SpaceResourceRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/resources/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  SpaceResourceRetrieve resourcesRetrieveById(@Param("id") String id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>resourcesRetrieveById</code>
   * method in a fluent style.
   */
  public static class ResourcesRetrieveByIdQueryParams extends HashMap<String, Object> {
    public ResourcesRetrieveByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Updates the specified resource
   *
   * @param body The resource information (required)
   * @param id Resource Id (required)
   */
  @RequestLine("PATCH /api/v1.0/resources/{id}")
  @Headers({"Accept: */*",})
  void resourcesUpdate(SpaceResourceUpdate body, @Param("id") String id);

  /**
   * Updates the specified resource
   *
   * @param id Resource Id (required)
   * @param spaceId (optional)
   * @param size (optional)
   * @param parameters (optional)
   * @param status (optional)
   */
  @RequestLine("PATCH /api/v1.0/resources/{id}")
  @Headers({"Accept: */*",})
  void resourcesUpdate(@Param("id") String id, @Param("spaceId") UUID spaceId, @Param("size") String size,
      @Param("parameters") Map<String, String> parameters, @Param("status") String status);
}
