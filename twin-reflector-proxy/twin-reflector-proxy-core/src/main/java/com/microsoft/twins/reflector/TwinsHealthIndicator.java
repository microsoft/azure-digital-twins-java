/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import java.util.List;
import org.springframework.boot.actuate.health.AbstractHealthIndicator;
import org.springframework.boot.actuate.health.Health.Builder;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.util.CollectionUtils;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.model.RoleDefinitionRetrieve;


/**
 * Spring {@link HealthIndicator} for Azure Active Directory access.
 *
 */
public class TwinsHealthIndicator extends AbstractHealthIndicator {

  private final TwinsApiClient twinsApiClient;

  public TwinsHealthIndicator(final TwinsApiClient twinsApiClient) {
    this.twinsApiClient = twinsApiClient;
  }

  @Override
  protected void doHealthCheck(final Builder builder) throws Exception {
    final List<RoleDefinitionRetrieve> roles = twinsApiClient.getSystemApi().systemRetrieveRoles();

    if (!CollectionUtils.isEmpty(roles)) {
      builder.up();
      return;
    }

    builder.down();
  }

}
