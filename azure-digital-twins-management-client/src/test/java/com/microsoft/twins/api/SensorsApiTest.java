/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.microsoft.twins.model.ExtendedPropertyCreate;
import com.microsoft.twins.model.SensorCreate;
import com.microsoft.twins.model.SensorUpdate;

/**
 * API tests for SensorsApi
 */
public class SensorsApiTest extends AbstractApiTest {
  private SensorsApi api;

  @Before
  public void setup() {
    api = TWINS_API_CLIENT.getSensorsApi();
  }

  /**
   * Creates a sensor
   *
   *
   */
  @Test
  public void sensorsCreateTest() {
    final SensorCreate body;
    // UUID response = api.sensorsCreate(body);
    // TODO: test validations
  }

  /**
   * Creates a property value
   *
   *
   */
  @Test
  public void sensorsCreatePropertyTest() {
    final ExtendedPropertyCreate body;
    final String id;
    // String response = api.sensorsCreateProperty(body, id);
    // TODO: test validations
  }

  /**
   * Deletes a sensor
   *
   *
   */
  @Test
  public void sensorsDeleteTest() {
    final String id;
    // api.sensorsDelete(id);
    // TODO: test validations
  }

  /**
   * Deletes all property values
   *
   *
   */
  @Test
  public void sensorsDeletePropertiesTest() {
    final String id;
    // api.sensorsDeleteProperties(id);
    // TODO: test validations
  }

  /**
   * Deletes the given property value
   *
   *
   */
  @Test
  public void sensorsDeletePropertyTest() {
    final String id;
    final String name;
    // api.sensorsDeleteProperty(id, name);
    // TODO: test validations
  }

  /**
   * Gets a sensor&#x27;s value
   *
   *
   */
  @Test
  public void sensorsGetValueTest() {
    final String id;
    // SensorValue response = api.sensorsGetValue(id);
    // TODO: test validations
  }

  /**
   * Gets the matchers that match the given sensor
   *
   *
   */
  @Test
  public void sensorsMatchersTest() {
    final String id;
    final String includes;
    // List<MatcherRetrieve> response = api.sensorsMatchers(id,
    // includes);
    // TODO: test validations
  }

  /**
   * Gets the matchers that match the given sensor
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void sensorsMatchersTestQueryMap() {
    final String id;
    final SensorsApi.SensorsMatchersQueryParams queryParams =
        new SensorsApi.SensorsMatchersQueryParams().includes(null);
    // List<MatcherRetrieve> response = api.sensorsMatchers(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Gets a list of sensors
   *
   *
   */
  @Test
  public void sensorsRetrieveTest() {
    final String ids;
    final String deviceIds;
    final String types;
    final String portTypes;
    final String dataTypes;
    final String dataSubtypes;
    final String dataUnitTypes;
    final String hardwareIds;
    final String includes;
    final String propertyKey;
    final String propertyValue;
    final String propertyValueSearchType;
    final String spaceId;
    final String traverse;
    final Integer minLevel;
    final Integer maxLevel;
    final Boolean minRelative;
    final Boolean maxRelative;
    // List<SensorRetrieve> response = api.sensorsRetrieve(ids,
    // deviceIds, types, portTypes, dataTypes, dataSubtypes, dataUnitTypes,
    // hardwareIds, includes, propertyKey, propertyValue,
    // propertyValueSearchType, spaceId, traverse, minLevel, maxLevel,
    // minRelative, maxRelative);
    // TODO: test validations
  }

  /**
   * Gets a list of sensors
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void sensorsRetrieveTestQueryMap() {
    final SensorsApi.SensorsRetrieveQueryParams queryParams = new SensorsApi.SensorsRetrieveQueryParams().ids(null)
        .deviceIds(null).types(null).portTypes(null).dataTypes(null).dataSubtypes(null).dataUnitTypes(null)
        .hardwareIds(null).includes(null).propertyKey(null).propertyValue(null).propertyValueSearchType(null)
        .spaceId(null).traverse(null).minLevel(null).maxLevel(null).minRelative(null).maxRelative(null);
    // List<SensorRetrieve> response =
    // api.sensorsRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Gets a sensor
   *
   *
   */
  @Test
  public void sensorsRetrieveByIdTest() {
    final String id;
    final String includes;
    // SensorRetrieve response = api.sensorsRetrieveById(id, includes);
    // TODO: test validations
  }

  /**
   * Gets a sensor
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void sensorsRetrieveByIdTestQueryMap() {
    final String id;
    final SensorsApi.SensorsRetrieveByIdQueryParams queryParams =
        new SensorsApi.SensorsRetrieveByIdQueryParams().includes(null);
    // SensorRetrieve response = api.sensorsRetrieveById(id, queryParams);
    // TODO: test validations
  }

  /**
   * Gets the value of a property
   *
   *
   */
  @Test
  public void sensorsRetrievePropertyTest() {
    final String id;
    final String name;
    // String response = api.sensorsRetrieveProperty(id, name);
    // TODO: test validations
  }

  /**
   * Updates a sensor
   *
   *
   */
  @Test
  public void sensorsUpdateTest() {
    final SensorUpdate body;
    final String id;
    // api.sensorsUpdate(body, id);
    // TODO: test validations
  }

  /**
   * Creates or updates property values
   *
   *
   */
  @Test
  public void sensorsUpdatePropertiesTest() {
    final List<ExtendedPropertyCreate> body;
    final String id;
    // Object response = api.sensorsUpdateProperties(body, id);
    // TODO: test validations
  }
}
