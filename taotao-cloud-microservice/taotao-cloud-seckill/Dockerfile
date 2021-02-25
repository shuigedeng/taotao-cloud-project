FROM anapsix/alpine-java:8_server-jre_unlimited

MAINTAINER 981376577@qq.com

ENV TZ=Asia/Shanghai

RUN ln -sf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

RUN mkdir -p /taotao-cloud-seckill

WORKDIR /taotao-cloud-seckill

EXPOSE 9800

ADD ./taotao-cloud-seckill-biz/target/taotao-cloud-seckill-biz-1.0.jar ./

CMD sleep 30; java -Djava.security.egd=file:/dev/./urandom -jar taotao-cloud-seckill-biz-1.0.jar
