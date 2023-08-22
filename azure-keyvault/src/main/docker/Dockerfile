FROM ghcr.io/piranhacloud/webprofile:23.8.0
ADD target/ROOT.war /home/piranha
CMD ["java", "-jar", "piranha-dist-webprofile.jar", "--war-file", "ROOT.war"]
