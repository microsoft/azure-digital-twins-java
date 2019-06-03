/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import org.junit.Before;
import org.junit.Test;
import com.microsoft.twins.model.KeyStoreCreate;
import com.microsoft.twins.model.KeyStoreUpdate;
import com.microsoft.twins.model.SecurityKeyUpdate;

/**
 * API tests for KeyStoresApi
 */
public class KeyStoresApiTest extends AbstractApiTest {
  private KeyStoresApi api;

  @Before
  public void setup() {
    api = TWINS_API_CLIENT.getKeyStoresApi();
  }

  /**
   * Creates a key store
   *
   *
   */
  @Test
  public void keyStoresCreateTest() {
    final KeyStoreCreate body;
    // UUID response = api.keyStoresCreate(body);
    // TODO: test validations
  }

  /**
   * Creates a new key
   *
   *
   */
  @Test
  public void keyStoresCreateKeyTest() {
    final String id;
    // Integer response = api.keyStoresCreateKey(id);
    // TODO: test validations
  }

  /**
   * Deletes a key store
   *
   *
   */
  @Test
  public void keyStoresDeleteTest() {
    final String id;
    // api.keyStoresDelete(id);
    // TODO: test validations
  }

  /**
   * Deletes the given key
   *
   *
   */
  @Test
  public void keyStoresDeleteKeyTest() {
    final String id;
    final Integer key;
    // api.keyStoresDeleteKey(id, key);
    // TODO: test validations
  }

  /**
   * Gets a token for the specified device using the specified key
   *
   *
   */
  @Test
  public void keyStoresGenerateTokenFromKeyByIdTest() {
    final String id;
    final Integer key;
    final String deviceMac;
    // String response = api.keyStoresGenerateTokenFromKeyById(id, key,
    // deviceMac);
    // TODO: test validations
  }

  /**
   * Gets a token for the specified device using the specified key
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void keyStoresGenerateTokenFromKeyByIdTestQueryMap() {
    final String id;
    final Integer key;
    final KeyStoresApi.KeyStoresGenerateTokenFromKeyByIdQueryParams queryParams =
        new KeyStoresApi.KeyStoresGenerateTokenFromKeyByIdQueryParams().deviceMac(null);
    // String response = api.keyStoresGenerateTokenFromKeyById(id, key,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Gets a token for the specified device for the last valid key.
   *
   *
   */
  @Test
  public void keyStoresGenerateTokenFromLastKeyTest() {
    final String id;
    final String deviceMac;
    // String response = api.keyStoresGenerateTokenFromLastKey(id,
    // deviceMac);
    // TODO: test validations
  }

  /**
   * Gets a token for the specified device for the last valid key.
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void keyStoresGenerateTokenFromLastKeyTestQueryMap() {
    final String id;
    final KeyStoresApi.KeyStoresGenerateTokenFromLastKeyQueryParams queryParams =
        new KeyStoresApi.KeyStoresGenerateTokenFromLastKeyQueryParams().deviceMac(null);
    // String response = api.keyStoresGenerateTokenFromLastKey(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Retrieves key stores
   *
   *
   */
  @Test
  public void keyStoresRetrieveTest() {
    final String spaceId;
    final String includes;
    // List<KeyStoreRetrieve> response =
    // api.keyStoresRetrieve(spaceId, includes);
    // TODO: test validations
  }

  /**
   * Retrieves key stores
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void keyStoresRetrieveTestQueryMap() {
    final KeyStoresApi.KeyStoresRetrieveQueryParams queryParams =
        new KeyStoresApi.KeyStoresRetrieveQueryParams().spaceId(null).includes(null);
    // List<KeyStoreRetrieve> response =
    // api.keyStoresRetrieve(queryParams);
    // TODO: test validations
  }

  /**
   * Retrieves a key store
   *
   *
   */
  @Test
  public void keyStoresRetrieveByIdTest() {
    final String id;
    final String includes;
    // KeyStoreRetrieve response = api.keyStoresRetrieveById(id, includes);
    // TODO: test validations
  }

  /**
   * Retrieves a key store
   *
   *
   *
   * This tests the overload of the method that uses a Map for query parameters instead of listing
   * them out individually.
   */
  @Test
  public void keyStoresRetrieveByIdTestQueryMap() {
    final String id;
    final KeyStoresApi.KeyStoresRetrieveByIdQueryParams queryParams =
        new KeyStoresApi.KeyStoresRetrieveByIdQueryParams().includes(null);
    // KeyStoreRetrieve response = api.keyStoresRetrieveById(id,
    // queryParams);
    // TODO: test validations
  }

  /**
   * Retrieves the given key
   *
   *
   */
  @Test
  public void keyStoresRetrieveKeyByIdTest() {
    final String id;
    final Integer key;
    // SecurityKeyRetrieve response = api.keyStoresRetrieveKeyById(id, key);
    // TODO: test validations
  }

  /**
   * Retrieves the store&#x27;s keys
   *
   *
   */
  @Test
  public void keyStoresRetrieveKeysTest() {
    final String id;
    // List<SecurityKeyRetrieve> response =
    // api.keyStoresRetrieveKeys(id);
    // TODO: test validations
  }

  /**
   * Retrieves the most recent valid key from the key store
   *
   *
   */
  @Test
  public void keyStoresRetrieveKeysLastTest() {
    final String id;
    // SecurityKeyRetrieve response = api.keyStoresRetrieveKeysLast(id);
    // TODO: test validations
  }

  /**
   * Updates a key store
   *
   *
   */
  @Test
  public void keyStoresUpdateTest() {
    final KeyStoreUpdate body;
    final String id;
    // api.keyStoresUpdate(body, id);
    // TODO: test validations
  }

  /**
   * Updates the given key
   *
   *
   */
  @Test
  public void keyStoresUpdateKeyTest() {
    final SecurityKeyUpdate body;
    final String id;
    final Integer key;
    // api.keyStoresUpdateKey(body, id, key);
    // TODO: test validations
  }
}
