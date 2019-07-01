/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.error;

import java.util.Calendar;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;

public class RetryOnStatusHandler extends ErrorDecoder.Default {

  @Override
  public Exception decode(final String methodKey, final Response response) {
    if (409 == response.status()) {
      return new RetryableException(response.status(), "conflict and retry", null, null);
    } else if (429 == response.status()) {
      final Calendar cal = Calendar.getInstance();
      cal.add(Calendar.SECOND, 1);

      return new RetryableException(response.status(), "to many request and retry in a second",
          response.request().httpMethod(), cal.getTime());
    } else {
      return super.decode(methodKey, response);
    }
  }

}
