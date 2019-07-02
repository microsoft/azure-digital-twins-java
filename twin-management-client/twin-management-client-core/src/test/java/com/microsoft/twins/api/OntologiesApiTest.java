/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import com.microsoft.twins.model.OntologyUpdate;

/**
 * API tests for OntologiesApi
 */
public class OntologiesApiTest extends AbstractApiTest {
  private OntologiesApi api;

  @BeforeEach
  public void setup() {
    api = TWINS_API_CLIENT.getOntologiesApi();
  }

  /**
   * Gets a list of ontologies
   *
   *
   */
  @Test
  public void ontologiesRetrieveTest() {
    final String names;
    final Boolean loaded;
    final String includes;
    // List<OntologyRetrieve> response =
    // api.ontologiesRetrieve(names, loaded, includes);
    // TODO: test validations
  }

  /**
   * Gets a list of ontologies
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void ontologiesRetrieveTestQueryMap() {
    final OntologiesApi.OntologiesRetrieveQueryParams queryParams =
        new OntologiesApi.OntologiesRetrieveQueryParams().names(null).loaded(null).includes(null);
    // List<OntologyRetrieve> response =
    // api.ontologiesRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Gets a specific ontology
   *
   *
   */
  @Test
  public void ontologiesRetrieveByIdTest() {
    final Integer id;
    final String includes;
    // OntologyRetrieve response = api.ontologiesRetrieveById(id, includes);
    // TODO: test validations
  }

  /**
   * Gets a specific ontology
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void ontologiesRetrieveByIdTestQueryMap() {
    final Integer id;
    final OntologiesApi.OntologiesRetrieveByIdQueryParams queryParams =
        new OntologiesApi.OntologiesRetrieveByIdQueryParams().includes(null);
    // OntologyRetrieve response = api.ontologiesRetrieveById(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Updates an ontology
   *
   *
   */
  @Test
  public void ontologiesUpdateTest() {
    final OntologyUpdate body;
    final Integer id;
    // api.ontologiesUpdate(body, id);
    // TODO: test validations
  }
}
