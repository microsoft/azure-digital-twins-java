/**
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import org.junit.Before;
import org.junit.Test;
import com.microsoft.twins.model.MatcherCreate;
import com.microsoft.twins.model.MatcherUpdate;

/**
 * API tests for MatchersApi
 */
public class MatchersApiTest extends AbstractApiTest {
  private MatchersApi api;

  @Before
  public void setup() {
    api = TWINS_API_CLIENT.getMatchersApi();
  }

  /**
   * Creates a matcher
   *
   *
   */
  @Test
  public void matchersCreateTest() {
    final MatcherCreate body;
    // UUID response = api.matchersCreate(body);
    // TODO: test validations
  }

  /**
   * Deletes the given matcher
   *
   *
   */
  @Test
  public void matchersDeleteTest() {
    final String id;
    // api.matchersDelete(id);
    // TODO: test validations
  }

  /**
   * Evaluates the matcher for a sensor
   *
   *
   */
  @Test
  public void matchersEvaluateTest() {
    final String id;
    final String sensorId;
    final Boolean enableLogging;
    // MatcherEvaluationResults response = api.matchersEvaluate(id,
    // sensorId, enableLogging);
    // TODO: test validations
  }

  /**
   * Evaluates the matcher for a sensor
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void matchersEvaluateTestQueryMap() {
    final String id;
    final String sensorId;
    final MatchersApi.MatchersEvaluateQueryParams queryParams =
        new MatchersApi.MatchersEvaluateQueryParams().enableLogging(null);
    // MatcherEvaluationResults response = api.matchersEvaluate(id,
    // sensorId, queryParams);
    // TODO: test validations
  }

  /**
   * Retrieves matchers
   *
   *
   */
  @Test
  public void matchersRetrieveTest() {
    final String ids;
    final String names;
    final String includes;
    final String spaceId;
    final String traverse;
    final Integer minLevel;
    final Integer maxLevel;
    final Boolean minRelative;
    final Boolean maxRelative;
    // java.util.List<MatcherRetrieve> response = api.matchersRetrieve(ids,
    // names, includes, spaceId, traverse, minLevel, maxLevel, minRelative,
    // maxRelative);
    // TODO: test validations
  }

  /**
   * Retrieves matchers
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void matchersRetrieveTestQueryMap() {
    final MatchersApi.MatchersRetrieveQueryParams queryParams =
        new MatchersApi.MatchersRetrieveQueryParams().ids(null).names(null).includes(null)
            .spaceId(null).traverse(null).minLevel(null).maxLevel(null).minRelative(null)
            .maxRelative(null);
    // java.util.List<MatcherRetrieve> response =
    // api.matchersRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Retrieves a matcher
   *
   *
   */
  @Test
  public void matchersRetrieveByIdTest() {
    final String id;
    final String includes;
    // MatcherRetrieve response = api.matchersRetrieveById(id, includes);
    // TODO: test validations
  }

  /**
   * Retrieves a matcher
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void matchersRetrieveByIdTestQueryMap() {
    final String id;
    final MatchersApi.MatchersRetrieveByIdQueryParams queryParams =
        new MatchersApi.MatchersRetrieveByIdQueryParams().includes(null);
    // MatcherRetrieve response = api.matchersRetrieveById(id, queryParams);
    // TODO: test validations
  }

  /**
   * Update a matcher
   *
   *
   */
  @Test
  public void matchersUpdateTest() {
    final MatcherUpdate body;
    final String id;
    // api.matchersUpdate(body, id);
    // TODO: test validations
  }
}
