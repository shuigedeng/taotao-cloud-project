### PostgreSQL

运行

> tips: 未做数据持久化配置！

```shell
docker-compose -f docker-compose-postgresql.yml -p postgresql up -d
```

连接

![postgresql-navicat-connection.png](../../image/postgresql-navicat-connection.png)

```shell
# 进入容器
docker exec -it postgresql /bin/bash
# 登录
psql -U postgres -W
# 查看版本
select version();
```
