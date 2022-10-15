# 制作jdk8的新镜像 -> 支持字体

```shell
# 构建镜像 注：有点慢
docker build -t registry.cn-hangzhou.aliyuncs.com/zhengqing/openjdk:8-jdk-alpine-font . --no-cache
# 推送镜像
docker push registry.cn-hangzhou.aliyuncs.com/zhengqing/openjdk:8-jdk-alpine-font

# Dockerfile中引用新镜像
# FROM registry.cn-hangzhou.aliyuncs.com/zhengqing/openjdk:8-jdk-alpine-font
```
