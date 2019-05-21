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
import com.microsoft.twins.model.DeviceCreate;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.DeviceUpdate;
import com.microsoft.twins.model.ExtendedPropertyCreate;
import com.microsoft.twins.model.KeyStoreRetrieve;
import com.microsoft.twins.model.Location;
import com.microsoft.twins.model.SensorCreateNoParent;
import com.microsoft.twins.model.SpaceResourceRetrieve;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface DevicesApi extends TwinsApiClient.Api {
  /**
   * Creates a device
   *
   * @param body The device information (required)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/devices")
  @Headers({"Accept: */*",})
  UUID devicesCreate(DeviceCreate body);

  /**
   * Creates a device
   *
   * @param name (optional)
   * @param friendlyName (optional)
   * @param description (optional)
   * @param type (optional)
   * @param typeId (optional)
   * @param subtype (optional)
   * @param subtypeId (optional)
   * @param hardwareId (optional)
   * @param gatewayId (optional)
   * @param spaceId (optional)
   * @param status (optional)
   * @param location (optional)
   * @param sensors (optional)
   * @param createIoTHubDevice (optional)
   * @param parentDeviceType (optional)
   * @param parentDeviceSubtype (optional)
   * @param properties (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/devices")
  @Headers({"Accept: */*",})
  UUID devicesCreate(@Param("name") String name, @Param("friendlyName") String friendlyName,
      @Param("description") String description, @Param("type") String type,
      @Param("typeId") Integer typeId, @Param("subtype") String subtype,
      @Param("subtypeId") Integer subtypeId, @Param("hardwareId") String hardwareId,
      @Param("gatewayId") String gatewayId, @Param("spaceId") UUID spaceId,
      @Param("status") String status, @Param("location") Location location,
      @Param("sensors") java.util.List<SensorCreateNoParent> sensors,
      @Param("createIoTHubDevice") Boolean createIoTHubDevice,
      @Param("parentDeviceType") String parentDeviceType,
      @Param("parentDeviceSubtype") String parentDeviceSubtype,
      @Param("properties") java.util.List<ExtendedPropertyCreate> properties);

  /**
   * Creates a blob This is a multi-part request. For more information, see sample app or doc
   * examples. Key value pairs specified in the Content-Disposition header in the blob-chunk of the
   * multipart request will be preserved as meta-data on the stored blob.
   *
   * @param metadata (optional)
   * @param contents (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/devices/blobs")
  @Headers({"Accept: */*",})
  UUID devicesCreateBlob(@Param("metadata") String metadata, @Param("contents") File contents);

  /**
   * Creates or updates a device using Name and SpaceId as the unique key
   *
   * @param body The device information (required)
   * @return UUID
   */
  @RequestLine("PUT /api/v1.0/devices")
  @Headers({"Accept: */*",})
  UUID devicesCreateOrUpdate(DeviceCreate body);

  /**
   * Creates or updates a device using Name and SpaceId as the unique key
   *
   * @param name (optional)
   * @param friendlyName (optional)
   * @param description (optional)
   * @param type (optional)
   * @param typeId (optional)
   * @param subtype (optional)
   * @param subtypeId (optional)
   * @param hardwareId (optional)
   * @param gatewayId (optional)
   * @param spaceId (optional)
   * @param status (optional)
   * @param location (optional)
   * @param sensors (optional)
   * @param createIoTHubDevice (optional)
   * @param parentDeviceType (optional)
   * @param parentDeviceSubtype (optional)
   * @param properties (optional)
   * @return UUID
   */
  @RequestLine("PUT /api/v1.0/devices")
  @Headers({"Accept: */*",})
  UUID devicesCreateOrUpdate(@Param("name") String name, @Param("friendlyName") String friendlyName,
      @Param("description") String description, @Param("type") String type,
      @Param("typeId") Integer typeId, @Param("subtype") String subtype,
      @Param("subtypeId") Integer subtypeId, @Param("hardwareId") String hardwareId,
      @Param("gatewayId") String gatewayId, @Param("spaceId") UUID spaceId,
      @Param("status") String status, @Param("location") Location location,
      @Param("sensors") java.util.List<SensorCreateNoParent> sensors,
      @Param("createIoTHubDevice") Boolean createIoTHubDevice,
      @Param("parentDeviceType") String parentDeviceType,
      @Param("parentDeviceSubtype") String parentDeviceSubtype,
      @Param("properties") java.util.List<ExtendedPropertyCreate> properties);

  /**
   * Creates a property value
   *
   * @param body Extended property data (required)
   * @param id Parent Id (required)
   * @return String
   */
  @RequestLine("POST /api/v1.0/devices/{id}/properties")
  @Headers({"Accept: */*",})
  String devicesCreateProperty(ExtendedPropertyCreate body, @Param("id") String id);

  /**
   * Creates a property value
   *
   * @param id Parent Id (required)
   * @param name (optional)
   * @param value (optional)
   * @return String
   */
  @RequestLine("POST /api/v1.0/devices/{id}/properties")
  @Headers({"Accept: */*",})
  String devicesCreateProperty(@Param("id") String id, @Param("name") String name,
      @Param("value") String value);

  /**
   * Deletes a device and its children such as sensors, blobs, ...
   *
   * @param id Device Id (required)
   */
  @RequestLine("DELETE /api/v1.0/devices/{id}")
  @Headers({"Accept: */*",})
  void devicesDelete(@Param("id") String id);

  /**
   * Deletes a blob Deleting a blob will delete its metadata, its content (all versions) and its
   * associations
   *
   * @param id Blob Id (required)
   */
  @RequestLine("DELETE /api/v1.0/devices/blobs/{id}")
  @Headers({"Accept: */*",})
  void devicesDeleteBlob(@Param("id") String id);

  /**
   * Delete the contents of the given version of the given blob Delete will fail if this version has
   * any associations
   *
   * @param id blob id (required)
   * @param version blob version (required)
   */
  @RequestLine("DELETE /api/v1.0/devices/blobs/{id}/contents/{version}")
  @Headers({"Accept: */*",})
  void devicesDeleteBlobContents(@Param("id") String id, @Param("version") Integer version);

  /**
   * Deletes all property values
   *
   * @param id Parent Id (required)
   */
  @RequestLine("DELETE /api/v1.0/devices/{id}/properties")
  @Headers({"Accept: */*",})
  void devicesDeleteProperties(@Param("id") String id);

  /**
   * Deletes the given property value
   *
   * @param id Parent Id (required)
   * @param name Property name (required)
   */
  @RequestLine("DELETE /api/v1.0/devices/{id}/properties/{name}")
  @Headers({"Accept: */*",})
  void devicesDeleteProperty(@Param("id") String id, @Param("name") String name);

  /**
   * Gets the contents of the given version of the given blob
   *
   * @param id blob id (required)
   * @param version blob content version (required)
   * @return File
   */
  @RequestLine("GET /api/v1.0/devices/blobs/{id}/contents/{version}")
  @Headers({"Accept: */*",})
  File devicesGetBlobContents(@Param("id") String id, @Param("version") Integer version);

  /**
   * Gets the contents of the latest version of the given blob
   *
   * @param id blob id (required)
   * @return File
   */
  @RequestLine("GET /api/v1.0/devices/blobs/{id}/contents/latest")
  @Headers({"Accept: */*",})
  File devicesGetLatestBlobContents(@Param("id") String id);

  /**
   * Gets the first resource of the given type by walking up the spaces hierarchy
   *
   * @param id Device Id (required)
   * @param type The resource type (required)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;Space,DependentChildren\&quot;. Defaults to None (optional)
   * @return SpaceResourceRetrieve
   */
  @RequestLine("GET /api/v1.0/devices/{id}/resources/{type}?includes={includes}")
  @Headers({"Accept: */*",})
  SpaceResourceRetrieve devicesGetResource(@Param("id") String id, @Param("type") String type,
      @Param("includes") String includes);

  /**
   * Gets the first resource of the given type by walking up the spaces hierarchy
   *
   * Note, this is equivalent to the other <code>devicesGetResource</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the {@link DevicesGetResourceQueryParams}
   * class that allows for building up this map in a fluent style.
   *
   * @param id Device Id (required)
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
  @RequestLine("GET /api/v1.0/devices/{id}/resources/{type}?includes={includes}")
  @Headers({"Content-Type: */*",})
  SpaceResourceRetrieve devicesGetResource(@Param("id") String id, @Param("type") String type,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>devicesGetResource</code>
   * method in a fluent style.
   */
  public static class DevicesGetResourceQueryParams extends HashMap<String, Object> {
    public DevicesGetResourceQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Registers a device to its parent space&#x27;s IoT Hub resource
   *
   * @param id Device Id (required)
   */
  @RequestLine("PATCH /api/v1.0/devices/{id}/register")
  @Headers({"Accept: */*",})
  void devicesRegister(@Param("id") String id);

  /**
   * Gets a list of devices
   *
   * @param ids Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)
   * @param hardwareIds Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of hardware ids (such
   *        as MAC addresses) to filter by (optional)
   * @param names Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of names to filter by
   *        (optional)
   * @param types Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of device types to filter
   *        by. (optional)
   * @param subtypes Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of device subtypes to
   *        filter by. (optional)
   * @param gateways Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of gateway ids to filter
   *        by (optional)
   * @param status Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of status to filter by.
   *        E.g: status&#x3D;Disabled,Offline (optional)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;Sensors,SasToken\&quot;. Defaults to None (optional)
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
   * @return java.util.List&lt;DeviceRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/devices?ids={ids}&hardwareIds={hardwareIds}&names={names}&types={types}&subtypes={subtypes}&gateways={gateways}&status={status}&includes={includes}&propertyKey={propertyKey}&propertyValue={propertyValue}&propertyValueSearchType={propertyValueSearchType}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  java.util.List<DeviceRetrieve> devicesRetrieve(@Param("ids") String ids,
      @Param("hardwareIds") String hardwareIds, @Param("names") String names,
      @Param("types") String types, @Param("subtypes") String subtypes,
      @Param("gateways") String gateways, @Param("status") String status,
      @Param("includes") String includes, @Param("propertyKey") String propertyKey,
      @Param("propertyValue") String propertyValue,
      @Param("propertyValueSearchType") String propertyValueSearchType,
      @Param("spaceId") String spaceId, @Param("traverse") String traverse,
      @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel,
      @Param("minRelative") Boolean minRelative, @Param("maxRelative") Boolean maxRelative);

  /**
   * Gets a list of devices
   *
   * Note, this is equivalent to the other <code>devicesRetrieve</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link DevicesRetrieveQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>ids - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)</li>
   *        <li>hardwareIds - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of hardware ids
   *        (such as MAC addresses) to filter by (optional)</li>
   *        <li>names - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of names to filter by
   *        (optional)</li>
   *        <li>types - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of device types to
   *        filter by. (optional)</li>
   *        <li>subtypes - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of device subtypes
   *        to filter by. (optional)</li>
   *        <li>gateways - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of gateway ids to
   *        filter by (optional)</li>
   *        <li>status - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of status to filter
   *        by. E.g: status&#x3D;Disabled,Offline (optional)</li>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;Sensors,SasToken\&quot;. Defaults to None (optional)</li>
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
   * @return java.util.List&lt;DeviceRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/devices?ids={ids}&hardwareIds={hardwareIds}&names={names}&types={types}&subtypes={subtypes}&gateways={gateways}&status={status}&includes={includes}&propertyKey={propertyKey}&propertyValue={propertyValue}&propertyValueSearchType={propertyValueSearchType}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  java.util.List<DeviceRetrieve> devicesRetrieve(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>devicesRetrieve</code> method
   * in a fluent style.
   */
  public static class DevicesRetrieveQueryParams extends HashMap<String, Object> {
    public DevicesRetrieveQueryParams ids(final String value) {
      put("ids", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams hardwareIds(final String value) {
      put("hardwareIds", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams names(final String value) {
      put("names", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams types(final String value) {
      put("types", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams subtypes(final String value) {
      put("subtypes", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams gateways(final String value) {
      put("gateways", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams status(final String value) {
      put("status", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams propertyKey(final String value) {
      put("propertyKey", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams propertyValue(final String value) {
      put("propertyValue", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams propertyValueSearchType(final String value) {
      put("propertyValueSearchType", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams spaceId(final String value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveQueryParams maxRelative(final Boolean value) {
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
  @RequestLine("GET /api/v1.0/devices/blobs?names={names}&ids={ids}&sharings={sharings}&types={types}&subtypes={subtypes}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  java.util.List<BlobMetadataRetrieve> devicesRetrieveBlobMetadata(@Param("names") String names,
      @Param("ids") String ids, @Param("sharings") String sharings, @Param("types") String types,
      @Param("subtypes") String subtypes, @Param("includes") String includes,
      @Param("spaceId") String spaceId, @Param("traverse") String traverse,
      @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel,
      @Param("minRelative") Boolean minRelative, @Param("maxRelative") Boolean maxRelative);

  /**
   * Gets a list of blobs
   *
   * Note, this is equivalent to the other <code>devicesRetrieveBlobMetadata</code> method, but with
   * the query parameters collected into a single Map parameter. This is convenient for services
   * with optional query parameters, especially when used with the
   * {@link DevicesRetrieveBlobMetadataQueryParams} class that allows for building up this map in a
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
  @RequestLine("GET /api/v1.0/devices/blobs?names={names}&ids={ids}&sharings={sharings}&types={types}&subtypes={subtypes}&includes={includes}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  java.util.List<BlobMetadataRetrieve> devicesRetrieveBlobMetadata(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>devicesRetrieveBlobMetadata</code> method in a fluent style.
   */
  public static class DevicesRetrieveBlobMetadataQueryParams extends HashMap<String, Object> {
    public DevicesRetrieveBlobMetadataQueryParams names(final String value) {
      put("names", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveBlobMetadataQueryParams ids(final String value) {
      put("ids", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveBlobMetadataQueryParams sharings(final String value) {
      put("sharings", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveBlobMetadataQueryParams types(final String value) {
      put("types", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveBlobMetadataQueryParams subtypes(final String value) {
      put("subtypes", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveBlobMetadataQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveBlobMetadataQueryParams spaceId(final String value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveBlobMetadataQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveBlobMetadataQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveBlobMetadataQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveBlobMetadataQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public DevicesRetrieveBlobMetadataQueryParams maxRelative(final Boolean value) {
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
  @RequestLine("GET /api/v1.0/devices/blobs/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  BlobMetadataRetrieve devicesRetrieveBlobMetadataById(@Param("id") String id,
      @Param("includes") String includes);

  /**
   * Gets a blob
   *
   * Note, this is equivalent to the other <code>devicesRetrieveBlobMetadataById</code> method, but
   * with the query parameters collected into a single Map parameter. This is convenient for
   * services with optional query parameters, especially when used with the
   * {@link DevicesRetrieveBlobMetadataByIdQueryParams} class that allows for building up this map
   * in a fluent style.
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
  @RequestLine("GET /api/v1.0/devices/blobs/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  BlobMetadataRetrieve devicesRetrieveBlobMetadataById(@Param("id") String id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>devicesRetrieveBlobMetadataById</code> method in a fluent style.
   */
  public static class DevicesRetrieveBlobMetadataByIdQueryParams extends HashMap<String, Object> {
    public DevicesRetrieveBlobMetadataByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a specific device
   *
   * @param id Device Id (required)
   * @param includes Comma separated list of what to include, for example
   *        \&quot;Sensors,SasToken\&quot;. Defaults to None (optional)
   * @return DeviceRetrieve
   */
  @RequestLine("GET /api/v1.0/devices/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  DeviceRetrieve devicesRetrieveById(@Param("id") String id, @Param("includes") String includes);

  /**
   * Gets a specific device
   *
   * Note, this is equivalent to the other <code>devicesRetrieveById</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the {@link DevicesRetrieveByIdQueryParams}
   * class that allows for building up this map in a fluent style.
   *
   * @param id Device Id (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;Sensors,SasToken\&quot;. Defaults to None (optional)</li>
   *        </ul>
   * @return DeviceRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/devices/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  DeviceRetrieve devicesRetrieveById(@Param("id") String id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>devicesRetrieveById</code>
   * method in a fluent style.
   */
  public static class DevicesRetrieveByIdQueryParams extends HashMap<String, Object> {
    public DevicesRetrieveByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets the first keystore by walking up the spaces hierarchy
   *
   * @param id Device Id (required)
   * @param includes Comma separated list of what to include, for example \&quot;Space,Keys\&quot;.
   *        Defaults to None (optional)
   * @return KeyStoreRetrieve
   */
  @RequestLine("GET /api/v1.0/devices/{id}/keystore?includes={includes}")
  @Headers({"Accept: */*",})
  KeyStoreRetrieve devicesRetrieveKeyStore(@Param("id") String id,
      @Param("includes") String includes);

  /**
   * Gets the first keystore by walking up the spaces hierarchy
   *
   * Note, this is equivalent to the other <code>devicesRetrieveKeyStore</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link DevicesRetrieveKeyStoreQueryParams} class that allows for building up this map in a
   * fluent style.
   *
   * @param id Device Id (required)
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
  @RequestLine("GET /api/v1.0/devices/{id}/keystore?includes={includes}")
  @Headers({"Content-Type: */*",})
  KeyStoreRetrieve devicesRetrieveKeyStore(@Param("id") String id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>devicesRetrieveKeyStore</code> method in a fluent style.
   */
  public static class DevicesRetrieveKeyStoreQueryParams extends HashMap<String, Object> {
    public DevicesRetrieveKeyStoreQueryParams includes(final String value) {
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
  @RequestLine("GET /api/v1.0/devices/{id}/properties/{name}")
  @Headers({"Accept: */*",})
  String devicesRetrieveProperty(@Param("id") String id, @Param("name") String name);

  /**
   * Unregisters a device from its parent space&#x27;s IoT Hub resource
   *
   * @param id Device Id (required)
   */
  @RequestLine("PATCH /api/v1.0/devices/{id}/unregister")
  @Headers({"Accept: */*",})
  void devicesUnregister(@Param("id") String id);

  /**
   * Updates a device
   *
   * @param body The device information (required)
   * @param id Device Id (required)
   */
  @RequestLine("PATCH /api/v1.0/devices/{id}")
  @Headers({"Accept: */*",})
  void devicesUpdate(DeviceUpdate body, @Param("id") String id);

  /**
   * Updates a device
   *
   * @param id Device Id (required)
   * @param name (optional)
   * @param friendlyName (optional)
   * @param description (optional)
   * @param hardwareId (optional)
   * @param gatewayId (optional)
   * @param spaceId (optional)
   * @param status (optional)
   * @param location (optional)
   * @param type (optional)
   * @param typeId (optional)
   * @param subtype (optional)
   * @param subtypeId (optional)
   */
  @RequestLine("PATCH /api/v1.0/devices/{id}")
  @Headers({"Accept: */*",})
  void devicesUpdate(@Param("id") String id, @Param("name") String name,
      @Param("friendlyName") String friendlyName, @Param("description") String description,
      @Param("hardwareId") String hardwareId, @Param("gatewayId") String gatewayId,
      @Param("spaceId") UUID spaceId, @Param("status") String status,
      @Param("location") Location location, @Param("type") String type,
      @Param("typeId") Integer typeId, @Param("subtype") String subtype,
      @Param("subtypeId") Integer subtypeId);

  /**
   * Updates a blob This is a multi-part request. For more information, see sample app or doc
   * examples.
   *
   * @param id blob Id (required)
   * @param metadata (optional)
   * @param contents (optional)
   */
  @RequestLine("PATCH /api/v1.0/devices/blobs/{id}")
  @Headers({"Accept: */*",})
  void devicesUpdateBlob(@Param("id") String id, @Param("metadata") String metadata,
      @Param("contents") File contents);

  /**
   * Creates or updates property values
   *
   * @param body The properties (required)
   * @param id Parent Id (required)
   * @return Object
   */
  @RequestLine("PUT /api/v1.0/devices/{id}/properties")
  @Headers({"Accept: */*",})
  Object devicesUpdateProperties(java.util.List<ExtendedPropertyCreate> body,
      @Param("id") String id);
}
