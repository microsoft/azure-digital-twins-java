/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import com.microsoft.twins.EncodingUtils;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.model.KeyStoreCreate;
import com.microsoft.twins.model.KeyStoreRetrieve;
import com.microsoft.twins.model.KeyStoreUpdate;
import com.microsoft.twins.model.SecurityKeyRetrieve;
import com.microsoft.twins.model.SecurityKeyUpdate;
import feign.Headers;
import feign.Param;
import feign.QueryMap;
import feign.RequestLine;

public interface KeyStoresApi extends TwinsApiClient.Api {
  /**
   * Creates a key store
   *
   * @param body The store information (required)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/keystores")
  @Headers({"Accept: */*",})
  UUID keyStoresCreate(KeyStoreCreate body);

  /**
   * Creates a key store
   *
   * @param name (optional)
   * @param description (optional)
   * @param spaceId (optional)
   * @return UUID
   */
  @RequestLine("POST /api/v1.0/keystores")
  @Headers({"Accept: */*",})
  UUID keyStoresCreate(@Param("name") String name, @Param("description") String description,
      @Param("spaceId") UUID spaceId);

  /**
   * Creates a new key
   *
   * @param id The key store identifier (required)
   * @return Integer
   */
  @RequestLine("POST /api/v1.0/keystores/{id}/keys")
  @Headers({"Accept: */*",})
  Integer keyStoresCreateKey(@Param("id") String id);

  /**
   * Deletes a key store
   *
   * @param id The identifier (required)
   */
  @RequestLine("DELETE /api/v1.0/keystores/{id}")
  @Headers({"Accept: */*",})
  void keyStoresDelete(@Param("id") String id);

  /**
   * Deletes the given key
   *
   * @param id The store identifier (required)
   * @param key The key identifier (required)
   */
  @RequestLine("DELETE /api/v1.0/keystores/{id}/keys/{key}")
  @Headers({"Accept: */*",})
  void keyStoresDeleteKey(@Param("id") String id, @Param("key") Integer key);

  /**
   * Gets a token for the specified device using the specified key
   *
   * @param id The key store identifier (required)
   * @param key The key identifier (required)
   * @param deviceMac The device to generate the token for (required)
   * @return String
   */
  @RequestLine("GET /api/v1.0/keystores/{id}/keys/{key}/token?deviceMac={deviceMac}")
  @Headers({"Accept: */*",})
  String keyStoresGenerateTokenFromKeyById(@Param("id") String id, @Param("key") Integer key,
      @Param("deviceMac") String deviceMac);

  /**
   * Gets a token for the specified device using the specified key
   *
   * Note, this is equivalent to the other <code>keyStoresGenerateTokenFromKeyById</code> method,
   * but with the query parameters collected into a single Map parameter. This is convenient for
   * services with optional query parameters, especially when used with the
   * {@link KeyStoresGenerateTokenFromKeyByIdQueryParams} class that allows for building up this map
   * in a fluent style.
   *
   * @param id The key store identifier (required)
   * @param key The key identifier (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>deviceMac - The device to generate the token for (required)</li>
   *        </ul>
   * @return String
   *
   */
  @RequestLine("GET /api/v1.0/keystores/{id}/keys/{key}/token?deviceMac={deviceMac}")
  @Headers({"Content-Type: */*",})
  String keyStoresGenerateTokenFromKeyById(@Param("id") String id, @Param("key") Integer key,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>keyStoresGenerateTokenFromKeyById</code> method in a fluent style.
   */
  public static class KeyStoresGenerateTokenFromKeyByIdQueryParams extends HashMap<String, Object> {
    public KeyStoresGenerateTokenFromKeyByIdQueryParams deviceMac(final String value) {
      put("deviceMac", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Gets a token for the specified device for the last valid key.
   *
   * @param id The identifier (required)
   * @param deviceMac The device to generate the token for (required)
   * @return String
   */
  @RequestLine("GET /api/v1.0/keystores/{id}/keys/last/token?deviceMac={deviceMac}")
  @Headers({"Accept: */*",})
  String keyStoresGenerateTokenFromLastKey(@Param("id") String id,
      @Param("deviceMac") String deviceMac);

  /**
   * Gets a token for the specified device for the last valid key.
   *
   * Note, this is equivalent to the other <code>keyStoresGenerateTokenFromLastKey</code> method,
   * but with the query parameters collected into a single Map parameter. This is convenient for
   * services with optional query parameters, especially when used with the
   * {@link KeyStoresGenerateTokenFromLastKeyQueryParams} class that allows for building up this map
   * in a fluent style.
   *
   * @param id The identifier (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>deviceMac - The device to generate the token for (required)</li>
   *        </ul>
   * @return String
   *
   */
  @RequestLine("GET /api/v1.0/keystores/{id}/keys/last/token?deviceMac={deviceMac}")
  @Headers({"Content-Type: */*",})
  String keyStoresGenerateTokenFromLastKey(@Param("id") String id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the
   * <code>keyStoresGenerateTokenFromLastKey</code> method in a fluent style.
   */
  public static class KeyStoresGenerateTokenFromLastKeyQueryParams extends HashMap<String, Object> {
    public KeyStoresGenerateTokenFromLastKeyQueryParams deviceMac(final String value) {
      put("deviceMac", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Retrieves key stores
   *
   * @param spaceId Optional filter on parent space id (optional)
   * @param includes Comma separated list of what to include, for example \&quot;Space,Keys\&quot;.
   *        Defaults to None (optional)
   * @return java.util.List&lt;KeyStoreRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/keystores?spaceId={spaceId}&includes={includes}")
  @Headers({"Accept: */*",})
  java.util.List<KeyStoreRetrieve> keyStoresRetrieve(@Param("spaceId") String spaceId,
      @Param("includes") String includes);

  /**
   * Retrieves key stores
   *
   * Note, this is equivalent to the other <code>keyStoresRetrieve</code> method, but with the query
   * parameters collected into a single Map parameter. This is convenient for services with optional
   * query parameters, especially when used with the {@link KeyStoresRetrieveQueryParams} class that
   * allows for building up this map in a fluent style.
   *
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>spaceId - Optional filter on parent space id (optional)</li>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;Space,Keys\&quot;. Defaults to None (optional)</li>
   *        </ul>
   * @return java.util.List&lt;KeyStoreRetrieve&gt;
   *
   */
  @RequestLine("GET /api/v1.0/keystores?spaceId={spaceId}&includes={includes}")
  @Headers({"Content-Type: */*",})
  java.util.List<KeyStoreRetrieve> keyStoresRetrieve(
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>keyStoresRetrieve</code>
   * method in a fluent style.
   */
  public static class KeyStoresRetrieveQueryParams extends HashMap<String, Object> {
    public KeyStoresRetrieveQueryParams spaceId(final String value) {
      put("spaceId", EncodingUtils.encode(value));
      return this;
    }

    public KeyStoresRetrieveQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Retrieves a key store
   *
   * @param id The identifier (required)
   * @param includes Comma separated list of what to include, for example \&quot;Space,Keys\&quot;.
   *        Defaults to None (optional)
   * @return KeyStoreRetrieve
   */
  @RequestLine("GET /api/v1.0/keystores/{id}?includes={includes}")
  @Headers({"Accept: */*",})
  KeyStoreRetrieve keyStoresRetrieveById(@Param("id") String id,
      @Param("includes") String includes);

  /**
   * Retrieves a key store
   *
   * Note, this is equivalent to the other <code>keyStoresRetrieveById</code> method, but with the
   * query parameters collected into a single Map parameter. This is convenient for services with
   * optional query parameters, especially when used with the
   * {@link KeyStoresRetrieveByIdQueryParams} class that allows for building up this map in a fluent
   * style.
   *
   * @param id The identifier (required)
   * @param queryParams Map of query parameters as name-value pairs
   *        <p>
   *        The following elements may be specified in the query map:
   *        </p>
   *        <ul>
   *        <li>includes - Comma separated list of what to include, for example
   *        \&quot;Space,Keys\&quot;. Defaults to None (optional)</li>
   *        </ul>
   * @return KeyStoreRetrieve
   *
   */
  @RequestLine("GET /api/v1.0/keystores/{id}?includes={includes}")
  @Headers({"Content-Type: */*",})
  KeyStoreRetrieve keyStoresRetrieveById(@Param("id") String id,
      @QueryMap(encoded = true) Map<String, Object> queryParams);

  /**
   * A convenience class for generating query parameters for the <code>keyStoresRetrieveById</code>
   * method in a fluent style.
   */
  public static class KeyStoresRetrieveByIdQueryParams extends HashMap<String, Object> {
    public KeyStoresRetrieveByIdQueryParams includes(final String value) {
      put("includes", EncodingUtils.encode(value));
      return this;
    }
  }

  /**
   * Retrieves the given key
   *
   * @param id The key store identifier (required)
   * @param key The key identifier (required)
   * @return SecurityKeyRetrieve
   */
  @RequestLine("GET /api/v1.0/keystores/{id}/keys/{key}")
  @Headers({"Accept: */*",})
  SecurityKeyRetrieve keyStoresRetrieveKeyById(@Param("id") String id, @Param("key") Integer key);

  /**
   * Retrieves the store&#x27;s keys
   *
   * @param id The identifier (required)
   * @return java.util.List&lt;SecurityKeyRetrieve&gt;
   */
  @RequestLine("GET /api/v1.0/keystores/{id}/keys")
  @Headers({"Accept: */*",})
  java.util.List<SecurityKeyRetrieve> keyStoresRetrieveKeys(@Param("id") String id);

  /**
   * Retrieves the most recent valid key from the key store
   *
   * @param id The identifier (required)
   * @return SecurityKeyRetrieve
   */
  @RequestLine("GET /api/v1.0/keystores/{id}/keys/last")
  @Headers({"Accept: */*",})
  SecurityKeyRetrieve keyStoresRetrieveKeysLast(@Param("id") String id);

  /**
   * Updates a key store
   *
   * @param body The store information (required)
   * @param id The identifier (required)
   */
  @RequestLine("PATCH /api/v1.0/keystores/{id}")
  @Headers({"Accept: */*",})
  void keyStoresUpdate(KeyStoreUpdate body, @Param("id") String id);

  /**
   * Updates a key store
   *
   * @param id The identifier (required)
   * @param name (optional)
   * @param description (optional)
   */
  @RequestLine("PATCH /api/v1.0/keystores/{id}")
  @Headers({"Accept: */*",})
  void keyStoresUpdate(@Param("id") String id, @Param("name") String name,
      @Param("description") String description);

  /**
   * Updates the given key
   *
   * @param body The key data (required)
   * @param id The store identifier (required)
   * @param key The key identifier (required)
   */
  @RequestLine("PATCH /api/v1.0/keystores/{id}/keys/{key}")
  @Headers({"Accept: */*",})
  void keyStoresUpdateKey(SecurityKeyUpdate body, @Param("id") String id,
      @Param("key") Integer key);

  /**
   * Updates the given key
   *
   * @param id The store identifier (required)
   * @param key The key identifier (required)
   * @param status (optional)
   */
  @RequestLine("PATCH /api/v1.0/keystores/{id}/keys/{key}")
  @Headers({"Accept: */*",})
  void keyStoresUpdateKey(@Param("id") String id, @Param("key") Integer key,
      @Param("status") String status);
}
