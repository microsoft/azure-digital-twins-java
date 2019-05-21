# Azure Digital Twins - Consume Events sample

This [Sprint Boot](https://spring.io/projects/spring-boot) App demonstrates how to consume events from Azure Digital Twins (ADT) by Azure Service Bus leveraging Spring JMS integration based on [Apache Qpid JMS is an AMQP 1.0 Java Message Service 2.0 client](https://qpid.apache.org/components/jms/index.html).

## Prerequisites

- Azure subscription.
- Azure Digital Twins instance.
- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli) to setup Azure Service Bus.
- OpenJDK 8 or 11 and Maven 3 to build and run the sample.

## Howto run the sample

### SetUp Azure Service Bus

First create a Azure resource group if you have not done so yet.

```bash
export resourcegroupname=digitalTwin
az group create --name $resourcegroupname --location westeurope
```

Now create your [Azure Service Bus](https://docs.microsoft.com/en-us/azure/service-bus-messaging/service-bus-messaging-overview) namespace, topic, queue and subscription. The namespace needs to be globally unique as it is used as DNS name as well.

```shell
destination=messages
topic=${destination}-topic
queue=${destination}-queue
subscription=${destination}-subscription
namespace=digitaltwins

az servicebus namespace create --resource-group $resourcegroupname \
    --name ${namespace}

az servicebus topic create --resource-group $resourcegroupname \
    --namespace-name ${namespace} \
    --name ${topic}

az servicebus queue create --resource-group $resourcegroupname \
    --namespace-name ${namespace} \
    --name ${queue}

az servicebus topic subscription create --resource-group $resourcegroupname  \
    --namespace-name ${namespace} --topic-name ${topic} \
    --name ${subscription} --forward-to ${queue}
```

Now we can create users for our sample and for the sample app to with `Listen` permission.

```bash
export sample_app_user_sas_key_name=sampleapp

az servicebus namespace authorization-rule create --resource-group $resourcegroupname \
        --namespace-name $namespace --name $sample_app_user_sas_key_name --rights {Listen}
```

Now the CLI again to retrieve the key:

```bash
az servicebus namespace authorization-rule keys list --resource-group $resourcegroupname \
        --namespace-name $namespace --name $sample_app_user_sas_key_name --query primaryKey
```

### Register in Azure Digital Twins

Now register the Service Bus as egress endpoints in ADT.

See docs here: https://docs.microsoft.com/en-gb/azure/digital-twins/how-to-egress-endpoints

```json
{
  "type": "ServiceBus",
  "eventTypes": ["TopologyOperation"],
  "connectionString": "",
  "secondaryConnectionString": "",
  "path": "messages-topic"
}
```

### Run the sample
