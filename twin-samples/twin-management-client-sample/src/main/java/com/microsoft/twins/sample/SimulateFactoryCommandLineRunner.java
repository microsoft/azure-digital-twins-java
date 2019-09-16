/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.sample;

import java.util.List;
import java.util.NavigableSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.google.common.collect.Lists;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.DevicesApi.DevicesRetrieveQueryParams;
import com.microsoft.twins.api.ResourcesApi;
import com.microsoft.twins.api.SpacesApi;
import com.microsoft.twins.api.SpacesApi.SpacesRetrieveQueryParams;
import com.microsoft.twins.client.TwinsApiClient;
import com.microsoft.twins.api.TypesApi;
import com.microsoft.twins.model.CategoryEnum;
import com.microsoft.twins.model.DeviceCreate;
import com.microsoft.twins.model.DeviceRetrieve;
import com.microsoft.twins.model.DeviceUpdate;
import com.microsoft.twins.model.ExtendedTypeCreate;
import com.microsoft.twins.model.SpaceCreate;
import com.microsoft.twins.model.SpaceResourceCreate;
import com.microsoft.twins.model.SpaceRetrieveWithChildren;
import com.microsoft.twins.model.SpaceTypeEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * This sample demonstrates ADT client usage by creating a simulated vehicle manufacturing line
 * including vehicles as ADT devices that are shifted through the manufacturing line (i.e. ADT
 * topology).
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class SimulateFactoryCommandLineRunner implements CommandLineRunner {
  private static final String ASSEMBLY_LINE_NAME_GORMAT = "WS-%02d/%03d";
  private static final String WORK_STATION_TYPE = "Station";
  private static final String ASSEMBLY_LINE_TYPE = "AssemblyLine";
  private static final String FACTORY_TYPE = "Factory";
  private static final String GOTOSALES_TYPE = "GoToSales";
  private static final String PARKING_TYPE = "Parking";
  private static final String VEHICLE_TYPE = "Vehicle";

  private static final boolean DELETE_EXISTING = false;

  private final TwinsApiClient client;

  @Override
  public void run(final String... args) throws Exception {
    final UUID tenant = createCustomerTenantSetup();

    createFactorySample(tenant);
    createGoToSales(tenant);
    shiftVehicles(tenant, "L-1", 20);
  }

  private void createGoToSales(final UUID tenant) {
    final SpacesApi spacesApi = client.getSpacesApi();

    // Delete existing setup
    final List<SpaceRetrieveWithChildren> existing =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().name("GoToSales"));

    if (!existing.isEmpty()) {
      if (DELETE_EXISTING) {
        existing.forEach(space -> spacesApi.spacesDelete(space.getId()));
      } else {
        return;
      }
    }

    // Create new setup
    final SpaceCreate goToSales = new SpaceCreate();
    goToSales.setName("GoToSales");
    goToSales.setFriendlyName("Go to sales");
    goToSales.setDescription("This is the representation of completed vehicles ready for .");
    goToSales.setType(GOTOSALES_TYPE);
    goToSales.setParentSpaceId(tenant);

    final SpaceCreate space1 = new SpaceCreate();
    space1.setName("S-1");
    space1.setFriendlyName("Parking Space One");
    space1.setType(PARKING_TYPE);

    final SpaceCreate space2 = new SpaceCreate();
    space2.setName("S-2");
    space2.setFriendlyName("Parking Space Two");
    space2.setType(PARKING_TYPE);

    goToSales.setChildren(List.of(space1, space2));

    spacesApi.spacesCreate(goToSales);
  }

  private void shiftVehicles(final UUID tenant, final String line, final int vehiclesToAssemble) {
    final SpacesApi spacesApi = client.getSpacesApi();

    final List<SpaceRetrieveWithChildren> parkingSpaces =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().types(PARKING_TYPE)
            .useParentSpace(true).spaceId(tenant).traverse("Down"));

    final UUID lineId =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().name(line)).get(0).getId();

    final NavigableSet<SpaceRetrieveWithChildren> stations =
        new TreeSet<>((o1, o2) -> o1.getName().compareTo(o2.getName()));
    stations.addAll(spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams()
        .types(WORK_STATION_TYPE).useParentSpace(true).spaceId(lineId)));

    for (int i = 0; i < vehiclesToAssemble; i++) {

      final DevicesApi devicesApi = client.getDevicesApi();
      final List<DeviceRetrieve> vehicles = devicesApi.devicesRetrieve(
          new DevicesRetrieveQueryParams().types(VEHICLE_TYPE).spaceId(lineId).traverse("Down"));

      vehicles.forEach(
          vehicle -> stations.stream().filter(space -> space.getId().equals(vehicle.getSpaceId()))
              .findAny().ifPresent(currentStation -> {
                final SpaceRetrieveWithChildren nextStation = stations.higher(currentStation);

                if (nextStation == null) {
                  final SpaceRetrieveWithChildren parkingSpace =
                      parkingSpaces.get(((int) (parkingSpaces.size() * Math.random())));

                  final DeviceUpdate vehicleInSpace = new DeviceUpdate();
                  vehicleInSpace.setSpaceId(parkingSpace.getId());

                  devicesApi.devicesUpdate(vehicleInSpace, vehicle.getId());

                  log.info("Vehicle [{}] has left the building is now in space [{}]",
                      vehicle.getName(), parkingSpace.getName());
                } else {
                  final DeviceUpdate newSpace = new DeviceUpdate();
                  newSpace.setSpaceId(nextStation.getId());

                  devicesApi.devicesUpdate(newSpace, vehicle.getId());

                  log.info("Vehicle [{}] is now pushed to [{}]", vehicle.getName(),
                      nextStation.getName());
                }
              }));

      // Add new vehicle to line
      final DeviceCreate vehicle = new DeviceCreate();
      final String vin = "XXX" + RandomStringUtils.randomAlphanumeric(14).toUpperCase();
      vehicle.setName(vin);
      vehicle.setHardwareId(vin);
      vehicle.setType(VEHICLE_TYPE);
      vehicle.setSpaceId(stations.first().getId());
      devicesApi.devicesCreate(vehicle);

      log.info("Vehicle [{}] has entered the line.", vehicle.getName());
      try {
        TimeUnit.MILLISECONDS.sleep(500);
      } catch (final InterruptedException e) {
        log.warn("Interrupted!", e);
        Thread.currentThread().interrupt();
      }
    }
  }

  private UUID createCustomerTenantSetup() {
    final SpacesApi spacesApi = client.getSpacesApi();

    // Check for existing setup
    final List<SpaceRetrieveWithChildren> found =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().name("TENANT"));
    if (!found.isEmpty()) {
      return found.get(0).getId();
    }

    final SpaceCreate tenant = new SpaceCreate();
    tenant.setName("TENANT");
    tenant.setFriendlyName("My friendly tenant");
    tenant.setDescription("Demo tenant");
    tenant.setType("Tenant");

    final UUID tenantId = spacesApi.spacesCreate(tenant);

    createTypes(tenantId);
    createResources(tenantId);

    return tenantId;
  }

  private void createResources(final UUID tenant) {
    final ResourcesApi resourcesApi = client.getResourcesApi();
    final SpaceResourceCreate iotHub = new SpaceResourceCreate();
    iotHub.setSpaceId(tenant);
    iotHub.setType(SpaceTypeEnum.IOTHUB);
    resourcesApi.resourcesCreate(iotHub);
  }

  private void createTypes(final UUID tenant) {
    final TypesApi typesApi = client.getTypesApi();
    final ExtendedTypeCreate type = new ExtendedTypeCreate();
    type.setCategory(CategoryEnum.SPACETYPE);
    type.setSpaceId(tenant);
    type.setName(WORK_STATION_TYPE);
    typesApi.typesCreate(type);
    type.setName(ASSEMBLY_LINE_TYPE);
    typesApi.typesCreate(type);
    type.setName(FACTORY_TYPE);
    typesApi.typesCreate(type);
    type.setName(GOTOSALES_TYPE);
    typesApi.typesCreate(type);
    type.setName(PARKING_TYPE);
    typesApi.typesCreate(type);

    type.setCategory(CategoryEnum.DEVICETYPE);
    type.setName(VEHICLE_TYPE);
    typesApi.typesCreate(type);
  }

  private void createFactorySample(final UUID tenant) {
    final SpacesApi spacesApi = client.getSpacesApi();

    // Delete existing setup
    final List<SpaceRetrieveWithChildren> existing =
        spacesApi.spacesRetrieve(new SpacesRetrieveQueryParams().name("Factory-1"));

    if (!existing.isEmpty()) {
      if (DELETE_EXISTING) {
        existing.forEach(space -> spacesApi.spacesDelete(space.getId()));
      } else {
        return;
      }
    }

    // Create new setup
    final SpaceCreate factory = new SpaceCreate();
    factory.setName("Factory-1");
    factory.setFriendlyName("Factory One");
    factory.setDescription("This is the representation of the entire factory.");
    factory.setType(FACTORY_TYPE);
    factory.setParentSpaceId(tenant);

    final SpaceCreate assemblyLine1 = new SpaceCreate();
    assemblyLine1.setName("L-1");
    assemblyLine1.setFriendlyName("Assembly Line One");
    assemblyLine1.setChildren(generateStations(1, 10));
    assemblyLine1.setType(ASSEMBLY_LINE_TYPE);

    final SpaceCreate assemblyLine2 = new SpaceCreate();
    assemblyLine2.setName("L-2");
    assemblyLine2.setFriendlyName("Assembly Line Two");
    assemblyLine2.setChildren(generateStations(2, 10));
    assemblyLine2.setType(ASSEMBLY_LINE_TYPE);

    factory.setChildren(List.of(assemblyLine1, assemblyLine2));

    spacesApi.spacesCreate(factory);
  }

  private static List<SpaceCreate> generateStations(final int line, final int size) {
    final List<SpaceCreate> result = Lists.newArrayListWithExpectedSize(size);
    for (int i = 1; i <= size; i++) {
      final SpaceCreate station = new SpaceCreate();
      station.setName(String.format(ASSEMBLY_LINE_NAME_GORMAT, line, i));
      station.setFriendlyName(String.format("Work Station-%03d/%03d", line, i));
      station.setType(WORK_STATION_TYPE);
      result.add(station);
    }
    return result;
  }
}
