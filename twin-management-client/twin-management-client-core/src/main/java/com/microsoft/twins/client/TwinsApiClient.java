/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.client;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Optional;
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
import com.microsoft.twins.error.RetryOnStatusHandler;
import feign.Client;
import feign.Feign;
import feign.Logger;
import feign.RequestTemplate;
import feign.Retryer;
import feign.httpclient.ApacheHttpClient;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import feign.slf4j.Slf4jLogger;

public class TwinsApiClient {
  protected ObjectMapper objectMapper;
  private final String twinsUrl;
  private Feign.Builder feignBuilder;
  private AadRequestInterceptor aadRequestInterceptor;

  public TwinsApiClient(final String authorityHost, final String tenant, final String clientId,
      final String clientSecret, final Duration timeout, final String twinsUrl) {
    this(twinsUrl);

    aadRequestInterceptor = new AadRequestInterceptor(authorityHost, tenant,
        "0b07f429-9f4b-4714-9392-cc5e8e80c8b0", clientId, clientSecret, timeout);

    feignBuilder.requestInterceptor(aadRequestInterceptor);
    feignBuilder.requestInterceptor(new CorrelationIdRequestInterceptor());
  }

  public TwinsApiClient(final String twinsUrl, final Client client, final Retryer retryer) {
    this.twinsUrl = twinsUrl;
    objectMapper = createObjectMapper();
    feignBuilder =
        Feign.builder().encoder(new JacksonEncoderWithContentType(objectMapper)).client(client)
            .retryer(retryer).errorDecoder(new RetryOnStatusHandler()).logLevel(Logger.Level.FULL)
            .decoder(new JacksonDecoder(objectMapper)).logger(new Slf4jLogger());
  }

  public TwinsApiClient(final String twinsUrl) {
    this(twinsUrl, new ApacheHttpClient(), new Retryer.Default());
  }

  public Optional<String> getAccessTokenWithoutCache() {
    if (aadRequestInterceptor == null) {
      return Optional.empty();
    }

    return aadRequestInterceptor.getAccessTokenWithoutCache();
  }

  public Feign.Builder getFeignBuilder() {
    return feignBuilder;
  }

  public TwinsApiClient setFeignBuilder(final Feign.Builder feignBuilder) {
    this.feignBuilder = feignBuilder;
    return this;
  }

  private static ObjectMapper createObjectMapper() {
    final ObjectMapper mapper = new ObjectMapper();
    mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
    mapper.enable(DeserializationFeature.READ_ENUMS_USING_TO_STRING);
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.disable(DeserializationFeature.FAIL_ON_INVALID_SUBTYPE);
    mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSS"));
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

  public ObjectMapper getObjectMapper() {
    return objectMapper;
  }

  public DevicesApi getDevicesApi() {
    return feignBuilder.target(DevicesApi.class, twinsUrl);
  }

  public EndpointsApi getEndpointsApi() {
    return feignBuilder.target(EndpointsApi.class, twinsUrl);
  }

  public KeyStoresApi getKeyStoresApi() {
    return feignBuilder.target(KeyStoresApi.class, twinsUrl);
  }

  public MatchersApi getMatchersApi() {
    return feignBuilder.target(MatchersApi.class, twinsUrl);
  }

  public OntologiesApi getOntologiesApi() {
    return feignBuilder.target(OntologiesApi.class, twinsUrl);
  }

  public PropertyKeysApi getPropertyKeysApi() {
    return feignBuilder.target(PropertyKeysApi.class, twinsUrl);
  }

  public ResourcesApi getResourcesApi() {
    return feignBuilder.target(ResourcesApi.class, twinsUrl);
  }

  public RoleAssignmentsApi getRoleAssignmentsApi() {
    return feignBuilder.target(RoleAssignmentsApi.class, twinsUrl);
  }

  public SensorsApi getSensorsApi() {
    return feignBuilder.target(SensorsApi.class, twinsUrl);
  }

  public SpacesApi getSpacesApi() {
    return feignBuilder.target(SpacesApi.class, twinsUrl);
  }

  public SystemApi getSystemApi() {
    return feignBuilder.target(SystemApi.class, twinsUrl);
  }

  public TypesApi getTypesApi() {
    return feignBuilder.target(TypesApi.class, twinsUrl);
  }

  public UserDefinedFunctionsApi getUserDefinedFunctionsApi() {
    return feignBuilder.target(UserDefinedFunctionsApi.class, twinsUrl);
  }

  public UsersApi getUsersApi() {
    return feignBuilder.target(UsersApi.class, twinsUrl);
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
