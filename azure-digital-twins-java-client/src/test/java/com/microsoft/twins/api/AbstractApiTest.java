/**
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package com.microsoft.twins.api;

import com.microsoft.twins.TwinsApiClient;

public abstract class AbstractApiTest {

  protected static final TwinsApiClient TWINS_API_CLIENT =
      new TwinsApiClient("http://localhost/management");

}
