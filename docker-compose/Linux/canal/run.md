### canal

阿里巴巴 MySQL binlog 增量订阅&消费组件

```shell
# 导入初始化SQL
Linux/canal/canal/canal_admin/canal_manager.sql

# 运行  (tips:先修改配置文件信息)
docker-compose -f docker-compose-canal.yml -p canal up -d
```

访问地址：[`http://ip地址:8089`](http://www.zhengqingya.com:8089)
默认登录账号密码：`admin/123456`
