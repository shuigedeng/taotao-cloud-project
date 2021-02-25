#  基础镜像
FROM java:openjdk-8-jre-alpine
# 维护者信息
MAINTAINER im.lihaodong@gmail.com
#Default to UTF-8 file.encoding
ENV LANG C.UTF-8
#设置alpine时区
ENV TIMEZONE Asia/Shanghai
#alpine自带的包含dl-cdn的域名非常慢，需要修改后才能下载数据。
RUN apk add -U tzdata && ln -snf /usr/share/zoneinfo/${TIMEZONE} /etc/localtime && echo "${TIMEZONE}" > /etc/timezone
RUN sed -i -e 's/dl-cdn/dl-4/g' /etc/apk/repositories && apk add -U tzdata && ln -snf /usr/share/zoneinfo/${TIMEZONE} /etc/localtime && echo "${TIMEZONE}" > /etc/timezone
#解决验证码问题
RUN apk add fontconfig && apk add --update ttf-dejavu && fc-cache --force
#添加应用
ADD pre-system-1.4.jar pre-system-1.4.jar
#参数
#ENV PARAMS=""
#执行操作 默认启动线上环境
ENTRYPOINT [ "sh", "-c", "java -Xmx50m -Djava.security.egd=file:/dev/./urandom -jar pre-system-1.4.jar --spring.profiles.active=prod" ]

