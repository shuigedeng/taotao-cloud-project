开发环境 win 配置：

1.redis 启动

打开一个命令窗口，进入到你解压的目录，输入命令：redis-server redis.windows.conf

2.部署redis为windows下的服务 命令如下：

再打开一个新的命令窗口，输入命令：redis-server --service-install redis.windows.conf

3.安装后的启动服务命令：redis-server --service-start

4.停止服务命令：redis-server --service-stop

redis的卸载命令：redis-server --service-uninstall

