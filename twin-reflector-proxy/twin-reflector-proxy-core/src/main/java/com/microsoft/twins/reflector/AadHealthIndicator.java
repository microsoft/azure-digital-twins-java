/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import java.util.Optional;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;
import com.microsoft.twins.TwinsApiClient;
import lombok.RequiredArgsConstructor;


/**
 * Spring {@link HealthIndicator} for Azure Active Directory access.
 *
 */
@RequiredArgsConstructor
public class AadHealthIndicator extends AbstractHealthIndicator {

  private final TwinsApiClient twinsApiClient;

  @Override
  protected void doHealthCheck(final Builder builder) throws Exception {
    final Optional<String> token = twinsApiClient.getAccessTokenWithoutCache();

    if (token.isPresent()) {
      builder.up();
      return;
    }

    builder.down();
  }

}
