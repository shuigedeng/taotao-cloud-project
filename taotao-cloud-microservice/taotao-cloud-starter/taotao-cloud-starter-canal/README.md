# canal 集成为 springboot 组件

***


## 想法

###### 随着 spring boot 框架的逐渐流行，越来越多的 spring boot 组件随之诞生，今天阿导带大家一起来实现 canal 集成为 spring boot 的组件的详细过程。

###### 我们大佬需要我们做一个数据迁移和数据同步的业务，我就想着将其封装成 springboot 组件，这是写这个组件的初衷。

###### 也许还有小伙伴没接触过 canal ，我其实也刚接触两天左右，想了解的伙伴请前往[官网文档](https://github.com/alibaba/canal)

###### 我就不介绍这个 canal 了，因为我也刚看不久，我的见解都会在代码展示，下面来介绍我的思路。


***

## 思路

- 配置构思和书写

- 连接规则制定

- 书写 canal 客户端

- 监听 canal 客户端从 canal 服务端推送的消息，并处理。

- 处理消息机制，这里使用了集成接口和注解两种方式

- 测试类去测试所写的组件

***

## 实现

- 创建配置文件

    1. [CanalConfig.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/config/CanalConfig.java)：读取 spring boot 配置文件信息

    2. [CanalClientConfiguration.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/config/CanalClientConfiguration.java)：加载 canal 配置，并启动客户端

- 注解使其成为 spring boot 组件

  [EnableCanalClient.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/EnableCanalClient.java)：该注解作用是启用 canal

- canal 客户端书写：[SimpleCanalClient.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/core/SimpleCanalClient.java)

    1. 初始化监听器（注解方式：[CanalEventListener.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/CanalEventListener.java)；实现接口方式：[DealCanalEventListener.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/core/DealCanalEventListener.java)），这里通过一个工具类，BeanUtil,通过反射注入 bean (包含通过接口方式实现数据同步和注解方式的数据同步).

    2. 开启 canal 客户端，若是开启多个客户端，会开启多个进程。

    3. 初始化一个线程池，使得线程复用，减小频繁创建线程带来的内存开销。

    4. 通过线程池开启 canal 客户端，每一个客户端都是一个线程。

- canal 客户端处理消息过程：[AbstractBasicMessageTransponder.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/abstracts/AbstractBasicMessageTransponder.java)；[DefaultMessageTransponder.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/transfer/DefaultMessageTransponder.java)

    1. 获取消息，判断消息可用性

    2. 可用的消息处理机制

    3. 消息消费完成确认

    4. 处理消息发生异常，等待设定的心跳时间进行重试，当重试机制次数超过指定的次数，停止 canal 客户端，结束线程。

- canal 处理消息操作，主要通过反射和代理模式实现：[ListenPoint.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/ListenPoint.java)；[AbstractDBOption.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/abstracts/option/AbstractDBOption.java)

    1. 创建表操作

       通过注解方式：[CreateTableListenPoint.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/table/CreateTableListenPoint.java)

       实现接口方式：[CreateTableOption.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/abstracts/option/table/CreateTableOption.java)

    2. 删除表操作

       通过注解方式：[DropTableListenPoint.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/table/DropTableListenPoint.java)

       实现接口方式：[DropTableOption.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/abstracts/option/table/DropTableOption.java)

    3. 修改表信息

       通过注解方式：[AlertTableListenPoint.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/table/AlertTableListenPoint.java)

       实现接口方式：[AlertTableOption.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/abstracts/option/table/AlertTableOption.java)

    4. 重新命名表

       通过注解方式：[RenameTableListenPoint.java](https://github.com/wanwujiedao/spring-boot-starter-canal/tree/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/table/RenameTableListenPoint.java)

       实现接口方式：[RenameTableOption.java](https://github.com/wanwujiedao/spring-boot-starter-canal/tree/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/abstracts/option/table/RenameTableOption.java)

    5. 创建索引

       通过注解方式：[CreateIndexListenPoint.java](https://github.com/wanwujiedao/spring-boot-starter-canal/tree/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/table/CreateIndexListenPoint.java)

       实现接口方式：[CreateIndexOption.java](https://github.com/wanwujiedao/spring-boot-starter-canal/tree/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/abstracts/option/table/CreateIndexOption.java)

    6. 删除索引

       通过注解方式：[DropIndexListenPoint.java](https://github.com/wanwujiedao/spring-boot-starter-canal/tree/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/table/DropIndexListenPoint.java)

       实现接口方式：[DropIndexOption.java](https://github.com/wanwujiedao/spring-boot-starter-canal/tree/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/abstracts/option/table/DropIndexOption.java)

    7. 新增数据

       通过注解方式：[InsertListenPoint.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/content/InsertListenPoint.java)

       实现接口方式：[InsertOption.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/abstracts/option/content/InsertOption.java)

    8. 更新数据

       通过注解方式：[UpdateListenPoint.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/content/UpdateListenPoint.java)

       实现接口方式：[UpdateOption.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/abstracts/option/content/UpdateOption.java)

    9. 删除数据

       通过注解方式：[DeleteListenPoint.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/annotation/content/DeleteListenPoint.java)

       实现接口方式：[DeleteOption.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/starter-canal/src/main/java/com/wwjd/starter/canal/client/abstracts/option/content/DeleteOption.java)

- 整个流程是怎么运行的呢？下面请允许我通 UML 图的方式呈现出来【放大后更清晰】

  ![canal UML 图](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/canal%20UML.jpg?raw=true)

***



## 使用

###### 组件写好了，那么怎么来使用呢？打个比方，锄头做好了，如何日当午才是关键，哈哈....

###### 首先我们来看看通过 @ConfigurationProperties 注解的 CanalConfig 这个类，能配置的内容在 Instance 这个内部静态类里面，目前支持如下配置：

```markdown

           
           #是否是集群模式
           canal.client.instances.${wwjd}.clusterEnabled=true
           canal.client.instances.${wwjd}.cluster-enabled=true
           
           #zookeeper 地址
           canal.client.instances.${wwjd}.zookeeperAddress=127.0.0.1:2181,127.1.1.1:2187
           canal.client.instances.${wwjd}.zookeeper-address=127.0.0.1:2181,127.1.1.1:2187
            
           #canal 服务器地址，默认是本地的环回地址
           canal.client.instances.${wwjd}.host=127.0.0.1
            
           #canal 服务设置的端口，默认 11111
           canal.client.instances.${wwjd}.port=11111

           #集群 设置的用户名
           canal.client.instances.${wwjd}.userName=root
           canal.client.instances.${wwjd}.user-name=root
    
           #集群 设置的密码
           canal.client.instances.${wwjd}.password=123456
    
            
           #批量从 canal 服务器获取数据的最多数目
           canal.client.instances.${wwjd}.batchSize=1000
           canal.client.instances.${wwjd}.batch-size=1000
    
           #是否有过滤规则
           canal.client.instances.${wwjd}.filter=.*\\..*
    
           #当错误发生时，重试次树
           canal.client.instances.${wwjd}.retryCount=20
           canal.client.instances.${wwjd}.retry-count=20
    
           #信息捕获心跳时间
           canal.client.instances.${wwjd}.acquireInterval=1000
           canal.client.instances.${wwjd}.acquire-interval=1000

            
```

####### 假若你所有的环境都搞定了，包括 mysql 开启 binlog 日志，canal 伪装从数据库连接到 mysql 等，然后配置信息都正确，那就开始正文了

- 通过继承接口的方式处理数据：[MyEventListenerimpl.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/canal-test/src/main/java/com/wwjd/canal/canaltest/test/MyEventListenerimpl.java)


- 通过注解的方式处理数据：[MyAnnoEventListener.java](https://github.com/wanwujiedao/spring-boot-starter-canal/blob/master/canal-test/src/main/java/com/wwjd/canal/canaltest/test/MyAnnoEventListener.java)


###### 启动服务，操作 db，观察数据，至于你想对这些数据干什么，只要不作奸犯科，那随你便了....

```markdown

    ======================接口方式（修改表信息操作）==========================
    use dao;
    /* ApplicationName=IntelliJ IDEA 2018.1.2 */ ALTER TABLE user ADD age int DEFAULT 18 NOT NULL COMMENT '年龄'
    
    ======================================================
   
    ======================注解方式（修改表信息操作）==========================
    use dao;
    /* ApplicationName=IntelliJ IDEA 2018.1.2 */ ALTER TABLE user ADD age int DEFAULT 18 NOT NULL COMMENT '年龄'
    
    ======================================================
    ======================接口方式（新增数据操作）==========================
    use dao;
    INSERT INTO user(id,name,age) VALUES('85','阿导','107');
    
    ======================================================
    ======================注解方式（新增数据操作）==========================
    use dao;
    INSERT INTO user(id,name,age) VALUES('85','阿导','107');
    
    ======================================================

```

## 感言

###### 初识 canal ，不足之处，还望多多指正和批评，阿导在此感谢。
