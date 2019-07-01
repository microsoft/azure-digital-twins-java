/**
 * Copyright (c) Microsoft Corporation. Licensed under the MIT License.
 */
package com.microsoft.twins.reflector;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Configuration;
import com.microsoft.twins.spring.configuration.DigitalTwinClientAutoConfiguration;

@EnableAutoConfiguration(
    exclude = {DigitalTwinClientAutoConfiguration.class, TwinReflectorProxyAutoConfiguration.class})
@Configuration
public class ReflectorTestConfiguration {



}
