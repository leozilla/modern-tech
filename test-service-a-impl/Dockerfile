FROM openjdk:9
ENTRYPOINT ["/usr/bin/java", "-jar", "/usr/share/test-service-a/test-service-a.jar"]

ADD target/lib /usr/share/test-service-a/lib
ADD target/test-service-a.jar /usr/share/test-service-a/test-service-a.jar