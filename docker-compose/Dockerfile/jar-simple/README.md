### 使用示例命令

```shell
# 打包镜像 -f:指定Dockerfile文件路径 --no-cache:构建镜像时不使用缓存
docker build -f Dockerfile -t "registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot" . --no-cache

# 运行
docker run -d -p 3001:8080 -v /home/zhengqingya/app.log:/home/app.log --name springboot registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot

# 删除旧容器
docker rm -f springboot

# 删除旧镜像
docker rmi -f registry.cn-hangzhou.aliyuncs.com/zhengqingya/springboot
```
