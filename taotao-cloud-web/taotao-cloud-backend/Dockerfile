FROM node:14-alpine AS build

#RUN npm install -g cnpm --registry=https://registry.npm.taobao.org

WORKDIR /app

COPY . /app

RUN apk add --no-cache \
            binutils-gold \
            g++ \
            gcc \
            gnupg \
            libgcc \
            linux-headers \
            make \
            python3

RUN npm install && npm run build

ARG TAOTAO_CLOUD_HOST_ADDRESS
ARG TAOTAO_CLOUD_HOST_PREFIX
ARG TAOTAO_CLOUD_HOST_VERSION

ENV TAOTAO_CLOUD_HOST_ADDRESS ${TAOTAO_CLOUD_HOST_ADDRESS:''}
ENV TAOTAO_CLOUD_HOST_PREFIX ${TAOTAO_CLOUD_HOST_PREFIX:''}
ENV TAOTAO_CLOUD_HOST_VERSION ${TAOTAO_CLOUD_HOST_VERSION:''}

FROM nginx:stable-alpine

COPY --from=build /app/dist /usr/share/nginx/html

EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]
