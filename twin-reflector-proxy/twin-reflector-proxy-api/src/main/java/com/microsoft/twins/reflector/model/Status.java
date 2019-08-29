/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

/**
 * Processing status of {@link IngressMessage}.
 *
 */
public enum Status {

  /**
   * {@link IngressMessage} processing failed.
   */
  ERROR,

  /**
   * {@link IngressMessage} processing successful. Changes applied to ADT.
   */
  PROCESSED;
}
