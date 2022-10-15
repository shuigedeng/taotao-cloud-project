### ELK

```shell
# 当前目录下所有文件赋予权限(读、写、执行)
chmod -R 777 ./elk
# 运行
docker-compose -f docker-compose-elk.yml -p elk up -d
# 若运行之后启动日志再次报相关权限问题，再次给新产生的文件赋予权限
chmod -R 777 ./elk
```

1. ES访问地址：[`ip地址:9200`](http://www.zhengqingya.com:9200)
   默认账号密码：`elastic/123456`
2. kibana访问地址：[`ip地址:5601`](http://www.zhengqingya.com:5601)
   默认账号密码：`elastic/123456`

#### 设置ES密码

```shell
# 进入容器
docker exec -it elasticsearch /bin/bash
# 设置密码-随机生成密码
# elasticsearch-setup-passwords auto
# 设置密码-手动设置密码
elasticsearch-setup-passwords interactive
```

![elk_elasticsearch_set_password](./../../image/elk_elasticsearch_set_password.png)
