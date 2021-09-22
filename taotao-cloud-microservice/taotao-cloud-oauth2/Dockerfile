FROM registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-openjdk11:alpine

MAINTAINER 981376577@qq.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /taotao-cloud-oauth2

WORKDIR /taotao-cloud-oauth2

EXPOSE 33336

ADD ./taotao-cloud-oauth2-server/build/libs/taotao-cloud-oauth2-server-2021.9.3.jar ./

CMD sleep 30; java -Djava.security.egd=file:/dev/./urandom -Dspring.profiles.active=dev -jar taotao-cloud-oauth2-server-2021.9.3.jar

