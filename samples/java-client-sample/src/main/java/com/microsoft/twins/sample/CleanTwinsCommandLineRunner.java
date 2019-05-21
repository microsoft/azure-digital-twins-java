/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.sample;

import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.microsoft.twins.TwinsApiClient;
import com.microsoft.twins.api.DevicesApi;
import com.microsoft.twins.api.DevicesApi.DevicesRetrieveQueryParams;
import com.microsoft.twins.model.DeviceRetrieve;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class CleanTwinsCommandLineRunner implements CommandLineRunner {
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
    final DevicesApi devicesApi = client.getDevicesApi();

    final List<DeviceRetrieve> devices =
        devicesApi.devicesRetrieve(new DevicesRetrieveQueryParams());

    devices.forEach(device -> devicesApi.devicesDelete(device.getId().toString()));



    System.exit(0);
  }


}
