/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.model.IEnumEntity;
import com.microsoft.twins.model.RoleDefinitionRetrieve;
import feign.Headers;
import feign.RequestLine;

public interface SystemApi extends TwinsApiClient.Api {
  /**
   * Retrieve all supported device statuses
   *
   * @return java.util.List&lt;IEnumEntity&gt;
   */
  @RequestLine("GET /api/v1.0/system/devices/statuses")
  @Headers({"Accept: */*",})
  java.util.List<IEnumEntity> systemRetrieveDeviceStatuses();

  /**
   * Retrieve all extended type categories
   *
   * @return java.util.List&lt;IEnumEntity&gt;
   */
  @RequestLine("GET /api/v1.0/system/extendedtypes/categories")
  @Headers({"Accept: */*",})
  java.util.List<IEnumEntity> systemRetrieveExtendedTypeCategories();

  /**
   * Retrieve all supported comparisons for matcher&#x27;s conditions
   *
   * @return java.util.List&lt;IEnumEntity&gt;
   */
  @RequestLine("GET /api/v1.0/system/matchers/conditions/comparisons")
  @Headers({"Accept: */*",})
  java.util.List<IEnumEntity> systemRetrieveMatcherConditionComparisons();

  /**
   * Retrieve all supported targets for matcher&#x27;s conditions
   *
   * @return java.util.List&lt;IEnumEntity&gt;
   */
  @RequestLine("GET /api/v1.0/system/matchers/conditions/targets")
  @Headers({"Accept: */*",})
  java.util.List<IEnumEntity> systemRetrieveMatcherConditionTargets();

  /**
   * Retrieve all extended property key scopes
   *
   * @return java.util.List&lt;IEnumEntity&gt;
   */
  @RequestLine("GET /api/v1.0/system/extendedpropertykeys/scopes")
  @Headers({"Accept: */*",})
  java.util.List<IEnumEntity> systemRetrievePropertyKeyScopes();

  /**
   * Retrieve all supported roles
   *
   * @return java.util.List&lt;RoleDefinitionRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/system/roles")
  @Headers({"Accept: */*",})
  java.util.List<RoleDefinitionRetrieve> systemRetrieveRoles();

  /**
   * Retrieve all supported space resource regions
   *
   * @return java.util.List&lt;IEnumEntity&gt;
   */
  @RequestLine("GET /api/v1.0/system/spacesresources/regions")
  @Headers({"Accept: */*",})
  java.util.List<IEnumEntity> systemRetrieveSpaceResourceRegions();

  /**
   * Retrieve all supported space resource sizes
   *
   * @return java.util.List&lt;IEnumEntity&gt;
   */
  @RequestLine("GET /api/v1.0/system/spacesresources/sizes")
  @Headers({"Accept: */*",})
  java.util.List<IEnumEntity> systemRetrieveSpaceResourceSizes();

  /**
   * Retrieve all supported space resource statuses
   *
   * @return java.util.List&lt;IEnumEntity&gt;
   */
  @RequestLine("GET /api/v1.0/system/spacesresources/statuses")
  @Headers({"Accept: */*",})
  java.util.List<IEnumEntity> systemRetrieveSpaceResourceStatuses();

  /**
   * Retrieve all supported timezones
   *
   * @return java.util.List&lt;IEnumEntity&gt;
   */
  @RequestLine("GET /api/v1.0/system/timezones")
  @Headers({"Accept: */*",})
  java.util.List<IEnumEntity> systemRetrieveTimeZones();
}
