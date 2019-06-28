/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.telemetry;

import javax.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("azure.event-hubs.test")
public class TestConfigurationProperties {

  @NotEmpty
  private String connectionString;

  @NotEmpty
  private String secondaryConnectionString;

  @NotEmpty
  private String devicesHubname;

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

  public String getDevicesHubname() {
    return devicesHubname;
  }

  public void setDevicesHubname(final String devicesHubname) {
    this.devicesHubname = devicesHubname;
  }



}
