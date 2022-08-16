FROM openresty/openresty:alpine

ENV GATEWAY_URL http://127.0.0.1:8080

COPY dist /usr/share/nginx/html

COPY nginx.conf /usr/local/openresty/nginx/conf/nginx.conf

RUN ls /usr/share/nginx/html

RUN openresty -t

EXPOSE 44444


