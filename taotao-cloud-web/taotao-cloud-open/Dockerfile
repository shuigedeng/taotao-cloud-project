FROM node:alpine as builder

WORKDIR /root

COPY . /root

RUN ls

RUN apk add autoconf automake libtool zlib-dev

RUN yarn

RUN yarn build

# Second stage: minimal runtime environment
FROM openresty/openresty:alpine

ENV GATEWAY_URL http://127.0.0.1:8080

# copy dist from the first stage
COPY --from=builder /root/dist /usr/share/nginx/html

# copy nginx.conf
COPY nginx.conf /usr/local/openresty/nginx/conf/nginx.conf

# expose port
EXPOSE 80
