/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy.v1;

import java.util.UUID;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.model.DeviceCreate;
import com.microsoft.twins.reflector.TwinReflectorProxyProperties;
import com.microsoft.twins.reflector.proxy.TenantResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class PropertBackedV1TenantResolver implements TenantResolver {
  private final TwinReflectorProxyProperties properties;
  private final DevicesApi devicesApi;

  private final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

  private UUID gateway;

  @Override
  public UUID getTenant() {
    return properties.getTenant();
  }

  @Override
  public UUID getGateway() {
    rwl.readLock().lock();
    try {
      if (gateway != null) {
        return gateway;
      }

      if (properties.getDefaultGateway() != null) {
        setGateway(properties.getDefaultGateway());
        return gateway;
      }

      setGateway(createGateway(devicesApi, properties));

      log.debug("Created default gateway [{}] as none was provided", gateway);
      return gateway;
    } finally {
      rwl.readLock().unlock();
    }
  }

  private void setGateway(final UUID newGateway) {
    // Must release read lock before acquiring write lock
    rwl.readLock().unlock();
    rwl.writeLock().lock();
    try {
      gateway = newGateway;
      // Downgrade by acquiring read lock before releasing write lock
      rwl.readLock().lock();
    } finally {
      rwl.writeLock().unlock(); // Unlock write, still hold read
    }
  }

  private static UUID createGateway(final DevicesApi devicesApi,
      final TwinReflectorProxyProperties properties) {
    final DeviceCreate device = new DeviceCreate();
    device.setName("DefaultGateway");
    device.setSpaceId(properties.getTenant());
    device.setHardwareId("DefaultGateway");
    device
        .setDescription("Auto created default gateway for Twin Reflector Proxy telemetry ingress.");
    device.setFriendlyName("Auto created default gateway");
    device.setCreateIoTHubDevice(true);

    return devicesApi.devicesCreate(device);
  }

}
