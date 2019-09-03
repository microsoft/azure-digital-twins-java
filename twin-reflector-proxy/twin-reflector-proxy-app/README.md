# Twin Reflector Proxy - Spring Boot App

## Properties

Notes: properties can be provided as [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/boot-features-external-config.html)

### Azure Active Directory and Azure Digital Twins integration

| Property                               | Default Value                       | Description                                                                                                         |
| -------------------------------------- | ----------------------------------- | ------------------------------------------------------------------------------------------------------------------- |
| com.microsoft.twins.twins-url          |                                     | Azure Digital Twins management API base URL, e.g. `https://YOU_INSTANCE.westeurope.azuresmartspaces.net/management` |
| com.microsoft.twins.aad.tenant         |                                     | AAD tenant, e.g. XXX.onmicrosoft.com.                                                                               |
| com.microsoft.twins.aad.client-id      |                                     | AAD application client ID.                                                                                          |
| com.microsoft.twins.aad.client-secret  |                                     | AAD application client secret.                                                                                      |
| com.microsoft.twins.aad.authority-host | `https://login.microsoftonline.com` | AAD application client ID.                                                                                          |
| com.microsoft.twins.aad.timeout        | 5s                                  | AAD bearer token request timeout.                                                                                   |

### Azure Event Hubs for Kafka egress and ingress point definitions

Note: Azure Event Hubs are accessed through its [Kafka API](https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-for-kafka-ecosystem-overview) support.

| Property                                                             | Default Value      | Description                                                                                                                                                                                           |
| -------------------------------------------------------------------- | ------------------ | ----------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| com.microsoft.twins.reflector.event-hubs.connection-string           |                    | Connection string for Azure Event Hubs.                                                                                                                                                               |
| com.microsoft.twins.reflector.event-hubs.secondary-connection-string |                    | Secondary connection string for Azure Event Hubs.                                                                                                                                                     |
| com.microsoft.twins.reflector.event-hubs.namespace                   |                    | Azure Events Hubs namespace.                                                                                                                                                                          |
| com.microsoft.twins.reflector.event-hubs.topology-operations.hubname | topologyoperations | Azure EventHub name used for ingress of [ADT TopologyOperation](https://docs.microsoft.com/en-us/azure/digital-twins/concepts-events-routing) events.                                                 |
| com.microsoft.twins.reflector.event-hubs.ingress.hubname             | proxyingress       | Azure EventHub name used for Twin Reflector Proxy [ingress messages](/twin-reflector-proxy/twin-reflector-proxy-api/src/main/java/com/microsoft/twins/reflector/model/IngressMessage.java).           |
| com.microsoft.twins.reflector.event-hubs.ingress.consumer-group      | proxyingresscg     | Azure EventHub consumer group used for Twin Reflector Proxy [ingress messages](/twin-reflector-proxy/twin-reflector-proxy-api/src/main/java/com/microsoft/twins/reflector/model/IngressMessage.java). |
| com.microsoft.twins.reflector.event-hubs.feedback.enabled            | false              | [Feedback message](/twin-reflector-proxy/twin-reflector-proxy-api/src/main/java/com/microsoft/twins/reflector/model/FeedbackMessage.java) egress. Set to `false` to disable.                          |
| com.microsoft.twins.reflector.event-hubs.feedback.hubname            | proxyfeedback      | Azure EventHub name used for [feedback message](/twin-reflector-proxy/twin-reflector-proxy-api/src/main/java/com/microsoft/twins/reflector/model/FeedbackMessage.java) egress.                        |

## Azure Application Insights integration

In order to enable [Azure Application Insights](https://docs.microsoft.com/en-us/azure/azure-monitor/app/app-insights-overview) set properties:

| Property                                       | Default Value | Description                                                                                                                           |
| ---------------------------------------------- | ------------- | ------------------------------------------------------------------------------------------------------------------------------------- |
| azure.application-insights.enabled             | false         | Set to _true_ to enable.                                                                                                              |
| azure.application-insights.instrumentation-key |               | Provided as part of an [Application Insights resource](https://docs.microsoft.com/en-us/azure/azure-monitor/app/create-new-resource). |
