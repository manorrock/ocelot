FROM maven AS build1
COPY . /mnt
WORKDIR /mnt
RUN cd /mnt && \
    mvn install && \
    export FINALNAME='target/'$(mvn exec:exec -Dexec.executable='echo' -Dexec.args='${project.build.finalName}' -q)'.war' && \
    cp $FINALNAME application.war

FROM tomcat:9-slim
RUN cd /usr/local/tomcat/webapps && \
    rm -rf ROOT
COPY --from=build1 /mnt/application.war /usr/local/tomcat/webapps/ROOT.war
