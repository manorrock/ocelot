# Manorrock Ocelot - Azure Key Vault

The Azure Key Vault project delivers you with a simulated Azure Key Vault
experience.

We welcome feedback!

## Running the simulator

To run the simulator use the command line below:

```
  docker run --rm -it -p 8100:8080 -p 8101:8443 manorrock/ocelot-azure-keyvault
```

## Mounting your own certificate directory

If you want to supply your own certificate instead of the generated one you
can mount the certificate directory.

```bash
  docker run --rm -it -p 8100:8080 -p 8101:8443 -v $PWD/certs:/home/piranha/certs manorrock/ocelot-azure-keyvault
```
