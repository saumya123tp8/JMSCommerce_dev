FROM eclipse-temurin:25-jdk-jammy As build

WORKDIR /app

COPY . .

RUN chmod +x gradlew && ./gradlew bootjar -x test --no-daemon

FROM eclipse-temurin:25-jre-jammy

WORKDIR /app

COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]