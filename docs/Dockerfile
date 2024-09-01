# Etapa 1: Construcción
FROM maven:3.9.4-eclipse-temurin-21 AS build

# Establecer el directorio de trabajo dentro del contenedor
WORKDIR /app

# Instalar Node.js y npm
RUN apt-get update && apt-get install -y curl \
    && curl -fsSL https://deb.nodesource.com/setup_18.x | bash - \
    && apt-get install -y nodejs

# Copiar los archivos de configuración de Maven y Node.js
COPY pom.xml package*.json ./

# Descargar dependencias de Maven
RUN mvn dependency:go-offline -B

# Instalar dependencias de npm
RUN npm install

# Copiar el código fuente de la aplicación al contenedor
COPY src ./src

# Ejecutar npm run build:tailwind para construir Tailwind CSS
RUN npx tailwindcss -i ./src/main/resources/static/css/input.css -o ./src/main/resources/static/css/output.css --minify

# Compilar y empaquetar la aplicación Spring Boot
RUN mvn clean package -DskipTests

# Etapa 2: Ejecución
FROM eclipse-temurin:21-jdk-alpine

# Establecer el directorio de trabajo
WORKDIR /app

# Copiar el archivo JAR desde la etapa de construcción
COPY --from=build /app/target/*.jar app.jar

# Exponer el puerto en el que se ejecutará la aplicación
EXPOSE 8080

# Comando para ejecutar la aplicación
ENTRYPOINT ["java", "-jar", "app.jar"]
