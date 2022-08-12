FROM amd64/ubuntu
RUN apt-get update
RUN apt-get install -y default-jre
RUN mkdir app
COPY target/ioto-device-example-1.0.jar app/ioto-device-example.jar
COPY cert app/cert
WORKDIR app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "ioto-device-example.jar"]