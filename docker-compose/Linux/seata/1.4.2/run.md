### Seata - 分布式事务

> 1. config.txt => https://github.com/seata/seata/blob/develop/script/config-center/config.txt
> 2. nacos-config.sh => https://github.com/seata/seata/blob/develop/script/config-center/nacos/nacos-config.sh
> 3. seata.sql => https://github.com/seata/seata/blob/develop/script/server/db/mysql.sql

```shell
# 运行
docker-compose -f docker-compose-seata.yml -p seata up -d
# 查看日志
docker exec -it seata_server sh
```
