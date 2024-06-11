FROM openjdk:22-rc-jdk as builder
ARG MSVC_NAME=msvc-usuarios
WORKDIR /app/$MSVC_NAME

COPY ./pom.xml /app
COPY ./$MSVC_NAME/.mvn ./.mvn
COPY ./$MSVC_NAME/mvnw .
COPY ./$MSVC_NAME/pom.xml .

RUN ./mvnw clean package -Dmaven.test.skip -Dmaven.main.skip -Dspring-boot.repackage.skip && rm -r ./target/

COPY ./$MSVC_NAME/src ./src

RUN chmod +x ./mvnw
RUN ./mvnw clean package -DskipTests

FROM openjdk:22-rc-jdk
#usamos el patron builder para reducir espacio en la imagen, lo que hace es solo es usar el .jar al final sin necesidad de cargar en la imagen todas las dependencias, funciona para reducir espacio.
#muy similar cuando solo copiaba el .jar de manera manual sin necesidad de copiar todo el proyecto, pero ahora se hace de manera automatica.
WORKDIR /app

COPY --from=builder /app/msvc-usuarios/target/msvc-usuarios-0.0.1-SNAPSHOT.jar .
#eso este comando por si quiero usar una variable de ambiente dentro de mi dockerFile y cuando corro el contenedor ps toma el valor pasado por el usuario o el valor defecto en caso de no ingresar nada.
#cuando ejecuto el docker run, puedo usar el -e para pasar las variables de ambiente o puedo pasarle el archivo .env que tiene ya todo y toma esos valores para configurar el proyecto.
#Docker me permite usar variables de ambiente que estan dentro de mi proyecto, solo haciendo uso del comando -e o --env-file ./ruta/.env
ENV PORT 8081
EXPOSE $PORT
#el ENTRYPOINT  es mucho más estricto tal vez paqra temas de seguridad si no quiero que entren a la linea de comandos
#ENTRYPOINT ["java","-jar","msvc-usuarios-0.0.1-SNAPSHOT.jar"]

#Es menos estricto, podemos modificar
CMD ["java","-jar","msvc-usuarios-0.0.1-SNAPSHOT.jar"]

#con CMD me permite reemplazar el comando para entrar dentro del contenedor e interactuar, pero en ENTRYPOINT no es modificable, o sea no me deja entrar al contedor para revisar carpetas, archivos, etc.
#docker run -p 8081:8081 -rm -it usuarios /bin/sh       -->Gracias a ese comando creo un contenedor, reemplazado el CMD y poder navegar por el contenedor haciendo uso de comandos de linux.

#con lo siguiente tambien funcionaria para entrar al terminal del docker (linux)
#CMD ["/bin/sh"] -->docker run -p 8081:8081 -rm -it usuarios