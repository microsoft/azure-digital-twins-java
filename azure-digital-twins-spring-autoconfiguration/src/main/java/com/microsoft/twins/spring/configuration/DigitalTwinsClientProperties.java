/**
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package com.microsoft.twins.spring.configuration;

import java.net.URL;
import java.time.Duration;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("com.microsoft.twins")
@Getter
@Setter
@Validated
public class DigitalTwinsClientProperties {

  @NotNull
  @Valid
  private URL twinsUrl;

  /**
   * Azure Active Directory properties.
   */
  @NotNull
  @Valid
  private AAD aad = new AAD();

  @Getter
  @Setter
  public static class AAD {
    /**
     * AAD tenant, e.g. XXX.onmicrosoft.com.
     */
    @NotBlank
    private String tenant;

    /**
     * AAD application client ID.
     */
    @NotBlank
    private String clientId;

    /**
     * AAD application client secret.
     */
    @NotBlank
    private String clientSecret;

    /**
     * AAD authority host.
     */
    @NotBlank
    private String authorityHost = "https://login.microsoftonline.com";

    /**
     * AAD bearer token request timeout.
     */
    @NotNull
    private Duration timeout = Duration.ofSeconds(5);
  }
}
