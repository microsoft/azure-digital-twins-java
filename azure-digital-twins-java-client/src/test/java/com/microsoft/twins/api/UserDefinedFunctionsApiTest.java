/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.io.File;
import org.junit.Before;
import org.junit.Test;

/**
 * API tests for UserDefinedFunctionsApi
 */
public class UserDefinedFunctionsApiTest extends AbstractApiTest {
  private UserDefinedFunctionsApi api;

  @Before
  public void setup() {
    api = TWINS_API_CLIENT.getUserDefinedFunctionsApi();
  }

  /**
   * Creates a udf
   *
   * This is a multi-part request. For more information, see doc examples. Key value pairs specified
   * in the Content-Disposition header in the udf-chunk of the multipart request will be preserved
   * as meta-data on the stored udf.
   */
  @Test
  public void userDefinedFunctionsCreateTest() {
    final String metadata;
    final File contents;
    // UUID response = api.userDefinedFunctionsCreate(metadata,
    // contents);
    // TODO: test validations
  }

  /**
   * Deletes a udf
   *
   *
   */
  @Test
  public void userDefinedFunctionsDeleteTest() {
    final String id;
    // api.userDefinedFunctionsDelete(id);
    // TODO: test validations
  }

  /**
   * Gets the contents of a udf
   *
   *
   */
  @Test
  public void userDefinedFunctionsGetBlobContentsTest() {
    final String id;
    // File response = api.userDefinedFunctionsGetBlobContents(id);
    // TODO: test validations
  }

  /**
   * Gets a list of udfs
   *
   *
   */
  @Test
  public void userDefinedFunctionsRetrieveTest() {
    final String names;
    final String ids;
    final String includes;
    final String spaceId;
    final String traverse;
    final Integer minLevel;
    final Integer maxLevel;
    final Boolean minRelative;
    final Boolean maxRelative;
    // java.util.List<UserDefinedFunctionRetrieve> response =
    // api.userDefinedFunctionsRetrieve(names, ids, includes, spaceId,
    // traverse, minLevel, maxLevel, minRelative, maxRelative);
    // TODO: test validations
  }

  /**
   * Gets a list of udfs
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void userDefinedFunctionsRetrieveTestQueryMap() {
    final UserDefinedFunctionsApi.UserDefinedFunctionsRetrieveQueryParams queryParams =
        new UserDefinedFunctionsApi.UserDefinedFunctionsRetrieveQueryParams().names(null).ids(null)
            .includes(null).spaceId(null).traverse(null).minLevel(null).maxLevel(null)
            .minRelative(null).maxRelative(null);
    // java.util.List<UserDefinedFunctionRetrieve> response =
    // api.userDefinedFunctionsRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Gets a udf
   *
   *
   */
  @Test
  public void userDefinedFunctionsRetrieveByIdTest() {
    final String id;
    final String includes;
    // UserDefinedFunctionRetrieve response =
    // api.userDefinedFunctionsRetrieveById(id, includes);
    // TODO: test validations
  }

  /**
   * Gets a udf
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void userDefinedFunctionsRetrieveByIdTestQueryMap() {
    final String id;
    final UserDefinedFunctionsApi.UserDefinedFunctionsRetrieveByIdQueryParams queryParams =
        new UserDefinedFunctionsApi.UserDefinedFunctionsRetrieveByIdQueryParams().includes(null);
    // UserDefinedFunctionRetrieve response =
    // api.userDefinedFunctionsRetrieveById(id, queryParams);
    // TODO: test validations
  }

  /**
   * Updates a udf
   *
   * This is a multi-part request. For more information, see doc examples.
   */
  @Test
  public void userDefinedFunctionsUpdateTest() {
    final String id;
    final String metadata;
    final File contents;
    // api.userDefinedFunctionsUpdate(id, metadata, contents);
    // TODO: test validations
  }
}
