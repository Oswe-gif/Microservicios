FROM openjdk:22-rc-jdk as builder

WORKDIR /app/msvc-cursos

COPY ./pom.xml /app
COPY ./msvc-cursos/.mvn ./.mvn
COPY ./msvc-cursos/mvnw .
COPY ./msvc-cursos/pom.xml .

#RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/
#uso este run para descargar las dependencias, puedo usar el de arriba si quiero o el de abajo, no hay mucha diferencia.
RUN ./mvnw dependency:go-offline

COPY ./msvc-cursos/src ./src

RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:22-rc-jdk
#usamos el patron builder para reducir espacio en la imagen, lo que hace es solo es usar el .jar al final sin necesidad de cargar en la imagen todas las dependencias, funciona para reducir espacio.
#muy similar cuando solo copiaba el .jar de manera manual sin necesidad de copiar todo el proyecto, pero ahora se hace de manera automatica.
WORKDIR /app

COPY --from=builder /app/msvc-cursos/target/msvc-cursos-0.0.1-SNAPSHOT.jar .

EXPOSE 8002

#el ENTRYPOINT  es mucho más estricto tal vez paqra temas de seguridad si no quiero que entren a la linea de comandos
#ENTRYPOINT ["java","-jar","msvc-usuarios-0.0.1-SNAPSHOT.jar"]

#Es menos estricto, podemos modificar
CMD ["java","-jar","msvc-cursos-0.0.1-SNAPSHOT.jar"]

#con CMD me permite reemplazar el comando para entrar dentro del contenedor e interactuar, pero en ENTRYPOINT no es modificable, o sea no me deja entrar al contedor para revisar carpetas, archivos, etc.
#docker run -p 8081:8081 -rm -it usuarios /bin/sh       -->Gracias a ese comando creo un contenedor, reemplazado el CMD y poder navegar por el contenedor haciendo uso de comandos de linux.

#con lo siguiente tambien funcionaria para entrar al terminal del docker (linux)
#CMD ["/bin/sh"] -->docker run -p 8081:8081 -rm -it usuarios