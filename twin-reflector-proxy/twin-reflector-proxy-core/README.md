# Twin Reflector Proxy - Core implementation

Core implementation handling ingress message streams and applying to Azure Digital Twins (i.e. topology operations) as well as the to embedded Azure IoT Hub (i.e. telemetry ingress).

Main features are:

- Message ingress handling from a Kafka topic (or Azure Event Hub).
- Local Azure Digital Twin entity caching to reduce ADT calls.
- Application of topology operations by leveraging the [ADT Java Management Client](/twin-management-client).
- Forward of telemetry ingress through [Azure IoT Device SDK](https://github.com/Azure/azure-iot-sdk-java/blob/master/doc/java-devbox-setup.md#installiotmaven).
- Optionally report ingress to [Azure Application Insights](https://docs.microsoft.com/en-us/azure/azure-monitor/insights/insights-overview).

See below for details.

## Message ingress handling

The ingress as well as optional feedback egress is message based and can either be connected to Kafka topics or Azure Event Hubs through its [Kafka API](https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-for-kafka-ecosystem-overview).

All topics/Event Hubs can be [configured](/twin-reflector-proxy/twin-reflector-proxy-app) but as default they are set as:

- Topic: _proxyingress_ / Group: _proxyingresscg_
  - Inbound channel for ingress messages (topology and/or telemetry) according to [specification](/twin-reflector-proxy).
- Topic: _proxyfeedback_
  - Outbound channel for optional feedback. Both successful application as well as error case.
- Topic: _topologyoperations_
  - Inbound channel for receiving [ADT TopologyOperation](https://docs.microsoft.com/en-us/azure/digital-twins/concepts-events-routing) events for cache invalidation. Note: ADT endpoint is automatically registered at startup time if it does not exist yet.

## Local ADT entity caching

The goal of the local cache is improve performance as well as reduce ADT calls in order to save cost and stay within the [public preview service limits](https://docs.microsoft.com/en-us/azure/digital-twins/concepts-service-limits).

The cache is managed by listening to [ADT TopologyOperation](https://docs.microsoft.com/en-us/azure/digital-twins/concepts-events-routing) event for entry invalidation.

Implementation leverages Spring Cache as abstraction layer. The default implementation is Caffeine as local cache but a central (e.g. Redis based) cache could be used as well.

## Application of topology operations

Topology operations are applied according to the [specification](/twin-reflector-proxy).

Supported [relationships](/twin-reflector-proxy/twin-reflector-proxy-api/src/main/java/com/microsoft/twins/reflector/model/Relationship.java) and [attributes](/twin-reflector-proxy/twin-reflector-proxy-api/src/main/java/com/microsoft/twins/reflector/model/IngressMessage.java) are documented in the API module.

## Forward of telemetry ingress

Telemetry ingress is by default based on a central _gateway_ device that has [Azure IoT Hub functionality](https://docs.microsoft.com/en-us/azure/digital-twins/concepts-device-ingress) enabled and is directly attached to the tenant space topology element. This gateway will be created for the tenant if it does not exist yet.

As alternative the proxy can be provided with a _gateway_ relationship to another device that is marked in ADT as IoT Hub device.

## Report to Azure Application Insights

Inbound ingress is reported to Azure Application Insights as custom event with the metadata _correlationId_, _deviceId_ and and _messageType_.
