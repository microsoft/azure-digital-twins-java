/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins;

import java.lang.reflect.Type;
import java.time.Duration;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.EndpointsApi;
import com.microsoft.twins.api.KeyStoresApi;
import com.microsoft.twins.api.MatchersApi;
import com.microsoft.twins.api.OntologiesApi;
import com.microsoft.twins.api.PropertyKeysApi;
import com.microsoft.twins.api.ResourcesApi;
import com.microsoft.twins.api.RoleAssignmentsApi;
import com.microsoft.twins.api.SensorsApi;
import com.microsoft.twins.api.SpacesApi;
import com.microsoft.twins.api.SystemApi;
import com.microsoft.twins.api.TypesApi;
import com.microsoft.twins.api.UserDefinedFunctionsApi;
import com.microsoft.twins.api.UsersApi;
import feign.Feign;
import feign.Logger;
import feign.RequestTemplate;
import feign.http2client.Http2Client;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

public class TwinsApiClient {
  public interface Api {
  }

  protected ObjectMapper objectMapper;
  private final String twinsUrl;
  private Feign.Builder feignBuilder;

  public TwinsApiClient(final String authorityHost, final String tenant, final String clientId,
      final String clientSecret, final Duration timeout, final String twinsUrl) {
    this(twinsUrl);

    final AadRequestInterceptor handler = new AadRequestInterceptor(authorityHost, tenant,
        "0b07f429-9f4b-4714-9392-cc5e8e80c8b0", clientId, clientSecret, timeout);

    feignBuilder.requestInterceptor(handler);
  }

  public TwinsApiClient(final String twinsUrl) {
    this.twinsUrl = twinsUrl;
    objectMapper = createObjectMapper();
    feignBuilder = Feign.builder().encoder(new JacksonEncoderWithContentType(objectMapper))
        .client(new Http2Client()).logLevel(Logger.Level.FULL)
        .decoder(new JacksonDecoder(objectMapper)).logger(new Slf4jLogger());
  }

  public Feign.Builder getFeignBuilder() {
    return feignBuilder;
  }

  public TwinsApiClient setFeignBuilder(final Feign.Builder feignBuilder) {
    this.feignBuilder = feignBuilder;
    return this;
  }

  private ObjectMapper createObjectMapper() {
    final ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    objectMapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    objectMapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    objectMapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    objectMapper.setDateFormat(new RFC3339DateFormat());
    objectMapper.registerModule(new JavaTimeModule());
    return objectMapper;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public DevicesApi getDevicesApi() {
    return buildClient(DevicesApi.class);
  }

  public EndpointsApi getEndpointsApi() {
    return buildClient(EndpointsApi.class);
  }

  public KeyStoresApi getKeyStoresApi() {
    return buildClient(KeyStoresApi.class);
  }

  public MatchersApi getMatchersApi() {
    return buildClient(MatchersApi.class);
  }

  public OntologiesApi getOntologiesApi() {
    return buildClient(OntologiesApi.class);
  }

  public PropertyKeysApi getPropertyKeysApi() {
    return buildClient(PropertyKeysApi.class);
  }

  public ResourcesApi getResourcesApi() {
    return buildClient(ResourcesApi.class);
  }

  public RoleAssignmentsApi getRoleAssignmentsApi() {
    return buildClient(RoleAssignmentsApi.class);
  }

  public SensorsApi getSensorsApi() {
    return buildClient(SensorsApi.class);
  }

  public SpacesApi getSpacesApi() {
    return buildClient(SpacesApi.class);
  }

  public SystemApi getSystemApi() {
    return buildClient(SystemApi.class);
  }

  public TypesApi getTypesApi() {
    return buildClient(TypesApi.class);
  }

  public UserDefinedFunctionsApi getUserDefinedFunctionsApi() {
    return buildClient(UserDefinedFunctionsApi.class);
  }

  public UsersApi getUsersApi() {
    return buildClient(UsersApi.class);
  }

  /**
   * Creates a feign client for given API interface.
   *
   * Usage: ApiClient apiClient = new ApiClient(); apiClient.setBasePath("http://localhost:8080");
   * XYZApi api = apiClient.buildClient(XYZApi.class); XYZResponse response = api.someMethod(...);
   *
   * @param <T> Type
   * @param clientClass Client class
   * @return The Client
   */
  private <T extends Api> T buildClient(final Class<T> clientClass) {
    return feignBuilder.target(clientClass, twinsUrl);
  }

  /**
   * Select the Accept header's value from the given accepts array: if JSON exists in the given
   * array, use it; otherwise use all of them (joining into a string)
   *
   * @param accepts The accepts array to select from
   * @return The Accept header to use. If the given array is empty, null will be returned (not to
   *         set the Accept header explicitly).
   */
  public String selectHeaderAccept(final String[] accepts) {
    if (accepts.length == 0) {
      return null;
    }
    if (StringUtil.containsIgnoreCase(accepts, "application/json")) {
      return "application/json";
    }
    return StringUtil.join(accepts, ",");
  }

  /**
   * Select the Content-Type header's value from the given array: if JSON exists in the given array,
   * use it; otherwise use the first one of the array.
   *
   * @param contentTypes The Content-Type array to select from
   * @return The Content-Type header to use. If the given array is empty, JSON will be used.
   */
  public String selectHeaderContentType(final String[] contentTypes) {
    if (contentTypes.length == 0) {
      return "application/json";
    }
    if (StringUtil.containsIgnoreCase(contentTypes, "application/json")) {
      return "application/json";
    }
    return contentTypes[0];
  }

  private static class JacksonEncoderWithContentType extends JacksonEncoder {
    public JacksonEncoderWithContentType(final ObjectMapper objectMapper) {
      super(objectMapper);
    }

    @Override
    public void encode(final Object object, final Type bodyType, final RequestTemplate template) {
      super.encode(object, bodyType, template);
      template.header("Content-type", "application/json");
    }
  }
}
