# Azure Digital Twin - Java application sample

This [Sprint Boot](https://spring.io/projects/spring-boot) app demonstrates how to leverage the ADT Java client to manage the ADT topology.

## Prerequisites

- Azure subscription.
- Azure Digital Twins instance.
- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli) to setup Azure Service Bus.
- OpenJDK 8 or 11 and Maven 3 to build and run the sample.

## Howto run the sample

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

### Wait until Devices can be created

https://INSTANCE.REGION.azuresmartspaces.net/management/api/v1.0/resources

### Run the sample

```bash
java -jar target/java-client-sample-0.1.0-SNAPSHOT.jar \
 --com.microsoft.twins.aad.clientSecret=AAD_PRINCIPAL_SECRET \
 --com.microsoft.twins.aad.clientId=AAD_PRINCIPAL_ID \
 --com.microsoft.twins.twinsUrl=https://INSTANCE.REGION.azuresmartspaces.net/management \
 --com.microsoft.twins.aad.tenant=TENANT.onmicrosoft.com
```
