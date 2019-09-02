/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class CorrelationIdRequestInterceptor implements RequestInterceptor {

  @Override
  public void apply(final RequestTemplate template) {
    if (CorrelationIdContext.getCorrelationId() != null) {
      template.header("X-Ms-Client-Request-Id", CorrelationIdContext.getCorrelationId().toString());
    }

  }

}
