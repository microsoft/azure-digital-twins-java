/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.net.UnknownHostException;
import java.time.Duration;
import java.time.Instant;
import java.util.Collection;
import java.util.Collections;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import com.microsoft.twins.TwinsApiClient;
import feign.Client;
import feign.Request;
import feign.Request.HttpMethod;
import feign.Request.Options;
import feign.Response;
import feign.RetryableException;
import feign.Retryer;
import feign.Util;

@RunWith(MockitoJUnitRunner.class)
public class FeignErrorHandlingTest {

  private static final String ADT_DUMMY_URL = "https://docs.westcentralus.azuresmartspaces.net/management";

  @Mock
  private Client clientMock;

  private final Request dummyRequest =
      Request.create(HttpMethod.DELETE, ADT_DUMMY_URL, Collections.<String, Collection<String>>emptyMap(), null);

  private DevicesApi devicesApi;

  @BeforeEach
  public void setup() {
    devicesApi = new TwinsApiClient(ADT_DUMMY_URL, clientMock, new Retryer.Default()).getDevicesApi();
  }

  @Test
  public void testSuccess() throws IOException {

    when(clientMock.execute(any(Request.class), any(Options.class))).thenReturn(Response.builder().status(200)
        .headers(Collections.<String, Collection<String>>emptyMap()).request(dummyRequest).build());

    devicesApi.devicesDelete("test");

    verify(clientMock, times(1)).execute(any(Request.class), any(Options.class));
  }

  @Test
  public void testDefaultRetryerGivingUp() throws IOException {

    when(clientMock.execute(any(Request.class), any(Options.class))).thenThrow(new UnknownHostException());

    try {
      devicesApi.devicesDelete("test");
      fail("not failing");
    } catch (final RetryableException e) {
      assertThat(e.getCause()).isInstanceOf(UnknownHostException.class);
    } finally {
      verify(clientMock, times(5)).execute(any(Request.class), any(Options.class));
    }
  }

  @Test
  public void testRetryerAttempts() throws IOException {

    when(clientMock.execute(any(Request.class), any(Options.class))).thenThrow(new UnknownHostException());

    final int maxAttempts = 3;

    devicesApi =
        new TwinsApiClient(ADT_DUMMY_URL, clientMock, new Retryer.Default(1, 100, maxAttempts)).getDevicesApi();

    try {
      devicesApi.devicesDelete("test");
      fail("not failing");
    } catch (final RetryableException e) {
      assertThat(e.getCause()).isInstanceOf(UnknownHostException.class);
    } finally {
      verify(clientMock, times(maxAttempts)).execute(any(Request.class), any(Options.class));
    }
  }

  @Test
  public void test409RetryConfigByErrorDecoder() throws IOException {

    when(clientMock.execute(any(Request.class), any(Options.class))).thenReturn(
        Response.builder().status(409).headers(Collections.<String, Collection<String>>emptyMap()).request(dummyRequest)
            .build(),
        Response.builder().status(200).headers(Collections.<String, Collection<String>>emptyMap()).request(dummyRequest)
            .build());

    devicesApi.devicesDelete("test");

    verify(clientMock, times(2)).execute(any(Request.class), any(Options.class));

  }

  @Test
  public void test429RetryConfigByErrorDecoder() throws IOException {

    when(clientMock.execute(any(Request.class), any(Options.class))).thenReturn(
        Response.builder().status(429).headers(Collections.<String, Collection<String>>emptyMap()).request(dummyRequest)
            .build(),
        Response.builder().status(200).headers(Collections.<String, Collection<String>>emptyMap()).request(dummyRequest)
            .build());


    final Instant start = Instant.now();
    devicesApi.devicesDelete("test");
    final Instant finish = Instant.now();
    final long timeElapsed = Duration.between(start, finish).toMillis();
    assertThat(timeElapsed).isGreaterThanOrEqualTo(Duration.ofSeconds(1).toMillis());

    verify(clientMock, times(2)).execute(any(Request.class), any(Options.class));

  }


  @Test
  public void test400ErrorWithRetryAfterHeader() throws IOException {

    when(clientMock.execute(any(Request.class), any(Options.class)))
        .thenReturn(
            Response.builder().status(400)
                .headers(
                    Collections.singletonMap(Util.RETRY_AFTER, (Collection<String>) Collections.singletonList("1")))
                .request(dummyRequest).build(),
            Response.builder().status(200).headers(Collections.<String, Collection<String>>emptyMap())
                .request(dummyRequest).build());


    devicesApi.devicesDelete("test");

    verify(clientMock, times(2)).execute(any(Request.class), any(Options.class));

  }
}
