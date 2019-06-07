/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.telemetry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.DevicesApi.DevicesRetrieveQueryParams;
import com.microsoft.twins.model.DeviceRetrieve;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeviceConnectionStringResolver {

  private final TwinsApiClient twinsApiClient;

  Optional<String> getConnectionStringByDeviceId(final UUID deviceId) {

    final DevicesApi devicesApi = twinsApiClient.getDevicesApi();
    final List<DeviceRetrieve> devices = devicesApi
        .devicesRetrieve(new DevicesRetrieveQueryParams().ids(deviceId.toString()).includes("ConnectionString"));

    if (devices.isEmpty()) {
      return Optional.empty();
    }

    return Optional.ofNullable(devices.get(0).getConnectionString());
  }

}
