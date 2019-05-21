/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.telemetry.ingress;

import java.util.List;
import java.util.UUID;
import org.junit.After;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.api.ResourcesApi;
import com.microsoft.twins.api.SpacesApi;
import com.microsoft.twins.api.SpacesApi.SpacesRetrieveQueryParams;
import com.microsoft.twins.api.TypesApi;
import com.microsoft.twins.model.ExtendedTypeCreate;
import com.microsoft.twins.model.ExtendedTypeCreate.CategoryEnum;
import com.microsoft.twins.model.SpaceCreate;
import com.microsoft.twins.model.SpaceResourceCreate;
import com.microsoft.twins.model.SpaceRetrieveWithChildren;
import com.microsoft.twins.spring.configuration.DigitalTwinClientAutoConfiguration;
import com.microsoft.twins.spring.configuration.TelemetryIngressAutoConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(
    classes = {DigitalTwinClientAutoConfiguration.class, TelemetryIngressAutoConfiguration.class})
@TestPropertySource(locations = "classpath:/test.properties")
public abstract class AbstractTest {
  private static final String TEST_SPACE_TYPE = "TestSpaces";
  private static final String TEST_DEVICE_TYPE = "TestDevices";

  @Autowired
  protected TwinsApiClient twinsApiClient;

  protected UUID tenant;

  @Before
  public void setup() {
    createTestTenantSetup();
    cleanTestSpaces();
  }

  @After
  public void cleanup() {
    cleanTestSpaces();
  }

  private void cleanTestSpaces() {
    final SpacesApi spacesApi = twinsApiClient.getSpacesApi();

    final List<SpaceRetrieveWithChildren> existing =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().types(TEST_SPACE_TYPE)
            .useParentSpace(true).spaceId(tenant.toString()).traverse("Down"));

    if (!existing.isEmpty()) {
      existing.forEach(space -> spacesApi.spacesDelete(space.getId().toString()));
    }
  }

  private void createTestTenantSetup() {
    final SpacesApi spacesApi = twinsApiClient.getSpacesApi();

    // Check for existing setup
    final List<SpaceRetrieveWithChildren> found =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().name("TEST_TENANT"));
    if (!found.isEmpty()) {
      tenant = found.get(0).getId();
      return;
    }

    final SpaceCreate tenantCreate = new SpaceCreate();
    tenantCreate.setName("TEST_TENANT");
    tenantCreate.setFriendlyName("My test tenant");
    tenantCreate.setDescription("Test tenant");
    tenantCreate.setType("Tenant");

    tenant = spacesApi.spacesCreate(tenantCreate);

    createTypes();
    createResources();
  }

  private void createTypes() {
    final TypesApi typesApi = twinsApiClient.getTypesApi();
    final ExtendedTypeCreate type = new ExtendedTypeCreate();
    type.setCategory(CategoryEnum.SPACETYPE);
    type.setSpaceId(tenant);
    type.setName(TEST_SPACE_TYPE);
    typesApi.typesCreate(type);

    type.setCategory(CategoryEnum.DEVICETYPE);
    type.setName(TEST_DEVICE_TYPE);
    typesApi.typesCreate(type);
  }

  private void createResources() {
    final ResourcesApi resourcesApi = twinsApiClient.getResourcesApi();
    final SpaceResourceCreate iotHub = new SpaceResourceCreate();
    iotHub.setSpaceId(tenant);
    iotHub.setType(SpaceResourceCreate.TypeEnum.IOTHUB);
    resourcesApi.resourcesCreate(iotHub);
  }

}
