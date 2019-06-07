/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license
// information.

package com.microsoft.twins.reflector.telemetry;

import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.TransportClient;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TelemetryForwarder implements Closeable {
  private static final Logger LOG = LoggerFactory.getLogger(TelemetryForwarder.class);

  private static final int D2C_MESSAGE_TIMEOUT = 2000; // 2 seconds

  private TransportClient transportClient;

  private final Map<String, DeviceClient> knownClients = new ConcurrentHashMap<>();

  private final DeviceConnectionStringResolver deviceConnectionStringResolver;


  protected static class EventCallback implements IotHubEventCallback {
    private static final Logger LOG = LoggerFactory.getLogger(EventCallback.class);

    @Override
    public void execute(final IotHubStatusCode status, final Object context) {
      final Message msg = (Message) context;

      switch (status) {
        case OK:
        case OK_EMPTY:
          LOG.debug("IoT Hub responded to message {} with status {}", msg.getMessageId(), status);
          break;
        case BAD_FORMAT:
        case ERROR:
        case HUB_OR_DEVICE_ID_NOT_FOUND:
        case INTERNAL_SERVER_ERROR:
        case MESSAGE_CANCELLED_ONCLOSE:
        case MESSAGE_EXPIRED:
        case PRECONDITION_FAILED:
        case REQUEST_ENTITY_TOO_LARGE:
        case SERVER_BUSY:
        case THROTTLED:
        case TOO_MANY_DEVICES:
        case UNAUTHORIZED:
          LOG.debug("IoT Hub responded with an error to message {} with status {}", msg.getMessageId(), status);
          break;
        default:
          break;

      }
    }
  }


  public void sendMessage(final String message, final UUID deviceId, final String correlationId)
      throws IOException, URISyntaxException {

    final String connString = deviceConnectionStringResolver.getConnectionStringByDeviceId(deviceId)
        .orElseThrow(() -> new IllegalArgumentException("Unkown device ID"));

    final DeviceClient client;
    if (knownClients.containsKey(connString)) {
      client = knownClients.get(connString);
    } else {
      if (transportClient != null) {
        transportClient.closeNow();
      }
      transportClient = new TransportClient(IotHubClientProtocol.AMQPS);
      knownClients.replaceAll((key, val) -> {
        try {
          return new DeviceClient(key, transportClient);
        } catch (final URISyntaxException e) {
          LOG.error("Failed to create DeviceClient", e);
          return null;
        }
      });

      client = new DeviceClient(connString, transportClient);
      knownClients.put(connString, client);

      transportClient.open();
    }

    final Message msg = new Message(message);
    msg.setContentTypeFinal("application/json");
    msg.setMessageId(UUID.randomUUID().toString());
    msg.setExpiryTime(D2C_MESSAGE_TIMEOUT);
    msg.setCorrelationId(correlationId);

    final EventCallback callback = new EventCallback();
    client.sendEventAsync(msg, callback, msg);
  }

  @Override
  public void close() throws IOException {
    transportClient.closeNow();
  }
}
