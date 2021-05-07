FROM gradle:jdk11 AS build1
COPY . /mnt
WORKDIR /mnt
RUN cd /mnt && \
    gradle -Dorg.gradle.vfs.watch=false build && \
    export BASENAME=$(gradle -Dorg.gradle.vfs.watch=false jar properties -q | grep archivesBaseName | awk '{printf $NF}' | head -n 1) && \
    export VERSION=$(gradle -Dorg.gradle.vfs.watch=false jar properties -q | grep version | awk '{printf $NF}' | head -n 1) && \
    export FINALNAME='build/libs/'$BASENAME'-'$VERSION'.jar' && \
    cp $FINALNAME application.jar

FROM adoptopenjdk/openjdk11:slim
COPY --from=build1 /mnt/application.jar /mnt/application.jar
WORKDIR /mnt
CMD ["java", "-jar", "application.jar"]
