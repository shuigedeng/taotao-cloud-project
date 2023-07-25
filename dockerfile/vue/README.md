### 使用示例命令

```shell
# 构建镜像
# -f：指定Dockerfile文件路径
# -t：镜像命名
# --no-cache：构建镜像时不使用缓存
# 最后有一个点 “.”：当构建的时候，由用户指定构建镜像的上下文环境路径，然后将此路径下的所有文件打包上传给Docker引擎，引擎内将这些内容展开后，就能获取到所有指定上下文中的文件了。
# ex: Dockerfile中`COPY dist/  /usr/share/nginx/html/` => 其实拷贝的并不是本机目录下的dist文件内容，而是Docker引擎中展开的构建上下文中的文件
docker build -f ./Docker/Dockerfile -t "registry.cn-hangzhou.aliyuncs.com/zhengqing/small-tools-web:prod" . --no-cache

# 推送镜像
docker push registry.cn-hangzhou.aliyuncs.com/zhengqing/small-tools-web:prod

# 拉取镜像
registry.cn-hangzhou.aliyuncs.com/zhengqing/small-tools-web:prod

# 运行
docker run -d --name small-tools-web -p 80:80 --restart=always registry.cn-hangzhou.aliyuncs.com/zhengqing/small-tools-web:prod

# 删除旧容器
docker ps -a | grep small-tools-web | grep prod | awk '{print $1}' | xargs -I docker stop {} | xargs -I docker rm {}

# 删除旧镜像
docker images | grep -E small-tools-web | grep prod | awk '{print $3}' | uniq | xargs -I {} docker rmi --force {}
```
