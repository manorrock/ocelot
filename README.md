_This project is no longer being maintained and unless a maintainer steps up it will be removed Jan 1st, 2023_

# Manorrock Ocelot

As a developer you have so many Cloud technologies that you have to use or you 
have to interact with but what you really want to do is to develop. Manorrock 
Ocelot is a set of tools that allow you to cut through the noise and make cloud
execution and/or deployment simple and lets you get back to developing your
applications.

## Prerequisites

1. Docker
1. Azure CLI

## Installing

Pick your OS below to download the installation bundle

| Linux | macOS | Windows |
|-------|-------|---------|
|<a href="https://repo1.maven.org/maven2/com/manorrock/ocelot/ocelot-cli-linux/"><img src="doc/images/logo-linux.png" height="75" width="75"></a>       |<a href="https://repo1.maven.org/maven2/com/manorrock/ocelot/ocelot-cli-macos/"><img src="doc/images/logo-apple.svg" height="75" width="75"></a>       |<a href="https://repo1.maven.org/maven2/com/manorrock/ocelot/ocelot-cli-windows/"><img src="doc/images/logo-windows.png" height="75" width="75"></a>|
| <a href="https://repo1.maven.org/maven2/com/manorrock/ocelot/ocelot-cli-linux/">Download</a> | <a href="https://repo1.maven.org/maven2/com/manorrock/ocelot/ocelot-cli-macos/">Download</a> | <a href="https://repo1.maven.org/maven2/com/manorrock/ocelot/ocelot-cli-windows/">Download</a> |

Note the links above will always take you to the top-level download location. Please click on the latest version available to get to the installation bundle.

### Linux

Once you have downloaded the `.tar.gz` bundle extract it to a directory of your choice and add the `bin` directory to ypur `PATH`.

### macOS

Once you have used the DMG installer to install the application into the `/Applications` directory add `/Applications/Manorrock Ocelot.app/Contents/MacOS` to your `PATH`.

### Windows

Once you have used the MSI installer to install the application add `C:\Program Files\Manorrock Ocelot` to your `PATH`

## Getting started

After installing you can get started by executing the following command line in the top-level directory of your project

```shell
  mo deploy
```

This will deploy your application using Docker.

## Deploying on Azure Container Instances

If you want to deploy your application on Azure Container Instances you can use the command line below.

```shell
  mo deploy --runtime aci
```
