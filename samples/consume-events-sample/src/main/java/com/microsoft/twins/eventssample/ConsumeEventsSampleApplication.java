/**
 * Copyright (c) Microsoft Corporation.
 * Licensed under the MIT License.
 */
package com.microsoft.twins.eventssample;

import java.util.HashMap;
import java.util.Map;
import javax.jms.ConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jms.DefaultJmsListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;
import com.microsoft.twins.event.model.TopologyOperationEvent;

@SpringBootApplication
@EnableJms
public class ConsumeEventsSampleApplication {
  private static final Logger LOG = LoggerFactory.getLogger(ConsumeEventsSampleApplication.class);

  public static void main(final String[] args) {
    SpringApplication.run(ConsumeEventsSampleApplication.class, args);
  }

  @Bean
  public JmsListenerContainerFactory<?> myFactory(final ConnectionFactory connectionFactory,
      final DefaultJmsListenerContainerFactoryConfigurer configurer) {
    final DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();

    // anonymous class
    factory.setErrorHandler(t -> LOG.error("An error has occurred in the transaction", t));

    configurer.configure(factory, connectionFactory);
    factory.setSessionTransacted(false);
    return factory;
  }

  @Bean
  public MessageConverter jacksonJmsMessageConverter() {
    final MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
    converter.setTargetType(MessageType.TEXT);
    converter.setTypeIdPropertyName("Type");

    final Map<String, Class<?>> typeIdMappings = new HashMap<>();
    // TODO other events
    // TODO migrate to auto config?
    typeIdMappings.put("TopologyOperation", TopologyOperationEvent.class);

    converter.setTypeIdMappings(typeIdMappings);
    return converter;
  }

}
