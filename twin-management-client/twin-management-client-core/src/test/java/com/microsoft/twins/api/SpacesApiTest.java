/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.io.File;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import com.microsoft.twins.model.ExtendedPropertyCreate;
import com.microsoft.twins.model.SpaceCreate;
import com.microsoft.twins.model.SpaceUpdate;
import com.microsoft.twins.model.UserCreate;

/**
 * API tests for SpacesApi
 */
public class SpacesApiTest extends AbstractApiTest {
  private SpacesApi api;

  @BeforeEach
  public void setup() {
    api = TWINS_API_CLIENT.getSpacesApi();
  }

  /**
   * Creates a space
   *
   *
   */
  @Test
  public void spacesCreateTest() {
    final SpaceCreate body;
    // UUID response = api.spacesCreate(body);
    // TODO: test validations
  }

  /**
   * Creates a blob
   *
   * This is a multi-part request. For more information, see sample app or doc examples. Key value
   * pairs specified in the Content-Disposition header in the blob-chunk of the multipart request
   * will be preserved as meta-data on the stored blob.
   */
  @Test
  public void spacesCreateBlobTest() {
    final String metadata;
    final File contents;
    // UUID response = api.spacesCreateBlob(metadata, contents);
    // TODO: test validations
  }

  /**
   * Adds or updates a user associated to the given space
   *
   *
   */
  @Test
  public void spacesCreateOrUpdateUserTest() {
    final UserCreate body;
    final String id;
    // String response = api.spacesCreateOrUpdateUser(body, id);
    // TODO: test validations
  }

  /**
   * Creates a property value
   *
   *
   */
  @Test
  public void spacesCreatePropertyTest() {
    final ExtendedPropertyCreate body;
    final String id;
    // String response = api.spacesCreateProperty(body, id);
    // TODO: test validations
  }

  /**
   * Deletes a space and its children, such as devices, sensors, users, ...
   *
   * Deleting a space will fail if one of these objects belongs in the space tree about to be
   * deleted: - Space resource: These need to be deleted first. - Sensors attached to devices that
   * do **not** belong in the space tree about to be deleted: these (or their parent device) need to
   * be deleted first.
   */
  @Test
  public void spacesDeleteTest() {
    final String id;
    // api.spacesDelete(id);
    // TODO: test validations
  }

  /**
   * Deletes a blob
   *
   * Deleting a blob will delete its metadata, its content (all versions) and its associations
   */
  @Test
  public void spacesDeleteBlobTest() {
    final String id;
    // api.spacesDeleteBlob(id);
    // TODO: test validations
  }

  /**
   * Delete the contents of the given version of the given blob
   *
   * Delete will fail if this version has any associations
   */
  @Test
  public void spacesDeleteBlobContentsTest() {
    final String id;
    final Integer version;
    // api.spacesDeleteBlobContents(id, version);
    // TODO: test validations
  }

  /**
   * Deletes all property values
   *
   *
   */
  @Test
  public void spacesDeletePropertiesTest() {
    final String id;
    // api.spacesDeleteProperties(id);
    // TODO: test validations
  }

  /**
   * Deletes the given property value
   *
   *
   */
  @Test
  public void spacesDeletePropertyTest() {
    final String id;
    final String name;
    // api.spacesDeleteProperty(id, name);
    // TODO: test validations
  }

  /**
   * Removes a user from the given space
   *
   *
   */
  @Test
  public void spacesDeleteUserTest() {
    final String id;
    final String upn;
    // api.spacesDeleteUser(id, upn);
    // TODO: test validations
  }

  /**
   * Gets the contents of the given version of the given blob
   *
   *
   */
  @Test
  public void spacesGetBlobContentsTest() {
    final String id;
    final Integer version;
    // File response = api.spacesGetBlobContents(id, version);
    // TODO: test validations
  }

  /**
   * Gets the contents of the latest version of the given blob
   *
   *
   */
  @Test
  public void spacesGetLatestBlobContentsTest() {
    final String id;
    // File response = api.spacesGetLatestBlobContents(id);
    // TODO: test validations
  }

  /**
   * Gets the aggregate of the values of the child sensors of the given type
   *
   *
   */
  @Test
  public void spacesGetValueTest() {
    final String id;
    final String sensorDataTypes;
    // List<SensorValue> response = api.spacesGetValue(id,
    // sensorDataTypes);
    // TODO: test validations
  }

  /**
   * Gets the aggregate of the values of the child sensors of the given type
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void spacesGetValueTestQueryMap() {
    final String id;
    final SpacesApi.SpacesGetValueQueryParams queryParams =
        new SpacesApi.SpacesGetValueQueryParams().sensorDataTypes(null);
    // List<SensorValue> response = api.spacesGetValue(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Moves the users with provided UPNs from one space to another
   *
   *
   */
  @Test
  public void spacesMoveUsersToSpaceTest() {
    final List<UserCreate> body;
    final String toSpaceId;
    final String id;
    final Boolean resetLocation;
    // Object response = api.spacesMoveUsersToSpace(body, toSpaceId, id,
    // resetLocation);
    // TODO: test validations
  }

  /**
   * Moves the users with provided UPNs from one space to another
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void spacesMoveUsersToSpaceTestQueryMap() {
    final List<UserCreate> body;
    final String id;
    final SpacesApi.SpacesMoveUsersToSpaceQueryParams queryParams =
        new SpacesApi.SpacesMoveUsersToSpaceQueryParams().tospaceId(null).resetLocation(null);
    // Object response = api.spacesMoveUsersToSpace(body, id, queryParams);
    // TODO: test validations
  }

  /**
   * Gets a list of spaces
   *
   *
   */
  @Test
  public void spacesRetrieveTest() {
    final String ids;
    final String name;
    final String types;
    final String subtypes;
    final String statuses;
    final Boolean useParentSpace;
    final String userUpn;
    final String sensorDataTypes;
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
    // List<SpaceRetrieveWithChildren> response =
    // api.spacesRetrieve(ids, name, types, subtypes, statuses,
    // useParentSpace, userUpn, sensorDataTypes, includes, propertyKey,
    // propertyValue, propertyValueSearchType, spaceId, traverse, minLevel,
    // maxLevel, minRelative, maxRelative);
    // TODO: test validations
  }

  /**
   * Gets a list of spaces
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void spacesRetrieveTestQueryMap() {
    final SpacesApi.SpacesRetrieveQueryParams queryParams =
        new SpacesApi.SpacesRetrieveQueryParams().ids(null).name(null).types(null).subtypes(null)
            .statuses(null).useParentSpace(null).userUpn(null).sensorDataTypes(null).includes(null)
            .propertyKey(null).propertyValue(null).propertyValueSearchType(null).spaceId(null)
            .traverse(null).minLevel(null).maxLevel(null).minRelative(null).maxRelative(null);
    // List<SpaceRetrieveWithChildren> response =
    // api.spacesRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Gets a list of blobs
   *
   *
   */
  @Test
  public void spacesRetrieveBlobMetadataTest() {
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
    // List<BlobMetadataRetrieve> response =
    // api.spacesRetrieveBlobMetadata(names, ids, sharings, types, subtypes,
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
  public void spacesRetrieveBlobMetadataTestQueryMap() {
    final SpacesApi.SpacesRetrieveBlobMetadataQueryParams queryParams =
        new SpacesApi.SpacesRetrieveBlobMetadataQueryParams().names(null).ids(null).sharings(null)
            .types(null).subtypes(null).includes(null).spaceId(null).traverse(null).minLevel(null)
            .maxLevel(null).minRelative(null).maxRelative(null);
    // List<BlobMetadataRetrieve> response =
    // api.spacesRetrieveBlobMetadata(queryParams);
    // TODO: test validations
  }

  /**
   * Gets a blob
   *
   *
   */
  @Test
  public void spacesRetrieveBlobMetadataByIdTest() {
    final String id;
    final String includes;
    // BlobMetadataRetrieve response =
    // api.spacesRetrieveBlobMetadataById(id, includes);
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
  public void spacesRetrieveBlobMetadataByIdTestQueryMap() {
    final String id;
    final SpacesApi.SpacesRetrieveBlobMetadataByIdQueryParams queryParams =
        new SpacesApi.SpacesRetrieveBlobMetadataByIdQueryParams().includes(null);
    // BlobMetadataRetrieve response =
    // api.spacesRetrieveBlobMetadataById(id, queryParams);
    // TODO: test validations
  }

  /**
   * Gets a specific space
   *
   *
   */
  @Test
  public void spacesRetrieveByIdTest() {
    final String id;
    final String includes;
    final String sensorDataTypes;
    // SpaceRetrieveWithChildren response = api.spacesRetrieveById(id,
    // includes, sensorDataTypes);
    // TODO: test validations
  }

  /**
   * Gets a specific space
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void spacesRetrieveByIdTestQueryMap() {
    final String id;
    final SpacesApi.SpacesRetrieveByIdQueryParams queryParams =
        new SpacesApi.SpacesRetrieveByIdQueryParams().includes(null).sensorDataTypes(null);
    // SpaceRetrieveWithChildren response = api.spacesRetrieveById(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Gets the first keystore by walking up the spaces hierarchy
   *
   *
   */
  @Test
  public void spacesRetrieveKeyStoreTest() {
    final String id;
    final String includes;
    // KeyStoreRetrieve response = api.spacesRetrieveKeyStore(id, includes);
    // TODO: test validations
  }

  /**
   * Gets the first keystore by walking up the spaces hierarchy
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void spacesRetrieveKeyStoreTestQueryMap() {
    final String id;
    final SpacesApi.SpacesRetrieveKeyStoreQueryParams queryParams =
        new SpacesApi.SpacesRetrieveKeyStoreQueryParams().includes(null);
    // KeyStoreRetrieve response = api.spacesRetrieveKeyStore(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Gets the first space of the given type by walking up the spaces hierarchy
   *
   *
   */
  @Test
  public void spacesRetrieveParentTest() {
    final String id;
    final String spaceType;
    final String includes;
    final String sensorDataTypes;
    // SpaceRetrieveWithChildren response = api.spacesRetrieveParent(id,
    // spaceType, includes, sensorDataTypes);
    // TODO: test validations
  }

  /**
   * Gets the first space of the given type by walking up the spaces hierarchy
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void spacesRetrieveParentTestQueryMap() {
    final String id;
    final SpacesApi.SpacesRetrieveParentQueryParams queryParams =
        new SpacesApi.SpacesRetrieveParentQueryParams().spaceType(null).includes(null)
            .sensorDataTypes(null);
    // SpaceRetrieveWithChildren response = api.spacesRetrieveParent(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Gets the value of a property
   *
   *
   */
  @Test
  public void spacesRetrievePropertyTest() {
    final String id;
    final String name;
    // String response = api.spacesRetrieveProperty(id, name);
    // TODO: test validations
  }

  /**
   * Gets the first resource of the given type by walking up the spaces hierarchy
   *
   *
   */
  @Test
  public void spacesRetrieveResourceTest() {
    final String id;
    final String type;
    final String includes;
    // SpaceResourceRetrieve response = api.spacesRetrieveResource(id, type,
    // includes);
    // TODO: test validations
  }

  /**
   * Gets the first resource of the given type by walking up the spaces hierarchy
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void spacesRetrieveResourceTestQueryMap() {
    final String id;
    final String type;
    final SpacesApi.SpacesRetrieveResourceQueryParams queryParams =
        new SpacesApi.SpacesRetrieveResourceQueryParams().includes(null);
    // SpaceResourceRetrieve response = api.spacesRetrieveResource(id, type,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Get a user in the given space
   *
   *
   */
  @Test
  public void spacesRetrieveUserByUpnTest() {
    final String id;
    final String upn;
    final String includes;
    // UserRetrieve response = api.spacesRetrieveUserByUpn(id, upn,
    // includes);
    // TODO: test validations
  }

  /**
   * Get a user in the given space
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void spacesRetrieveUserByUpnTestQueryMap() {
    final String id;
    final String upn;
    final SpacesApi.SpacesRetrieveUserByUpnQueryParams queryParams =
        new SpacesApi.SpacesRetrieveUserByUpnQueryParams().includes(null);
    // UserRetrieve response = api.spacesRetrieveUserByUpn(id, upn,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Gets the list of users associated with the given space
   *
   *
   */
  @Test
  public void spacesRetrieveUsersTest() {
    final String id;
    final String traverse;
    final Boolean unmapped;
    final String firstName;
    final String lastName;
    final String includes;
    final String propertyKey;
    final String propertyValue;
    final String propertyValueSearchType;
    // List<UserRetrieve> response = api.spacesRetrieveUsers(id,
    // traverse, unmapped, firstName, lastName, includes, propertyKey,
    // propertyValue, propertyValueSearchType);
    // TODO: test validations
  }

  /**
   * Gets the list of users associated with the given space
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void spacesRetrieveUsersTestQueryMap() {
    final String id;
    final SpacesApi.SpacesRetrieveUsersQueryParams queryParams =
        new SpacesApi.SpacesRetrieveUsersQueryParams().traverse(null).unmapped(null).firstName(null)
            .lastName(null).includes(null).propertyKey(null).propertyValue(null)
            .propertyValueSearchType(null);
    // List<UserRetrieve> response = api.spacesRetrieveUsers(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Updates a space
   *
   *
   */
  @Test
  public void spacesUpdateTest() {
    final SpaceUpdate body;
    final String id;
    // api.spacesUpdate(body, id);
    // TODO: test validations
  }

  /**
   * Updates a blob
   *
   * This is a multi-part request. For more information, see sample app or doc examples.
   */
  @Test
  public void spacesUpdateBlobTest() {
    final String id;
    final String metadata;
    final File contents;
    // api.spacesUpdateBlob(id, metadata, contents);
    // TODO: test validations
  }

  /**
   * Creates or updates property values
   *
   *
   */
  @Test
  public void spacesUpdatePropertiesTest() {
    final List<ExtendedPropertyCreate> body;
    final String id;
    // Object response = api.spacesUpdateProperties(body, id);
    // TODO: test validations
  }
}
