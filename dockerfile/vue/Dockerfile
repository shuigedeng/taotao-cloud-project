# 拉取nginx基础镜像
FROM nginx:1.21.1

# 维护者信息
MAINTAINER zhengqingya

# 将dist文件中的内容复制到 `/usr/share/nginx/html/` 这个目录下面
COPY dist/  /usr/share/nginx/html/
# 用本地配置文件来替换nginx镜像里的默认配置
COPY Docker/nginx/nginx.conf /etc/nginx/nginx.conf

# 对外暴漏的端口号
# [注：EXPOSE指令只是声明容器运行时提供的服务端口，给读者看有哪些端口，在运行时只会开启程序自身的端口！！]
EXPOSE 80

# 启动nginx容器
CMD ["nginx", "-g", "daemon off;"]
