/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import javax.validation.constraints.NotEmpty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("com.microsoft.twins.reflector")
@Getter
@Setter
@Validated
public class TwinReflectorProxyProperties {

  private final TopologyChangeRegistration topologyChangeRegistration = new TopologyChangeRegistration();

  @Getter
  @Setter
  @Validated
  public static class TopologyChangeRegistration {

    @NotEmpty
    private String connectionString;

    @NotEmpty
    private String secondaryConnectionString;

    @NotEmpty
    private String hubname;
  }

}
