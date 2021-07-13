# Dockerfile

# https://docs.docker.com/engine/reference/builder/#from
# java 8 사용
FROM java:8

# https://docs.docker.com/engine/reference/builder/#expose
# 애플리케이션 포트
EXPOSE 8080

# https://docs.docker.com/engine/reference/builder/#add
# 애플리케이션 파일 추가
ADD ./build/libs/lvlz1s-1.0-SNAPSHOT.jar application.jar

# https://docs.docker.com/engine/reference/builder/#entrypoint
# 실행
ENTRYPOINT ["java", "-jar", "application.jar"]
