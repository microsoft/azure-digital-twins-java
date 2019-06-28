/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.eventssample;

import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.api.DevicesApi.DevicesRetrieveQueryParams;
import com.microsoft.twins.api.SpacesApi;
import com.microsoft.twins.api.SpacesApi.SpacesRetrieveQueryParams;
import com.microsoft.twins.event.model.TopologyOperationEvent;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.SpaceRetrieveWithChildren;

@Component
public class MessageConsumer {
  private static final String PARKING_TYPE = "Parking";

  private final TwinsApiClient twinsApiClient;
  private List<String> parkingSpaces;

  MessageConsumer(final TwinsApiClient twinsApiClient) {
    this.twinsApiClient = twinsApiClient;
  }

  private static final Logger LOG = LoggerFactory.getLogger(MessageConsumer.class);

  @JmsListener(destination = "messages-queue", containerFactory = "myFactory")
  public void processTopologyOperationEvent(final TopologyOperationEvent message) {
    if (CollectionUtils.isEmpty(parkingSpaces)) {
      parkingSpaces = getParkingSpaces();
    }

    LOG.trace("============= Received: {}", message);

    if (message.getSpacesToNotify().stream().anyMatch(parkingSpaces::contains)) {

      final List<DeviceRetrieve> device = twinsApiClient.getDevicesApi()
          .devicesRetrieve(new DevicesRetrieveQueryParams().ids(message.getId().toString()).includes("space"));

      if (!device.isEmpty()) {
        LOG.info("Device {} ready in space {}", device.get(0).getName(), device.get(0).getSpace().getName());
      }

    }

  }

  private List<String> getParkingSpaces() {
    final SpacesApi spacesApi = twinsApiClient.getSpacesApi();

    // Check for existing setup
    final List<SpaceRetrieveWithChildren> tenant =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().name("TENANT"));

    return spacesApi
        .spacesRetrieve(new SpacesRetrieveQueryParams().types(PARKING_TYPE).useParentSpace(true)
            .spaceId(tenant.get(0).getId().toString()).traverse("Down"))
        .stream().map(space -> space.getId().toString()).collect(Collectors.toList());
  }
}
