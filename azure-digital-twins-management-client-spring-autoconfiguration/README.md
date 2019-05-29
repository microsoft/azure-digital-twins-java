# Sprint auto configuration and integration tests

## Prerequisites

- Azure subscription.
- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli) to setup Azure Event Hubs.
- OpenJDK 11 and Maven 3.5 to build and run the tests.

## Howto run the tests

### SetUp Azure Events Hubs

First create a Azure resource group if you have not done so yet.

```bash
export resourceGroupName=bosch
az group create --name $resourceGroupName --location westeurope
```

Now create your [Azure Event Hub's](https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-about) namespace and hub. The namespace needs to be globally unique as it is used as DNS name as well.

Note: this sample leverages Azure Event Hub's Kafka support as an option for consuming events.

```bash
export namespace=boschhub
export eventHubName=twinsTestEvents
export consumerGroup=twinscg

az eventhubs namespace create --name $namespace --resource-group $resourceGroupName --enable-kafka

az eventhubs eventhub create --name $eventHubName --resource-group $resourceGroupName --namespace-name $namespace --message-retention 1 --partition-count 2

az eventhubs eventhub consumer-group create --eventhub-name $eventHubName --resource-group $resourceGroupName --namespace-name $namespace --name $consumerGroup
```

Now we can create users for your app and ADT with `Send` permission and for the sample app to `Listen`.

```bash
export adt_user_sas_key_name=dittouser
export test_user_sas_key_name=samplereader

az eventhubs eventhub authorization-rule create --resource-group $resourceGroupName --namespace-name $namespace --eventhub-name $eventHubName  --name $adt_user_sas_key_name --rights Send
az eventhubs eventhub authorization-rule create --resource-group $resourceGroupName --namespace-name $namespace --eventhub-name $eventHubName  --name $test_user_sas_key_name --rights Listen
```

Now the CLI again to retrieve the keys necessary:

First the connection strings for ADT:

```bash
az eventhubs eventhub authorization-rule keys list --resource-group $resourceGroupName --namespace-name $namespace --eventhub-name $eventHubName --name $adt_user_sas_key_name
```

First the connection strings for the test to consume:

```bash
az eventhubs eventhub authorization-rule keys list --resource-group $resourceGroupName --namespace-name $namespace --eventhub-name $eventHubName --name $test_user_sas_key_name
```