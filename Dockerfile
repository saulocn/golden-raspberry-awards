FROM alpine/java:21-jdk

USER 185

COPY --chown=185 target/*-runner.jar /deployments/app.jar

EXPOSE 8080
USER 185
ENV JAVA_OPTS_APPEND="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

ENTRYPOINT [ "java","-jar", "/deployments/app.jar" ]

