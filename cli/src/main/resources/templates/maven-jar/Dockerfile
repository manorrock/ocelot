#
# Execute Maven build to generate application.jar
#
FROM maven AS step1
COPY . /mnt
WORKDIR /mnt
RUN cd /mnt && \
    mvn install && \
    export FINALNAME='target/'$(mvn exec:exec -Dexec.executable='echo' -Dexec.args='${project.build.finalName}' -q)'.jar' && \
    cp $FINALNAME application.jar

#
# Create Java runtime image
#
FROM adoptopenjdk/openjdk11:slim
COPY --from=step1 /mnt/application.jar /mnt/application.jar
WORKDIR /mnt
CMD ["java", "-jar", "application.jar"]
