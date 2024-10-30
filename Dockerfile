# Gradle 빌드를 위한 이미지 (Java 17)
FROM gradle:7.5.1-jdk17 AS build

# 프로젝트 디렉토리 설정
WORKDIR /app

# Gradle 캐시를 활용하기 위해 필요한 파일만 먼저 복사 후 의존성 설치
COPY build.gradle.kts settings.gradle.kts ./
RUN gradle build --no-daemon || return 0

# 나머지 프로젝트 파일 복사 및 빌드
COPY . .
RUN gradle build --no-daemon

# JAR 파일 실행을 위한 경량 JRE 이미지 사용
FROM openjdk:17-jdk-slim

# 앱 디렉토리 설정
WORKDIR /app

# 빌드 단계에서 생성된 JAR 파일 복사
COPY --from=build /app/build/libs/*.jar app.jar

# 포트 설정 (예시로 기본 8080 포트 사용)
EXPOSE 8080

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "app.jar"]
