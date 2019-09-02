/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;
import lombok.Getter;
import lombok.Setter;

@ConfigurationProperties("com.microsoft.twins.reflector")
@Getter
@Setter
@Validated
public class TwinReflectorProxyProperties {

  @NotNull
  private UUID tenant;

  private UUID defaultGateway;

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
    private final Hub topologyOperations = new HubWithConsumer();

    @Valid
    private final Hub ingress = new HubWithConsumer();

    @Valid
    private final Hub feedback = new Hub();

    @Getter
    @Setter
    public static class HubWithConsumer extends Hub {
      @NotEmpty
      private String consumerGroup;
    }

    @Getter
    @Setter
    public static class Hub {
      @NotEmpty
      private String hubname;
    }
  }

}
