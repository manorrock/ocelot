FROM ghcr.io/piranhacloud/webprofile:23.8.0
COPY target/http-simulator.war ROOT.war
USER root
RUN chown -R piranha:piranha /home/piranha
USER piranha
CMD ["java", "-jar", "piranha-dist-webprofile.jar", "--war-file", "ROOT.war", "--verbose"]
