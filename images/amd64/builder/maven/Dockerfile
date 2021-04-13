FROM adoptopenjdk/openjdk11:slim
ENV MAVEN_VERSION 3.6.3
RUN cd /usr/local && \
    curl -o maven.tar.gz https://downloads.apache.org/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    tar xfvz maven.tar.gz && \
    mv apache-maven-${MAVEN_VERSION} maven && \
    rm maven.tar.gz
ENV PATH $PATH:/usr/local/maven/bin
WORKDIR /mnt
CMD ["mvn", "clean", "install"]
