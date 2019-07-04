/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.spring.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import com.microsoft.twins.TwinsApiClient;
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

@Configuration
@EnableConfigurationProperties(DigitalTwinsClientProperties.class)
@PropertySource("classpath:/digitial-twins-client-defaults.properties")
public class DigitalTwinClientAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  @ConditionalOnClass(TwinsApiClient.class)
  TwinsApiClient twinsApiClient(final DigitalTwinsClientProperties properties) {

    return new TwinsApiClient(properties.getAad().getAuthorityHost(),
        properties.getAad().getTenant(), properties.getAad().getClientId(),
        properties.getAad().getClientSecret(), properties.getAad().getTimeout(),
        properties.getTwinsUrl().toString());
  }

  @Bean
  DevicesApi devicesApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getDevicesApi();
  }

  @Bean
  EndpointsApi endpointsApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getEndpointsApi();
  }

  @Bean
  KeyStoresApi keyStoresApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getKeyStoresApi();
  }

  @Bean
  MatchersApi matchersApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getMatchersApi();
  }

  @Bean
  OntologiesApi ontologiesApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getOntologiesApi();
  }

  @Bean
  PropertyKeysApi propertyKeysApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getPropertyKeysApi();
  }

  @Bean
  ResourcesApi resourcesApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getResourcesApi();
  }

  @Bean
  RoleAssignmentsApi roleAssignmentsApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getRoleAssignmentsApi();
  }

  @Bean
  SensorsApi sensorsApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getSensorsApi();
  }

  @Bean
  SpacesApi spacesApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getSpacesApi();
  }

  @Bean
  SystemApi systemApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getSystemApi();
  }

  @Bean
  TypesApi typesApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getTypesApi();
  }

  @Bean
  UserDefinedFunctionsApi userDefinedFunctionsApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getUserDefinedFunctionsApi();
  }

  @Bean
  UsersApi usersApi(final TwinsApiClient twinsApiClient) {
    return twinsApiClient.getUsersApi();
  }
}
