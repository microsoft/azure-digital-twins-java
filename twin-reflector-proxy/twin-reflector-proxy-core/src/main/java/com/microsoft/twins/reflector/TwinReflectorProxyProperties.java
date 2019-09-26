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

  private Feedback feedback = Feedback.DISABLED;

  public enum Feedback {
    ENABLED, ERROR_ONLY, DISABLED;
  }


  @Getter
  @Setter
  @Validated
  public static class EventHubs {

    @NotEmpty
    private String primaryConnectionString;

    @NotEmpty
    private String secondaryConnectionString;

    @NotEmpty
    private String namespace;

    /**
     * Set to true to treat ingress message headers as AMQP encoded.
     */
    private boolean amqpHeaderDecodeEnabled = false;

    /**
     * Azure EventHub name used for ingress of ADT TopologyOperation events. Anonymous access.
     */
    @Valid
    private final Hub topologyOperations = new Hub();

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
