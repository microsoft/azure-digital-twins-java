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
import com.microsoft.twins.model.EndpointCreate;
import com.microsoft.twins.model.EndpointRetrieve;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface EndpointsApi extends TwinsApiClient.Api {
  /**
   * Creates an endpoint
   *
   * @param body The endpoint information (required)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/endpoints")
  @Headers({"Accept: */*",})
  UUID endpointsCreate(EndpointCreate body);

  /**
   * Creates an endpoint
   *
   * @param type                      (optional)
   * @param eventTypes                (optional)
   * @param connectionString          (optional)
   * @param secondaryConnectionString (optional)
   * @param path                      (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/endpoints")
  @Headers({"Accept: */*",})
  UUID endpointsCreate(@Param("type") String type, @Param("eventTypes") List<String> eventTypes,
      @Param("connectionString") String connectionString,
      @Param("secondaryConnectionString") String secondaryConnectionString,
      @Param("path") String path);

  /**
   * Deletes the specified endpoint
   *
   * @param id Endpoint Id (required)
   */
  @RequestLine("DELETE /api/v1.0/endpoints/{id}")
  @Headers({"Accept: */*",})
  void endpointsDelete(@Param("id") UUID id);

  /**
   * Modifies an existing endpoint. Does not create if it doesn&#x27;t exist.
   *
   * @param body Update payload (required)
   * @param id   Endpoint id (required)
   * @return Object
   */
  @RequestLine("PUT /api/v1.0/endpoints/{id}")
  @Headers({"Accept: */*",})
  Object endpointsModify(EndpointCreate body, @Param("id") UUID id);

  /**
   * Modifies an existing endpoint. Does not create if it doesn&#x27;t exist.
   *
   * @param id                        Endpoint id (required)
   * @param type                      (optional)
   * @param eventTypes                (optional)
   * @param connectionString          (optional)
   * @param secondaryConnectionString (optional)
   * @param path                      (optional)
   * @return Object
   */
  @RequestLine("PUT /api/v1.0/endpoints/{id}")
  @Headers({"Accept: */*",})
  Object endpointsModify(@Param("id") UUID id, @Param("type") String type,
      @Param("eventTypes") List<String> eventTypes,
      @Param("connectionString") String connectionString,
      @Param("secondaryConnectionString") String secondaryConnectionString,
      @Param("path") String path);

  /**
   * Gets a list of endpoints
   *
   * @param timeUpdated Minimum last updated UTC time (optional)
   * @param types       Endpoint types (optional)
   * @param eventTypes  Types of event (optional)
   * @return java.util.List&lt;EndpointRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/endpoints?timeUpdated={timeUpdated}&types={types}&eventTypes={eventTypes}")
  @Headers({"Accept: */*",})
  List<EndpointRetrieve> endpointsRetrieve(@Param("timeUpdated") String timeUpdated,
      @Param("types") String types, @Param("eventTypes") String eventTypes);

  /**
   * Gets a list of endpoints
   *
   * Note, this is equivalent to the other <code>endpointsRetrieve</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link EndpointsRetrieveQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *                    <p>
   *                    The following elements may be specified in the query map:
   *                    </p>
   *                    <ul>
   *                    <li>timeUpdated - Minimum last updated UTC time (optional)</li>
   *                    <li>types - Endpoint types (optional)</li>
   *                    <li>eventTypes - Types of event (optional)</li>
   *                    </ul>
   * @return java.util.List&lt;EndpointRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/endpoints?timeUpdated={timeUpdated}&types={types}&eventTypes={eventTypes}")
  @Headers({"Content-Type: */*",})
  List<EndpointRetrieve> endpointsRetrieve(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>endpointsRetrieve</code>
   * method in a fluent style.
   */
  public static class EndpointsRetrieveQueryParams extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public EndpointsRetrieveQueryParams timeUpdated(final String value) {
      put("timeUpdated", EncodingUtils.encode(value));
      return this;
    }

    public EndpointsRetrieveQueryParams types(final String value) {
      put("types", EncodingUtils.encode(value));
      return this;
    }

    public EndpointsRetrieveQueryParams eventTypes(final String value) {
      put("eventTypes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a specific endpoint
   *
   * @param id Endpoint Id (required)
   * @return EndpointRetrieve
   */
  @RequestLine("GET /api/v1.0/endpoints/{id}")
  @Headers({"Accept: */*",})
  EndpointRetrieve endpointsRetrieveById(@Param("id") UUID id);
}
