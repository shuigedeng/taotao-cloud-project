### RabbitMQ

```shell
# 当前目录下所有文件赋予权限(读、写、执行)
chmod -R 777 ./rabbitmq
# 运行 [ 注：如果之前有安装过，需要清除浏览器缓存和删除rabbitmq相关的存储数据(如:这里映射到宿主机的data数据目录)，再重装，否则会出现一定问题！ ]
docker-compose -f docker-compose-rabbitmq.yml -p rabbitmq up -d

# 运行3.7.8-management版本
# docker-compose -f docker-compose-rabbitmq-3.7.8-management.yml -p rabbitmq up -d

# 进入容器
docker exec -it rabbitmq /bin/bash
# 启用延时插件
rabbitmq-plugins enable rabbitmq_delayed_message_exchange
# 查看已安装插件
rabbitmq-plugins list
```

web管理端：[`ip地址:15672`](http://www.zhengqingya.com:15672)
登录账号密码：`admin/admin`

### RabbitMQ - 集群

```shell
# 当前目录下所有文件赋予权限(读、写、执行)
chmod -R 777 ./rabbitmq-cluster
# 设置Erlang Cookie文件权限(cookie文件必须只允许拥有者有权操作)
chmod 600 ./rabbitmq-cluster/.erlang.cookie
# 运行 [ 注：如果之前有安装过，需要清除浏览器缓存和删除rabbitmq相关的存储数据(如:这里映射到宿主机的data数据目录)，再重装，否则会出现一定问题！ ]
docker-compose -f docker-compose-rabbitmq-cluster.yml -p rabbitmq-cluster up -d

# 启用延迟插件(注：如果节点类型为内存节点，则无法启用延迟插件)
docker exec rabbitmq-1 /bin/bash -c 'rabbitmq-plugins enable rabbitmq_delayed_message_exchange'
docker exec rabbitmq-2 /bin/bash -c 'rabbitmq-plugins enable rabbitmq_delayed_message_exchange'

# 执行脚本 => 配置集群
sh ./rabbitmq-cluster/init-rabbitmq.sh

# 配置镜像队列(允许内建双活冗余选项，与普通队列不同，镜像节点在集群中的其他节点拥有从队列拷贝，一旦主节点不可用，最老的从队列将被选举为新的主队列)
docker exec -it rabbitmq-1 /bin/bash
rabbitmqctl set_policy -p my_vhost ha-all "^" '{"ha-mode":"all"}' --apply-to all
# 查看镜像队列`ha-all`
rabbitmqctl list_policies -p my_vhost
# 删除镜像队列`ha-all`
rabbitmqctl clear_policy -p my_vhost ha-all
```

web管理端：[`ip地址:15672`](http://www.zhengqingya.com:15672)
登录账号密码：`admin/admin`

![rabbitmq-cluster](./../../image/rabbitmq-cluster.png)
