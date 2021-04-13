FROM adoptopenjdk/openjdk11:slim
ENV GRADLE_VERSION 7.0
RUN apt-get update -y && \
    apt-get install -y unzip && \
    cd /usr/local && \
    curl -L -o gradle.zip https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip && \
    unzip gradle.zip && \
    mv gradle-${GRADLE_VERSION} gradle && \
    rm gradle.zip
ENV PATH $PATH:/usr/local/gradle/bin
WORKDIR /mnt
CMD ["gradle"]
