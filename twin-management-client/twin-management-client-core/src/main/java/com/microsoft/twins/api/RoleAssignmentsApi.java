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
import com.microsoft.twins.model.RoleAssignmentCreate;
import com.microsoft.twins.model.RoleAssignmentRetrieve;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface RoleAssignmentsApi extends TwinsApiClient.Api {
  /**
   * Checks permissions for a given path, user, domain, tenant id, accessType and resource type
   *
   * @param path The path (required)
   * @param userId The AAD user ObjectId (required)
   * @param accessType The access type (required)
   * @param resourceType The resource type (required)
   * @param domain The domain to check preceded by the &#x27;@&#x27; character, or the upn that
   *        belongs to the user. Examples: &#x60;user@example.com&#x60;, &#x60;@example.com&#x60;
   *        (optional)
   * @param tenantId The AAD tenantId of the user, disallowed for GatewayDevice role assignments
   *        (optional)
   * @return Boolean
   */
  @RequestLine("GET /api/v1.0/roleassignments/check?path={path}&userId={userId}&accessType={accessType}&resourceType={resourceType}&domain={domain}&tenantId={tenantId}")
  @Headers({"Accept: */*",})
  Boolean roleAssignmentsCheck(@Param("path") String path, @Param("userId") String userId,
      @Param("accessType") String accessType, @Param("resourceType") String resourceType,
      @Param("domain") String domain, @Param("tenantId") String tenantId);

  /**
   * Checks permissions for a given path, user, domain, tenant id, accessType and resource type
   *
   * Note, this is equivalent to the other <code>roleAssignmentsCheck</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link RoleAssignmentsCheckQueryParams} class that allows for building up this map in a fluent
   * style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>path - The path (required)</li>
   *        <li>userId - The AAD user ObjectId (required)</li>
   *        <li>accessType - The access type (required)</li>
   *        <li>resourceType - The resource type (required)</li>
   *        <li>domain - The domain to check preceded by the &#x27;@&#x27; character, or the upn
   *        that belongs to the user. Examples: &#x60;user@example.com&#x60;,
   *        &#x60;@example.com&#x60; (optional)</li>
   *        <li>tenantId - The AAD tenantId of the user, disallowed for GatewayDevice role
   *        assignments (optional)</li>
   *        </ul>
   * @return Boolean
   *
   */
  @RequestLine("GET /api/v1.0/roleassignments/check?path={path}&userId={userId}&accessType={accessType}&resourceType={resourceType}&domain={domain}&tenantId={tenantId}")
  @Headers({"Content-Type: */*",})
  Boolean roleAssignmentsCheck(@QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>roleAssignmentsCheck</code>
   * method in a fluent style.
   */
  public static class RoleAssignmentsCheckQueryParams extends HashMap<String, Object> {
    public RoleAssignmentsCheckQueryParams path(final String value) {
      put("path", EncodingUtils.encode(value));
      return this;
    }

    public RoleAssignmentsCheckQueryParams userId(final String value) {
      put("userId", EncodingUtils.encode(value));
      return this;
    }

    public RoleAssignmentsCheckQueryParams accessType(final String value) {
      put("accessType", EncodingUtils.encode(value));
      return this;
    }

    public RoleAssignmentsCheckQueryParams resourceType(final String value) {
      put("resourceType", EncodingUtils.encode(value));
      return this;
    }

    public RoleAssignmentsCheckQueryParams domain(final String value) {
      put("domain", EncodingUtils.encode(value));
      return this;
    }

    public RoleAssignmentsCheckQueryParams tenantId(final String value) {
      put("tenantId", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Creates a role assignment
   *
   * @param body The role assignment information. (required)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/roleassignments")
  @Headers({"Accept: */*",})
  UUID roleAssignmentsCreate(RoleAssignmentCreate body);

  /**
   * Creates a role assignment
   *
   * @param roleId (optional)
   * @param objectId (optional)
   * @param objectIdType (optional)
   * @param tenantId (optional)
   * @param path (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/roleassignments")
  @Headers({"Accept: */*",})
  UUID roleAssignmentsCreate(@Param("roleId") UUID roleId, @Param("objectId") String objectId,
      @Param("objectIdType") String objectIdType, @Param("tenantId") UUID tenantId,
      @Param("path") String path);

  /**
   * Deletes a role assignment
   *
   * @param id Role assignment id (required)
   */
  @RequestLine("DELETE /api/v1.0/roleassignments/{id}")
  @Headers({"Accept: */*",})
  void roleAssignmentsDelete(@Param("id") String id);

  /**
   * Gets role assignments under a given path
   *
   * @param path The path under which role assignments are searched (required)
   * @param objectId Optional object id filter. This corresponds to an object id used in a role
   *        assignment creation. (optional)
   * @param personal Optionally make the search return only the role assignments that are applicable
   *        to the user sending the request (optional)
   * @param traverse Optionally specify if you want to recursively include children (Down) of the
   *        specified path. Other traversal directions are not supported. (optional)
   * @return java.util.List&lt;RoleAssignmentRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/roleassignments?path={path}&objectId={objectId}&personal={personal}&traverse={traverse}")
  @Headers({"Accept: */*",})
  List<RoleAssignmentRetrieve> roleAssignmentsRetrieve(@Param("path") String path,
      @Param("objectId") String objectId, @Param("personal") Boolean personal,
      @Param("traverse") String traverse);

  /**
   * Gets role assignments under a given path
   *
   * Note, this is equivalent to the other <code>roleAssignmentsRetrieve</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link RoleAssignmentsRetrieveQueryParams} class that allows for building up this map in a
   * fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>path - The path under which role assignments are searched (required)</li>
   *        <li>objectId - Optional object id filter. This corresponds to an object id used in a
   *        role assignment creation. (optional)</li>
   *        <li>personal - Optionally make the search return only the role assignments that are
   *        applicable to the user sending the request (optional)</li>
   *        <li>traverse - Optionally specify if you want to recursively include children (Down) of
   *        the specified path. Other traversal directions are not supported. (optional)</li>
   *        </ul>
   * @return java.util.List&lt;RoleAssignmentRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/roleassignments?path={path}&objectId={objectId}&personal={personal}&traverse={traverse}")
  @Headers({"Content-Type: */*",})
  List<RoleAssignmentRetrieve> roleAssignmentsRetrieve(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>roleAssignmentsRetrieve</code> method in a fluent style.
   */
  public static class RoleAssignmentsRetrieveQueryParams extends HashMap<String, Object> {
    public RoleAssignmentsRetrieveQueryParams path(final String value) {
      put("path", EncodingUtils.encode(value));
      return this;
    }

    public RoleAssignmentsRetrieveQueryParams objectId(final String value) {
      put("objectId", EncodingUtils.encode(value));
      return this;
    }

    public RoleAssignmentsRetrieveQueryParams personal(final Boolean value) {
      put("personal", EncodingUtils.encode(value));
      return this;
    }

    public RoleAssignmentsRetrieveQueryParams traverse(final String value) {
      put("traverse", EncodingUtils.encode(value));
      return this;
    }
  }
}
