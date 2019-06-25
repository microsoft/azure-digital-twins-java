/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.topology;

import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.DevicesApi.DevicesRetrieveQueryParams;
import com.microsoft.twins.model.DeviceRetrieve;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class CachedDigitalTwinProxy {

  private final TwinsApiClient twinsApiClient;

  @Cacheable
  Optional<DeviceRetrieve> getElementByName(final String name) {

    final DevicesApi devicesApi = twinsApiClient.getDevicesApi();
    final List<DeviceRetrieve> devices = devicesApi.devicesRetrieve(new DevicesRetrieveQueryParams().names(name));

    if (devices.isEmpty()) {
      return Optional.empty();
    }

    return Optional.ofNullable(devices.get(0));
  }

  @CacheEvict
  void deleteElementByName(final String name) {
    final DevicesApi devicesApi = twinsApiClient.getDevicesApi();


    final DeviceRetrieve device = getElementByName(name).orElseThrow();
    devicesApi.devicesDelete(device.getId().toString());
  }

}
