/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
// Copyright (c) Microsoft. All rights reserved.
// Licensed under the MIT license. See LICENSE file in the project root for full license
// information.

package com.microsoft.twins.telemetry.ingress;

import java.io.Closeable;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import com.microsoft.azure.sdk.iot.device.DeviceClient;
import com.microsoft.azure.sdk.iot.device.IotHubClientProtocol;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeCallback;
import com.microsoft.azure.sdk.iot.device.IotHubConnectionStatusChangeReason;
import com.microsoft.azure.sdk.iot.device.IotHubEventCallback;
import com.microsoft.azure.sdk.iot.device.IotHubStatusCode;
import com.microsoft.azure.sdk.iot.device.Message;
import com.microsoft.azure.sdk.iot.device.TransportClient;
import com.microsoft.azure.sdk.iot.device.transport.IotHubConnectionStatus;


public class TelemetryForwarder implements Closeable {
  private static final int D2C_MESSAGE_TIMEOUT = 2000; // 2 seconds
  // FIXME
  private static List failedMessageListOnClose = new ArrayList();

  private final TransportClient transportClient;

  public TelemetryForwarder() throws IllegalStateException, IOException {
    this.transportClient = new TransportClient(IotHubClientProtocol.AMQPS);
    transportClient.open();
  }


  protected static class EventCallback implements IotHubEventCallback {
    @Override
    public void execute(final IotHubStatusCode status, final Object context) {
      final Message msg = (Message) context;

      System.out.println(
          "IoT Hub responded to message " + msg.getMessageId() + " with status " + status.name());

      if (status == IotHubStatusCode.MESSAGE_CANCELLED_ONCLOSE) {
        failedMessageListOnClose.add(msg.getMessageId());
      }
    }
  }

  protected static class IotHubConnectionStatusChangeCallbackLogger
      implements IotHubConnectionStatusChangeCallback {
    @Override
    public void execute(final IotHubConnectionStatus status,
        final IotHubConnectionStatusChangeReason statusChangeReason, final Throwable throwable,
        final Object callbackContext) {
      System.out.println();
      System.out.println("CONNECTION STATUS UPDATE: " + status);
      System.out.println("CONNECTION STATUS REASON: " + statusChangeReason);
      System.out.println(
          "CONNECTION STATUS THROWABLE: " + (throwable == null ? "null" : throwable.getMessage()));
      System.out.println();

      if (throwable != null) {
        throwable.printStackTrace();
      }

      if (status == IotHubConnectionStatus.DISCONNECTED) {
        // connection was lost, and is not being re-established. Look at provided exception for
        // how to resolve this issue. Cannot send messages until this issue is resolved, and you
        // manually
        // re-open the device client
      } else if (status == IotHubConnectionStatus.DISCONNECTED_RETRYING) {
        // connection was lost, but is being re-established. Can still send messages, but they won't
        // be sent until the connection is re-established
      } else if (status == IotHubConnectionStatus.CONNECTED) {
        // Connection was successfully re-established. Can send messages.
      }
    }
  }

  public void sendMessage(final String message, final String connString, final String deviceId)
      throws IOException, IllegalArgumentException, URISyntaxException {
    System.out.println("Starting...");


    try (final DeviceClient client = new DeviceClient(connString, transportClient)) {

      System.out.println("Successfully created an IoT Hub client.");

      // Set your token expiry time limit here
      final long time = 2400;
      client.setOption("SetSASTokenExpiryTime", time);
      System.out.println("Updated token expiry time to " + time);

      client.registerConnectionStatusChangeCallback(
          new IotHubConnectionStatusChangeCallbackLogger(), new Object());

      client.open();

      System.out.println("Opened connection to IoT Hub.");
      System.out.println("Sending the following event messages:");

      // final double temperature = 0.0;
      // final double humidity = 0.0;

      // for (int i = 0; i < 10; ++i) {
      // temperature = 20 + Math.random() * 10;
      // humidity = 30 + Math.random() * 20;
      //
      // final String msgStr = "{\"deviceId\":\"" + deviceId + "\",\"messageId\":" + i
      // + ",\"temperature\":" + temperature + ",\"humidity\":" + humidity + "}";

      try {
        final Message msg = new Message(message);
        msg.setContentTypeFinal("application/json");
        msg.setMessageId(java.util.UUID.randomUUID().toString());
        msg.setExpiryTime(D2C_MESSAGE_TIMEOUT);
        System.out.println(message);

        final EventCallback callback = new EventCallback();
        client.sendEventAsync(msg, callback, msg);
      }

      // FIXME
      catch (final Exception e) {

        e.printStackTrace(); // Trace the exception
      }
      // }

      // TODO introduce callback
      // System.out.println(
      // "Wait for " + D2C_MESSAGE_TIMEOUT / 1000 + " second(s) for response from the IoT Hub...");
      //
      // // Wait for IoT Hub to respond.
      // try {
      // Thread.sleep(D2C_MESSAGE_TIMEOUT);
      // } catch (final InterruptedException e) {
      // e.printStackTrace();
      // }
    }

    if (!failedMessageListOnClose.isEmpty()) {
      System.out.println(
          "List of messages that were cancelled on close:" + failedMessageListOnClose.toString());
    }

    System.out.println("Shutting down...");
  }

  @Override
  public void close() throws IOException {
    transportClient.closeNow();
  }
}
