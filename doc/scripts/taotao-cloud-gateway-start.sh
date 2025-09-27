#!/bin/bash
docker pull registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-gateway:2025.11

docker tag registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-gateway:2025.11 taotao-cloud-gateway:latest

docker stop gateway

docker rm gateway

docker run -d -p 33335:33335 --name gateway taotao-cloud-gateway:latest

#删除未使用的镜像
docker image prune -
