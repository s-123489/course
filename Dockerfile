FROM eclipse-temurin:17-jre-alpine

RUN addgroup -S spring && adduser -S spring -G spring
WORKDIR /app

# 使用正确的通配符复制JAR文件
COPY target/course-management-system-*.jar app.jar

RUN mkdir -p /app/logs && chown -R spring:spring /app
USER spring
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
