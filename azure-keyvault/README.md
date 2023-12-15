# Manorrock Ocelot - Azure Key Vault simulator

The Azure Key Vault project delivers you with a simulated Azure Key Vault
experience.

## Running the simulator

To run the simulator use the command line below:

```
  docker run --rm -it -p 8100:8080 -p 8101:8443 manorrock/ocelot-azure-keyvault
```

## Validate the simulator is up and running

To validate the simulator is up and running point your browser to 
http://localhost:8100/ Or if you want to access the simulator over HTTPS (which
is what the Azure SDK would use) browse to https://localhost:8101 

Note if your browser complains about the HTTPS link above it means you browser
does not trust its certificate and you will have to import it into your browser
certificate store.

## Mounting your own certificate directory

If you want to supply your own certificate instead of the generated one you
can mount the certificate directory.

```bash
  docker run --rm -it -p 8100:8080 -p 8101:8443 \
    -v $PWD/certs:/home/piranha/certs manorrock/ocelot-azure-keyvault
```

Replace $PWD/certs with the local directory that contains the `keystore` file.
