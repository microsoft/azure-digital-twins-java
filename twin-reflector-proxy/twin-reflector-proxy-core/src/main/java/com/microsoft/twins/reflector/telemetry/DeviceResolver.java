/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.telemetry;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.DevicesApi.DevicesRetrieveQueryParams;
import com.microsoft.twins.api.SensorsApi;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.SensorRetrieve;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeviceResolver {

  private final TwinsApiClient twinsApiClient;

  @Cacheable
  Optional<String> getConnectionStringByDeviceId(final UUID deviceId) {

    final DevicesApi devicesApi = twinsApiClient.getDevicesApi();
    final List<DeviceRetrieve> devices = devicesApi
        .devicesRetrieve(new DevicesRetrieveQueryParams().ids(deviceId.toString()).includes("ConnectionString"));

    if (CollectionUtils.isEmpty(devices)) {
      return Optional.empty();
    }

    return Optional.ofNullable(devices.get(0).getConnectionString());
  }

  @Cacheable
  Optional<UUID> getDeviceIdByHardwareId(final String hardwareId) {
    // Check first if hardware ID belongs to sensor
    final SensorsApi sensorsApi = twinsApiClient.getSensorsApi();
    final List<SensorRetrieve> sensors = sensorsApi
        .sensorsRetrieve(new SensorsApi.SensorsRetrieveQueryParams().hardwareIds(hardwareId).includes("device"));

    if (!CollectionUtils.isEmpty(sensors)) {
      if (StringUtils.isEmpty(sensors.get(0).getDevice().getGatewayId())) {
        return Optional.of(sensors.get(0).getDevice().getId());
      }
      return Optional.of(UUID.fromString(sensors.get(0).getDevice().getGatewayId()));
    }

    // Check next if hardware ID belongs to device
    final DevicesApi devicesApi = twinsApiClient.getDevicesApi();
    final List<DeviceRetrieve> devices =
        devicesApi.devicesRetrieve(new DevicesRetrieveQueryParams().hardwareIds(hardwareId));

    if (CollectionUtils.isEmpty(devices)) {
      return Optional.empty();
    }

    if (StringUtils.isEmpty(devices.get(0).getGatewayId())) {
      return Optional.of(devices.get(0).getId());
    }

    return Optional.of(UUID.fromString(devices.get(0).getGatewayId()));
  }

}
