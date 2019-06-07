/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TwinReflectorProxyApplication {

  public static void main(final String[] args) {
    SpringApplication.run(TwinReflectorProxyApplication.class, args);
  }
}
