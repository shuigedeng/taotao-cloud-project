### Jenkins

```shell
# 当前目录下所有文件赋予权限(读、写、执行)
chmod -R 777 ./jenkins
# 运行
docker-compose -f docker-compose-jenkins.yml -p jenkins up -d
```

访问地址：[`ip地址:8080`](http://www.zhengqingya.com:8080)

###### 查看密码

```shell
# 普通权限进入到docker容器
docker exec -it jenkins /bin/bash
# 使用root权限进入到docker容器
docker exec -it -u root jenkins /bin/bash
# 查看密码
cat /var/jenkins_home/secrets/initialAdminPassword
```

###### jenkins升级问题

```shell
# docker下jenkins升级只要需要替换容器中的jenkins.war文件并重启docker容器
# 1.进入docker容器，其中-u root是使用root权限登录
docker exec -u root -it jenkins /bin/bash 
# 2.使用wget命令下载最新版本的jenkins.war文件
# 3.使用whereis jenkins命令查看jenkins的安装路径       `/usr/share/jenkins/jenkins.war`
# 4.使用cp命令将新的war包覆盖旧文件即可
# 5.浏览器访问ip:8080/restart 重启即可升级成功
# 备注：在进行容器部署时可以将容器的【/user/share/jenkins】目录挂载在宿主机上，以后升级只需替换jenkins.war文件即可。此种方式存在一个问题，在部署后由于宿主机的挂载文件夹为空，所以在部署后无法正常启动容器，放入jenkins.war与ref文件即可正常启动。
```

###### jenkins时区设置问题
```shell script
# 1.进入系统管理->脚本命令行，执行下面命令设置为上海时间(该方式重启后失效)
System.setProperty('org.apache.commons.jelly.tags.fmt.timeZone', 'Asia/Shanghai') 
# 2.在部署容器时添加参数，-e JAVA_OPTS=-Duser.timezone=Asia/Shanghai（一直有效）
```