# Azure Digital twin Java sample

## TODOs

### Permit your daemon app on Twins instance

1. Create app as described here

2. Get Service Principal ID of your app

```shell
az ad sp show --id YOUR_AP_ID --query objectId -o tsv
```

3. Permitt your app as `DeviceAdministrator` and `SpaceAdministrator`

3.1. GET IDs of the roles

on `https://INSTANCE.REGION.azuresmartspaces.net/management/api/v1.0/system/roles`

3.2. POST the assigments

Note: to setup postman follow this guide https://docs.microsoft.com/en-us/azure/digital-twins/how-to-configure-postman

Examples values:

- Callback URL: https://www.getpostman.com/oauth2/callback
- Auth URL: https://login.microsoftonline.com/contoso.onmicrosoft.com/oauth2/authorize?resource=0b07f429-9f4b-4714-9392-cc5e8e80c8b0
- Client ID: XXXX

on `https://INSTANCE.REGION.azuresmartspaces.net/management/api/v1.0/roleassignments`

```json
{
  "roleId": "ID of DeviceAdministrator/SpaceAdministrator in your instance",
  "objectId": "Service Principal ID from above",
  "objectIdType": "ServicePrincipalId",
  "path": "/",
  "tenantId": "Directory ID your your AAD tenant"
}
```

### Wait until Devices can be created

https://YOUR_TWINS.westeurope.azuresmartspaces.net/management/api/v1.0/resources

### Register EventGrid

1. Register the topic

Follow the documentation here https://docs.microsoft.com/en-us/azure/event-grid/custom-event-quickstart

```shell
az eventgrid topic create --name twinsTopologyChange -l westeurope -g digitalTwin
```

2. Register in Twins service

See docs here: https://docs.microsoft.com/en-gb/azure/digital-twins/how-to-egress-endpoints

```json
{
  "type": "EventGrid",
  "eventTypes": ["TopologyOperation"],
  "connectionString": "",
  "secondaryConnectionString": "",
  "path": "twinstopologychange.westeurope-1.eventgrid.azure.net"
}
```

3. Deploy grid viewer app sample

Follow the documentation here https://docs.microsoft.com/en-us/azure/event-grid/custom-event-quickstart

```shell
az group deployment create \
  --resource-group digitalTwin \
  --template-uri "https://raw.githubusercontent.com/Azure-Samples/azure-event-grid-viewer/master/azuredeploy.json" \
  --parameters siteName=twinsevents hostingPlanName=viewerhost
```

```shell
az extension add -n eventgrid

endpoint=https://twinsevents.azurewebsites.net/api/updates

az eventgrid event-subscription create \
  -g digitalTwin \
  --topic-name twinsTopologyChange \
  --name twinsTopologyChangeSub \
  --advanced-filter data.type stringin device \
  --advanced-filter data.accesstype stringin create \
  --endpoint $endpoint
```

### Register Service Bus

1. Create Service Bus Sub

```shell

destination=messages
topic=${destination}-topic
queue=${destination}-queue
subscription=${destination}-subscription
namespace=digitaltwins
resourcegroupname=digitalTwin

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

2. Register in Twins service

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

### Create an Event Hub

```shell
az eventhubs namespace create --name digitaltwinseh --resource-group digitalTwin -l westeurope --enable-kafka

az eventhubs eventhub create --name twinshub --resource-group digitalTwin --namespace-name digitaltwinseh
```

2. Register in Twins service

See docs here: https://docs.microsoft.com/en-gb/azure/digital-twins/how-to-egress-endpoints

```json
{
  "type": "EventHub",
  "eventTypes": ["TopologyOperation"],
  "connectionString": "",
  "secondaryConnectionString": "",
  "path": "twinshub"
}
```
