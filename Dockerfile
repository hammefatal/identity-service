# Dockerfile (identity-service/Dockerfile)
# 빌드 단계 1: 애플리케이션을 빌드하여 JAR 파일 생성
# 사용되는 이미지는 OpenJDK (Temurin)의 JDK 버전 21-jdk-alpine을 사용 (경량화된 리눅스 기반)
FROM eclipse-temurin:21-jdk-alpine AS build

# 작업 디렉터리를 /app으로 설정
WORKDIR /app

# Gradle Wrapper 관련 파일들을 복사 (빌드를 위해 필요)
COPY gradlew .
COPY gradle gradle

# Gradle 빌드 스크립트 파일 복사
COPY build.gradle .
COPY settings.gradle .

# 소스 코드 복사 (여기서 ./src는 프로젝트 루트의 src 폴더)
# *주의*: 만약 multi-module 프로젝트라면 COPY ./your-submodule-name/src ./your-submodule-name/src 등으로 명시해야 할 수 있습니다.
COPY src ./src

# Spring Boot 애플리케이션을 JAR 파일로 빌드 (bootJar 태스크 실행)
# `bootJar`는 Spring Boot 애플리케이션의 실행 가능한 JAR 파일을 생성합니다.
# `./gradlew bootJar`는 Dockerfile 내부에서 Gradle Wrapper를 실행하는 표준 방식입니다.
# --no-daemon : 빌드 속도 향상을 위해 데몬을 사용하지 않도록 설정
# -x test : 빌드 시 테스트를 건너뛰도록 설정 (CI/CD 파이프라인에서 테스트를 따로 수행하는 경우 유용)
RUN ./gradlew bootJar --no-daemon -x test

# 빌드 단계 2: 실행 가능한 최종 이미지 생성 (더 가벼운 JRE 이미지 사용)
# `amazoncorretto:21-alpine`는 AWS에서 제공하는 경량화된 OpenJDK JRE 이미지
FROM amazoncorretto:21-alpine

# 작업 디렉터리를 /app으로 설정
WORKDIR /app

# 빌드 단계 1에서 생성된 JAR 파일을 최종 이미지로 복사
# /app/build/libs/*.jar는 빌드 단계에서 생성된 JAR 파일의 기본 위치 (예: /app/build/libs/identity-service-0.0.1-SNAPSHOT.jar)
COPY --from=build /app/build/libs/*.jar app.jar

# 컨테이너 외부로 노출할 애플리케이션 포트 지정 (Spring Boot 기본 포트 8080)
EXPOSE 8080

# 컨테이너 시작 시 실행될 명령어 정의 (JAR 파일 실행)
# "-Dspring.profiles.active=production" 과 같은 JVM 인수를 추가할 수 있습니다.
ENTRYPOINT ["java", "-jar", "app.jar"]

# Docker Build Args를 사용하거나 환경 변수로 JVM Arguments를 설정하는 예시:
# ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE:-default}", "-jar", "app.jar"]