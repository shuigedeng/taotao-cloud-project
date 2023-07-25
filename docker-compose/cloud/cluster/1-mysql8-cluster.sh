[mysqld]
character-set-server=utf8mb4
lower-case-table-names=1
default_authentication_plugin=mysql_native_password
secure_file_priv=/var/lib/mysql
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
# 主从复制-主机配置
# 主服务器唯一ID
server-id=1
# 启用二进制日志
log-bin=mysql-bin
# 设置不要复制的数据库(可设置多个)
#binlog-ignore-db=mysql
#binlog-ignore-db=information_schema
#binlog-ignore-db=performance_schema
# 设置需要复制的数据库(可设置多个)
#binlog-do-db=test
# 设置logbin格式
binlog_format=STATEMENT
#将从服务器从主服务器收到的更新记入到从服务器自己的二进制日志文件中
log-slave-updates
#控制binlog的写入频率。每执行多少次事务写入一次(这个参数性能消耗很大，但可减小MySQL崩溃造成的损失)
sync_binlog = 1
#这个参数一般用在主主同步中，用来错开自增值, 防止键值冲突
auto_increment_offset = 1
#这个参数一般用在主主同步中，用来错开自增值, 防止键值冲突
auto_increment_increment = 1
#二进制日志自动删除的天数，默认值为0,表示“没有自动删除”，启动时和二进制日志循环时可能删除
expire_logs_days = 7
#将函数复制到slave
log_bin_trust_function_creators = 1

docker run --name cluster2-master  \
--restart=always \
--privileged=true \
-e MYSQL_ROOT_PASSWORD=123456 \
-p 3376:3306 \
-v /root/mysql/cluster2/master/data/:/var/lib/mysql \
-v /root/mysql/cluster2/master/conf/my.cnf:/etc/mysql/my.cnf \
-d mysql

use mysql;
update user set host='%' where user='root';
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
alter user 'root'@'%' identified by '123456' password expire never;
flush privileges;
// 刷新权限

################################################################
[mysqld]
character-set-server=utf8mb4
lower-case-table-names=1
default_authentication_plugin=mysql_native_password
secure_file_priv=/var/lib/mysql
sql_mode=STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION
# 主从复制-主机配置
# 主服务器唯一ID
server-id=2
# 启用二进制日志
log-bin=mysql-bin
relay-log=mysql-relay
# 设置不要复制的数据库(可设置多个)
#binlog-ignore-db=mysql
#binlog-ignore-db=information_schema
#binlog-ignore-db=performance_schema
# 设置需要复制的数据库(可设置多个)
#binlog-do-db=test
# 设置logbin格式
binlog_format=STATEMENT
#将从服务器从主服务器收到的更新记入到从服务器自己的二进制日志文件中
log-slave-updates
#控制binlog的写入频率。每执行多少次事务写入一次(这个参数性能消耗很大，但可减小MySQL崩溃造成的损失)
sync_binlog = 1
#这个参数一般用在主主同步中，用来错开自增值, 防止键值冲突
auto_increment_offset = 1
#这个参数一般用在主主同步中，用来错开自增值, 防止键值冲突
auto_increment_increment = 1
#二进制日志自动删除的天数，默认值为0,表示“没有自动删除”，启动时和二进制日志循环时可能删除
expire_logs_days = 7
#将函数复制到slave
log_bin_trust_function_creators = 1

# slave1
docker run --name cluster2-slave1  \
--restart=always \
--privileged=true \
-e MYSQL_ROOT_PASSWORD=123456 \
-p 3386:3306 \
-v /root/mysql/cluster2/slave1/data/:/var/lib/mysql \
-v /root/mysql/cluster2/slave1/conf/my.cnf:/etc/mysql/my.cnf \
-d mysql

# slave2
docker run --name cluster2-slave2  \
--restart=always \
--privileged=true \
-e MYSQL_ROOT_PASSWORD=123456 \
-p 3396:3306 \
-v /root/mysql/cluster2/slave2/data/:/var/lib/mysql \
-v /root/mysql/cluster2/slave2/conf/my.cnf:/etc/mysql/my.cnf \
-d mysql

show variables like 'server_id';

mysql -uroot -p -h192.168.10.200 -P3316
create user 'slave'@'%' identified with mysql_native_password by '123456';
grant replication slave on *.* to 'slave'@'%';
flush privileges;

show master status;
show master status\G

stop slave;
change master to master_host='192.168.10.200',master_port=3376,master_user='slave',master_password='123456',master_log_file='mysql-bin.000003',master_log_pos=1611;
start slave;

show slave status;
show slave status\G



mysql -h 127.0.0.1 -uroot -p123456 -e "
use mysql;
update user set host='%' where user='root';
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
alter user 'root'@'%' identified by '123456' password expire never;
flush privileges;

create database `taotao-cloud-nacos-2.1.0`;
use `taotao-cloud-nacos-2.1.0`;
source nacos.sql;

create database `taotao-cloud-seata-1.5.1`;
use `taotao-cloud-seata-1.5.1`;
source seata.sql;

create database `taotao-cloud-zipkin-0.3.0`;
use `taotao-cloud-zipkin-0.3.0`;
source zipkin.sql;
"
