FROM maven AS build1
COPY . /mnt
WORKDIR /mnt
RUN cd /mnt && \
    mvn install && \
    export FINALNAME='target/'$(mvn exec:exec -Dexec.executable='echo' -Dexec.args='${project.build.finalName}' -q)'.jar' && \
    cp $FINALNAME application.jar

FROM adoptopenjdk/openjdk11:slim
COPY --from=build1 /mnt/application.jar /mnt/application.jar
WORKDIR /mnt
CMD ["java", "-jar", "application.jar"]
