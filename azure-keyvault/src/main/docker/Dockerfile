FROM tomcat:10.1.18-jre17-temurin
ADD target/ROOT.war /usr/local/tomcat/webapps
ADD src/main/docker/server.xml /usr/local/tomcat/conf
WORKDIR /usr/local/tomcat/conf
RUN mkdir certs && \
    keytool -genkey -alias tomcat -keyalg RSA -keystore certs/keystore -keysize 4096 -storepass changeit -dname "CN=localhost"
