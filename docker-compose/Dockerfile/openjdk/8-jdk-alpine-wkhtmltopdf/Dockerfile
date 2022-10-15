FROM openjdk:8-jdk-alpine

# 解决时差8小时问题
ENV TZ=Asia/Shanghai
RUN ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && echo $TZ > /etc/timezone

# 支持字体
RUN apk add --update ttf-dejavu fontconfig && rm -rf /var/cache/apk/*

# 添加中文字体
ADD simsun.ttc /usr/share/fonts/

# 安装wkhtmltopdf
RUN apk add --no-cache wkhtmltopdf
