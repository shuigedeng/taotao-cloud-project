#FROM anapsix/alpine-java:8_server-jre_unlimited

FROM openjdk:8-jdk-alpine

MAINTAINER 981376577@qq.com

ARG APP_NAME=taotao-cloud-uc-biz

ARG workdir=/application/${APP_NAME}

LABEL app_name=${APP_NAME}

RUN mkdir -p ${workdir}

VOLUME ${workdir}

WORKDIR ${workdir}

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

EXPOSE 9800

ADD ./taotao-cloud-uc-biz/build/libs/taotao-cloud-uc-biz-1.6.0.jar ./

CMD sleep 30; java -Djava.security.egd=file:/dev/./urandom -jar taotao-cloud-uc-biz-1.6.0.jar
