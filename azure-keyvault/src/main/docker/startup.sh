#!/bin/bash

#
# Run the application
#
java -jar piranha-dist-webprofile.jar --war-file ROOT.war --http-port 8100 --https-port 8200 --https-keystore-file certs/keystore --https-keystore-password password
