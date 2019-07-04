/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.sample;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class TwinsSampleApplication {

  public static void main(final String[] args) {
    final ConfigurableApplicationContext context =
        SpringApplication.run(TwinsSampleApplication.class, args);
    context.close();
    System.exit(0);
  }

}
