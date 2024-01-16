FROM ghcr.io/piranhacloud/webprofile:24.1.0
ADD target/ROOT.war /home/piranha
ADD src/main/docker/startup.sh /home/piranha
WORKDIR /home/piranha
USER piranha
RUN mkdir certs && \
    keytool -genkey -alias self-signed -keyalg RSA -keystore certs/keystore -keysize 4096 -storepass password -dname "CN=localhost"
CMD ["sh", "./startup.sh"]
