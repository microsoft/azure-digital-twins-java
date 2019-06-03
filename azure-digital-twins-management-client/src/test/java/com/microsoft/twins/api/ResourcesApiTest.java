/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import org.junit.Before;
import org.junit.Test;
import com.microsoft.twins.model.SpaceResourceCreate;
import com.microsoft.twins.model.SpaceResourceUpdate;

/**
 * API tests for ResourcesApi
 */
public class ResourcesApiTest extends AbstractApiTest {
  private ResourcesApi api;

  @Before
  public void setup() {
    api = TWINS_API_CLIENT.getResourcesApi();
  }

  /**
   * Creates a resource
   *
   *
   */
  @Test
  public void resourcesCreateTest() {
    final SpaceResourceCreate body;
    // UUID response = api.resourcesCreate(body);
    // TODO: test validations
  }

  /**
   * Deletes the specified resource
   *
   *
   */
  @Test
  public void resourcesDeleteTest() {
    final String id;
    // api.resourcesDelete(id);
    // TODO: test validations
  }

  /**
   * Retrieves resources
   *
   *
   */
  @Test
  public void resourcesRetrieveTest() {
    final String type;
    final Boolean isExternallyCreated;
    final String includes;
    final String spaceId;
    final String traverse;
    final Integer minLevel;
    final Integer maxLevel;
    final Boolean minRelative;
    final Boolean maxRelative;
    // List<SpaceResourceRetrieve> response =
    // api.resourcesRetrieve(type, isExternallyCreated, includes, spaceId,
    // traverse, minLevel, maxLevel, minRelative, maxRelative);
    // TODO: test validations
  }

  /**
   * Retrieves resources
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void resourcesRetrieveTestQueryMap() {
    final ResourcesApi.ResourcesRetrieveQueryParams queryParams =
        new ResourcesApi.ResourcesRetrieveQueryParams().type(null).isExternallyCreated(null)
            .includes(null).spaceId(null).traverse(null).minLevel(null).maxLevel(null)
            .minRelative(null).maxRelative(null);
    // List<SpaceResourceRetrieve> response =
    // api.resourcesRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Retrieves the specified resource
   *
   *
   */
  @Test
  public void resourcesRetrieveByIdTest() {
    final String id;
    final String includes;
    // SpaceResourceRetrieve response = api.resourcesRetrieveById(id,
    // includes);
    // TODO: test validations
  }

  /**
   * Retrieves the specified resource
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void resourcesRetrieveByIdTestQueryMap() {
    final String id;
    final ResourcesApi.ResourcesRetrieveByIdQueryParams queryParams =
        new ResourcesApi.ResourcesRetrieveByIdQueryParams().includes(null);
    // SpaceResourceRetrieve response = api.resourcesRetrieveById(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Updates the specified resource
   *
   *
   */
  @Test
  public void resourcesUpdateTest() {
    final SpaceResourceUpdate body;
    final String id;
    // api.resourcesUpdate(body, id);
    // TODO: test validations
  }
}
