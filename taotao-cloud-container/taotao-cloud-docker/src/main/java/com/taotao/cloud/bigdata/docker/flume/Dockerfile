#FROM probablyfine/flume
#
#MAINTAINER saraivaufc <www.saraiva.dev>
#
#WORKDIR /opt/
#
#COPY ./lib/* /opt/flume/lib/
#
#COPY kafka_flume_hadoop.conf /opt/flume-config/flume.conf
#
## ENTRYPOINT
#COPY ./docker-entrypoint.sh docker-entrypoint.sh
#RUN chmod +x docker-entrypoint.sh
#ENTRYPOINT ["./docker-entrypoint.sh"]

#FROM adoptopenjdk/openjdk11:alpine-jre
#
#ENV FLUME_AGENT_NAME=agent
#ENV FLUME_CONF_DIR=conf
#ENV FLUME_CONF=conf/kafka_flume_hadoop.conf
#
#COPY ./software/apache-flume-1.9.0-bin.tar.gz /opt/
#
#WORKDIR /opt
#RUN tar -xzf apache-flume-1.9.0-bin.tar.gz
#RUN rm -rf apache-flume-1.9.0-bin.tar.gz
#RUN mv apache-flume-1.9.0-bin flume
#
#RUN cp flume/conf/flume-conf.properties.template flume/conf/flume-conf.properties
#ADD flume-env.sh flume/conf/flume-env.sh
#
#COPY kafka_flume_hadoop.conf flume/conf/
#COPY ./lib/* flume/lib/
#
#COPY ./docker-entrypoint.sh docker-entrypoint.sh
#RUN chmod +x docker-entrypoint.sh
#ENTRYPOINT ["./docker-entrypoint.sh"]

#CMD [ "sh","-c", "/opt/flume/bin/flume-ng agent -n agent -c /opt/flume/conf -f /opt/flume/conf/kafka_flume_hadoop.conf","&"]

FROM ubuntu:18.04

MAINTAINER saraivaufc <www.saraiva.dev>

WORKDIR /opt/

RUN cp /etc/apt/sources.list /etc/apt/sources.list.bak
RUN rm -f /etc/apt/sources.list

# 配置 国内 apt-get 更新源
RUN echo "deb http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-security main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-updates main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-proposed main restricted universe multiverse" >> /etc/apt/sources.list
RUN echo "deb-src http://mirrors.aliyun.com/ubuntu/ bionic-backports main restricted universe multiverse" >> /etc/apt/sources.list

RUN apt-get -qq update -y
RUN apt-get -qqy install axel openssh-server openssh-client sudo

# To download Java
RUN apt update && apt install -y openjdk-8-jdk

# To download flume
wget http://www.apache.org/dyn/closer.lua/flume/1.9.0/apache-flume-1.9.0-bin.tar.gz
RUN tar -xvzf apache-flume-1.9.0-bin.tar.gz
RUN mv apache-flume-1.9.0-bin flume

# BASH FILES
COPY ./bash_files/* /root/

RUN cp flume/conf/flume-conf.properties.template flume/conf/flume-conf.properties
COPY flume-env.sh flume/conf/flume-env.sh

COPY kafka_flume_hadoop.conf flume/conf/
COPY kafka_flume_hadoop_back.conf flume/conf/

# 需要copy  hadoop jar 包
COPY ./lib/* flume/lib/

# ENTRYPOINT
COPY ./docker-entrypoint.sh docker-entrypoint.sh
RUN chmod +x docker-entrypoint.sh
ENTRYPOINT ["./docker-entrypoint.sh"]
