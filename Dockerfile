# Usa una imagen base de Amazon Corretto
FROM amazoncorretto:17-alpine-jdk

# Establece el directorio de trabajo
WORKDIR /app

# Copia el empaquetado de tu aplicación a la imagen
COPY ./target/twelveshockcrm-1.0.0-SNAPSHOT.jar /app/twelveshock-crm-app.jar

# Expone el puerto en el que la aplicación escuchará
EXPOSE 8080 5005

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "twelveshock-crm-app.jar"]


