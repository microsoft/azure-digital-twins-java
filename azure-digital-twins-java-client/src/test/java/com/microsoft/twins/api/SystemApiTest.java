/**
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import org.junit.Before;
import org.junit.Test;

/**
 * API tests for SystemApi
 */
public class SystemApiTest extends AbstractApiTest {
  private SystemApi api;

  @Before
  public void setup() {
    api = TWINS_API_CLIENT.getSystemApi();
  }

  /**
   * Retrieve all supported device statuses
   *
   *
   */
  @Test
  public void systemRetrieveDeviceStatusesTest() {
    // java.util.List<IEnumEntity> response =
    // api.systemRetrieveDeviceStatuses();
    // TODO: test validations
  }

  /**
   * Retrieve all extended type categories
   *
   *
   */
  @Test
  public void systemRetrieveExtendedTypeCategoriesTest() {
    // java.util.List<IEnumEntity> response =
    // api.systemRetrieveExtendedTypeCategories();
    // TODO: test validations
  }

  /**
   * Retrieve all supported comparisons for matcher&#x27;s conditions
   *
   *
   */
  @Test
  public void systemRetrieveMatcherConditionComparisonsTest() {
    // java.util.List<IEnumEntity> response =
    // api.systemRetrieveMatcherConditionComparisons();
    // TODO: test validations
  }

  /**
   * Retrieve all supported targets for matcher&#x27;s conditions
   *
   *
   */
  @Test
  public void systemRetrieveMatcherConditionTargetsTest() {
    // java.util.List<IEnumEntity> response =
    // api.systemRetrieveMatcherConditionTargets();
    // TODO: test validations
  }

  /**
   * Retrieve all extended property key scopes
   *
   *
   */
  @Test
  public void systemRetrievePropertyKeyScopesTest() {
    // java.util.List<IEnumEntity> response =
    // api.systemRetrievePropertyKeyScopes();
    // TODO: test validations
  }

  /**
   * Retrieve all supported roles
   *
   *
   */
  @Test
  public void systemRetrieveRolesTest() {
    // java.util.List<RoleDefinitionRetrieve> response =
    // api.systemRetrieveRoles();
    // TODO: test validations
  }

  /**
   * Retrieve all supported space resource regions
   *
   *
   */
  @Test
  public void systemRetrieveSpaceResourceRegionsTest() {
    // java.util.List<IEnumEntity> response =
    // api.systemRetrieveSpaceResourceRegions();
    // TODO: test validations
  }

  /**
   * Retrieve all supported space resource sizes
   *
   *
   */
  @Test
  public void systemRetrieveSpaceResourceSizesTest() {
    // java.util.List<IEnumEntity> response =
    // api.systemRetrieveSpaceResourceSizes();
    // TODO: test validations
  }

  /**
   * Retrieve all supported space resource statuses
   *
   *
   */
  @Test
  public void systemRetrieveSpaceResourceStatusesTest() {
    // java.util.List<IEnumEntity> response =
    // api.systemRetrieveSpaceResourceStatuses();
    // TODO: test validations
  }

  /**
   * Retrieve all supported timezones
   *
   *
   */
  @Test
  public void systemRetrieveTimeZonesTest() {
    // java.util.List<IEnumEntity> response = api.systemRetrieveTimeZones();
    // TODO: test validations
  }
}
