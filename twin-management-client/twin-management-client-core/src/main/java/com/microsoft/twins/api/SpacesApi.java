/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.microsoft.twins.EncodingUtils;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.model.BlobMetadataRetrieve;
import com.microsoft.twins.model.ExtendedPropertyCreate;
import com.microsoft.twins.model.KeyStoreRetrieve;
import com.microsoft.twins.model.Location;
import com.microsoft.twins.model.SensorValue;
import com.microsoft.twins.model.SpaceCreate;
import com.microsoft.twins.model.SpaceResourceRetrieve;
import com.microsoft.twins.model.SpaceRetrieveWithChildren;
import com.microsoft.twins.model.SpaceUpdate;
import com.microsoft.twins.model.UserCreate;
import com.microsoft.twins.model.UserRetrieve;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface SpacesApi extends TwinsApiClient.Api {
  /**
   * Creates a space
   *
   * @param body The space information (required)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/spaces")
  @Headers({"Accept: */*"})
  UUID spacesCreate(SpaceCreate body);

  /**
   * Creates a space
   *
   * @param name (optional)
   * @param description (optional)
   * @param friendlyName (optional)
   * @param type (optional)
   * @param typeId (optional)
   * @param parentSpaceId (optional)
   * @param subtype (optional)
   * @param subtypeId (optional)
   * @param location (optional)
   * @param timeZoneId (optional)
   * @param status (optional)
   * @param statusId (optional)
   * @param properties (optional)
   * @param timeZoneName (optional)
   * @param children (optional)
   * @param users (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/spaces")
  @Headers({"Accept: */*",})
  UUID spacesCreate(@Param("name") String name, @Param("description") String description,
      @Param("friendlyName") String friendlyName, @Param("type") String type,
      @Param("typeId") Integer typeId, @Param("parentSpaceId") UUID parentSpaceId,
      @Param("subtype") String subtype, @Param("subtypeId") Integer subtypeId,
      @Param("location") Location location, @Param("timeZoneId") Integer timeZoneId,
      @Param("status") String status, @Param("statusId") Integer statusId,
      @Param("properties") List<ExtendedPropertyCreate> properties,
      @Param("timeZoneName") String timeZoneName, @Param("children") List<SpaceCreate> children,
      @Param("users") List<UserCreate> users);

  /**
   * Creates a blob This is a multi-part request. For more information, see sample app or doc
   * examples. Key value pairs specified in the Content-Disposition header in the blob-chunk of the
   * multipart request will be preserved as meta-data on the stored blob.
   *
   * @param metadata (optional)
   * @param contents (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/spaces/blobs")
  @Headers({"Accept: */*",})
  UUID spacesCreateBlob(@Param("metadata") String metadata, @Param("contents") File contents);

  /**
   * Adds or updates a user associated to the given space
   *
   * @param body The user&#x27;s data (required)
   * @param id Space Id (required)
   * @return String
   */
  @RequestLine("PUT /api/v1.0/spaces/{id}/users")
  @Headers({"Accept: */*",})
  String spacesCreateOrUpdateUser(UserCreate body, @Param("id") UUID id);

  /**
   * Adds or updates a user associated to the given space
   *
   * @param id Space Id (required)
   * @param upn (optional)
   * @param location (optional)
   * @param firstName (optional)
   * @param lastName (optional)
   * @param managerName (optional)
   * @param metadata (optional)
   * @param properties (optional)
   * @return String
   */
  @RequestLine("PUT /api/v1.0/spaces/{id}/users")
  @Headers({"Accept: */*",})
  String spacesCreateOrUpdateUser(@Param("id") UUID id, @Param("upn") String upn,
      @Param("location") Location location, @Param("firstName") String firstName,
      @Param("lastName") String lastName, @Param("managerName") String managerName,
      @Param("metadata") Map<String, String> metadata,
      @Param("properties") List<ExtendedPropertyCreate> properties);

  /**
   * Creates a property value
   *
   * @param body Extended property data (required)
   * @param id Parent Id (required)
   * @return String
   */
  @RequestLine("POST /api/v1.0/spaces/{id}/properties")
  @Headers({"Accept: */*",})
  String spacesCreateProperty(ExtendedPropertyCreate body, @Param("id") UUID id);

  /**
   * Creates a property value
   *
   * @param id Parent Id (required)
   * @param name (optional)
   * @param value (optional)
   * @return String
   */
  @RequestLine("POST /api/v1.0/spaces/{id}/properties")
  @Headers({"Accept: */*",})
  String spacesCreateProperty(@Param("id") UUID id, @Param("name") String name,
      @Param("value") String value);

  /**
   * Deletes a space and its children, such as devices, sensors, users, ... Deleting a space will
   * fail if one of these objects belongs in the space tree about to be deleted: - Space resource:
   * These need to be deleted first. - Sensors attached to devices that do **not** belong in the
   * space tree about to be deleted: these (or their parent device) need to be deleted first.
   *
   * @param id Space Id (required)
   */
  @RequestLine("DELETE /api/v1.0/spaces/{id}")
  @Headers({"Accept: */*",})
  void spacesDelete(@Param("id") UUID id);

  /**
   * Deletes a blob Deleting a blob will delete its metadata, its content (all versions) and its
   * associations
   *
   * @param id Blob Id (required)
   */
  @RequestLine("DELETE /api/v1.0/spaces/blobs/{id}")
  @Headers({"Accept: */*",})
  void spacesDeleteBlob(@Param("id") UUID id);

  /**
   * Delete the contents of the given version of the given blob Delete will fail if this version has
   * any associations
   *
   * @param id blob id (required)
   * @param version blob version (required)
   */
  @RequestLine("DELETE /api/v1.0/spaces/blobs/{id}/contents/{version}")
  @Headers({"Accept: */*",})
  void spacesDeleteBlobContents(@Param("id") UUID id, @Param("version") Integer version);

  /**
   * Deletes all property values
   *
   * @param id Parent Id (required)
   */
  @RequestLine("DELETE /api/v1.0/spaces/{id}/properties")
  @Headers({"Accept: */*",})
  void spacesDeleteProperties(@Param("id") UUID id);

  /**
   * Deletes the given property value
   *
   * @param id Parent Id (required)
   * @param name Property name (required)
   */
  @RequestLine("DELETE /api/v1.0/spaces/{id}/properties/{name}")
  @Headers({"Accept: */*",})
  void spacesDeleteProperty(@Param("id") UUID id, @Param("name") String name);

  /**
   * Removes a user from the given space
   *
   * @param id Space Id (required)
   * @param upn The user&#x27;s UPN (required)
   */
  @RequestLine("DELETE /api/v1.0/spaces/{id}/users/{upn}")
  @Headers({"Accept: */*",})
  void spacesDeleteUser(@Param("id") UUID id, @Param("upn") String upn);

  /**
   * Gets the contents of the given version of the given blob
   *
   * @param id blob id (required)
   * @param version blob content version (required)
   * @return File
   */
  @RequestLine("GET /api/v1.0/spaces/blobs/{id}/contents/{version}")
  @Headers({"Accept: */*",})
  File spacesGetBlobContents(@Param("id") UUID id, @Param("version") Integer version);

  /**
   * Gets the contents of the latest version of the given blob
   *
   * @param id blob id (required)
   * @return File
   */
  @RequestLine("GET /api/v1.0/spaces/blobs/{id}/contents/latest")
  @Headers({"Accept: */*",})
  File spacesGetLatestBlobContents(@Param("id") UUID id);

  /**
   * Gets the aggregate of the values of the child sensors of the given type
   *
   * @param id The identifier (required)
   * @param sensorDataTypes Optional &#x27;;&#x27; or &#x27;,&#x27; list of types of values to
   *        retrieve when specifying Values for includes, for example \&quot;Temperature,
   *        Motion\&quot; (optional)
   * @return java.util.List&lt;SensorValue&gt;
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/values?sensorDataTypes={sensorDataTypes}")
  @Headers({"Accept: */*",})
  List<SensorValue> spacesGetValue(@Param("id") UUID id,
      @Param("sensorDataTypes") String sensorDataTypes);

  /**
   * Gets the aggregate of the values of the child sensors of the given type
   *
   * Note, this is equivalent to the other <code>spacesGetValue</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link SpacesGetValueQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param id The identifier (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>sensorDataTypes - Optional &#x27;;&#x27; or &#x27;,&#x27; list of types of values to
   *        retrieve when specifying Values for includes, for example \&quot;Temperature,
   *        Motion\&quot; (optional)</li>
   *        </ul>
   * @return java.util.List&lt;SensorValue&gt;
   *
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/values?sensorDataTypes={sensorDataTypes}")
  @Headers({"Content-Type: */*",})
  List<SensorValue> spacesGetValue(@Param("id") UUID id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>spacesGetValue</code> method
   * in a fluent style.
   */
  public static class SpacesGetValueQueryParams extends HashMap<String, Object> {
    public SpacesGetValueQueryParams sensorDataTypes(final String value) {
      put("sensorDataTypes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Moves the users with provided UPNs from one space to another
   *
   * @param body List of users who need to be re-assigned (required)
   * @param toSpaceId Space Id where the users have to be moved to (required)
   * @param id Space Id where the users are currently associated (required)
   * @param resetLocation If true reset locations not defined in payload (optional)
   * @return Object
   */
  @RequestLine("POST /api/v1.0/spaces/{id}/users/move?toSpaceId={toSpaceId}&resetLocation={resetLocation}")
  @Headers({"Accept: */*",})
  Object spacesMoveUsersToSpace(List<UserCreate> body, @Param("toSpaceId") String toSpaceId,
      @Param("id") UUID id, @Param("resetLocation") Boolean resetLocation);

  /**
   * Moves the users with provided UPNs from one space to another
   *
   * Note, this is equivalent to the other <code>spacesMoveUsersToSpace</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link SpacesMoveUsersToSpaceQueryParams} class that allows for building up this map in a
   * fluent style.
   *
   * @param body List of users who need to be re-assigned (required)
   * @param id Space Id where the users are currently associated (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>toSpaceId - Space Id where the users have to be moved to (required)</li>
   *        <li>resetLocation - If true reset locations not defined in payload (optional)</li>
   *        </ul>
   * @return Object
   *
   */
  @RequestLine("POST /api/v1.0/spaces/{id}/users/move?toSpaceId={toSpaceId}&resetLocation={resetLocation}")
  @Headers({"Content-Type: */*",})
  Object spacesMoveUsersToSpace(List<UserCreate> body, @Param("id") UUID id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>spacesMoveUsersToSpace</code>
   * method in a fluent style.
   */
  public static class SpacesMoveUsersToSpaceQueryParams extends HashMap<String, Object> {
    public SpacesMoveUsersToSpaceQueryParams tospaceId(final UUID value) {
      put("toSpaceId", EncodingUtils.encode(value));
      return this;
    }

    public SpacesMoveUsersToSpaceQueryParams resetLocation(final Boolean value) {
      put("resetLocation", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a list of spaces
   *
   * @param ids Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)
   * @param name Optional name filter (optional)
   * @param types Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of space types to filter
   *        by. (optional)
   * @param subtypes Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of space subtypes to
   *        filter by. (optional)
   * @param statuses Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of space statuses to
   *        filter by. (optional)
   * @param useParentSpace If true, spaceId represents the space&#x27;s parent. Defaults to false.
   *        (optional)
   * @param userUpn Optional filter for spaces associated with the given user (optional)
   * @param sensorDataTypes Optional &#x27;;&#x27; or &#x27;,&#x27; list of types of values to
   *        retrieve when specifying Values for includes, for example \&quot;Temperature,
   *        Motion\&quot; (optional)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;Sensors,Devices\&quot;. Defaults to None (optional)
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
   * @return java.util.List&lt;SpaceRetrieveWithChildren&gt;
   */
  @RequestLine("GET /api/v1.0/spaces?ids={ids}&name={name}&types={types}&subtypes={subtypes}&statuses={statuses}&useParentSpace={useParentSpace}&userUpn={userUpn}&sensorDataTypes={sensorDataTypes}&includes={includes}&propertyKey={propertyKey}&propertyValue={propertyValue}&propertyValueSearchType={propertyValueSearchType}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  List<SpaceRetrieveWithChildren> spacesRetrieve(@Param("ids") UUID ids,
      @Param("name") String name, @Param("types") String types, @Param("subtypes") String subtypes,
      @Param("statuses") String statuses, @Param("useParentSpace") Boolean useParentSpace,
      @Param("userUpn") String userUpn, @Param("sensorDataTypes") String sensorDataTypes,
      @Param("includes") String includes, @Param("propertyKey") String propertyKey,
      @Param("propertyValue") String propertyValue,
      @Param("propertyValueSearchType") String propertyValueSearchType,
      @Param("spaceId") String spaceId, @Param("traverse") String traverse,
      @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel,
      @Param("minRelative") Boolean minRelative, @Param("maxRelative") Boolean maxRelative);

  /**
   * Gets a list of spaces
   *
   * Note, this is equivalent to the other <code>spacesRetrieve</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link SpacesRetrieveQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>ids - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)</li>
   *        <li>name - Optional name filter (optional)</li>
   *        <li>types - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of space types to
   *        filter by. (optional)</li>
   *        <li>subtypes - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of space subtypes
   *        to filter by. (optional)</li>
   *        <li>statuses - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of space statuses
   *        to filter by. (optional)</li>
   *        <li>useParentSpace - If true, spaceId represents the space&#x27;s parent. Defaults to
   *        false. (optional)</li>
   *        <li>userUpn - Optional filter for spaces associated with the given user (optional)</li>
   *        <li>sensorDataTypes - Optional &#x27;;&#x27; or &#x27;,&#x27; list of types of values to
   *        retrieve when specifying Values for includes, for example \&quot;Temperature,
   *        Motion\&quot; (optional)</li>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;Sensors,Devices\&quot;. Defaults to None (optional)</li>
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
   * @return java.util.List&lt;SpaceRetrieveWithChildren&gt;
   *
   */
  @RequestLine("GET /api/v1.0/spaces?ids={ids}&name={name}&types={types}&subtypes={subtypes}&statuses={statuses}&useParentSpace={useParentSpace}&userUpn={userUpn}&sensorDataTypes={sensorDataTypes}&includes={includes}&propertyKey={propertyKey}&propertyValue={propertyValue}&propertyValueSearchType={propertyValueSearchType}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  List<SpaceRetrieveWithChildren> spacesRetrieve(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>spacesRetrieve</code> method
   * in a fluent style.
   */
  public static class SpacesRetrieveQueryParams extends HashMap<String, Object> {
    public SpacesRetrieveQueryParams ids(final UUID value) {
      put("ids", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams name(final String value) {
      put("name", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams types(final String value) {
      put("types", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams subtypes(final String value) {
      put("subtypes", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams statuses(final String value) {
      put("statuses", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams useParentSpace(final Boolean value) {
      put("useParentSpace", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams userUpn(final String value) {
      put("userUpn", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams sensorDataTypes(final String value) {
      put("sensorDataTypes", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams propertyKey(final String value) {
      put("propertyKey", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams propertyValue(final String value) {
      put("propertyValue", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams propertyValueSearchType(final String value) {
      put("propertyValueSearchType", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams spaceId(final UUID value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveQueryParams maxRelative(final Boolean value) {
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
  @RequestLine("GET /api/v1.0/spaces/blobs?names={names}&ids={ids}&sharings={sharings}&types={types}&subtypes={subtypes}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  List<BlobMetadataRetrieve> spacesRetrieveBlobMetadata(@Param("names") String names,
      @Param("ids") UUID ids, @Param("sharings") String sharings, @Param("types") String types,
      @Param("subtypes") String subtypes, @Param("includes") String includes,
      @Param("spaceId") String spaceId, @Param("traverse") String traverse,
      @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel,
      @Param("minRelative") Boolean minRelative, @Param("maxRelative") Boolean maxRelative);

  /**
   * Gets a list of blobs
   *
   * Note, this is equivalent to the other <code>spacesRetrieveBlobMetadata</code> method, but with
   * the query parameters collected into a single Map parameter. This is convenient for services
   * with optional query parameters, especially when used with the
   * {@link SpacesRetrieveBlobMetadataQueryParams} class that allows for building up this map in a
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
  @RequestLine("GET /api/v1.0/spaces/blobs?names={names}&ids={ids}&sharings={sharings}&types={types}&subtypes={subtypes}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  List<BlobMetadataRetrieve> spacesRetrieveBlobMetadata(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>spacesRetrieveBlobMetadata</code> method in a fluent style.
   */
  public static class SpacesRetrieveBlobMetadataQueryParams extends HashMap<String, Object> {
    public SpacesRetrieveBlobMetadataQueryParams names(final String value) {
      put("names", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveBlobMetadataQueryParams ids(final UUID value) {
      put("ids", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveBlobMetadataQueryParams sharings(final String value) {
      put("sharings", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveBlobMetadataQueryParams types(final String value) {
      put("types", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveBlobMetadataQueryParams subtypes(final String value) {
      put("subtypes", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveBlobMetadataQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveBlobMetadataQueryParams spaceId(final UUID value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveBlobMetadataQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveBlobMetadataQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveBlobMetadataQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveBlobMetadataQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveBlobMetadataQueryParams maxRelative(final Boolean value) {
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
  @RequestLine("GET /api/v1.0/spaces/blobs/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  BlobMetadataRetrieve spacesRetrieveBlobMetadataById(@Param("id") UUID id,
      @Param("includes") String includes);

  /**
   * Gets a blob
   *
   * Note, this is equivalent to the other <code>spacesRetrieveBlobMetadataById</code> method, but
   * with the query parameters collected into a single Map parameter. This is convenient for
   * services with optional query parameters, especially when used with the
   * {@link SpacesRetrieveBlobMetadataByIdQueryParams} class that allows for building up this map in
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
  @RequestLine("GET /api/v1.0/spaces/blobs/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  BlobMetadataRetrieve spacesRetrieveBlobMetadataById(@Param("id") UUID id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>spacesRetrieveBlobMetadataById</code> method in a fluent style.
   */
  public static class SpacesRetrieveBlobMetadataByIdQueryParams extends HashMap<String, Object> {
    public SpacesRetrieveBlobMetadataByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a specific space
   *
   * @param id Space Id (required)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;Sensors,Devices\&quot;. Defaults to None (optional)
   * @param sensorDataTypes Optional &#x27;;&#x27; or &#x27;,&#x27; list of types of values to
   *        retrieve when specifying Values for includes, for example \&quot;Temperature,
   *        Motion\&quot; (optional)
   * @return SpaceRetrieveWithChildren
   */
  @RequestLine("GET /api/v1.0/spaces/{id}?includes={includes}&sensorDataTypes={sensorDataTypes}")
  @Headers({"Accept: */*",})
  SpaceRetrieveWithChildren spacesRetrieveById(@Param("id") UUID id,
      @Param("includes") String includes, @Param("sensorDataTypes") String sensorDataTypes);

  /**
   * Gets a specific space
   *
   * Note, this is equivalent to the other <code>spacesRetrieveById</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the {@link SpacesRetrieveByIdQueryParams}
   * class that allows for building up this map in a fluent style.
   *
   * @param id Space Id (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;Sensors,Devices\&quot;. Defaults to None (optional)</li>
   *        <li>sensorDataTypes - Optional &#x27;;&#x27; or &#x27;,&#x27; list of types of values to
   *        retrieve when specifying Values for includes, for example \&quot;Temperature,
   *        Motion\&quot; (optional)</li>
   *        </ul>
   * @return SpaceRetrieveWithChildren
   *
   */
  @RequestLine("GET /api/v1.0/spaces/{id}?includes={includes}&sensorDataTypes={sensorDataTypes}")
  @Headers({"Content-Type: */*",})
  SpaceRetrieveWithChildren spacesRetrieveById(@Param("id") UUID id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>spacesRetrieveById</code>
   * method in a fluent style.
   */
  public static class SpacesRetrieveByIdQueryParams extends HashMap<String, Object> {
    public SpacesRetrieveByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveByIdQueryParams sensorDataTypes(final String value) {
      put("sensorDataTypes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets the first keystore by walking up the spaces hierarchy
   *
   * @param id Space Id (required)
   * @param includes Comma separated list of what to include, for example \&quot;Space,Keys\&quot;.
   *        Defaults to None (optional)
   * @return KeyStoreRetrieve
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/keystore?includes={includes}")
  @Headers({"Accept: */*",})
  KeyStoreRetrieve spacesRetrieveKeyStore(@Param("id") UUID id,
      @Param("includes") String includes);

  /**
   * Gets the first keystore by walking up the spaces hierarchy
   *
   * Note, this is equivalent to the other <code>spacesRetrieveKeyStore</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link SpacesRetrieveKeyStoreQueryParams} class that allows for building up this map in a
   * fluent style.
   *
   * @param id Space Id (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;Space,Keys\&quot;. Defaults to None (optional)</li>
   *        </ul>
   * @return KeyStoreRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/keystore?includes={includes}")
  @Headers({"Content-Type: */*",})
  KeyStoreRetrieve spacesRetrieveKeyStore(@Param("id") UUID id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>spacesRetrieveKeyStore</code>
   * method in a fluent style.
   */
  public static class SpacesRetrieveKeyStoreQueryParams extends HashMap<String, Object> {
    public SpacesRetrieveKeyStoreQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets the first space of the given type by walking up the spaces hierarchy
   *
   * @param id Space Id (required)
   * @param spaceType The parent space type (required)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;Sensors,Devices\&quot;. Defaults to None (optional)
   * @param sensorDataTypes Optional &#x27;;&#x27; or &#x27;,&#x27; list of types of values to
   *        retrieve when specifying Values for includes, for example \&quot;Temperature,
   *        Motion\&quot; (optional)
   * @return SpaceRetrieveWithChildren
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/parent?spaceType={spaceType}&includes={includes}&sensorDataTypes={sensorDataTypes}")
  @Headers({"Accept: */*",})
  SpaceRetrieveWithChildren spacesRetrieveParent(@Param("id") UUID id,
      @Param("spaceType") String spaceType, @Param("includes") String includes,
      @Param("sensorDataTypes") String sensorDataTypes);

  /**
   * Gets the first space of the given type by walking up the spaces hierarchy
   *
   * Note, this is equivalent to the other <code>spacesRetrieveParent</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link SpacesRetrieveParentQueryParams} class that allows for building up this map in a fluent
   * style.
   *
   * @param id Space Id (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>spaceType - The parent space type (required)</li>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;Sensors,Devices\&quot;. Defaults to None (optional)</li>
   *        <li>sensorDataTypes - Optional &#x27;;&#x27; or &#x27;,&#x27; list of types of values to
   *        retrieve when specifying Values for includes, for example \&quot;Temperature,
   *        Motion\&quot; (optional)</li>
   *        </ul>
   * @return SpaceRetrieveWithChildren
   *
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/parent?spaceType={spaceType}&includes={includes}&sensorDataTypes={sensorDataTypes}")
  @Headers({"Content-Type: */*",})
  SpaceRetrieveWithChildren spacesRetrieveParent(@Param("id") UUID id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>spacesRetrieveParent</code>
   * method in a fluent style.
   */
  public static class SpacesRetrieveParentQueryParams extends HashMap<String, Object> {
    public SpacesRetrieveParentQueryParams spaceType(final String value) {
      put("spaceType", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveParentQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveParentQueryParams sensorDataTypes(final String value) {
      put("sensorDataTypes", EncodingUtils.encode(value));
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
  @RequestLine("GET /api/v1.0/spaces/{id}/properties/{name}")
  @Headers({"Accept: */*",})
  String spacesRetrieveProperty(@Param("id") UUID id, @Param("name") String name);

  /**
   * Gets the first resource of the given type by walking up the spaces hierarchy
   *
   * @param id The space id (required)
   * @param type The resource type (required)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;Space,DependentChildren\&quot;. Defaults to None (optional)
   * @return SpaceResourceRetrieve
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/resources/{type}?includes={includes}")
  @Headers({"Accept: */*",})
  SpaceResourceRetrieve spacesRetrieveResource(@Param("id") UUID id, @Param("type") String type,
      @Param("includes") String includes);

  /**
   * Gets the first resource of the given type by walking up the spaces hierarchy
   *
   * Note, this is equivalent to the other <code>spacesRetrieveResource</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link SpacesRetrieveResourceQueryParams} class that allows for building up this map in a
   * fluent style.
   *
   * @param id The space id (required)
   * @param type The resource type (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;Space,DependentChildren\&quot;. Defaults to None (optional)</li>
   *        </ul>
   * @return SpaceResourceRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/resources/{type}?includes={includes}")
  @Headers({"Content-Type: */*",})
  SpaceResourceRetrieve spacesRetrieveResource(@Param("id") UUID id, @Param("type") String type,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>spacesRetrieveResource</code>
   * method in a fluent style.
   */
  public static class SpacesRetrieveResourceQueryParams extends HashMap<String, Object> {
    public SpacesRetrieveResourceQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Get a user in the given space
   *
   * @param id Space Id (required)
   * @param upn The user&#x27;s UPN (required)
   * @param includes Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None (optional)
   * @return UserRetrieve
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/users/{upn}?includes={includes}")
  @Headers({"Accept: */*",})
  UserRetrieve spacesRetrieveUserByUpn(@Param("id") UUID id, @Param("upn") String upn,
      @Param("includes") String includes);

  /**
   * Get a user in the given space
   *
   * Note, this is equivalent to the other <code>spacesRetrieveUserByUpn</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link SpacesRetrieveUserByUpnQueryParams} class that allows for building up this map in a
   * fluent style.
   *
   * @param id Space Id (required)
   * @param upn The user&#x27;s UPN (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None (optional)</li>
   *        </ul>
   * @return UserRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/users/{upn}?includes={includes}")
  @Headers({"Content-Type: */*",})
  UserRetrieve spacesRetrieveUserByUpn(@Param("id") UUID id, @Param("upn") String upn,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>spacesRetrieveUserByUpn</code> method in a fluent style.
   */
  public static class SpacesRetrieveUserByUpnQueryParams extends HashMap<String, Object> {
    public SpacesRetrieveUserByUpnQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets the list of users associated with the given space
   *
   * @param id Space Id (required)
   * @param traverse Optionally specify if you want to recursively include children (Down) or
   *        ancestors (Up) of the specified space Id (optional)
   * @param unmapped Optionally filter on mapped or unmapped users (optional)
   * @param firstName Optional filter on first name (optional)
   * @param lastName Optional filter on last name (optional)
   * @param includes Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None (optional)
   * @param propertyKey Optional filter on objects that have the given property key defined
   *        (optional)
   * @param propertyValue Optional filter on the value of the given property key (optional)
   * @param propertyValueSearchType Optional type of search on property value. Contains or Equals.
   *        Defaults to Equals (optional)
   * @return java.util.List&lt;UserRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/users?traverse={traverse}&unmapped={unmapped}&firstName={firstName}&lastName={lastName}&includes={includes}&propertyKey={propertyKey}&propertyValue={propertyValue}&propertyValueSearchType={propertyValueSearchType}")
  @Headers({"Accept: */*",})
  List<UserRetrieve> spacesRetrieveUsers(@Param("id") UUID id, @Param("traverse") String traverse,
      @Param("unmapped") Boolean unmapped, @Param("firstName") String firstName,
      @Param("lastName") String lastName, @Param("includes") String includes,
      @Param("propertyKey") String propertyKey, @Param("propertyValue") String propertyValue,
      @Param("propertyValueSearchType") String propertyValueSearchType);

  /**
   * Gets the list of users associated with the given space
   *
   * Note, this is equivalent to the other <code>spacesRetrieveUsers</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the {@link SpacesRetrieveUsersQueryParams}
   * class that allows for building up this map in a fluent style.
   *
   * @param id Space Id (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>traverse - Optionally specify if you want to recursively include children (Down) or
   *        ancestors (Up) of the specified space Id (optional)</li>
   *        <li>unmapped - Optionally filter on mapped or unmapped users (optional)</li>
   *        <li>firstName - Optional filter on first name (optional)</li>
   *        <li>lastName - Optional filter on last name (optional)</li>
   *        <li>includes - Comma separated list of what to include, for example \&quot;Space\&quot;.
   *        Defaults to None (optional)</li>
   *        <li>propertyKey - Optional filter on objects that have the given property key defined
   *        (optional)</li>
   *        <li>propertyValue - Optional filter on the value of the given property key
   *        (optional)</li>
   *        <li>propertyValueSearchType - Optional type of search on property value. Contains or
   *        Equals. Defaults to Equals (optional)</li>
   *        </ul>
   * @return java.util.List&lt;UserRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/spaces/{id}/users?traverse={traverse}&unmapped={unmapped}&firstName={firstName}&lastName={lastName}&includes={includes}&propertyKey={propertyKey}&propertyValue={propertyValue}&propertyValueSearchType={propertyValueSearchType}")
  @Headers({"Content-Type: */*",})
  List<UserRetrieve> spacesRetrieveUsers(@Param("id") UUID id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>spacesRetrieveUsers</code>
   * method in a fluent style.
   */
  public static class SpacesRetrieveUsersQueryParams extends HashMap<String, Object> {
    public SpacesRetrieveUsersQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveUsersQueryParams unmapped(final Boolean value) {
      put("unmapped", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveUsersQueryParams firstName(final String value) {
      put("firstName", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveUsersQueryParams lastName(final String value) {
      put("lastName", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveUsersQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveUsersQueryParams propertyKey(final String value) {
      put("propertyKey", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveUsersQueryParams propertyValue(final String value) {
      put("propertyValue", EncodingUtils.encode(value));
      return this;
    }

    public SpacesRetrieveUsersQueryParams propertyValueSearchType(final String value) {
      put("propertyValueSearchType", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Updates a space
   *
   * @param body The space information (required)
   * @param id Space Id (required)
   */
  @RequestLine("PATCH /api/v1.0/spaces/{id}")
  @Headers({"Accept: */*",})
  void spacesUpdate(SpaceUpdate body, @Param("id") UUID id);

  /**
   * Updates a space
   *
   * @param id Space Id (required)
   * @param name (optional)
   * @param description (optional)
   * @param friendlyName (optional)
   * @param type (optional)
   * @param typeId (optional)
   * @param parentSpaceId (optional)
   * @param subtype (optional)
   * @param subtypeId (optional)
   * @param location (optional)
   * @param timeZoneId (optional)
   * @param timeZoneName (optional)
   * @param status (optional)
   * @param statusId (optional)
   */
  @RequestLine("PATCH /api/v1.0/spaces/{id}")
  @Headers({"Accept: */*",})
  void spacesUpdate(@Param("id") UUID id, @Param("name") String name,
      @Param("description") String description, @Param("friendlyName") String friendlyName,
      @Param("type") String type, @Param("typeId") Integer typeId,
      @Param("parentSpaceId") UUID parentSpaceId, @Param("subtype") String subtype,
      @Param("subtypeId") Integer subtypeId, @Param("location") Location location,
      @Param("timeZoneId") Integer timeZoneId, @Param("timeZoneName") String timeZoneName,
      @Param("status") String status, @Param("statusId") Integer statusId);

  /**
   * Updates a blob This is a multi-part request. For more information, see sample app or doc
   * examples.
   *
   * @param id blob Id (required)
   * @param metadata (optional)
   * @param contents (optional)
   */
  @RequestLine("PATCH /api/v1.0/spaces/blobs/{id}")
  @Headers({"Accept: */*",})
  void spacesUpdateBlob(@Param("id") UUID id, @Param("metadata") String metadata,
      @Param("contents") File contents);

  /**
   * Creates or updates property values
   *
   * @param body The properties (required)
   * @param id Parent Id (required)
   * @return Object
   */
  @RequestLine("PUT /api/v1.0/spaces/{id}/properties")
  @Headers({"Accept: */*",})
  Object spacesUpdateProperties(List<ExtendedPropertyCreate> body, @Param("id") UUID id);
}
