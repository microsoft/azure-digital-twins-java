/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.telemetry.ingress;

import javax.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("azure.event-hubs")
public class TestConfigurationProperties {

  @NotEmpty
  private String connectionString;

  @NotEmpty
  private String secondaryConnectionString;

  @NotEmpty
  private String hubname;

  public String getConnectionString() {
    return connectionString;
  }

  public void setConnectionString(final String connectionString) {
    this.connectionString = connectionString;
  }

  public String getSecondaryConnectionString() {
    return secondaryConnectionString;
  }

  public void setSecondaryConnectionString(final String secondaryConnectionString) {
    this.secondaryConnectionString = secondaryConnectionString;
  }

  public String getHubname() {
    return hubname;
  }

  public void setHubname(final String hubname) {
    this.hubname = hubname;
  }



}
