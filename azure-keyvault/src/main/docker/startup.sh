#!/bin/bash

#
# Run the application
#
java -jar piranha-dist-webprofile.jar --war-file ROOT.war --https-port 8443 --https-keystore-file certs/keystore --https-keystore-password password
