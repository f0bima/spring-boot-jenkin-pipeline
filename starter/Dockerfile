# Stage 1: Build aplikasi menggunakan Java 25
FROM eclipse-temurin:25-jdk AS build

WORKDIR /app

# Salin Maven Wrapper dan konfigurasi Maven
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

RUN chmod +x mvnw

# Unduh dependency terlebih dahulu
RUN ./mvnw -B dependency:go-offline

# Salin source code
COPY src/ src/

# Build aplikasi tanpa menjalankan unit test lagi
RUN ./mvnw -B package -DskipTests


# Stage 2: Jalankan aplikasi menggunakan Java 25 JRE
FROM eclipse-temurin:25-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]