FROM grafana/promtail:make-images-static-26a87c9 as builder

FROM openresty/openresty:alpine
ENV TINI_VERSION v0.18.0
RUN apk add --update \
    &&  apk add --no-cache tini
ADD entrypoint.sh /entrypoint.sh
ADD promtail.sh  /promtail.sh
COPY nginx.conf usr/local/openresty/nginx/conf/
COPY --from=builder /usr/bin/promtail /usr/bin/
EXPOSE 80
ENTRYPOINT ["/sbin/tini","-s", "--", "/entrypoint.sh"]
