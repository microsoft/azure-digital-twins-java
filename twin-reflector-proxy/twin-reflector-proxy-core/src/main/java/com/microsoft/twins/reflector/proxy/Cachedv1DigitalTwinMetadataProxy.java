/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy;

import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import com.microsoft.twins.api.PropertyKeysApi;
import com.microsoft.twins.model.ExtendedPropertyKeyCreate;
import com.microsoft.twins.model.ExtendedPropertyKeyCreate.PrimitiveDataTypeEnum;
import com.microsoft.twins.model.ExtendedPropertyKeyCreate.ScopeEnum;
import com.microsoft.twins.model.ExtendedPropertyKeyRetrieve;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Validated
public class Cachedv1DigitalTwinMetadataProxy implements DigitalTwinMetadataProxy {
  private static final String CACHE_PROPERTY_KEY_BY_NAME_AND_SCOPE =
      "cachePropertyKeyByNameAndScope";


  private final TenantResolver tenantResolver;
  private final PropertyKeysApi propertyKeysApi;

  @Override
  @Cacheable(CACHE_PROPERTY_KEY_BY_NAME_AND_SCOPE)
  public String getPropertykey(final String name, final String scope) {
    final Optional<ExtendedPropertyKeyRetrieve> found = propertyKeysApi
        .propertyKeysRetrieve(new PropertyKeysApi.PropertyKeysRetrieveQueryParams()
            .spaceId(tenantResolver.getTenant().toString()).scope(scope))
        .stream().filter(p -> name.equalsIgnoreCase(p.getName())).findAny();

    if (!found.isPresent()) {
      propertyKeysApi.propertyKeysCreate(
          new ExtendedPropertyKeyCreate().name(name).spaceId(tenantResolver.getTenant())
              .scope(ScopeEnum.fromValue(scope)).primitiveDataType(PrimitiveDataTypeEnum.STRING));
    }

    return name;
  }

}
