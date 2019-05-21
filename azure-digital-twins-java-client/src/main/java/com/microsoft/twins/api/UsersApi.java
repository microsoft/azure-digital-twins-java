/**
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.microsoft.twins.EncodingUtils;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.model.BlobMetadataRetrieve;
import com.microsoft.twins.model.ExtendedPropertyCreate;
import com.microsoft.twins.model.Location;
import com.microsoft.twins.model.UserCreateWithSpace;
import com.microsoft.twins.model.UserRetrieve;
import com.microsoft.twins.model.UserUpdate;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface UsersApi extends TwinsApiClient.Api {
  /**
   * Creates a blob This is a multi-part request. For more information, see sample app or doc
   * examples. Key value pairs specified in the Content-Disposition header in the blob-chunk of the
   * multipart request will be preserved as meta-data on the stored blob.
   *
   * @param metadata (optional)
   * @param contents (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/users/blobs")
  @Headers({"Accept: */*",})
  UUID usersCreateBlob(@Param("metadata") String metadata, @Param("contents") File contents);

  /**
   * Creates or updates a user, using SpaceId and upn as lookup keys
   *
   * @param body User settings (required)
   * @return UUID
   */
  @RequestLine("PUT /api/v1.0/users")
  @Headers({"Accept: */*",})
  UUID usersCreateOrUpdate(UserCreateWithSpace body);

  /**
   * Creates or updates a user, using SpaceId and upn as lookup keys
   *
   * @param spaceId (optional)
   * @param upn (optional)
   * @param location (optional)
   * @param firstName (optional)
   * @param lastName (optional)
   * @param managerName (optional)
   * @param metadata (optional)
   * @param properties (optional)
   * @return UUID
   */
  @RequestLine("PUT /api/v1.0/users")
  @Headers({"Accept: */*",})
  UUID usersCreateOrUpdate(@Param("spaceId") UUID spaceId, @Param("upn") String upn,
      @Param("location") Location location, @Param("firstName") String firstName,
      @Param("lastName") String lastName, @Param("managerName") String managerName,
      @Param("metadata") java.util.Map<String, String> metadata,
      @Param("properties") java.util.List<ExtendedPropertyCreate> properties);

  /**
   * Creates a property value
   *
   * @param body Extended property data (required)
   * @param id Parent Id (required)
   * @return String
   */
  @RequestLine("POST /api/v1.0/users/{id}/properties")
  @Headers({"Accept: */*",})
  String usersCreateProperty(ExtendedPropertyCreate body, @Param("id") String id);

  /**
   * Creates a property value
   *
   * @param id Parent Id (required)
   * @param name (optional)
   * @param value (optional)
   * @return String
   */
  @RequestLine("POST /api/v1.0/users/{id}/properties")
  @Headers({"Accept: */*",})
  String usersCreateProperty(@Param("id") String id, @Param("name") String name,
      @Param("value") String value);

  /**
   * Deletes a user
   *
   * @param id User Id (required)
   */
  @RequestLine("DELETE /api/v1.0/users/{id}")
  @Headers({"Accept: */*",})
  void usersDelete(@Param("id") String id);

  /**
   * Deletes a blob Deleting a blob will delete its metadata, its content (all versions) and its
   * associations
   *
   * @param id Blob Id (required)
   */
  @RequestLine("DELETE /api/v1.0/users/blobs/{id}")
  @Headers({"Accept: */*",})
  void usersDeleteBlob(@Param("id") String id);

  /**
   * Delete the contents of the given version of the given blob Delete will fail if this version has
   * any associations
   *
   * @param id blob id (required)
   * @param version blob version (required)
   */
  @RequestLine("DELETE /api/v1.0/users/blobs/{id}/contents/{version}")
  @Headers({"Accept: */*",})
  void usersDeleteBlobContents(@Param("id") String id, @Param("version") Integer version);

  /**
   * Deletes all property values
   *
   * @param id Parent Id (required)
   */
  @RequestLine("DELETE /api/v1.0/users/{id}/properties")
  @Headers({"Accept: */*",})
  void usersDeleteProperties(@Param("id") String id);

  /**
   * Deletes the given property value
   *
   * @param id Parent Id (required)
   * @param name Property name (required)
   */
  @RequestLine("DELETE /api/v1.0/users/{id}/properties/{name}")
  @Headers({"Accept: */*",})
  void usersDeleteProperty(@Param("id") String id, @Param("name") String name);

  /**
   * Gets the contents of the given version of the given blob
   *
   * @param id blob id (required)
   * @param version blob content version (required)
   * @return File
   */
  @RequestLine("GET /api/v1.0/users/blobs/{id}/contents/{version}")
  @Headers({"Accept: */*",})
  File usersGetBlobContents(@Param("id") String id, @Param("version") Integer version);

  /**
   * Gets the contents of the latest version of the given blob
   *
   * @param id blob id (required)
   * @return File
   */
  @RequestLine("GET /api/v1.0/users/blobs/{id}/contents/latest")
  @Headers({"Accept: */*",})
  File usersGetLatestBlobContents(@Param("id") String id);

  /**
   * Gets a list of users
   *
   * @param upn Optional filter on user UPN (optional)
   * @param firstName Optional filter on first name (optional)
   * @param lastName Optional filter on last name (optional)
   * @param includes Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None. A user can request their own RoleAssignments by passing in the
   *        \&quot;RoleAssigments\&quot; include parameter, but they cannot request the role
   *        assignments of others (optional)
   * @param propertyKey Optional filter on objects that have the given property key defined
   *        (optional)
   * @param propertyValue Optional filter on the value of the given property key (optional)
   * @param propertyValueSearchType Optional type of search on property value. Contains or Equals.
   *        Defaults to Equals (optional)
   * @param spaceId Optionally filter on objects based on their location in the topology relative to
   *        the specified spaceId (optional)
   * @param traverse None (the default) for the specified spaceId only, Down for space and
   *        descendants, Up for spaceId and ancestors, Any for both (optional)
   * @param minLevel Optional filter on minimum level, inclusive (optional)
   * @param maxLevel Optional filter on maximum level, inclusive (optional)
   * @param minRelative Specify if min level is absolute or relative (optional)
   * @param maxRelative Specify if max level is absolute or relative (optional)
   * @return java.util.List&lt;UserRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/users?upn={upn}&firstName={firstName}&lastName={lastName}&includes={includes}&propertyKey={propertyKey}&propertyValue={propertyValue}&propertyValueSearchType={propertyValueSearchType}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  java.util.List<UserRetrieve> usersRetrieve(@Param("upn") String upn,
      @Param("firstName") String firstName, @Param("lastName") String lastName,
      @Param("includes") String includes, @Param("propertyKey") String propertyKey,
      @Param("propertyValue") String propertyValue,
      @Param("propertyValueSearchType") String propertyValueSearchType,
      @Param("spaceId") String spaceId, @Param("traverse") String traverse,
      @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel,
      @Param("minRelative") Boolean minRelative, @Param("maxRelative") Boolean maxRelative);

  /**
   * Gets a list of users
   *
   * Note, this is equivalent to the other <code>usersRetrieve</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link UsersRetrieveQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>upn - Optional filter on user UPN (optional)</li>
   *        <li>firstName - Optional filter on first name (optional)</li>
   *        <li>lastName - Optional filter on last name (optional)</li>
   *        <li>includes - Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None. A user can request their own RoleAssignments by passing in the
   *        \&quot;RoleAssigments\&quot; include parameter, but they cannot request the role
   *        assignments of others (optional)</li>
   *        <li>propertyKey - Optional filter on objects that have the given property key defined
   *        (optional)</li>
   *        <li>propertyValue - Optional filter on the value of the given property key
   *        (optional)</li>
   *        <li>propertyValueSearchType - Optional type of search on property value. Contains or
   *        Equals. Defaults to Equals (optional)</li>
   *        <li>spaceId - Optionally filter on objects based on their location in the topology
   *        relative to the specified spaceId (optional)</li>
   *        <li>traverse - None (the default) for the specified spaceId only, Down for space and
   *        descendants, Up for spaceId and ancestors, Any for both (optional)</li>
   *        <li>minLevel - Optional filter on minimum level, inclusive (optional)</li>
   *        <li>maxLevel - Optional filter on maximum level, inclusive (optional)</li>
   *        <li>minRelative - Specify if min level is absolute or relative (optional)</li>
   *        <li>maxRelative - Specify if max level is absolute or relative (optional)</li>
   *        </ul>
   * @return java.util.List&lt;UserRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/users?upn={upn}&firstName={firstName}&lastName={lastName}&includes={includes}&propertyKey={propertyKey}&propertyValue={propertyValue}&propertyValueSearchType={propertyValueSearchType}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  java.util.List<UserRetrieve> usersRetrieve(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>usersRetrieve</code> method
   * in a fluent style.
   */
  public static class UsersRetrieveQueryParams extends HashMap<String, Object> {
    public UsersRetrieveQueryParams upn(final String value) {
      put("upn", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams firstName(final String value) {
      put("firstName", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams lastName(final String value) {
      put("lastName", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams propertyKey(final String value) {
      put("propertyKey", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams propertyValue(final String value) {
      put("propertyValue", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams propertyValueSearchType(final String value) {
      put("propertyValueSearchType", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams spaceId(final String value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveQueryParams maxRelative(final Boolean value) {
      put("maxRelative", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a list of blobs
   *
   * @param names Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of blob names to filter by
   *        (optional)
   * @param ids Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)
   * @param sharings Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of Sharing modes to
   *        filter by (optional)
   * @param types Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of types to filter by
   *        (optional)
   * @param subtypes Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of subtypes to filter by
   *        (optional)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;ContentInfo,Description\&quot;. Defaults to None (optional)
   * @param spaceId Optionally filter on objects based on their location in the topology relative to
   *        the specified spaceId (optional)
   * @param traverse None (the default) for the specified spaceId only, Down for space and
   *        descendants, Up for spaceId and ancestors, Any for both (optional)
   * @param minLevel Optional filter on minimum level, inclusive (optional)
   * @param maxLevel Optional filter on maximum level, inclusive (optional)
   * @param minRelative Specify if min level is absolute or relative (optional)
   * @param maxRelative Specify if max level is absolute or relative (optional)
   * @return java.util.List&lt;BlobMetadataRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/users/blobs?names={names}&ids={ids}&sharings={sharings}&types={types}&subtypes={subtypes}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  java.util.List<BlobMetadataRetrieve> usersRetrieveBlobMetadata(@Param("names") String names,
      @Param("ids") String ids, @Param("sharings") String sharings, @Param("types") String types,
      @Param("subtypes") String subtypes, @Param("includes") String includes,
      @Param("spaceId") String spaceId, @Param("traverse") String traverse,
      @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel,
      @Param("minRelative") Boolean minRelative, @Param("maxRelative") Boolean maxRelative);

  /**
   * Gets a list of blobs
   *
   * Note, this is equivalent to the other <code>usersRetrieveBlobMetadata</code> method, but with
   * the query parameters collected into a single Map parameter. This is convenient for services
   * with optional query parameters, especially when used with the
   * {@link UsersRetrieveBlobMetadataQueryParams} class that allows for building up this map in a
   * fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>names - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of blob names to
   *        filter by (optional)</li>
   *        <li>ids - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)</li>
   *        <li>sharings - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of Sharing modes
   *        to filter by (optional)</li>
   *        <li>types - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of types to filter by
   *        (optional)</li>
   *        <li>subtypes - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of subtypes to
   *        filter by (optional)</li>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;ContentInfo,Description\&quot;. Defaults to None (optional)</li>
   *        <li>spaceId - Optionally filter on objects based on their location in the topology
   *        relative to the specified spaceId (optional)</li>
   *        <li>traverse - None (the default) for the specified spaceId only, Down for space and
   *        descendants, Up for spaceId and ancestors, Any for both (optional)</li>
   *        <li>minLevel - Optional filter on minimum level, inclusive (optional)</li>
   *        <li>maxLevel - Optional filter on maximum level, inclusive (optional)</li>
   *        <li>minRelative - Specify if min level is absolute or relative (optional)</li>
   *        <li>maxRelative - Specify if max level is absolute or relative (optional)</li>
   *        </ul>
   * @return java.util.List&lt;BlobMetadataRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/users/blobs?names={names}&ids={ids}&sharings={sharings}&types={types}&subtypes={subtypes}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  java.util.List<BlobMetadataRetrieve> usersRetrieveBlobMetadata(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>usersRetrieveBlobMetadata</code> method in a fluent style.
   */
  public static class UsersRetrieveBlobMetadataQueryParams extends HashMap<String, Object> {
    public UsersRetrieveBlobMetadataQueryParams names(final String value) {
      put("names", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveBlobMetadataQueryParams ids(final String value) {
      put("ids", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveBlobMetadataQueryParams sharings(final String value) {
      put("sharings", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveBlobMetadataQueryParams types(final String value) {
      put("types", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveBlobMetadataQueryParams subtypes(final String value) {
      put("subtypes", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveBlobMetadataQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveBlobMetadataQueryParams spaceId(final String value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveBlobMetadataQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveBlobMetadataQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveBlobMetadataQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveBlobMetadataQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public UsersRetrieveBlobMetadataQueryParams maxRelative(final Boolean value) {
      put("maxRelative", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a blob
   *
   * @param id Blob Id (required)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;ContentInfo,Description\&quot;. Defaults to None (optional)
   * @return BlobMetadataRetrieve
   */
  @RequestLine("GET /api/v1.0/users/blobs/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  BlobMetadataRetrieve usersRetrieveBlobMetadataById(@Param("id") String id,
      @Param("includes") String includes);

  /**
   * Gets a blob
   *
   * Note, this is equivalent to the other <code>usersRetrieveBlobMetadataById</code> method, but
   * with the query parameters collected into a single Map parameter. This is convenient for
   * services with optional query parameters, especially when used with the
   * {@link UsersRetrieveBlobMetadataByIdQueryParams} class that allows for building up this map in
   * a fluent style.
   *
   * @param id Blob Id (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;ContentInfo,Description\&quot;. Defaults to None (optional)</li>
   *        </ul>
   * @return BlobMetadataRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/users/blobs/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  BlobMetadataRetrieve usersRetrieveBlobMetadataById(@Param("id") String id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>usersRetrieveBlobMetadataById</code> method in a fluent style.
   */
  public static class UsersRetrieveBlobMetadataByIdQueryParams extends HashMap<String, Object> {
    public UsersRetrieveBlobMetadataByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a specific user
   *
   * @param id User identifier (required)
   * @param includes Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None. A user can request their own RoleAssignments by passing in the
   *        \&quot;RoleAssigments\&quot; include parameter, but they cannot request the role
   *        assignments of others (optional)
   * @return UserRetrieve
   */
  @RequestLine("GET /api/v1.0/users/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  UserRetrieve usersRetrieveById(@Param("id") String id, @Param("includes") String includes);

  /**
   * Gets a specific user
   *
   * Note, this is equivalent to the other <code>usersRetrieveById</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link UsersRetrieveByIdQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param id User identifier (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None. A user can request their own RoleAssignments by passing in the
   *        \&quot;RoleAssigments\&quot; include parameter, but they cannot request the role
   *        assignments of others (optional)</li>
   *        </ul>
   * @return UserRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/users/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  UserRetrieve usersRetrieveById(@Param("id") String id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>usersRetrieveById</code>
   * method in a fluent style.
   */
  public static class UsersRetrieveByIdQueryParams extends HashMap<String, Object> {
    public UsersRetrieveByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets the value of a property
   *
   * @param id Parent Id (required)
   * @param name Property name (required)
   * @return String
   */
  @RequestLine("GET /api/v1.0/users/{id}/properties/{name}")
  @Headers({"Accept: */*",})
  String usersRetrieveProperty(@Param("id") String id, @Param("name") String name);

  /**
   * Updates a user
   *
   * @param body User settings (required)
   * @param id User identifier (required)
   */
  @RequestLine("PATCH /api/v1.0/users/{id}")
  @Headers({"Accept: */*",})
  void usersUpdate(UserUpdate body, @Param("id") String id);

  /**
   * Updates a user
   *
   * @param id User identifier (required)
   * @param spaceId (optional)
   * @param upn (optional)
   * @param location (optional)
   * @param firstName (optional)
   * @param lastName (optional)
   * @param managerName (optional)
   * @param metadata (optional)
   * @param properties (optional)
   */
  @RequestLine("PATCH /api/v1.0/users/{id}")
  @Headers({"Accept: */*",})
  void usersUpdate(@Param("id") String id, @Param("spaceId") UUID spaceId, @Param("upn") String upn,
      @Param("location") Location location, @Param("firstName") String firstName,
      @Param("lastName") String lastName, @Param("managerName") String managerName,
      @Param("metadata") java.util.Map<String, String> metadata,
      @Param("properties") java.util.List<ExtendedPropertyCreate> properties);

  /**
   * Updates a blob This is a multi-part request. For more information, see sample app or doc
   * examples.
   *
   * @param id blob Id (required)
   * @param metadata (optional)
   * @param contents (optional)
   */
  @RequestLine("PATCH /api/v1.0/users/blobs/{id}")
  @Headers({"Accept: */*",})
  void usersUpdateBlob(@Param("id") String id, @Param("metadata") String metadata,
      @Param("contents") File contents);

  /**
   * Creates or updates property values
   *
   * @param body The properties (required)
   * @param id Parent Id (required)
   * @return Object
   */
  @RequestLine("PUT /api/v1.0/users/{id}/properties")
  @Headers({"Accept: */*",})
  Object usersUpdateProperties(java.util.List<ExtendedPropertyCreate> body, @Param("id") String id);
}
