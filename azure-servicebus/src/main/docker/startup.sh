#!/bin/bash

#
# Run the application
#
java -jar piranha-dist-webprofile.jar --war-file ROOT.war --http-port 8102 --https-port 8202 --https-keystore-file certs/keystore --https-keystore-password password
