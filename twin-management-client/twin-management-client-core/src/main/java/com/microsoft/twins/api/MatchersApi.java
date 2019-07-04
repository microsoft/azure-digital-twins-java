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
import com.microsoft.twins.model.ConditionCreate;
import com.microsoft.twins.model.ConditionUpdate;
import com.microsoft.twins.model.MatcherCreate;
import com.microsoft.twins.model.MatcherEvaluationResults;
import com.microsoft.twins.model.MatcherRetrieve;
import com.microsoft.twins.model.MatcherUpdate;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface MatchersApi extends TwinsApiClient.Api {
  /**
   * Creates a matcher
   *
   * @param body The matcher information (required)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/matchers")
  @Headers({"Accept: */*",})
  UUID matchersCreate(MatcherCreate body);

  /**
   * Creates a matcher
   *
   * @param name         (optional)
   * @param description  (optional)
   * @param friendlyName (optional)
   * @param conditions   (optional)
   * @param spaceId      (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/matchers")
  @Headers({"Accept: */*",})
  UUID matchersCreate(@Param("name") String name, @Param("description") String description,
      @Param("friendlyName") String friendlyName,
      @Param("conditions") List<ConditionCreate> conditions, @Param("spaceId") UUID spaceId);

  /**
   * Deletes the given matcher
   *
   * @param id Matcher identifier (required)
   */
  @RequestLine("DELETE /api/v1.0/matchers/{id}")
  @Headers({"Accept: */*",})
  void matchersDelete(@Param("id") String id);

  /**
   * Evaluates the matcher for a sensor
   *
   * @param id            Matcher identifier (required)
   * @param sensorId      Sensor identifier (required)
   * @param enableLogging If true, return verbose logs for the evaluation (optional)
   * @return MatcherEvaluationResults
   */
  @RequestLine("GET /api/v1.0/matchers/{id}/evaluate/{sensorId}?enableLogging={enableLogging}")
  @Headers({"Accept: */*",})
  MatcherEvaluationResults matchersEvaluate(@Param("id") String id,
      @Param("sensorId") String sensorId, @Param("enableLogging") Boolean enableLogging);

  /**
   * Evaluates the matcher for a sensor
   *
   * Note, this is equivalent to the other <code>matchersEvaluate</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link MatchersEvaluateQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param id          Matcher identifier (required)
   * @param sensorId    Sensor identifier (required)
   * @param queryParams Map of query parameters as name-value pairs
   *                    <p>
   *                    The following elements may be specified in the query map:
   *                    </p>
   *                    <ul>
   *                    <li>enableLogging - If true, return verbose logs for the evaluation
   *                    (optional)</li>
   *                    </ul>
   * @return MatcherEvaluationResults
   *
   */
  @RequestLine("GET /api/v1.0/matchers/{id}/evaluate/{sensorId}?enableLogging={enableLogging}")
  @Headers({"Content-Type: */*",})
  MatcherEvaluationResults matchersEvaluate(@Param("id") String id,
      @Param("sensorId") String sensorId,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>matchersEvaluate</code>
   * method in a fluent style.
   */
  public static class MatchersEvaluateQueryParams extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    public MatchersEvaluateQueryParams enableLogging(final Boolean value) {
      put("enableLogging", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Retrieves matchers
   *
   * @param ids         Optional &#x27;;&#x27; delimited list of ids (optional)
   * @param names       Optional &#x27;;&#x27; delimited list of names (optional)
   * @param includes    Comma separated list of what to include, for example
   *                    \&quot;Space,Condition\&quot;. Defaults to None (optional)
   * @param spaceId     Optionally filter on objects based on their location in the topology
   *                    relative to the specified spaceId (optional)
   * @param traverse    None (the default) for the specified spaceId only, Down for space and
   *                    descendants, Up for spaceId and ancestors, Any for both (optional)
   * @param minLevel    Optional filter on minimum level, inclusive (optional)
   * @param maxLevel    Optional filter on maximum level, inclusive (optional)
   * @param minRelative Specify if min level is absolute or relative (optional)
   * @param maxRelative Specify if max level is absolute or relative (optional)
   * @return java.util.List&lt;MatcherRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/matchers?ids={ids}&names={names}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  List<MatcherRetrieve> matchersRetrieve(@Param("ids") String ids, @Param("names") String names,
      @Param("includes") String includes, @Param("spaceId") String spaceId,
      @Param("traverse") String traverse, @Param("minLevel") Integer minLevel,
      @Param("maxLevel") Integer maxLevel, @Param("minRelative") Boolean minRelative,
      @Param("maxRelative") Boolean maxRelative);

  /**
   * Retrieves matchers
   *
   * Note, this is equivalent to the other <code>matchersRetrieve</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link MatchersRetrieveQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *                    <p>
   *                    The following elements may be specified in the query map:
   *                    </p>
   *                    <ul>
   *                    <li>ids - Optional &#x27;;&#x27; delimited list of ids (optional)</li>
   *                    <li>names - Optional &#x27;;&#x27; delimited list of names (optional)</li>
   *                    <li>includes - Comma separated list of what to include, for example
   *                    \&quot;Space,Condition\&quot;. Defaults to None (optional)</li>
   *                    <li>spaceId - Optionally filter on objects based on their location in the
   *                    topology relative to the specified spaceId (optional)</li>
   *                    <li>traverse - None (the default) for the specified spaceId only, Down for
   *                    space and descendants, Up for spaceId and ancestors, Any for both
   *                    (optional)</li>
   *                    <li>minLevel - Optional filter on minimum level, inclusive (optional)</li>
   *                    <li>maxLevel - Optional filter on maximum level, inclusive (optional)</li>
   *                    <li>minRelative - Specify if min level is absolute or relative
   *                    (optional)</li>
   *                    <li>maxRelative - Specify if max level is absolute or relative
   *                    (optional)</li>
   *                    </ul>
   * @return java.util.List&lt;MatcherRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/matchers?ids={ids}&names={names}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  List<MatcherRetrieve> matchersRetrieve(@QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>matchersRetrieve</code>
   * method in a fluent style.
   */
  public static class MatchersRetrieveQueryParams extends HashMap<String, Object> {
    public MatchersRetrieveQueryParams ids(final String value) {
      put("ids", EncodingUtils.encode(value));
      return this;
    }

    public MatchersRetrieveQueryParams names(final String value) {
      put("names", EncodingUtils.encode(value));
      return this;
    }

    public MatchersRetrieveQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public MatchersRetrieveQueryParams spaceId(final String value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public MatchersRetrieveQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public MatchersRetrieveQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public MatchersRetrieveQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public MatchersRetrieveQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public MatchersRetrieveQueryParams maxRelative(final Boolean value) {
      put("maxRelative", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Retrieves a matcher
   *
   * @param id       Matcher identifier (required)
   * @param includes Comma separated list of what to include (optional)
   * @return MatcherRetrieve
   */
  @RequestLine("GET /api/v1.0/matchers/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  MatcherRetrieve matchersRetrieveById(@Param("id") String id, @Param("includes") String includes);

  /**
   * Retrieves a matcher
   *
   * Note, this is equivalent to the other <code>matchersRetrieveById</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link MatchersRetrieveByIdQueryParams} class that allows for building up this map in a fluent
   * style.
   *
   * @param id          Matcher identifier (required)
   * @param queryParams Map of query parameters as name-value pairs
   *                    <p>
   *                    The following elements may be specified in the query map:
   *                    </p>
   *                    <ul>
   *                    <li>includes - Comma separated list of what to include (optional)</li>
   *                    </ul>
   * @return MatcherRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/matchers/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  MatcherRetrieve matchersRetrieveById(@Param("id") String id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>matchersRetrieveById</code>
   * method in a fluent style.
   */
  public static class MatchersRetrieveByIdQueryParams extends HashMap<String, Object> {
    public MatchersRetrieveByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Update a matcher
   *
   * @param body The matcher information (required)
   * @param id   Matcher identifier (required)
   */
  @RequestLine("PATCH /api/v1.0/matchers/{id}")
  @Headers({"Accept: */*",})
  void matchersUpdate(MatcherUpdate body, @Param("id") String id);

  /**
   * Update a matcher
   *
   * @param id           Matcher identifier (required)
   * @param name         (optional)
   * @param description  (optional)
   * @param friendlyName (optional)
   * @param conditions   (optional)
   */
  @RequestLine("PATCH /api/v1.0/matchers/{id}")
  @Headers({"Accept: */*",})
  void matchersUpdate(@Param("id") String id, @Param("name") String name,
      @Param("description") String description, @Param("friendlyName") String friendlyName,
      @Param("conditions") List<ConditionUpdate> conditions);
}
