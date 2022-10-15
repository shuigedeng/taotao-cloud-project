# 制作镜像

```shell
# 构建镜像
docker build -t registry.cn-hangzhou.aliyuncs.com/zhengqing/sentinel-dashboard:1.8.2-mysql . --no-cache
# 推送镜像
docker push registry.cn-hangzhou.aliyuncs.com/zhengqing/sentinel-dashboard:1.8.2-mysql
```
