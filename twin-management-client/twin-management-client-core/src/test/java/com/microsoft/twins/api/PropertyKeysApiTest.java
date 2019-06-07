/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import org.junit.Before;
import org.junit.Test;
import com.microsoft.twins.model.ExtendedPropertyKeyCreate;
import com.microsoft.twins.model.ExtendedPropertyKeyUpdate;

/**
 * API tests for PropertyKeysApi
 */
public class PropertyKeysApiTest extends AbstractApiTest {
  private PropertyKeysApi api;

  @Before
  public void setup() {
    api = TWINS_API_CLIENT.getPropertyKeysApi();
  }

  /**
   * Creates a property key
   *
   *
   */
  @Test
  public void propertyKeysCreateTest() {
    final ExtendedPropertyKeyCreate body;
    // Integer response = api.propertyKeysCreate(body);
    // TODO: test validations
  }

  /**
   * Adds or updates a property key using SpaceId, Name and Scope as lookup keys
   *
   *
   */
  @Test
  public void propertyKeysCreateOrUpdateTest() {
    final ExtendedPropertyKeyCreate body;
    // Integer response = api.propertyKeysCreateOrUpdate(body);
    // TODO: test validations
  }

  /**
   * Deletes the given property key
   *
   *
   */
  @Test
  public void propertyKeysDeleteTest() {
    final Integer id;
    // api.propertyKeysDelete(id);
    // TODO: test validations
  }

  /**
   * Deletes property keys for child objects of the given space
   *
   *
   */
  @Test
  public void propertyKeysDeleteBySpaceTest() {
    final String spaceId;
    final String scope;
    final String keys;
    // api.propertyKeysDeleteBySpace(spaceId, scope, keys);
    // TODO: test validations
  }

  /**
   * Deletes property keys for child objects of the given space
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void propertyKeysDeleteBySpaceTestQueryMap() {
    final PropertyKeysApi.PropertyKeysDeleteBySpaceQueryParams queryParams =
        new PropertyKeysApi.PropertyKeysDeleteBySpaceQueryParams().spaceId(null).scope(null)
            .keys(null);
    // api.propertyKeysDeleteBySpace(queryParams);
    // TODO: test validations
  }

  /**
   * Retrieves property keys
   *
   *
   */
  @Test
  public void propertyKeysRetrieveTest() {
    final String scope;
    final String category;
    final String includes;
    final String spaceId;
    final String traverse;
    final Integer minLevel;
    final Integer maxLevel;
    final Boolean minRelative;
    final Boolean maxRelative;
    // List<ExtendedPropertyKeyRetrieve> response =
    // api.propertyKeysRetrieve(scope, category, includes, spaceId,
    // traverse, minLevel, maxLevel, minRelative, maxRelative);
    // TODO: test validations
  }

  /**
   * Retrieves property keys
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void propertyKeysRetrieveTestQueryMap() {
    final PropertyKeysApi.PropertyKeysRetrieveQueryParams queryParams =
        new PropertyKeysApi.PropertyKeysRetrieveQueryParams().scope(null).category(null)
            .includes(null).spaceId(null).traverse(null).minLevel(null).maxLevel(null)
            .minRelative(null).maxRelative(null);
    // List<ExtendedPropertyKeyRetrieve> response =
    // api.propertyKeysRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Retrieves a property key
   *
   *
   */
  @Test
  public void propertyKeysRetrieveByIdTest() {
    final Integer id;
    final String includes;
    // ExtendedPropertyKeyRetrieve response =
    // api.propertyKeysRetrieveById(id, includes);
    // TODO: test validations
  }

  /**
   * Retrieves a property key
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void propertyKeysRetrieveByIdTestQueryMap() {
    final Integer id;
    final PropertyKeysApi.PropertyKeysRetrieveByIdQueryParams queryParams =
        new PropertyKeysApi.PropertyKeysRetrieveByIdQueryParams().includes(null);
    // ExtendedPropertyKeyRetrieve response =
    // api.propertyKeysRetrieveById(id, queryParams);
    // TODO: test validations
  }

  /**
   * Updates a property key
   *
   *
   */
  @Test
  public void propertyKeysUpdateTest() {
    final ExtendedPropertyKeyUpdate body;
    final Integer id;
    // api.propertyKeysUpdate(body, id);
    // TODO: test validations
  }
}
