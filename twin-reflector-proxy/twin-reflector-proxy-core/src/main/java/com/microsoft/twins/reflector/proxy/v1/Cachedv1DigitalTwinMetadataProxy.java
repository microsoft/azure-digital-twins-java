/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector.proxy.v1;

import java.util.List;
import java.util.Optional;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.annotation.Validated;
import com.microsoft.twins.api.PropertyKeysApi;
import com.microsoft.twins.api.TypesApi;
import com.microsoft.twins.api.TypesApi.TypesRetrieveQueryParams;
import com.microsoft.twins.model.CategoryEnum;
import com.microsoft.twins.model.ExtendedPropertyKeyCreate;
import com.microsoft.twins.model.ExtendedPropertyKeyRetrieve;
import com.microsoft.twins.model.ExtendedTypeCreate;
import com.microsoft.twins.model.ExtendedTypeRetrieve;
import com.microsoft.twins.model.PrimitiveDataTypeEnum;
import com.microsoft.twins.model.ScopeEnum;
import com.microsoft.twins.reflector.proxy.DigitalTwinMetadataProxy;
import com.microsoft.twins.reflector.proxy.TenantResolver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Validated
public class Cachedv1DigitalTwinMetadataProxy implements DigitalTwinMetadataProxy {
  private static final String CACHE_PROPERTY_KEY_BY_NAME_AND_SCOPE =
      "cachePropertyKeyByNameAndScope";

  private static final String CACHE_TYPE_BY_NAME_AND_CATEGORY = "cacheTypeByNameAndcategory";


  private final TenantResolver tenantResolver;
  private final PropertyKeysApi propertyKeysApi;
  private final TypesApi typesApi;

  @Override
  @Cacheable(CACHE_PROPERTY_KEY_BY_NAME_AND_SCOPE)
  public String getPropertykey(final String name, final String scope) {
    final Optional<ExtendedPropertyKeyRetrieve> found = propertyKeysApi
        .propertyKeysRetrieve(new PropertyKeysApi.PropertyKeysRetrieveQueryParams()
            .spaceId(tenantResolver.getTenant()).scope(scope))
        .stream().filter(p -> name.equalsIgnoreCase(p.getName())).findAny();

    if (!found.isPresent()) {
      log.debug("PropertyKey [{}] in scope [{}] not found. I will create one", name, scope);
      propertyKeysApi.propertyKeysCreate(
          new ExtendedPropertyKeyCreate().name(name).spaceId(tenantResolver.getTenant())
              .scope(ScopeEnum.fromValue(scope)).primitiveDataType(PrimitiveDataTypeEnum.STRING));
    }

    return name;
  }

  @Override
  @Cacheable(CACHE_TYPE_BY_NAME_AND_CATEGORY)
  public int getType(final String name, final CategoryEnum category) {
    final List<ExtendedTypeRetrieve> found =
        typesApi.typesRetrieve(new TypesRetrieveQueryParams().spaceId(tenantResolver.getTenant())
            .names(name.replaceAll("\\s+", "")).categories(category));

    if (CollectionUtils.isEmpty(found)) {
      log.debug("Type [{}] in category [{}] not found. I will create one", name, category);
      return typesApi.typesCreate(new ExtendedTypeCreate().name(name.replaceAll("\\s+", ""))
          .friendlyName(name).category(category).spaceId(tenantResolver.getTenant()));
    }

    return found.get(0).getId();
  }
}
