/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import org.junit.Before;
import org.junit.Test;
import com.microsoft.twins.model.ExtendedTypeCreate;
import com.microsoft.twins.model.ExtendedTypeUpdate;

/**
 * API tests for TypesApi
 */
public class TypesApiTest extends AbstractApiTest {
  private TypesApi api;

  @Before
  public void setup() {
    api = TWINS_API_CLIENT.getTypesApi();
  }

  /**
   * Creates an extended type
   *
   *
   */
  @Test
  public void typesCreateTest() {
    final ExtendedTypeCreate body;
    // Integer response = api.typesCreate(body);
    // TODO: test validations
  }

  /**
   * Deletes an extended type
   *
   *
   */
  @Test
  public void typesDeleteTest() {
    final Integer id;
    // api.typesDelete(id);
    // TODO: test validations
  }

  /**
   * Gets a list of extended types
   *
   *
   */
  @Test
  public void typesRetrieveTest() {
    final String ids;
    final String categories;
    final String names;
    final Boolean system;
    final Boolean disabled;
    final String includes;
    final String spaceId;
    final String traverse;
    final Integer minLevel;
    final Integer maxLevel;
    final Boolean minRelative;
    final Boolean maxRelative;
    // List<ExtendedTypeRetrieve> response =
    // api.typesRetrieve(ids, categories, names, system, disabled, includes,
    // spaceId, traverse, minLevel, maxLevel, minRelative, maxRelative);
    // TODO: test validations
  }

  /**
   * Gets a list of extended types
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void typesRetrieveTestQueryMap() {
    final TypesApi.TypesRetrieveQueryParams queryParams =
        new TypesApi.TypesRetrieveQueryParams().ids(null).categories(null).names(null).system(null)
            .disabled(null).includes(null).spaceId(null).traverse(null).minLevel(null)
            .maxLevel(null).minRelative(null).maxRelative(null);
    // List<ExtendedTypeRetrieve> response =
    // api.typesRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Gets a specific extended type
   *
   *
   */
  @Test
  public void typesRetrieveByIdTest() {
    final Integer id;
    final String includes;
    // ExtendedTypeRetrieve response = api.typesRetrieveById(id, includes);
    // TODO: test validations
  }

  /**
   * Gets a specific extended type
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void typesRetrieveByIdTestQueryMap() {
    final Integer id;
    final TypesApi.TypesRetrieveByIdQueryParams queryParams =
        new TypesApi.TypesRetrieveByIdQueryParams().includes(null);
    // ExtendedTypeRetrieve response = api.typesRetrieveById(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Updates an extended type
   *
   *
   */
  @Test
  public void typesUpdateTest() {
    final ExtendedTypeUpdate body;
    final Integer id;
    // api.typesUpdate(body, id);
    // TODO: test validations
  }
}
