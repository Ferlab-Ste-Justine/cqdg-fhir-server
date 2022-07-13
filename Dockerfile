FROM maven:3.8.5-openjdk-17-slim as build-hapi

WORKDIR /tmp/cqdg-fhir-server
COPY . .

RUN mvn clean install -DskipTests

FROM tomcat:9-jre17
RUN rm -rf /usr/local/tomcat/webapps/*
RUN mkdir -p /data/hapi/lucenefiles && chmod 775 /data/hapi/lucenefiles
COPY --from=build-hapi /tmp/cqdg-fhir-server/target/ROOT.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080

CMD ["catalina.sh", "run"]