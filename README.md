# Manorrock Ocelot

As a developer you have so many Cloud technologies that you have to use or you 
have to interact with but what you really want to do is to develop. Manorrock 
Ocelot is a set of tools that allow you to cut through the noise and make cloud
execution and/or deployment simple and lets you get back to developing your
applications.

## Prerequisites

1. Docker
1. Azure CLI

## Getting started

After installing the CLI and making sure it is added to your PATH you can get started by executing the following command line in the top-level directory of your project

```shell
  mo deploy
```

This will deploy your application using Docker. 

## Deploying on Azure Container Instances

If you want to deploy your application on Azure Container Instances you can use the command line below.

```shell
  mo deploy --runtime aci
```