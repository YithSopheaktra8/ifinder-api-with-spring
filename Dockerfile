# Builder stage
FROM gradle:jdk21-alpine AS builder
WORKDIR /app
COPY . .
  #Note: Skipping tests is generally not recommended except for diagnostic purposes.
#RUN gradle build --no-daemon -x test

  # Build the application
RUN gradle build --no-daemon

  # Final stage
FROM openjdk:21-jdk-oracle
WORKDIR /app
  # Copy the built JAR from the builder stage and rename it for simplicity
COPY --from=builder /app/build/libs/*-SNAPSHOT.jar app.jar
EXPOSE 8080
VOLUME /home/pheaktra/spring-ifinder-deploy/media/IMAGE/
#VOLUME /keys
  # Now you can reference a fixed name in the ENTRYPOINT
ENTRYPOINT ["java", "-jar", "app.jar"]