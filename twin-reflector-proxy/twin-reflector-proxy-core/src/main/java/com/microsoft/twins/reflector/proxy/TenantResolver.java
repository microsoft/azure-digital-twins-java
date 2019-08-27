/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy;

import java.util.UUID;

public interface TenantResolver {
  UUID getTenant();

  UUID getGateway();
}
