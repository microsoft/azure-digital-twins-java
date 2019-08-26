/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy;

import javax.validation.constraints.NotEmpty;

public interface DigitalTwinMetadataProxy {

  String getPropertykey(@NotEmpty String name, @NotEmpty String scope);

}
