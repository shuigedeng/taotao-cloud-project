FROM openresty/openresty:1.19.3.2-alpine-apk

LABEL maintainer="??? <???@???.com>"

ENV TZ=Asia/Shanghai

RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.aliyun.com/g' /etc/apk/repositories && \
    # deps
    apk update && \
    apk --no-cache add bind-tools \
        tzdata \
        && \
    # DNS
    [ ! -e /etc/nsswitch.conf ] && echo 'hosts: files dns' > /etc/nsswitch.conf && \
    # timezone
    ln -snf /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo '$TZ' > /etc/timezone && \
    # /www
    # /wwwlog
    # /app
    addgroup -g 500 -S www && \
    adduser -u 500 -D -S -G www www && \
    mkdir /www && mkdir /wwwlog && mkdir -p /app && \
    chown -R www:www /www && chown -R www:www /wwwlog && chown -R www:www /app && \
    addgroup www tty && \
    sed -i 's/\/home\/www:\/sbin\/nologin/\/home\/www:\/bin\/ash/g' /etc/passwd && \
    # password
    passwd -u root -d "!!!Production!!!" && \
    passwd -u www  -d "!!!Production!!!"

COPY conf/nginx.conf            /usr/local/openresty/nginx/conf/nginx.conf
