# Azure Digital Twins - Java support collection

This projects aims to provide modules and samples for leveraging [Azure Digital Twins](https://docs.microsoft.com/en-us/azure/digital-twins/about-digital-twins) service from Java based applications. The project is community maintained and will provide generic Java components as well as off-the-shelf Spring Boot integration and auto configuration.

[![Build status](https://dev.azure.com/kaiatms/Twins-Event-Ingress/_apis/build/status/ADT%20Java%20collection%20-%20MASTER%20build)](https://dev.azure.com/kaiatms/Twins-Event-Ingress/_build/latest?definitionId=20) [![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=com.microsoft.twins%3Aazure-digital-twins-java&metric=alert_status)](https://sonarcloud.io/dashboard?id=com.microsoft.twins%3Aazure-digital-twins-java)

## Modules

- [ADT Java Management Client](twin-management-client) including optional Spring Boot integration
- [ADT Java Samples](twin-samples) leveraging the management client including a sample to consume events from ADT.
- [Twin Reflector Proxy](twin-reflector-proxy) - aims to provide a single event driven cloud ingress point for messages that contain either ADT topology changes or telemetry ingress.

## Getting started

### Prerequisites

- OpenJDK >=11
- Maven >=3.5

### Build

`mvn clean install`

## Contributing

See [Contributing](CONTRIBUTING.md) guidance.
