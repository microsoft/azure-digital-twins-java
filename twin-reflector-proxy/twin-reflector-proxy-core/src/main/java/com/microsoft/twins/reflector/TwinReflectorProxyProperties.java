/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import java.util.UUID;
import javax.validation.Valid;
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

  private UUID tenant;

  private final EventHubs eventHubs = new EventHubs();


  @Getter
  @Setter
  @Validated
  public static class EventHubs {

    @NotEmpty
    private String connectionString;

    @NotEmpty
    private String secondaryConnectionString;

    @NotEmpty
    private String namespace;

    @Valid
    private final Hub topologyOperations = new Hub();

    @Valid
    private final Hub ingress = new Hub();

    @Getter
    @Setter
    public static class Hub {
      @NotEmpty
      private String hubname;

      @NotEmpty
      private String consumerGroup;
    }
  }

}
