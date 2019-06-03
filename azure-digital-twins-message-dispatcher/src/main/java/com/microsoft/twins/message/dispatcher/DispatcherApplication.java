/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.message.dispatcher;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DispatcherApplication {

  public static void main(final String[] args) {
    SpringApplication.run(DispatcherApplication.class, args);
  }
}
