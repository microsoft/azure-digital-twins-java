/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import com.microsoft.twins.EncodingUtils;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.model.ExtendedPropertyCreate;
import com.microsoft.twins.model.Location;
import com.microsoft.twins.model.MatcherRetrieve;
import com.microsoft.twins.model.SensorCreate;
import com.microsoft.twins.model.SensorRetrieve;
import com.microsoft.twins.model.SensorUpdate;
import com.microsoft.twins.model.SensorValue;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface SensorsApi extends TwinsApiClient.Api {
  /**
   * Creates a sensor
   *
   * @param body The sensor information (required)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/sensors")
  @Headers({"Accept: */*",})
  UUID sensorsCreate(SensorCreate body);

  /**
   * Creates a sensor
   *
   * @param deviceId (optional)
   * @param port (optional)
   * @param pollRate (optional)
   * @param dataType (optional)
   * @param type (optional)
   * @param portType (optional)
   * @param dataUnitType (optional)
   * @param dataSubtype (optional)
   * @param spaceId (optional)
   * @param location (optional)
   * @param portTypeId (optional)
   * @param dataUnitTypeId (optional)
   * @param typeId (optional)
   * @param dataTypeId (optional)
   * @param dataSubtypeId (optional)
   * @param hardwareId (optional)
   * @param properties (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/sensors")
  @Headers({"Accept: */*",})
  UUID sensorsCreate(@Param("deviceId") UUID deviceId, @Param("port") String port,
      @Param("pollRate") Integer pollRate, @Param("dataType") String dataType,
      @Param("type") String type, @Param("portType") String portType,
      @Param("dataUnitType") String dataUnitType, @Param("dataSubtype") String dataSubtype,
      @Param("spaceId") UUID spaceId, @Param("location") Location location,
      @Param("portTypeId") Integer portTypeId, @Param("dataUnitTypeId") Integer dataUnitTypeId,
      @Param("typeId") Integer typeId, @Param("dataTypeId") Integer dataTypeId,
      @Param("dataSubtypeId") Integer dataSubtypeId, @Param("hardwareId") String hardwareId,
      @Param("properties") List<ExtendedPropertyCreate> properties);

  /**
   * Creates a property value
   *
   * @param body Extended property data (required)
   * @param id Parent Id (required)
   * @return String
   */
  @RequestLine("POST /api/v1.0/sensors/{id}/properties")
  @Headers({"Accept: */*",})
  String sensorsCreateProperty(ExtendedPropertyCreate body, @Param("id") UUID id);

  /**
   * Creates a property value
   *
   * @param id Parent Id (required)
   * @param name (optional)
   * @param value (optional)
   * @return String
   */
  @RequestLine("POST /api/v1.0/sensors/{id}/properties")
  @Headers({"Accept: */*",})
  String sensorsCreateProperty(@Param("id") UUID id, @Param("name") String name,
      @Param("value") String value);

  /**
   * Deletes a sensor
   *
   * @param id The sensor Id (required)
   */
  @RequestLine("DELETE /api/v1.0/sensors/{id}")
  @Headers({"Accept: */*",})
  void sensorsDelete(@Param("id") UUID id);

  /**
   * Deletes all property values
   *
   * @param id Parent Id (required)
   */
  @RequestLine("DELETE /api/v1.0/sensors/{id}/properties")
  @Headers({"Accept: */*",})
  void sensorsDeleteProperties(@Param("id") UUID id);

  /**
   * Deletes the given property value
   *
   * @param id Parent Id (required)
   * @param name Property name (required)
   */
  @RequestLine("DELETE /api/v1.0/sensors/{id}/properties/{name}")
  @Headers({"Accept: */*",})
  void sensorsDeleteProperty(@Param("id") UUID id, @Param("name") String name);

  /**
   * Gets a sensor&#x27;s value
   *
   * @param id The identifier (required)
   * @return SensorValue
   */
  @RequestLine("GET /api/v1.0/sensors/{id}/value")
  @Headers({"Accept: */*",})
  SensorValue sensorsGetValue(@Param("id") UUID id);

  /**
   * Gets the matchers that match the given sensor
   *
   * @param id Sensor identifier (required)
   * @param includes What to include (optional)
   * @return java.util.List&lt;MatcherRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/sensors/{id}/matchers?includes={includes}")
  @Headers({"Accept: */*",})
  List<MatcherRetrieve> sensorsMatchers(@Param("id") UUID id, @Param("includes") String includes);

  /**
   * Gets the matchers that match the given sensor
   *
   * Note, this is equivalent to the other <code>sensorsMatchers</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link SensorsMatchersQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param id Sensor identifier (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - What to include (optional)</li>
   *        </ul>
   * @return java.util.List&lt;MatcherRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/sensors/{id}/matchers?includes={includes}")
  @Headers({"Content-Type: */*",})
  List<MatcherRetrieve> sensorsMatchers(@Param("id") UUID id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>sensorsMatchers</code> method
   * in a fluent style.
   */
  public static class SensorsMatchersQueryParams extends HashMap<String, Object> {
    public SensorsMatchersQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a list of sensors
   *
   * @param ids Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)
   * @param deviceIds Optionally filter on sensors that belong to the given devices (optional)
   * @param types Optionally filter on types (optional)
   * @param portTypes Optionally on port types (optional)
   * @param dataTypes Optionally on data subtypes (optional)
   * @param dataSubtypes Optionally on data types (optional)
   * @param dataUnitTypes Optionally on data unit types (optional)
   * @param hardwareIds Optionally on hardwareIds (optional)
   * @param includes What to include (optional)
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
   * @return java.util.List&lt;SensorRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/sensors?ids={ids}&deviceIds={deviceIds}&types={types}&portTypes={portTypes}&dataTypes={dataTypes}&dataSubtypes={dataSubtypes}&dataUnitTypes={dataUnitTypes}&hardwareIds={hardwareIds}&includes={includes}&propertyKey={propertyKey}&propertyValue={propertyValue}&propertyValueSearchType={propertyValueSearchType}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Accept: */*",})
  List<SensorRetrieve> sensorsRetrieve(@Param("ids") UUID ids, @Param("deviceIds") String deviceIds,
      @Param("types") String types, @Param("portTypes") String portTypes,
      @Param("dataTypes") String dataTypes, @Param("dataSubtypes") String dataSubtypes,
      @Param("dataUnitTypes") String dataUnitTypes, @Param("hardwareIds") String hardwareIds,
      @Param("includes") String includes, @Param("propertyKey") String propertyKey,
      @Param("propertyValue") String propertyValue,
      @Param("propertyValueSearchType") String propertyValueSearchType,
      @Param("spaceId") String spaceId, @Param("traverse") String traverse,
      @Param("minLevel") Integer minLevel, @Param("maxLevel") Integer maxLevel,
      @Param("minRelative") Boolean minRelative, @Param("maxRelative") Boolean maxRelative);

  /**
   * Gets a list of sensors
   *
   * Note, this is equivalent to the other <code>sensorsRetrieve</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link SensorsRetrieveQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>ids - Optional &#x27;;&#x27; or &#x27;,&#x27; delimited list of ids to filter by
   *        (optional)</li>
   *        <li>deviceIds - Optionally filter on sensors that belong to the given devices
   *        (optional)</li>
   *        <li>types - Optionally filter on types (optional)</li>
   *        <li>portTypes - Optionally on port types (optional)</li>
   *        <li>dataTypes - Optionally on data subtypes (optional)</li>
   *        <li>dataSubtypes - Optionally on data types (optional)</li>
   *        <li>dataUnitTypes - Optionally on data unit types (optional)</li>
   *        <li>hardwareIds - Optionally on hardwareIds (optional)</li>
   *        <li>includes - What to include (optional)</li>
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
   * @return java.util.List&lt;SensorRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/sensors?ids={ids}&deviceIds={deviceIds}&types={types}&portTypes={portTypes}&dataTypes={dataTypes}&dataSubtypes={dataSubtypes}&dataUnitTypes={dataUnitTypes}&hardwareIds={hardwareIds}&includes={includes}&propertyKey={propertyKey}&propertyValue={propertyValue}&propertyValueSearchType={propertyValueSearchType}&spaceId={spaceId}&traverse={traverse}&minLevel={minLevel}&maxLevel={maxLevel}&minRelative={minRelative}&maxRelative={maxRelative}")
  @Headers({"Content-Type: */*",})
  List<SensorRetrieve> sensorsRetrieve(@QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>sensorsRetrieve</code> method
   * in a fluent style.
   */
  public static class SensorsRetrieveQueryParams extends HashMap<String, Object> {
    public SensorsRetrieveQueryParams ids(final UUID value) {
      put("ids", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams deviceIds(final UUID value) {
      put("deviceIds", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams types(final String value) {
      put("types", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams portTypes(final String value) {
      put("portTypes", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams dataTypes(final String value) {
      put("dataTypes", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams dataSubtypes(final String value) {
      put("dataSubtypes", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams dataUnitTypes(final String value) {
      put("dataUnitTypes", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams hardwareIds(final String value) {
      put("hardwareIds", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams propertyKey(final String value) {
      put("propertyKey", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams propertyValue(final String value) {
      put("propertyValue", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams propertyValueSearchType(final String value) {
      put("propertyValueSearchType", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams spaceId(final UUID value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams minLevel(final Integer value) {
      put("minLevel", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams maxLevel(final Integer value) {
      put("maxLevel", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams minRelative(final Boolean value) {
      put("minRelative", EncodingUtils.encode(value));
      return this;
    }

    public SensorsRetrieveQueryParams maxRelative(final Boolean value) {
      put("maxRelative", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a sensor
   *
   * @param id The identifier (required)
   * @param includes What to include (optional)
   * @return SensorRetrieve
   */
  @RequestLine("GET /api/v1.0/sensors/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  SensorRetrieve sensorsRetrieveById(@Param("id") UUID id, @Param("includes") String includes);

  /**
   * Gets a sensor
   *
   * Note, this is equivalent to the other <code>sensorsRetrieveById</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the {@link SensorsRetrieveByIdQueryParams}
   * class that allows for building up this map in a fluent style.
   *
   * @param id The identifier (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - What to include (optional)</li>
   *        </ul>
   * @return SensorRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/sensors/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  SensorRetrieve sensorsRetrieveById(@Param("id") UUID id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>sensorsRetrieveById</code>
   * method in a fluent style.
   */
  public static class SensorsRetrieveByIdQueryParams extends HashMap<String, Object> {
    public SensorsRetrieveByIdQueryParams includes(final String value) {
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
  @RequestLine("GET /api/v1.0/sensors/{id}/properties/{name}")
  @Headers({"Accept: */*",})
  String sensorsRetrieveProperty(@Param("id") UUID id, @Param("name") String name);

  /**
   * Updates a sensor
   *
   * @param body The sensor information (required)
   * @param id The sensor Id (required)
   */
  @RequestLine("PATCH /api/v1.0/sensors/{id}")
  @Headers({"Accept: */*",})
  void sensorsUpdate(SensorUpdate body, @Param("id") UUID id);

  /**
   * Updates a sensor
   *
   * @param id The sensor Id (required)
   * @param port (optional)
   * @param pollRate (optional)
   * @param dataType (optional)
   * @param dataSubtype (optional)
   * @param type (optional)
   * @param deviceId (optional)
   * @param spaceId (optional)
   * @param location (optional)
   * @param portType (optional)
   * @param dataUnitType (optional)
   * @param portTypeId (optional)
   * @param dataUnitTypeId (optional)
   * @param dataSubtypeId (optional)
   * @param typeId (optional)
   * @param dataTypeId (optional)
   * @param hardwareId (optional)
   */
  @RequestLine("PATCH /api/v1.0/sensors/{id}")
  @Headers({"Accept: */*",})
  void sensorsUpdate(@Param("id") UUID id, @Param("port") String port,
      @Param("pollRate") Integer pollRate, @Param("dataType") String dataType,
      @Param("dataSubtype") String dataSubtype, @Param("type") String type,
      @Param("deviceId") UUID deviceId, @Param("spaceId") UUID spaceId,
      @Param("location") Location location, @Param("portType") String portType,
      @Param("dataUnitType") String dataUnitType, @Param("portTypeId") Integer portTypeId,
      @Param("dataUnitTypeId") Integer dataUnitTypeId,
      @Param("dataSubtypeId") Integer dataSubtypeId, @Param("typeId") Integer typeId,
      @Param("dataTypeId") Integer dataTypeId, @Param("hardwareId") String hardwareId);

  /**
   * Creates or updates property values
   *
   * @param body The properties (required)
   * @param id Parent Id (required)
   * @return Object
   */
  @RequestLine("PUT /api/v1.0/sensors/{id}/properties")
  @Headers({"Accept: */*",})
  Object sensorsUpdateProperties(List<ExtendedPropertyCreate> body, @Param("id") UUID id);
}
