/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import com.microsoft.twins.model.RoleAssignmentCreate;

/**
 * API tests for RoleAssignmentsApi
 */
public class RoleAssignmentsApiTest extends AbstractApiTest {
  private RoleAssignmentsApi api;

  @BeforeEach
  public void setup() {
    api = TWINS_API_CLIENT.getRoleAssignmentsApi();
  }

  /**
   * Checks permissions for a given path, user, domain, tenant id, accessType and resource type
   *
   *
   */
  @Test
  public void roleAssignmentsCheckTest() {
    final String path;
    final String userId;
    final String accessType;
    final String resourceType;
    final String domain;
    final String tenantId;
    // Boolean response = api.roleAssignmentsCheck(path, userId, accessType,
    // resourceType, domain, tenantId);
    // TODO: test validations
  }

  /**
   * Checks permissions for a given path, user, domain, tenant id, accessType and resource type
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void roleAssignmentsCheckTestQueryMap() {
    final RoleAssignmentsApi.RoleAssignmentsCheckQueryParams queryParams =
        new RoleAssignmentsApi.RoleAssignmentsCheckQueryParams().path(null).userId(null)
            .accessType(null).resourceType(null).domain(null).tenantId(null);
    // Boolean response = api.roleAssignmentsCheck(queryParams);
    // TODO: test validations
  }

  /**
   * Creates a role assignment
   *
   *
   */
  @Test
  public void roleAssignmentsCreateTest() {
    final RoleAssignmentCreate body;
    // UUID response = api.roleAssignmentsCreate(body);
    // TODO: test validations
  }

  /**
   * Deletes a role assignment
   *
   *
   */
  @Test
  public void roleAssignmentsDeleteTest() {
    final String id;
    // api.roleAssignmentsDelete(id);
    // TODO: test validations
  }

  /**
   * Gets role assignments under a given path
   *
   *
   */
  @Test
  public void roleAssignmentsRetrieveTest() {
    final String path;
    final String objectId;
    final Boolean personal;
    final String traverse;
    // List<RoleAssignmentRetrieve> response =
    // api.roleAssignmentsRetrieve(path, objectId, personal, traverse);
    // TODO: test validations
  }

  /**
   * Gets role assignments under a given path
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void roleAssignmentsRetrieveTestQueryMap() {
    final RoleAssignmentsApi.RoleAssignmentsRetrieveQueryParams queryParams =
        new RoleAssignmentsApi.RoleAssignmentsRetrieveQueryParams().path(null).objectId(null)
            .personal(null).traverse(null);
    // List<RoleAssignmentRetrieve> response =
    // api.roleAssignmentsRetrieve(queryParams);
    // TODO: test validations
  }
}
