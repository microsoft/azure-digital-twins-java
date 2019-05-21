/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.io.File;
import org.junit.Before;
import org.junit.Test;
import com.microsoft.twins.model.ExtendedPropertyCreate;
import com.microsoft.twins.model.UserCreateWithSpace;
import com.microsoft.twins.model.UserUpdate;

/**
 * API tests for UsersApi
 */
public class UsersApiTest extends AbstractApiTest {
  private UsersApi api;

  @Before
  public void setup() {
    api = TWINS_API_CLIENT.getUsersApi();
  }

  /**
   * Creates a blob
   *
   * This is a multi-part request. For more information, see sample app or doc examples. Key value
   * pairs specified in the Content-Disposition header in the blob-chunk of the multipart request
   * will be preserved as meta-data on the stored blob.
   */
  @Test
  public void usersCreateBlobTest() {
    final String metadata;
    final File contents;
    // UUID response = api.usersCreateBlob(metadata, contents);
    // TODO: test validations
  }

  /**
   * Creates or updates a user, using SpaceId and upn as lookup keys
   *
   *
   */
  @Test
  public void usersCreateOrUpdateTest() {
    final UserCreateWithSpace body;
    // UUID response = api.usersCreateOrUpdate(body);
    // TODO: test validations
  }

  /**
   * Creates a property value
   *
   *
   */
  @Test
  public void usersCreatePropertyTest() {
    final ExtendedPropertyCreate body;
    final String id;
    // String response = api.usersCreateProperty(body, id);
    // TODO: test validations
  }

  /**
   * Deletes a user
   *
   *
   */
  @Test
  public void usersDeleteTest() {
    final String id;
    // api.usersDelete(id);
    // TODO: test validations
  }

  /**
   * Deletes a blob
   *
   * Deleting a blob will delete its metadata, its content (all versions) and its associations
   */
  @Test
  public void usersDeleteBlobTest() {
    final String id;
    // api.usersDeleteBlob(id);
    // TODO: test validations
  }

  /**
   * Delete the contents of the given version of the given blob
   *
   * Delete will fail if this version has any associations
   */
  @Test
  public void usersDeleteBlobContentsTest() {
    final String id;
    final Integer version;
    // api.usersDeleteBlobContents(id, version);
    // TODO: test validations
  }

  /**
   * Deletes all property values
   *
   *
   */
  @Test
  public void usersDeletePropertiesTest() {
    final String id;
    // api.usersDeleteProperties(id);
    // TODO: test validations
  }

  /**
   * Deletes the given property value
   *
   *
   */
  @Test
  public void usersDeletePropertyTest() {
    final String id;
    final String name;
    // api.usersDeleteProperty(id, name);
    // TODO: test validations
  }

  /**
   * Gets the contents of the given version of the given blob
   *
   *
   */
  @Test
  public void usersGetBlobContentsTest() {
    final String id;
    final Integer version;
    // File response = api.usersGetBlobContents(id, version);
    // TODO: test validations
  }

  /**
   * Gets the contents of the latest version of the given blob
   *
   *
   */
  @Test
  public void usersGetLatestBlobContentsTest() {
    final String id;
    // File response = api.usersGetLatestBlobContents(id);
    // TODO: test validations
  }

  /**
   * Gets a list of users
   *
   *
   */
  @Test
  public void usersRetrieveTest() {
    final String upn;
    final String firstName;
    final String lastName;
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
    // java.util.List<UserRetrieve> response = api.usersRetrieve(upn,
    // firstName, lastName, includes, propertyKey, propertyValue,
    // propertyValueSearchType, spaceId, traverse, minLevel, maxLevel,
    // minRelative, maxRelative);
    // TODO: test validations
  }

  /**
   * Gets a list of users
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void usersRetrieveTestQueryMap() {
    final UsersApi.UsersRetrieveQueryParams queryParams = new UsersApi.UsersRetrieveQueryParams()
        .upn(null).firstName(null).lastName(null).includes(null).propertyKey(null)
        .propertyValue(null).propertyValueSearchType(null).spaceId(null).traverse(null)
        .minLevel(null).maxLevel(null).minRelative(null).maxRelative(null);
    // java.util.List<UserRetrieve> response =
    // api.usersRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Gets a list of blobs
   *
   *
   */
  @Test
  public void usersRetrieveBlobMetadataTest() {
    final String names;
    final String ids;
    final String sharings;
    final String types;
    final String subtypes;
    final String includes;
    final String spaceId;
    final String traverse;
    final Integer minLevel;
    final Integer maxLevel;
    final Boolean minRelative;
    final Boolean maxRelative;
    // java.util.List<BlobMetadataRetrieve> response =
    // api.usersRetrieveBlobMetadata(names, ids, sharings, types, subtypes,
    // includes, spaceId, traverse, minLevel, maxLevel, minRelative,
    // maxRelative);
    // TODO: test validations
  }

  /**
   * Gets a list of blobs
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void usersRetrieveBlobMetadataTestQueryMap() {
    final UsersApi.UsersRetrieveBlobMetadataQueryParams queryParams =
        new UsersApi.UsersRetrieveBlobMetadataQueryParams().names(null).ids(null).sharings(null)
            .types(null).subtypes(null).includes(null).spaceId(null).traverse(null).minLevel(null)
            .maxLevel(null).minRelative(null).maxRelative(null);
    // java.util.List<BlobMetadataRetrieve> response =
    // api.usersRetrieveBlobMetadata(queryParams);
    // TODO: test validations
  }

  /**
   * Gets a blob
   *
   *
   */
  @Test
  public void usersRetrieveBlobMetadataByIdTest() {
    final String id;
    final String includes;
    // BlobMetadataRetrieve response = api.usersRetrieveBlobMetadataById(id,
    // includes);
    // TODO: test validations
  }

  /**
   * Gets a blob
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void usersRetrieveBlobMetadataByIdTestQueryMap() {
    final String id;
    final UsersApi.UsersRetrieveBlobMetadataByIdQueryParams queryParams =
        new UsersApi.UsersRetrieveBlobMetadataByIdQueryParams().includes(null);
    // BlobMetadataRetrieve response = api.usersRetrieveBlobMetadataById(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Gets a specific user
   *
   *
   */
  @Test
  public void usersRetrieveByIdTest() {
    final String id;
    final String includes;
    // UserRetrieve response = api.usersRetrieveById(id, includes);
    // TODO: test validations
  }

  /**
   * Gets a specific user
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void usersRetrieveByIdTestQueryMap() {
    final String id;
    final UsersApi.UsersRetrieveByIdQueryParams queryParams =
        new UsersApi.UsersRetrieveByIdQueryParams().includes(null);
    // UserRetrieve response = api.usersRetrieveById(id, queryParams);
    // TODO: test validations
  }

  /**
   * Gets the value of a property
   *
   *
   */
  @Test
  public void usersRetrievePropertyTest() {
    final String id;
    final String name;
    // String response = api.usersRetrieveProperty(id, name);
    // TODO: test validations
  }

  /**
   * Updates a user
   *
   *
   */
  @Test
  public void usersUpdateTest() {
    final UserUpdate body;
    final String id;
    // api.usersUpdate(body, id);
    // TODO: test validations
  }

  /**
   * Updates a blob
   *
   * This is a multi-part request. For more information, see sample app or doc examples.
   */
  @Test
  public void usersUpdateBlobTest() {
    final String id;
    final String metadata;
    final File contents;
    // api.usersUpdateBlob(id, metadata, contents);
    // TODO: test validations
  }

  /**
   * Creates or updates property values
   *
   *
   */
  @Test
  public void usersUpdatePropertiesTest() {
    final java.util.List<ExtendedPropertyCreate> body;
    final String id;
    // Object response = api.usersUpdateProperties(body, id);
    // TODO: test validations
  }
}
