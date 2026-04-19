# Use JDK
FROM eclipse-temurin:17-jdk-alpine

# Set working directory inside container
WORKDIR /app

# Copy jar file into container
COPY target/*.jar app.jar

# Run the application
ENTRYPOINT ["java","-jar","/app/app.jar"]