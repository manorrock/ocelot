# Manorrock Ocelot - Azure Key Vault simulator

The Azure Key Vault project delivers you with a simulated Azure Key Vault
experience.

## Running the simulator

To run the simulator use the command line below:

```
  docker run --rm -it -p 8100:8100 -p 8200:8200 ghcr.io/manorrock/ocelot-azure-keyvault
```

## Validate the simulator is up and running

To validate the simulator is up and running point your browser to 
http://localhost:8100/ Or if you want to access the simulator over HTTPS (which
is what the Azure SDK would use) browse to https://localhost:8200

Note if your browser complains about the HTTPS link above it means you browser
does not trust its certificate and you will have to import it into your browser
certificate store.

## Generate your own certificate

If you want to generate your own certificate you can use the command-line below:

```
  keytool -genkey -alias tomcat -keyalg RSA -keystore keystore \
    -keysize 4096 -storepass changeit -dname "CN=localhost"
```

## Mounting your own certificate directory

If you want to supply your own certificate instead of the generated one you
can mount the certificate directory.

For example:

```bash
  docker run --rm -it -p 8100:8100 -p 8200:8200 \
    -v $PWD/certs:/usr/local/tomcat/conf/certs manorrock/ocelot-azure-keyvault
```

Replace $PWD/certs with the local directory that contains the `keystore` file.

## What is the Key Vault URL?

If the port used is `8200` and the name of the keyvault is 'mykeyvault' the 
Key Vault URL would be:

```text
  https://localhost:8200/api/mykeyvault
```

Note if you change the port number from `8200` to something else you will need
to also pass the BASE_URL environment variable on the command-line as the 
simulator needs to know the outside Key Vault base URL to properly generate
ids, links and what not.

For example:

```bash
  docker run --rm -it -p 7100:8100 -p 7200:8200 \
    -e BASE_URL=https://localhost:7200 \
    -v $PWD/certs:/usr/local/tomcat/conf/certs manorrock/ocelot-azure-keyvault
```

## Supported operations

1. [Get Secret](https://learn.microsoft.com/en-us/rest/api/keyvault/secrets/get-secret/get-secret?view=rest-keyvault-secrets-7.4&tabs=HTTP)
1. [Set Secret](https://learn.microsoft.com/en-us/rest/api/keyvault/secrets/set-secret/set-secret?view=rest-keyvault-secrets-7.4&tabs=HTTP)

## Official Azure Key Vault documentation

See the [REST API](https://learn.microsoft.com/en-us/rest/api/keyvault/).
