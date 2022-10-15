### Seata - 分布式事务

> `seata-server.sql` => https://github.com/seata/seata/blob/develop/script/server/db/mysql.sql
> `seata-server.properties` => https://github.com/seata/seata/blob/develop/script/config-center/config.txt

```shell
# 修改seata配置文件`./seata-server/resources/application.yml`
# 修改`docker-compose-seata.yml`相关IP配置
# nacos命名空间`prod`下新建配置`seata-server.properties`
# 新建数据库`seata-server`，导入sql脚本`./sql/seata-server.sql`
# 运行
docker-compose -f docker-compose-seata.yml -p seata up -d
# 进入容器
docker exec -it seata-server sh
# 查看日志
docker logs -f seata-server
```

访问seata控制台：[`ip地址:7091`](http://www.zhengqingya.com:7091)
登录账号密码默认：`seata/seata`

![seata-login.png](../../../image/seata-login.png)

![seata-TransactionInfo.png](../../../image/seata-TransactionInfo.png)

![seata-GlobalLockInfo.png](../../../image/seata-GlobalLockInfo.png)

nacos服务注册

![seata-nacos.png](../../../image/seata-nacos.png)