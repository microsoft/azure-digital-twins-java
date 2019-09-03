/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.model;

/**
 * Error codes for {@link FeedbackMessage}s with {@link Status#ERROR}.
 *
 */
public enum ErrorCode {

  /**
   * Topology element with given name/id does not exist.
   */
  ELEMENT_DOES_NOT_EXIST,

  /**
   * Provided topology is not consistent, e.g. mandatory relationships missing.
   */
  INCONSISTENT_TOPOLOGY,

  /**
   * Provided {@link IngressMessage#getAttributes()} key is not supported or does not exist in ADT
   * instance.
   */
  ATTRIBUTE_NOT_SUPPORTED,

  /**
   * Provided {@link IngressMessage#getEntityType()} does not exist in ADT instance.
   */
  ENTITY_NOT_SUPPORTED;

}
