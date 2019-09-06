/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.error;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.Calendar;
import org.apache.commons.io.IOUtils;
import feign.Response;
import feign.RetryableException;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

@Slf4j
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
      case 400:

        if (response.body() != null) {
          try (InputStream message = response.body().asInputStream()) {
            log.error("Client error. Response with Body: [{}]",
                IOUtils.toString(message, Charset.defaultCharset()));
          } catch (final IOException e) {
            log.error("Could not print Client error body", e);
          }
        }
        return super.decode(methodKey, response);
      default:
        return super.decode(methodKey, response);
    }
  }

}
