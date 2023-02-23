FROM openjdk:8-jre

MAINTAINER Sinda(sindazeng@gmail.com)

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /XHuicloud-logs-service

WORKDIR /XHuicloud-logs-service

EXPOSE 18000

ADD ./target/XHuiCloud-logs-service.jar ./

CMD sleep 90;java -Xms512m -Xmx512m -Xss256k -Djava.security.egd=file:/dev/./urandom -jar XHuiCloud-logs-service.jar
