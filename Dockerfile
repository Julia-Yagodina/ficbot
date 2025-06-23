# Этап 1: Сборка приложения
FROM maven:3.9.6-amazoncorretto-21 AS build

WORKDIR /app
# Копируем только POM сначала для кэширования зависимостей
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Копируем исходный код и собираем приложение
COPY src ./src
RUN mvn clean package -DskipTests

# Этап 2: Финальный образ
FROM amazoncorretto:21-alpine

WORKDIR /app

# Создаем пользователя для безопасности
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

# Копируем собранный JAR-файл
COPY --from=build /app/target/*.jar app.jar

# Открываем порт приложения
EXPOSE 8080

# Команда запуска
ENTRYPOINT ["java", "-jar", "app.jar"]