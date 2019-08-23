/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api.error;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collections;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.api.DevicesApi;
import feign.Client;
import feign.Request;
import feign.Request.HttpMethod;
import feign.Request.Options;
import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.Util;

@ExtendWith(MockitoExtension.class)
public class FeignErrorHandlingTest {

  private static final String ADT_TEST_URL =
      "https://docs.westcentralus.azuresmartspaces.net/management";

  @Mock
  private Client client;

  private final Request dummyRequest =
      Request.create(HttpMethod.DELETE, ADT_TEST_URL, Collections.emptyMap(), null);

  private DevicesApi devicesApi;

  @BeforeEach
  public void setup() {
    devicesApi = new TwinsApiClient(ADT_TEST_URL, client, new Retryer.Default()).getDevicesApi();
  }

  @Test
  public void successMeansNoRetry() throws IOException {

    when(client.execute(any(Request.class), any(Options.class))).thenReturn(Response.builder()
        .status(200).headers(Collections.emptyMap()).request(dummyRequest).build());

    devicesApi.devicesDelete(UUID.randomUUID());

    verify(client, times(1)).execute(any(Request.class), any(Options.class));
  }

  @Test
  public void defaultRetryGivingUpAfter5Tries() throws IOException {

    when(client.execute(any(Request.class), any(Options.class)))
        .thenThrow(new UnknownHostException());

    try {
      devicesApi.devicesDelete(UUID.randomUUID());
      fail("not failing");
    } catch (final RetryableException e) {
      assertThat(e.getCause()).isInstanceOf(UnknownHostException.class);
    } finally {
      verify(client, times(5)).execute(any(Request.class), any(Options.class));
    }
  }

  @Test
  public void definedRetryAttempts() throws IOException {

    when(client.execute(any(Request.class), any(Options.class)))
        .thenThrow(new UnknownHostException());

    final int maxAttempts = 3;

    devicesApi = new TwinsApiClient(ADT_TEST_URL, client, new Retryer.Default(1, 100, maxAttempts))
        .getDevicesApi();

    try {
      devicesApi.devicesDelete(UUID.randomUUID());
      fail("not failing");
    } catch (final RetryableException e) {
      assertThat(e.getCause()).isInstanceOf(UnknownHostException.class);
    } finally {
      verify(client, times(maxAttempts)).execute(any(Request.class), any(Options.class));
    }
  }

  @Test
  public void conflictResultsInRetry() throws IOException {

    when(client.execute(any(Request.class), any(Options.class))).thenReturn(
        Response.builder().status(409).headers(Collections.emptyMap()).request(dummyRequest)
            .build(),
        Response.builder().status(200).headers(Collections.emptyMap()).request(dummyRequest)
            .build());

    devicesApi.devicesDelete(UUID.randomUUID());

    verify(client, times(2)).execute(any(Request.class), any(Options.class));

  }

  @Test
  public void tooManyRequestsResultsInRetry() throws IOException {

    when(client.execute(any(Request.class), any(Options.class))).thenReturn(
        Response.builder().status(429).headers(Collections.emptyMap()).request(dummyRequest)
            .build(),
        Response.builder().status(200).headers(Collections.emptyMap()).request(dummyRequest)
            .build());


    final Instant start = Instant.now();
    devicesApi.devicesDelete(UUID.randomUUID());
    final Instant finish = Instant.now();
    final long timeElapsed = Duration.between(start, finish).toMillis();
    assertThat(timeElapsed).isGreaterThanOrEqualTo(Duration.ofMillis(800).toMillis());

    verify(client, times(2)).execute(any(Request.class), any(Options.class));

  }


  @Test
  public void retryAfterHeaderIsApplied() throws IOException {

    when(
        client.execute(any(Request.class), any(Options.class)))
            .thenReturn(
                Response.builder().status(400)
                    .headers(
                        Collections.singletonMap(Util.RETRY_AFTER, Collections.singletonList("1")))
                    .request(dummyRequest).build(),
                Response.builder().status(200).headers(Collections.emptyMap()).request(dummyRequest)
                    .build());


    devicesApi.devicesDelete(UUID.randomUUID());

    verify(client, times(2)).execute(any(Request.class), any(Options.class));

  }
}
