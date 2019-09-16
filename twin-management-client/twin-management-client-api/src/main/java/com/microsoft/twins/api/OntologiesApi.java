/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.microsoft.twins.model.EncodingUtils;
import com.microsoft.twins.model.OntologyRetrieve;
import com.microsoft.twins.model.OntologyUpdate;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface OntologiesApi {
  /**
   * Gets a list of ontologies
   *
   * @param names Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of names to filter by
   *        (optional)
   * @param loaded Optional filter on the loaded flag (optional)
   * @param includes Comma separated list of what to include, for example \&quot;Types\&quot;.
   *        Defaults to None (optional)
   * @return java.util.List&lt;OntologyRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/ontologies?names={names}&loaded={loaded}&includes={includes}")
  @Headers({"Accept: */*",})
  List<OntologyRetrieve> ontologiesRetrieve(@Param("names") String names,
      @Param("loaded") Boolean loaded, @Param("includes") String includes);

  /**
   * Gets a list of ontologies
   *
   * Note, this is equivalent to the other <code>ontologiesRetrieve</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the {@link OntologiesRetrieveQueryParams}
   * class that allows for building up this map in a fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>names - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of names to filter by
   *        (optional)</li>
   *        <li>loaded - Optional filter on the loaded flag (optional)</li>
   *        <li>includes - Comma separated list of what to include, for example \&quot;Types\&quot;.
   *        Defaults to None (optional)</li>
   *        </ul>
   * @return java.util.List&lt;OntologyRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/ontologies?names={names}&loaded={loaded}&includes={includes}")
  @Headers({"Content-Type: */*",})
  List<OntologyRetrieve> ontologiesRetrieve(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>ontologiesRetrieve</code>
   * method in a fluent style.
   */
  public static class OntologiesRetrieveQueryParams extends HashMap<String, Object> {
    public OntologiesRetrieveQueryParams names(final String value) {
      put("names", EncodingUtils.encode(value));
      return this;
    }

    public OntologiesRetrieveQueryParams loaded(final Boolean value) {
      put("loaded", EncodingUtils.encode(value));
      return this;
    }

    public OntologiesRetrieveQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a specific ontology
   *
   * @param id Ontology id (required)
   * @param includes Comma separated list of what to include, for example \&quot;Types\&quot;.
   *        Defaults to None (optional)
   * @return OntologyRetrieve
   */
  @RequestLine("GET /api/v1.0/ontologies/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  OntologyRetrieve ontologiesRetrieveById(@Param("id") Integer id,
      @Param("includes") String includes);

  /**
   * Gets a specific ontology
   *
   * Note, this is equivalent to the other <code>ontologiesRetrieveById</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link OntologiesRetrieveByIdQueryParams} class that allows for building up this map in a
   * fluent style.
   *
   * @param id Ontology id (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example \&quot;Types\&quot;.
   *        Defaults to None (optional)</li>
   *        </ul>
   * @return OntologyRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/ontologies/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  OntologyRetrieve ontologiesRetrieveById(@Param("id") Integer id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>ontologiesRetrieveById</code>
   * method in a fluent style.
   */
  public static class OntologiesRetrieveByIdQueryParams extends HashMap<String, Object> {
    public OntologiesRetrieveByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Updates an ontology
   *
   * @param body Update payload (required)
   * @param id Ontology id (required)
   */
  @RequestLine("PATCH /api/v1.0/ontologies/{id}")
  @Headers({"Accept: */*",})
  void ontologiesUpdate(OntologyUpdate body, @Param("id") Integer id);

  /**
   * Updates an ontology
   *
   * @param id Ontology id (required)
   * @param loaded (optional)
   */
  @RequestLine("PATCH /api/v1.0/ontologies/{id}")
  @Headers({"Accept: */*",})
  void ontologiesUpdate(@Param("id") Integer id, @Param("loaded") Boolean loaded);
}
