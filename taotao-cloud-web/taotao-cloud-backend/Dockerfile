
FROM 192.168.99.240:8443/taotao-cloud/taotao-cloud-node:latest as build-stage

#RUN npm install -g cnpm --registry=https://registry.npm.taobao.org

WORKDIR /app

COPY . /app

RUN npm install

RUN npm run taotao-cloud-backend-react:build:start

ARG TAOTAO_CLOUD_HOST_ADDRESS
ARG TAOTAO_CLOUD_HOST_PREFIX
ARG TAOTAO_CLOUD_HOST_VERSION

ENV TAOTAO_CLOUD_HOST_ADDRESS $TAOTAO_CLOUD_HOST_ADDRESS
ENV TAOTAO_CLOUD_HOST_PREFIX $TAOTAO_CLOUD_HOST_PREFIX
ENV TAOTAO_CLOUD_HOST_VERSION $TAOTAO_CLOUD_HOST_VERSION

FROM nginx:stable-alpine as production-stage

COPY --from=build-stage /app/dist /usr/share/nginx/html

EXPOSE 80

ENTRYPOINT ["nginx", "-g", "daemon off;"]
