# Twin Reflector Proxy deployment on Microsoft Azure

This guide describes the most simplistic installation of Twin Reflector Proxy on Microsoft Azure. It is not meant for productive use but rather for evaluation as well as demonstration purposes or as a baseline to evolve a production grade [Application architecture](https://docs.microsoft.com/en-us/azure/architecture/guide/) out of it which includes the Twin Reflector Proxy.

## Prerequisites

- An [Azure subscription](https://azure.microsoft.com/en-us/get-started/).
- [Azure CLI](https://docs.microsoft.com/en-us/cli/azure/install-azure-cli) installed to setup the infrastructure.
- [kubectl](https://kubernetes.io/docs/tasks/tools/install-kubectl/) and [helm](https://helm.sh/docs/using_helm/#installing-helm) installed to deploy the Twin Reflector Proxy into [Azure Kubernetes Service (AKS)](https://docs.microsoft.com/en-us/azure/aks/intro-kubernetes).

## HowTo install the Twin Reflector Proxy

First we are going to setup the basic Kubernetes infrastructure.

As described [here](https://docs.microsoft.com/en-gb/azure/aks/kubernetes-service-principal) we will create an explicit service principal first. You can add roles to this principal later, e.g. to access a [Azure Container Registry (ACR)](https://docs.microsoft.com/en-us/azure/container-registry/container-registry-intro).

```bash
service_principal=`az ad sp create-for-rbac --name http://proxyServicePrincipal --skip-assignment --output tsv`
app_id_principal=`echo $service_principal|cut -f1 -d ' '`
password_principal=`echo $service_principal|cut -f4 -d ' '`
object_id_principal=`az ad sp show --id $app_id_principal --query objectId --output tsv`
```

Note: it might take a few seconds until the principal is available to the cluster in the later steps. So maybe time to get up and stretch a bit.

```bash
resourcegroup_name=armtest
az group create --name $resourcegroup_name --location "westeurope"
```

With the next command we will use the provided [Azure Resource Manager (ARM)](https://docs.microsoft.com/en-us/azure/azure-resource-manager/resource-group-overview) templates to setup the AKS cluster. This might take a while. So maybe time to try out this meditation thing :smile:

```bash
unique_solution_prefix=myprefix
az group deployment create --name TwinReflectorProxyBasicInfrastructure --resource-group $resourcegroup_name --template-file arm/proxyInfrastructureDeployment.json --parameters uniqueSolutionPrefix=$unique_solution_prefix servicePrincipalObjectId=$object_id_principal servicePrincipalClientId=$app_id_principal servicePrincipalClientSecret=$password_principal
```

The output of the command will provide you with the name of your AKS cluster as well as the created Vnet which should be `YOUR_PREFIXproxyaks` and `YOUR_PREFIXproxyvnet`.

Note: AKS cluster name, IP address name for the load balancer as well as virtual network name can be provided as parameter to the template as well.

Optional: retrieve created static public IP address for the load balancer:

```bash
ip_address_name=$unique_solution_prefix
ip_address_name+="proxypip"
ip_address=`az network public-ip show --resource-group $resourcegroup_name --name $ip_address_name --query ipAddress --output tsv`
```

Now you can set your cluster in `kubectl`.

```bash
aks_cluster_name=$unique_solution_prefix
aks_cluster_name+="proxyaks"
az aks get-credentials --resource-group $resourcegroup_name --name $aks_cluster_name
```

Next deploy helm on your cluster. It will take a moment until tiller is booted up. So maybe time again to get up and stretch a bit.

```bash
kubectl apply -f helm-rbac.yaml
helm init --service-account tiller
```

Next we prepare the k8s environment and our chart for deployment.

```bash
k8s_namespace=proxyns
kubectl create namespace $k8s_namespace
```

Now install Twin Reflector Proxy:

```bash
helm upgrade twin-reflector-proxy ../helm/twin-reflector-proxy/ --namespace $k8s_namespace --set service.type=LoadBalancer,service.loadBalancerIP.enabled=true,service.loadBalancerIP.address=$ip_address,service.annotations."service\.beta\.kubernetes\.io/azure-load-balancer-resource-group"=$resourcegroup_name --wait --install
```

Have fun with the Twin Reflector Proxy on Microsoft Azure!
