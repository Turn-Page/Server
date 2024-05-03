#base 이미지 가져오기 , builder라는 스테이지
FROM amazoncorretto:17-alpine-jdk AS builder

#작업 디렉토리를 /app으로 설정
WORKDIR /app

#gradlew, build.gradle, settings.gradle, gradle, src 가져오기
COPY gradlew build.gradle settings.gradle ./
COPY gradle ./gradle
COPY src ./src

# jar파일 생성
RUN ./gradlw bootJar

#두번째 레이어

#base 이미지 가져오기
FROM amazoncorretto:17-alpine-jdk

# 작업 디렉토리 설정
WORKDIR /app

# jar파일 가져오기 , 이전 스테이지 builder에서 생성된 jar파일을 복사해옴
COPY --from=builder /app/build/libs/*.jar /app/turnpage.jar
