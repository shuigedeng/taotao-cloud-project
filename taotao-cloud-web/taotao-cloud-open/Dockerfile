# Second stage: minimal runtime environment
FROM openresty/openresty:alpine

ENV GATEWAY_URL http://127.0.0.1:8080

# copy dist from the first stage
COPY dist /usr/share/nginx/html

# copy nginx.conf
COPY nginx.conf /usr/local/openresty/nginx/conf/nginx.conf

RUN ls /usr/share/nginx/html

RUN openresty -t

# expose port
EXPOSE 80 443
