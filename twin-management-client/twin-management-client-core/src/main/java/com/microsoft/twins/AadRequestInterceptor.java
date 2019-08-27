/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins;

import java.net.MalformedURLException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.lang3.time.StopWatch;
import com.microsoft.aad.adal4j.AuthenticationContext;
import com.microsoft.aad.adal4j.AuthenticationResult;
import com.microsoft.aad.adal4j.ClientCredential;
import com.microsoft.twins.error.FailedToAcquireBearerTokenException;
import feign.RequestInterceptor;
import feign.RequestTemplate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
public class AadRequestInterceptor implements RequestInterceptor {

  private static final String AUTHORIZATION_HEADER = "Authorization";
  private static final String BEARER_TOKEN_TYPE = "Bearer";
  private static final Duration BUFFER = Duration.ofSeconds(10);

  private final ReadWriteLock rwLock = new ReentrantReadWriteLock();
  private final ExecutorService threadPoolExecutor = Executors.newFixedThreadPool(5);

  private String accessToken;
  private Instant accessTokenExpiresAt;
  private AuthenticationContext context;

  private final String authorityHost;
  private final String tenant;
  private final String resource;
  private final String clientId;
  private final String clientSecret;
  private final Duration timeout;

  private AuthenticationContext getContext() throws MalformedURLException {
    if (context == null) {
      context = new AuthenticationContext(authorityHost + "/" + tenant, true, threadPoolExecutor);
    }
    return context;
  }

  /**
   * @return bearer token ignoring cache, i.e. by direct AAD call.
   */
  public Optional<String> getAccessTokenWithoutCache() {
    try {
      log.debug("Try to acquire token from AAD {}", authorityHost);

      final StopWatch watch = new StopWatch();
      watch.start();
      final AuthenticationResult response = acquireToken();
      watch.stop();
      log.debug("AAD call handled in {} ms", watch.getTime(TimeUnit.MILLISECONDS));

      if (response == null) {
        return Optional.empty();
      }

      log.debug("Got token of type '{}' from AAD that expires after {}s",
          response.getAccessTokenType(), response.getExpiresAfter());
      rwLock.writeLock().lock();
      try {
        accessToken = response.getAccessToken();
        accessTokenExpiresAt =
            Instant.now().plusSeconds(response.getExpiresAfter()).minusMillis(BUFFER.toMillis());
      } finally {
        rwLock.writeLock().unlock();
      }

      log.debug("Token from AAD stored in cache, will time out at {}", accessTokenExpiresAt);
    } catch (MalformedURLException | ExecutionException | TimeoutException e) {
      log.error("Failed to acquire bearer token from AAD.", e);
      throw new FailedToAcquireBearerTokenException("Failed to aquire bearer token from AAD.", e);
    }

    rwLock.readLock().lock();
    try {
      return Optional.ofNullable(accessToken);
    } finally {
      rwLock.readLock().unlock();
    }
  }

  private AuthenticationResult acquireToken()
      throws ExecutionException, TimeoutException, MalformedURLException {
    try {
      return getContext().acquireToken(resource, new ClientCredential(clientId, clientSecret), null)
          .get(timeout.get(ChronoUnit.SECONDS), TimeUnit.SECONDS);
    } catch (final InterruptedException e) {
      log.error("Interrupted. Did not acquire token!", e);
      // Restore interrupted state...
      Thread.currentThread().interrupt();

      return null;
    }
  }

  /**
   * @return bearer token. Takes from cache if below {@link AuthenticationResult#getExpiresAfter()}.
   */
  public Optional<String> getAccessToken() {
    rwLock.readLock().lock();
    try {
      if (accessToken != null && accessTokenExpiresAt != null
          && accessTokenExpiresAt.isAfter(Instant.now())) {
        log.debug("Retieved bearer token from cache.");
        return Optional.of(accessToken);
      }
    } finally {
      rwLock.readLock().unlock();
    }
    log.debug("Cache empty or invalid. Call for new token.");
    return getAccessTokenWithoutCache();
  }

  @Override
  public void apply(final RequestTemplate template) {
    if (template.headers().containsKey(AUTHORIZATION_HEADER)) {
      log.debug("The Authorization token has been already set");
    } else {
      log.debug("Constructing Header {} for Token {}", AUTHORIZATION_HEADER, BEARER_TOKEN_TYPE);

      getAccessToken().ifPresentOrElse(
          token -> template.header(AUTHORIZATION_HEADER,
              String.format("%s %s", BEARER_TOKEN_TYPE, token)),
          () -> log.error("No bearer token available from AAD for {}!", template));
    }
  }
}
