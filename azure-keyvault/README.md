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
http://localhost:8100/

## Generate your own certificate

If you want to generate your own certificate you can use the command-line below:

```
  keytool -genkey -alias tomcat -keyalg RSA -keystore keystore \
    -keysize 4096 -storepass password -dname "CN=localhost"
```

## Use your .NET development certificate

You first must to export the certificate into a .pfx file.

```
  dotnet dev-certs https -ep cert.pfx -p password
```

Then you must convert the .pfx file to a Java keystore.

```
  keytool -importkeystore -srckeystore cert.pfx -srcstorepass password -destkeystore keystore -deststorepass password
```

And now you can use the instructions below to use the keystore.

## Mounting your own certificate directory

If you want to supply your own certificate instead of the generated one you
can mount the certificate directory.

For example:

```bash
  docker run --rm -it -p 8100:8100 -p 8200:8200 \
    -v $PWD/certs:/home/piranha/certs manorrock/ocelot-azure-keyvault
```

Replace $PWD/certs with the local directory that contains the `keystore` file.

## What is the Key Vault URL?

If the port used is `8200` the Key Vault URL would be:

```text
  https://localhost:8200/
```

Note if you change the port number from `8200` to something else you will need
to also pass the BASE_URL environment variable on the command-line as the 
simulator needs to know the outside Key Vault base URL to properly generate
secret ids.

For example:

```bash
  docker run --rm -it -p 7100:8100 -p 7200:8200 \
    -e BASE_URL=https://localhost:7200 \
    -v $PWD/certs:/home/piranha/certs manorrock/ocelot-azure-keyvault
```

## How do you use this with the Azure SDK for Java?

The sample snippet below shows you how you would interact with the simulator
using the Azure SDK for Java.

```java
    var credential = new BasicAuthenticationCredential("username", "password");
    var keyVaultUri = "https://localhost:8200";
    var secretClient = new SecretClientBuilder()
                .vaultUrl(keyVaultUri)
                .credential(credential)
                .httpLogOptions(new HttpLogOptions().setLogLevel(BODY_AND_HEADERS))
                .disableChallengeResourceVerification()
                .buildClient();

    secretClient.setSecret("mySecret", "mySecretValue");
    String value = secretClient.getSecret("mySecret");
```

## Supported operations

1. [Get Secret](https://learn.microsoft.com/en-us/rest/api/keyvault/secrets/get-secret/get-secret?view=rest-keyvault-secrets-7.4&tabs=HTTP)
1. [Set Secret](https://learn.microsoft.com/en-us/rest/api/keyvault/secrets/set-secret/set-secret?view=rest-keyvault-secrets-7.4&tabs=HTTP)

## Official Azure Key Vault documentation

See the [REST API](https://learn.microsoft.com/en-us/rest/api/keyvault/).
