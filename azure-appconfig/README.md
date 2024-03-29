# Manorrock Ocelot - Azure App Configuration simulator

The Azure App Configuration project delivers you with a simulated Azure App
Configuration experience.

## Running the simulator

To run the simulator use the command line below:

```
  docker run --rm -it -p 8101:8101 -p 8201:8201 ghcr.io/manorrock/ocelot-azure-appconfig
```

## Validate the simulator is up and running

To validate the simulator is up and running point your browser to 
http://localhost:8101/ Or if you want to access the simulator over HTTPS (which
is what the Azure SDK would use) browse to https://localhost:8201

## Generate your own certificate

If you want to generate your own certificate you can use the command-line below:

```
  keytool -genkey -alias self-signed -keyalg RSA -keystore keystore \
    -keysize 4096 -storepass password -dname "CN=localhost"
```

## Use your .NET development certificate

You first must to export the certificate into a .pfx file.

```
  dotnet dev-certs https -ep cert.pfx -p password
```

Then you must convert the .pfx file to a Java keystore.

```
  keytool -importkeystore -srckeystore cert.pfx -srcstorepass password \
    -destkeystore keystore -deststorepass password
```

And now you can use the instructions below to use the keystore.

## Mounting your own certificate directory

If you want to supply your own certificate instead of the generated one you
can mount the certificate directory.

For example:

```bash
  docker run --rm -it -p 8101:8101 -p 8201:8201 \
    -v $PWD/certs:/home/piranha/certs manorrock/ocelot-azure-appconfig
```

Replace $PWD/certs with the local directory that contains the `keystore` file.

## What is the App Configuration URL?

If the port used is `8201` the App Configuration URL would be:

```text
  https://localhost:8201
```

## How do you use the simulator with the Azure SDK for Java?

The sample snippet below shows you how you would interact with the simulator
using the Azure SDK for Java.

```java
    var credential = new BasicAuthenticationCredential("username", "password");
    var endpoint = "https://localhost:8201";

    var configClient = new ConfigurationClientBuilder()
            .endpoint(endpoint)
            .credential(credential)
            .httpLogOptions(new HttpLogOptions().setLogLevel(BODY_AND_HEADERS))
            .buildClient();
        
    configClient.setConfigurationSetting("key", "label", "my_value");
    String value = configClient.getConfigurationSetting("key", "label").getValue());
```

## How do you use the simulator with the Azure SDK for .NET?

The sample snippet below shows you how you would interact with the simulator
using the Azure SDK for .NET

```csharp
  var appConfigUrl = "https://localhost:8201";
  var client = new ConfigurationClient(new Uri(appConfigUrl), 
      new TestCredential(), CreateDefaultOptions());
  var value = client.GetConfigurationSetting("myConfig"); 

  ConfigurationClientOptions CreateDefaultOptions() =>
    new()
    {
        Diagnostics =
        {
            LoggedContentSizeLimit = 8192,
            IsLoggingContentEnabled = true
        },
            Retry =
        {
            Delay = TimeSpan.FromSeconds(2),
            MaxDelay = TimeSpan.FromSeconds(16),
            MaxRetries = 5,
            Mode = RetryMode.Exponential
        }
    };

  internal class TestCredential : TokenCredential
    {
        public override AccessToken GetToken(TokenRequestContext requestContext, CancellationToken cancellationToken)
        {
            return new AccessToken("test", DateTimeOffset.UtcNow.AddHours(100));
        }

        public override ValueTask<AccessToken> GetTokenAsync(TokenRequestContext requestContext, CancellationToken cancellationToken)
        {
            return new ValueTask<AccessToken>(GetToken(requestContext, cancellationToken));
        }
    }
```
## Supported operations

1. [Get Key-Value](https://learn.microsoft.com/en-us/azure/azure-app-configuration/rest-api-key-value#get-key-value)
1. [Set Key](https://learn.microsoft.com/en-us/azure/azure-app-configuration/rest-api-key-value#set-key)

## Official Azure App Configuration documentation

See the [REST API](https://learn.microsoft.com/en-us/azure/azure-app-configuration/rest-api/).
