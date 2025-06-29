#!/bin/bash
docker pull registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-monitor:2025.08

docker tag registry.cn-hangzhou.aliyuncs.com/taotao-cloud-project/taotao-cloud-monitor:2025.08 taotao-cloud-monitor:latest

docker stop monitor

docker rm monitor

docker run -d -p 33334:33334 --name monitor taotao-cloud-monitor:latest

#删除未使用的镜像
docker image prune -
