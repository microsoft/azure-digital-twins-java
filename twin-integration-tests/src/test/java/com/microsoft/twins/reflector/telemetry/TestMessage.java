/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.telemetry;

import lombok.EqualsAndHashCode;

@EqualsAndHashCode
public class TestMessage {
  private final String hardwareId;
  private final String payload;

  protected TestMessage(final String hardwareId, final String payload) {
    this.hardwareId = hardwareId;
    this.payload = payload;
  }


}
