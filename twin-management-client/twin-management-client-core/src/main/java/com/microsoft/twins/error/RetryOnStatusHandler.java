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

    switch (response.status()) {
      case 404:
      case 409:
        return new RetryableException(response.status(), response.status() + " will retry", null,
            null);
      case 429:
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.SECOND, 1);

        return new RetryableException(response.status(), "to many request: retry in a second",
            response.request().httpMethod(), cal.getTime());
      default:
        return super.decode(methodKey, response);
    }
  }

}
