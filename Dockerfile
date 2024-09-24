##De que imagen partimos
FROM amazoncorretto:17-alpine-jdk

##propietario
MAINTAINER TS
##Copia el empaquetado a github
COPY target/twelveshockcrm-1.0.0-SNAPSHOT.jar twelveshockcrm-app.jar
##es la instrucción que se va a ejecutar primero
ENTRYPOINT ["java","-jar","/twelveshockcrm-app.jar"]