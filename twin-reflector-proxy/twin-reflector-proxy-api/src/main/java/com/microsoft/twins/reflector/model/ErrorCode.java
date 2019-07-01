/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

public enum ErrorCode {
  ELEMENT_DOES_NOT_EXIST(1);

  private final int code;

  private ErrorCode(final int code) {
    this.code = code;
  }

  public int getCode() {
    return code;
  }
}
