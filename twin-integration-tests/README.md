# Azure Digital Twins - Java integration tests

## Prerequisites

- Azure subscription.
- - Azure Digital Twins instance.
- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli) to setup Azure Event Hubs.
- OpenJDK 11 and Maven 3.5 to build and run the tests.

## Howto run the tests

### Permit your daemon app on Twins instance

1. Create app as described [here](https://docs.microsoft.com/en-us/azure/digital-twins/how-to-configure-postman).

2. Get Service Principal ID of your app

```shell
az ad sp show --id YOUR_AP_ID --query objectId -o tsv
```

3. Permitt your app as `DeviceAdministrator` and `SpaceAdministrator`

3.1. GET IDs of the roles

on `https://INSTANCE.REGION.azuresmartspaces.net/management/api/v1.0/system/roles`

Note: to setup postman follow this guide https://docs.microsoft.com/en-us/azure/digital-twins/how-to-configure-postman

Examples values:

- Callback URL: https://www.getpostman.com/oauth2/callback
- Auth URL: https://login.microsoftonline.com/contoso.onmicrosoft.com/oauth2/authorize?resource=0b07f429-9f4b-4714-9392-cc5e8e80c8b0
- Client ID: XXXX

  3.2. POST the assignments

on `https://INSTANCE.REGION.azuresmartspaces.net/management/api/v1.0/roleassignments`

```json
{
  "roleId": "ID of DeviceAdministrator/SpaceAdministrator in your instance",
  "objectId": "Service Principal ID from above",
  "objectIdType": "ServicePrincipalId",
  "path": "/",
  "tenantId": "Directory ID of your AAD tenant"
}
```

### SetUp Azure Events Hubs

First create a Azure resource group if you have not done so yet.

```bash
resourceGroupName=adttest
az group create --name $resourceGroupName --location westeurope
```

Now create your [Azure Event Hub's](https://docs.microsoft.com/en-us/azure/event-hubs/event-hubs-about) namespace and hub. The namespace needs to be globally unique as it is used as DNS name as well.

Note: the integration tests leverage Azure Event Hub's Kafka support as an option for consuming events.

```bash
namespace=adttest

az eventhubs namespace create --name $namespace --resource-group $resourceGroupName --enable-kafka

az eventhubs eventhub create --name twinstestevents --resource-group $resourceGroupName --namespace-name $namespace --message-retention 1 --partition-count 3
az eventhubs eventhub consumer-group create --eventhub-name twinstestevents --resource-group $resourceGroupName --namespace-name $namespace --name twinscg

az eventhubs eventhub create --name proxyingress --resource-group $resourceGroupName --namespace-name $namespace --message-retention 1 --partition-count 3
az eventhubs eventhub consumer-group create --eventhub-name proxyingress --resource-group $resourceGroupName --namespace-name $namespace --name proxyingresscg

az eventhubs eventhub create --name topologyoperations --resource-group $resourceGroupName --namespace-name $namespace --message-retention 1 --partition-count 3
az eventhubs eventhub consumer-group create --eventhub-name topologyoperations --resource-group $resourceGroupName --namespace-name $namespace --name topologyoperationscg
```

Next the connection strings for the tests:

```bash
primary_connection_string=`az eventhubs namespace authorization-rule keys list --resource-group $resourceGroupName --namespace-name $namespace --name RootManageSharedAccessKey -o tsv --query primaryConnectionString`
secondary_connection_string=`az eventhubs namespace authorization-rule keys list --resource-group $resourceGroupName --namespace-name $namespace --name RootManageSharedAccessKey -o tsv --query secondaryConnectionString`
```

### Run Integration test

```bash
mvn test  -Dcom.microsoft.twins.aad.clientSecret=AAD_PRINCIPAL_SECRET \
 -Dcom.microsoft.twins.aad.clientId=AAD_PRINCIPAL_ID \
 -Dcom.microsoft.twins.twinsUrl=https://INSTANCE.REGION.azuresmartspaces.net/management \
 -Dcom.microsoft.twins.aad.tenant=TENANT.onmicrosoft.com \
 -Dazure.event-hubs.test.connection-string=$primary_connection_string \
 -Dazure.event-hubs.test.secondary-connection-string=$secondary_connection_string \
 -Dazure.event-hubs.test.namespace=$namespace
```
