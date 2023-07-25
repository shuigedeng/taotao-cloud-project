FROM openjdk:8-jre-alpine

MAINTAINER zhengqingya

# 解决时差8小时问题
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 设置环境变量-运行时也可传参进来耍哈
ENV SENTINEL_AUTH_USERNAME sentinel
ENV SENTINEL_AUTH_PASSWORD sentinel
ENV MYSQL_SERVICE_HOST 127.0.0.1
ENV MYSQL_SERVICE_PORT 3306
ENV MYSQL_SERVICE_USER root
ENV MYSQL_SERVICE_PASSWORD root
ENV MYSQL_SERVICE_DB_NAME sentinel

# 添加jar包到容器中
ADD sentinel-dashboard.jar /app.jar

# 对外暴漏的端口号
EXPOSE 8858

# 运行🏃🏃🏃
CMD echo "****** start run ... " & \
    java -Djava.security.egd=file:/dev/./urandom -Dauth.username=${SENTINEL_AUTH_USERNAME} -Dauth.password=${SENTINEL_AUTH_PASSWORD} -Dmysql.host=${MYSQL_SERVICE_HOST} -Dmysql.port=${MYSQL_SERVICE_PORT} -Dmysql.username=${MYSQL_SERVICE_USER} -Dmysql.password=${MYSQL_SERVICE_PASSWORD} -Dmysql.db-name=${MYSQL_SERVICE_DB_NAME} -Dserver.port=8858 -Dcsp.sentinel.api.port=8719 -Dcsp.sentinel.dashboard.server=localhost:8858 -Dproject.name=sentinel-dashboard -jar /app.jar
