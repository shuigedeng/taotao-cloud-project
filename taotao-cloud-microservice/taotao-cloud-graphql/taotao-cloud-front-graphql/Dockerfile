FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER 981376577@qq.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /taotao-cloud-mall

WORKDIR /taotao-cloud-mall

EXPOSE 9800

ADD ./taotao-cloud-mall-biz/target/taotao-cloud-mall-biz-1.0.jar ./

CMD sleep 30; java -Djava.security.egd=file:/dev/./urandom -jar taotao-cloud-mall-biz-1.0.jar
