/**
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import org.junit.Before;
import org.junit.Test;
import com.microsoft.twins.model.EndpointCreate;

/**
 * API tests for EndpointsApi
 */
public class EndpointsApiTest extends AbstractApiTest {
  private EndpointsApi api;

  @Before
  public void setup() {
    api = TWINS_API_CLIENT.getEndpointsApi();
  }

  /**
   * Creates an endpoint
   *
   *
   */
  @Test
  public void endpointsCreateTest() {
    final EndpointCreate body;
    // UUID response = api.endpointsCreate(body);
    // TODO: test validations
  }

  /**
   * Deletes the specified endpoint
   *
   *
   */
  @Test
  public void endpointsDeleteTest() {
    final String id;
    // api.endpointsDelete(id);
    // TODO: test validations
  }

  /**
   * Modifies an existing endpoint. Does not create if it doesn&#x27;t exist.
   *
   *
   */
  @Test
  public void endpointsModifyTest() {
    final EndpointCreate body;
    final String id;
    // Object response = api.endpointsModify(body, id);
    // TODO: test validations
  }

  /**
   * Gets a list of endpoints
   *
   *
   */
  @Test
  public void endpointsRetrieveTest() {
    final String timeUpdated;
    final String types;
    final String eventTypes;
    // java.util.List<EndpointRetrieve> response =
    // api.endpointsRetrieve(timeUpdated, types, eventTypes);
    // TODO: test validations
  }

  /**
   * Gets a list of endpoints
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void endpointsRetrieveTestQueryMap() {
    final EndpointsApi.EndpointsRetrieveQueryParams queryParams =
        new EndpointsApi.EndpointsRetrieveQueryParams().timeUpdated(null).types(null)
            .eventTypes(null);
    // java.util.List<EndpointRetrieve> response =
    // api.endpointsRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Gets a specific endpoint
   *
   *
   */
  @Test
  public void endpointsRetrieveByIdTest() {
    final String id;
    // EndpointRetrieve response = api.endpointsRetrieveById(id);
    // TODO: test validations
  }
}
