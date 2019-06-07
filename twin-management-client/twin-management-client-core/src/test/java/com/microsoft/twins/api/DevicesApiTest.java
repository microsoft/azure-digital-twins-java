/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.io.File;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import com.microsoft.twins.model.DeviceCreate;
import com.microsoft.twins.model.DeviceUpdate;
import com.microsoft.twins.model.ExtendedPropertyCreate;

/**
 * API tests for DevicesApi
 */
public class DevicesApiTest extends AbstractApiTest {
  private DevicesApi api;

  @Before
  public void setup() {
    api = TWINS_API_CLIENT.getDevicesApi();
  }

  /**
   * Creates a device
   *
   *
   */
  @Test
  public void devicesCreateTest() {
    final DeviceCreate body;
    // UUID response = api.devicesCreate(body);
    // TODO: test validations
  }

  /**
   * Creates a blob
   *
   * This is a multi-part request. For more information, see sample app or doc examples. Key value
   * pairs specified in the Content-Disposition header in the blob-chunk of the multipart request will
   * be preserved as meta-data on the stored blob.
   */
  @Test
  public void devicesCreateBlobTest() {
    final String metadata;
    final File contents;
    // UUID response = api.devicesCreateBlob(metadata, contents);
    // TODO: test validations
  }

  /**
   * Creates or updates a device using Name and SpaceId as the unique key
   *
   *
   */
  @Test
  public void devicesCreateOrUpdateTest() {
    final DeviceCreate body;
    // UUID response = api.devicesCreateOrUpdate(body);
    // TODO: test validations
  }

  /**
   * Creates a property value
   *
   *
   */
  @Test
  public void devicesCreatePropertyTest() {
    final ExtendedPropertyCreate body;
    final String id;
    // String response = api.devicesCreateProperty(body, id);
    // TODO: test validations
  }

  /**
   * Deletes a device and its children such as sensors, blobs, ...
   *
   *
   */
  @Test
  public void devicesDeleteTest() {
    final String id;
    // api.devicesDelete(id);
    // TODO: test validations
  }

  /**
   * Deletes a blob
   *
   * Deleting a blob will delete its metadata, its content (all versions) and its associations
   */
  @Test
  public void devicesDeleteBlobTest() {
    final String id;
    // api.devicesDeleteBlob(id);
    // TODO: test validations
  }

  /**
   * Delete the contents of the given version of the given blob
   *
   * Delete will fail if this version has any associations
   */
  @Test
  public void devicesDeleteBlobContentsTest() {
    final String id;
    final Integer version;
    // api.devicesDeleteBlobContents(id, version);
    // TODO: test validations
  }

  /**
   * Deletes all property values
   *
   *
   */
  @Test
  public void devicesDeletePropertiesTest() {
    final String id;
    // api.devicesDeleteProperties(id);
    // TODO: test validations
  }

  /**
   * Deletes the given property value
   *
   *
   */
  @Test
  public void devicesDeletePropertyTest() {
    final String id;
    final String name;
    // api.devicesDeleteProperty(id, name);
    // TODO: test validations
  }

  /**
   * Gets the contents of the given version of the given blob
   *
   *
   */
  @Test
  public void devicesGetBlobContentsTest() {
    final String id;
    final Integer version;
    // File response = api.devicesGetBlobContents(id, version);
    // TODO: test validations
  }

  /**
   * Gets the contents of the latest version of the given blob
   *
   *
   */
  @Test
  public void devicesGetLatestBlobContentsTest() {
    final String id;
    // File response = api.devicesGetLatestBlobContents(id);
    // TODO: test validations
  }

  /**
   * Gets the first resource of the given type by walking up the spaces hierarchy
   *
   *
   */
  @Test
  public void devicesGetResourceTest() {
    final String id;
    final String type;
    final String includes;
    // SpaceResourceRetrieve response = api.devicesGetResource(id, type,
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
  public void devicesGetResourceTestQueryMap() {
    final String id;
    final String type;
    final DevicesApi.DevicesGetResourceQueryParams queryParams =
        new DevicesApi.DevicesGetResourceQueryParams().includes(null);
    // SpaceResourceRetrieve response = api.devicesGetResource(id, type,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Registers a device to its parent space&#x27;s IoT Hub resource
   *
   *
   */
  @Test
  public void devicesRegisterTest() {
    final String id;
    // api.devicesRegister(id);
    // TODO: test validations
  }

  /**
   * Gets a list of devices
   *
   *
   */
  @Test
  public void devicesRetrieveTest() {
    final String ids;
    final String hardwareIds;
    final String names;
    final String types;
    final String subtypes;
    final String gateways;
    final String status;
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
    // List<DeviceRetrieve> response = api.devicesRetrieve(ids,
    // hardwareIds, names, types, subtypes, gateways, status, includes,
    // propertyKey, propertyValue, propertyValueSearchType, spaceId,
    // traverse, minLevel, maxLevel, minRelative, maxRelative);
    // TODO: test validations
  }

  /**
   * Gets a list of devices
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void devicesRetrieveTestQueryMap() {
    final DevicesApi.DevicesRetrieveQueryParams queryParams = new DevicesApi.DevicesRetrieveQueryParams().ids(null)
        .hardwareIds(null).names(null).types(null).subtypes(null).gateways(null).status(null).includes(null)
        .propertyKey(null).propertyValue(null).propertyValueSearchType(null).spaceId(null).traverse(null).minLevel(null)
        .maxLevel(null).minRelative(null).maxRelative(null);
    // List<DeviceRetrieve> response =
    // api.devicesRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Gets a list of blobs
   *
   *
   */
  @Test
  public void devicesRetrieveBlobMetadataTest() {
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
    // api.devicesRetrieveBlobMetadata(names, ids, sharings, types,
    // subtypes, includes, spaceId, traverse, minLevel, maxLevel,
    // minRelative, maxRelative);
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
  public void devicesRetrieveBlobMetadataTestQueryMap() {
    final DevicesApi.DevicesRetrieveBlobMetadataQueryParams queryParams =
        new DevicesApi.DevicesRetrieveBlobMetadataQueryParams().names(null).ids(null).sharings(null).types(null)
            .subtypes(null).includes(null).spaceId(null).traverse(null).minLevel(null).maxLevel(null).minRelative(null)
            .maxRelative(null);
    // List<BlobMetadataRetrieve> response =
    // api.devicesRetrieveBlobMetadata(queryParams);
    // TODO: test validations
  }

  /**
   * Gets a blob
   *
   *
   */
  @Test
  public void devicesRetrieveBlobMetadataByIdTest() {
    final String id;
    final String includes;
    // BlobMetadataRetrieve response =
    // api.devicesRetrieveBlobMetadataById(id, includes);
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
  public void devicesRetrieveBlobMetadataByIdTestQueryMap() {
    final String id;
    final DevicesApi.DevicesRetrieveBlobMetadataByIdQueryParams queryParams =
        new DevicesApi.DevicesRetrieveBlobMetadataByIdQueryParams().includes(null);
    // BlobMetadataRetrieve response =
    // api.devicesRetrieveBlobMetadataById(id, queryParams);
    // TODO: test validations
  }

  /**
   * Gets a specific device
   *
   *
   */
  @Test
  public void devicesRetrieveByIdTest() {
    final String id;
    final String includes;
    // DeviceRetrieve response = api.devicesRetrieveById(id, includes);
    // TODO: test validations
  }

  /**
   * Gets a specific device
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void devicesRetrieveByIdTestQueryMap() {
    final String id;
    final DevicesApi.DevicesRetrieveByIdQueryParams queryParams =
        new DevicesApi.DevicesRetrieveByIdQueryParams().includes(null);
    // DeviceRetrieve response = api.devicesRetrieveById(id, queryParams);
    // TODO: test validations
  }

  /**
   * Gets the first keystore by walking up the spaces hierarchy
   *
   *
   */
  @Test
  public void devicesRetrieveKeyStoreTest() {
    final String id;
    final String includes;
    // KeyStoreRetrieve response = api.devicesRetrieveKeyStore(id,
    // includes);
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
  public void devicesRetrieveKeyStoreTestQueryMap() {
    final String id;
    final DevicesApi.DevicesRetrieveKeyStoreQueryParams queryParams =
        new DevicesApi.DevicesRetrieveKeyStoreQueryParams().includes(null);
    // KeyStoreRetrieve response = api.devicesRetrieveKeyStore(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Gets the value of a property
   *
   *
   */
  @Test
  public void devicesRetrievePropertyTest() {
    final String id;
    final String name;
    // String response = api.devicesRetrieveProperty(id, name);
    // TODO: test validations
  }

  /**
   * Unregisters a device from its parent space&#x27;s IoT Hub resource
   *
   *
   */
  @Test
  public void devicesUnregisterTest() {
    final String id;
    // api.devicesUnregister(id);
    // TODO: test validations
  }

  /**
   * Updates a device
   *
   *
   */
  @Test
  public void devicesUpdateTest() {
    final DeviceUpdate body;
    final String id;
    // api.devicesUpdate(body, id);
    // TODO: test validations
  }

  /**
   * Updates a blob
   *
   * This is a multi-part request. For more information, see sample app or doc examples.
   */
  @Test
  public void devicesUpdateBlobTest() {
    final String id;
    final String metadata;
    final File contents;
    // api.devicesUpdateBlob(id, metadata, contents);
    // TODO: test validations
  }

  /**
   * Creates or updates property values
   *
   *
   */
  @Test
  public void devicesUpdatePropertiesTest() {
    final List<ExtendedPropertyCreate> body;
    final String id;
    // Object response = api.devicesUpdateProperties(body, id);
    // TODO: test validations
  }
}
