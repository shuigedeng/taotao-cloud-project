## 介绍

![Version](https://img.shields.io/static/v1?label=Version&message=2.2.0&color=brightgreen)
![Jdk](https://img.shields.io/static/v1?label=JDK&message=8.0&color=green)
![Nacos](https://img.shields.io/static/v1?label=Nacos&message=1.43&color=orange)
![Netty](https://img.shields.io/static/v1?label=Netty&message=4.1.75.Final&color=blueviolet)
![License](https://img.shields.io/static/v1?label=License&message=Apache2&color=blue)

一个分布式微服务RPC框架 | [英文说明文档](/README.md) | [SpringBoot整合RPC](/document/springboot整合rpc-netty-framework.md)

- [x] 基于`Socket`和`Netty`异步非阻塞通信的解决方案；
- [x] 适用基于`Netty`的`IO`密集型场景应用，性能虽不如`CPU`密集型场景应用，但并发性是最好的；
- [x] 支持分布式超时重试机制、幂等历史结果淘汰策略、异步缓存实现高效通信；
- [x] 实现采用`Jedis/Lettuce`两种基于雪花算法的`id`生成器;
- [x] 支持`JDK`内置`SPI`机制，实现接口与实现解耦；
- [x] 注册中心高可用性，提供集群注册中心，所有注册节点宕机后仍能通过缓存为用户持续提供服务；
- [x] 提供个性化服务，推出个性化服务`name`、服务`group`，适合在测试、实验和正式环境的服务，以及为后期版本的兼容、维护和升级提供更好的服务；
- [ ] 提供集群注册中心宕机重启服务；
- [x] 提供服务无限制横向扩展；
- [x] 提供服务的两种负载均衡策略，如随机和轮询负载；
- [x] 提供请求超时重试，且保障业务执行的幂等性，超时重试能降低线程池任务的延迟，线程池保障了高并发场景下线程数创建数量的稳定，却因而带来延迟问题，处理该问题可以启用重试请求，且重试达到阈值将放弃请求，认为该服务暂时不可用，造成业务损耗，请慎用；
- [x] 提供自定义注解扩展服务，使用代理扩展，能无侵入式扩展个性化服务；
- [x] 提供可扩展的序列化服务，目前提供`Kryo`和`Jackson`两种序列化方式；
- [x] 提供日志框架`Logback`；
- [x] 提供Netty可扩展的通信协议，通信协议头使用与Class一样的16位魔数`0xCAFEBABE`、包辨识id，用来辨识请求包和响应包、`res`长度，用来防止粘包，以及最后的`res`，内部加入检验码和唯一识别id，让服务器能高效地同时处理多个不同请求包或重发请求包，以及包校验；
- [ ] 支持秒级时钟回拨服务端采取主动屏蔽客户端请求策略、分级以上时钟回拨服务端采取主动下线策略；
- [x] 配合超时重试机制对劫持包采用沉默和重发处理，加强通信的安全性。
- [x] 支持服务端单向延时关闭处理请求。
- [x] 支持扩展`Bean`自定义实例化，满足第三方框架如`Spring`依赖注入、切面执行。

架构图

- 重试机制架构图

![分布式异步超时重试机制.png](https://yupeng-tuchuang.oss-cn-shenzhen.aliyuncs.com/分布式异步超时重试机制.png)


- 服务发现与注册架构图

![服务发现与注册.png.png](https://yupeng-tuchuang.oss-cn-shenzhen.aliyuncs.com/服务发现与注册.png)

### 1. 服务提供
- 负载均衡策略
- 序列化策略
- 自动发现和注销服务
- 注册中心
- 单机与集群

---- 

### 2. 安全策略
- 心跳机制
- 信息摘要
- 超时重试机制
- 幂等性
- 雪花算法
- 健壮性

---- 

### 3. 设计模式
- 单例模式
- 动态代理
- 静态工厂
- 建造者
- 策略模式
- Future(观察者）

---- 

## 亮点
### 1. 信息摘要算法的应用
对于信息摘要算法的使用，其实并不难，在数据包中添加 `String` 类型的成员变量 `checkCode` 用来防伪的就可以实现。
- 原理

发送端把原信息用`HASH`函数加密成摘要,然后把数字摘要和原信息一起发送到接收端,接收端也用HASH函数把原消息加密为摘要,看两个摘要是否相同,若相同,则表明信息的完整.否则不完整。
- 实现

客户端在发出 请求包，服务端会在该请求包在请求执行的结果的内容转成字节码后，使用 MD5 单向加密成为 唯一的信息摘要（128 比特，16 字节）存储到响应包对应的成员变量 `checkCode` 中，所以客户端拿到响应包后，最有利用价值的地方（请求要执行的结果被改动），那么 `checkCode` 将不能保证一致性，这就是信息摘要的原理应用。

**安全性再增强**

考虑到这只是针对客户需求的结果返回一致性，并不能确保请求包之间存在相同的请求内容，所以引入了请求 `id`。

每个包都会生成唯一的 `requestId`，发出请求包后，该包只能由该请求发出的客户端所接受，就算两处有一点被对方恶意改动了，客户端都会报错并丢弃收到的响应包，不会拆包后去返回给用户。

如果不是单单改动了返回结果，而是将结果跟信息摘要都修改了，对方很难保证修改的内容加密后与修改后的信息摘要一致，因为要保证一致的数据传输协议和数据编解码。

---- 

### 2. 心跳机制

心跳机制的 `RPC` 上应用的很广泛，本项目对心跳机制的实现很简单，而且应对措施是服务端强制断开连接，当然有些 `RPC` 框架实现了服务端去主动尝试重连。
- 原理

对于心跳机制的应用，其实是使用了 `Netty` 框架中的一个 `handler` 处理器，通过该 处理器，去定时发送心跳包，让服务端知道该客户端保持活性状态。

- 实现

利用了 `Netty` 框架中的 `IdleStateEvent` 事件监听器，重写`userEventTriggered()` 方法，在服务端监听读操作，读取客户端的 写操作，在客户端监听写操作，监听本身是否还在活动，即有没有向服务端发送请求。

如果客户端没有主动断开与服务端的连接，而继续保持连接着，那么客户端的写操作超时后，也就是客户端的监听器监听到客户端没有的规定时间内做出写操作事件，那么这时客户端该处理器主动发送心跳包给服务端，保证客户端让服务端确保自己保持着活性。

---- 

### 3. SPI 机制

资源目录`META-INF/services`下新建接口全限定名作为文件名，内容为实现类全限定名，支持`JDK`内置`SPI`。

本质通过反射来无参构造创建实例，如果构造函数涉及到通过参数来实现注入成员，那么可将接口转为抽象类，抽象类暴露set方法来让子类重写，从而间接实现注入。

该机制将注册中心逻辑层处理服务发现和注册的接口时实现分离到配置文件`META-INF/services`，从而更好地去支持其他插件，如`Zookeeper`、`Eureka`的扩展。

应用到的配置文件：
- `cn.fyupeng.discovery.ServiceDiscovery`

```properties
cn.fyupeng.discovery.NacosServiceDiscovery
```
- `cn.fyupeng.provider.ServiceProvider`

```properties
cn.fyupeng.provider.DefaultServiceProvider
```
- `cn.fyupeng.registry.ServiceRegistry`
```properties
cn.fyupeng.registry.NacosServiceRegistry
```

---- 

### 4. IO 异步非阻塞

IO 异步非阻塞 能够让客户端在请求数据时处于阻塞状态，而且能够在请求数据返回时间段里去处理自己感兴趣的事情。

- 原理

使用 java8 出世的 `CompletableFuture` 并发工具类，能够异步处理数据，并在将来需要时获取。


- 实现

数据在服务端与客户端之间的通道 `channel` 中传输，客户端向通道发出请求包，需要等待服务端返回，这时可使用 `CompletableFuture` 作为返回结果，只需让客户端读取到数据后，将结果通过 `complete()`方法将值放进去后，在将来时通过`get()`方法获取结果。

---- 

### 5. RNF 协议

- 定义

```java
/**
     * 自定义对象头 协议 16 字节
     * 4 字节 魔数
     * 4 字节 协议包类型
     * 4 字节 序列化类型
     * 4 字节 数据长度
     *
     *       The transmission protocol is as follows :
     * +---------------+---------------+-----------------+-------------+
     * | Magic Number  | Package Type  | Serializer Type | Data Length |
     * | 4 bytes       | 4 bytes       | 4 bytes         | 4 bytes     |
     * +---------------+---------------+-----------------+-------------+
     * |                           Data Bytes                          |
     * |                       Length: ${Data Length}                  |
     * +---------------+---------------+-----------------+-------------+
     */
```

`RNF`协议为上层应用协议，处于应用层中，`TCP`协议为传输协议，即上层传输有`TCP`拆包成`RNF`包，下层传输为`RNF`包封装成`TCP`包。

- 拆解分析

```postgresql
Frame 30759: 368 bytes on wire (2944 bits), 368 bytes captured (2944 bits) on interface \Device\NPF_Loopback, id 0
Null/Loopback
Internet Protocol Version 4, Src: 192.168.2.185, Dst: 192.168.2.185
Transmission Control Protocol, Src Port: 53479, Dst Port: 8085, Seq: 4861, Ack: 4486, Len: 324
RNF Protocol
    Identifier: 0xcafebabe
    PackType: 726571
    SerializerCode: 2
    Length: 308
    Data [truncated]: C\036cn.fyupeng.protocol.RpcRequest�\trequestId\rinterfaceName\nmethodName\nreturnType\005group\theartBeat\nparameters\nparamTypes`\025230113199152359542784\fhelloService\bsayHelloC\017java.lang.Class�\004namea\036cn
```

`Identifier` (`0xcafebabe`): 表示标识，而0xCAFEBABE为java对象的对象头表示。

`PackType` (`726571`, `726573`): `726571`由字符`res`的`ASCCI`码(`\u0072\u0065\u0073`)转换而来，`req`同理。  

`SerializerCode` (`0,1,2`): 目前提供三种(`json,kryo,hessian2`)序列化方式，分别对应(`0,1,2`)。

`Length` (`308`): 表示`Data`的长度。

`Data [truncated]`(``C\036cn.fyupeng.protocol.RpcRequest...``): 表示使用`${SerializerCode}`序列化方式、长度为`${Length}`的`${PackType}`包。

使用`Wireshare`本地连接抓取，并用`lua`语言编写`RNF`协议解码器，可以更直观了解包的层次结构。

想了解解码器的可以从根目录直接下载，放于`Wireshark`的`lua`插件`plugins`中，重新加载插件就可以了。

----  

## 快速开始

### 1.依赖

#### 1.1 直接引入

首先引入两个jar包文件`rpc-core-1.0.0.jar` 和 `rpc-core-1.0.0-jar-with-dependencies.jar`

`jar`包中包括字节码文件和`java`源码，引入后会自动把`class`和`sources`一并引入，源码可作为参考

![依赖](https://yupeng-tuchuang.oss-cn-shenzhen.aliyuncs.com/依赖.png)

#### 1.2 maven引入

引入以下`maven`，会一并引入`rpc-common`与默认使用的注册中心`nacos-client`相关依赖
 
```xml
<dependency>
    <groupId>cn.fyupeng</groupId>
    <artifactId>rpc-core</artifactId>
    <version>2.2.0</version>
</dependency>
```

`2.1.0`版本之前仅支持配置
```properties
# 单机模式
cn.fyupeng.nacos.register-addr=192.168.10.1:8848
# 集群模式
cn.fyupeng.nacos.cluster.use=false
cn.fyupeng.nacos.cluster.load-balancer=round
cn.fyupeng.nacos.cluster.nodes=192.168.43.33:8847|192.168.43.33.1:8848;192.168.43.33.1:8849
```
`1.0`版本仅支持`@Service`与`@ServiceScan`注解

`2.0.5`版本为单机版本，支持`@Reference`注解，使用本地缓存来解决单节点超时重试，无法处理多节点超时重试。

> **注意**

使用注解`@Reference`获取代理必须将该注解所在类传递给代理，否则该注解将失效
```java
/**
 * Client
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class Client {

    private static RandomLoadBalancer randomLoadBalancer = new RandomLoadBalancer();
    private static NettyClient nettyClient = new NettyClient(randomLoadBalancer, CommonSerializer.HESSIAN_SERIALIZER);
    private static RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyClient);
    /**
     * 传递 Client.class 给代理，代理才能捕获到注解 @Reference
     */
    @Reference(name = "helloService", group = "1.0.0", retries = 2, timeout = 2000, asyncTime = 18000)
    private static HelloWorldService service = rpcClientProxy.getProxy(HelloWorldService.class, Client.class);
}
```

`2.1.0`及之后引入
```properties
# 单机模式
cn.fyupeng.nacos.register-addr=192.168.10.1:8848
# 集群模式
cn.fyupeng.nacos.cluster.use=false
cn.fyupeng.nacos.cluster.load-balancer=round
cn.fyupeng.nacos.cluster.nodes=192.168.43.33:8847|192.168.43.33.1:8848;192.168.43.33.1:8849

# 实现分布式缓存（必要，不做默认开启与否）
cn.fyupeng.redis.server-addr=127.0.0.1:6379
cn.fyupeng.redis.server-auth=true
cn.fyupeng.redis.server-pwd=yupengRedis
cn.fyupeng.redis.server-way=lettuce
cn.fyupeng.redis.client-way=jedis
cn.fyupeng.redis.server-async=true
```
支持注解`@Reference`，用于解决超时重试场景。

推荐使用最新版本`2.1.10`，`2.0`版本引入分布式缓存，解决了分布式场景出现的一些问题。
```xml
<dependency>
  <groupId>cn.fyupeng</groupId
  <artifactId>rpc-core</artifactId>
  <version>2.1.10</version>
</dependency>
```

阿里仓库`10`月份开始处于系统升级，有些版本还没同步过去，推荐另一个`maven`官方仓库：
```xml
<mirror>
  <id>repo1maven</id>
  <mirrorOf>*</mirrorOf>
  <name>maven公共仓库</name>
  <url>https://repo1.maven.org/maven2</url>
</mirror>
```

---- 

### 2. 启动 Nacos

`-m:模式`，`standalone:单机`

命令使用:

```ruby
startup -m standalone
```

> 注意：开源RPC 默认使用 nacos 指定的本地端口号 8848 

官方文档：https://nacos.io/zh-cn/docs/quick-start.html

优势：

选用`Nacos`作为注册中心，是因为有较高的可用性，可实现服务长期可靠
- 注册中心服务列表在本地保存一份
- 宕机节点在自动重启恢复期间，服务依旧可用

Nacos 启动效果：

![效果](https://yupeng-tuchuang.oss-cn-shenzhen.aliyuncs.com/nacos.png)

---- 

### 3. 提供接口
```java
public interface HelloService {
    String sayHello(String message);
}
```

---- 

### 4. 启动服务
- 真实服务
```java
/**
 * HelloServiceImpl
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public String sayHello( String message ) {
        return "hello, here is service！";
    }
}
```
- 服务启动器
```java
/**
 * MyServer
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@ServiceScan
public class MyServer {

    public static void main( String[] args ) {
        try {
            NettyServer nettyServer = new NettyServer("127.0.0.1", 5000, SerializerCode.KRYO.getCode());
            nettyServer.start();
        } catch (RpcException e) {
            e.printStackTrace();
        }
    }
}
```
> 注意：<br/>
> 1. 增加注解`cn.fyupeng.annotation.Service`和`cn.fyupeng.annotation.ServiceScan`才可被自动发现服务扫描并注册到 nacos。<br/>
> 2. 自管理容器默认采用反射创建方式，会导致Spring注解依赖注入和切面失效，解决方案请移步 [SpringBoot整合RPC](/document/springboot整合rpc-netty-framework.md)

---- 

### 5. 启动客户端
初始化客户端时连接服务端有两种方式：
- 直连
- 使用负载均衡
```java
/**
 * MyClient
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class MyClient {

    public static void main( String[] args ) {
        RoundRobinLoadBalancer roundRobinLoadBalancer = new RoundRobinLoadBalancer();
        NettyClient nettyClient = new NettyClient(roundRobinLoadBalancer, CommonSerializer.KRYO_SERIALIZER);

        RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyClient);
        HelloService helloService = rpcClientProxy.getProxy(HelloService.class);
        String result = helloService.sayHello("hello");
        System.out.println(result);
    }
}
```
### 6. 额外配置

#### 6.1 配置文件

- 项目方式启动

在 resources 中加入 resource.properties

主机名使用`localhost`或`127.0.0.1`指定
```properties
cn.fyupeng.nacos.register-addr=127.0.0.1:8848
```

- `Jar`方式启动

，兼容`springboot`的外部启动配置文件注入，需要在`Jar`包同目录下新建`config`文件夹，在`config`中与`springboot`一样注入配置文件，只不过`springboot`注入的配置文件默认约束名为`application.properties`，而`rpc-netty-framework`默认约束名为`resource.properties`。


#### 6.2 日志配置

在 `resources` 中加入 `logback.xml`
```xml
<?xml version="1.0" encoding="UTF-8"?>
<configuration>
  <appender name="console" class="ch.qos.logback.core.ConsoleAppender">
    <encoder>
      <!--%date{HH:mm:ss.SSS} %c -->
      <pattern>%date{HH:mm:ss.SSS} [%level] %c [%t] - %m%n</pattern>
    </encoder>
  </appender>

  <root level="info">
    <appender-ref ref="console"/>
  </root>
</configuration>
```
除此之外，框架还提供了 Socket 方式的 Rpc 服务

---- 

### 7. 场景应用

- 支持 springBoot 集成

为了支持`springBoot`集成`logback`日志，继承`rpc-netty-framework`使用同一套日志，抛弃`nacos-client`内置的`slf4j-api`与`commons-loging`原有`Jar`包，因为该框架会导致在整合`springboot`时，出现重复的日志绑定和日志打印方法的参数兼容问题，使用`jcl-over-slf4j-api`可解决该问题；

在`springboot1.0`和`2.0`版本中，不使用它默认版本的`spring-boot-starter-log4j`,推荐使用`1.3.8.RELEASE`；

在场景测试下，突破万字文章的

springboot简单配置如下
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter</artifactId>
        <exclusions>
            <!-- 排除 springboot 默认的 logback 日志框架 -->
            <exclusion>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-starter-logging</artifactId>
            </exclusion>
            <!-- 排除 springboot 默认的 commons-logging 实现（版本低，出现方法找不到问题） -->
            <exclusion>
                <groupId>org.springframework</groupId>
                <artifactId>spring-jcl</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    
    <!-- 与 logback 整合（通过 @Slf4j 注解即可使用） -->
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <version>1.18.10</version>
    </dependency>
    <!--引入log4j日志依赖，目的是使用 jcl-over-slf4j 来重写 commons logging 的实现-->
    <dependency>
      <groupId>org.springframework.boot</groupId>
      <artifactId>spring-boot-starter-log4j</artifactId>
      <version>1.3.8.RELEASE</version>
      <exclusions>
        <exclusion>
          <artifactId>slf4j-log4j12</artifactId>
          <groupId>org.slf4j</groupId>
        </exclusion>
      </exclusions>
    </dependency>
</dependencies>

```

---- 

### 8. 高可用集群

```properties
cn.fyupeng.nacos.cluster.use=true
cn.fyupeng.nacos.cluster.load-balancer=random
cn.fyupeng.nacos.cluster.nodes=192.168.10.1:8847,192.168.10.1:8848,192.168.10.1:8849
```
- 使用方法及注意要点

默认情况下，`cn.fyupeng.nacos.cluster.use`服从约定优于配置设定，且默认值为`false`，表示默认不打开集群；

`cn.fyupeng.nacos.cluster.load-balancer`与`cn.fyupeng.nacos.cluster.nodes`使用前必须先打开集群模式

- 集群节点负载策略
  - `random`：随机策略
  - `round`：轮询策略

集群节点理论上允许无限扩展，可使用分隔符`[;,|]`扩展配置

- 集群节点容错切换
  - 节点宕机：遇到节点宕机将重新从节点配置列表中选举新的正常节点，否则无限重试

---- 

### 9. 超时重试机制

默认不使用重试机制，为了保证服务的正确性，因为无法保证幂等性。

原因是客户端无法探测是客户端网络传输过程出现问题，或者是服务端正确接收后返回途中网络传输出现问题，因为如果是前者那么重试后能保证幂等性，如果为后者，可能将导致多次同个业务的执行，这对客户端来说结果是非一致的。

超时重试处理会导致出现幂等性问题，因此在服务器中利用`HashSet`添加请求`id`来做超时处理

- 超时重试：`cn.fyupeng.annotation.Reference`注解提供重试次数、超时时间和异步时间三个配置参数，其中：
  - 重试次数：服务端未能在超时时间内 响应，允许触发超时的次数
  - 超时时间：即客户端最长允许等待 服务端时长，超时即触发重试机制
  - 异步时间：即等待服务端异步响应的时间，且只能在超时重试机制使用，非超时重试情况下默认使用阻塞等待方式

> 示例：
```java
/**
 * README.CN.md
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
class private static RandomLoadBalancer randomLoadBalancer = new RandomLoadBalancer();
private static NettyClient nettyClient = new NettyClient(randomLoadBalancer, CommonSerializer.KRYO_SERIALIZER);
private static RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyClient);

@Reference(retries = 2, timeout = 3000, asyncTime = 5000)
private static HelloWorldService service = rpcClientProxy.getProxy(HelloWorldService.class, Client.class){
}
```
重试的实现也不难，采用`代理 + for + 参数`来实现即可。
> 核心代码实现：
```java
for( /**
 * README.CN.md
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
class int i = 0;i <=retries;i++){
    long startTime = System.currentTimeMillis();

    CompletableFuture<RpcResponse> completableFuture = (CompletableFuture<RpcResponse>) rpcClient.sendRequest(
            rpcRequest);
    try {
        rpcResponse = completableFuture.get(asyncTime, TimeUnit.MILLISECONDS);
    } catch (TimeoutException e) {
        // 忽视 超时引发的异常，自行处理，防止程序中断
        timeoutRes.incrementAndGet();
        if (timeout >= asyncTime) {
            log.warn("asyncTime [ {} ] should be greater than timeout [ {} ]", asyncTime, timeout);
        }
        continue;
    }

    long endTime = System.currentTimeMillis();
    long handleTime = endTime - startTime;
    if (handleTime >= timeout) {
        // 超时重试
        log.warn("invoke service timeout and retry to invoke");
    } else {
        // 没有超时不用再重试
        // 进一步校验包
        if (RpcMessageChecker.check(rpcRequest, rpcResponse)) {
            res.incrementAndGet();
            return rpcResponse.getData();
        }
    }
}{
}
```

- 幂等性

重试机制服务端要保证重试包的一次执行原则，即要实现幂等性。

实现思路：借助分布式缓存对首次请求包进行异步缓存请求`id`，分布式缓存选型`Redis`，客户端选型`Jedis与Lettuce`，给定一个`key`失效时间满足重试机制，减少再次清理缓存的步骤。

幂等性作用在失效时间，也影响到超时机制重试次数以及高并发场景，有雪花算法的优势，不同时间上不会生成重复id，于是可以大胆将失效时间设置大些，这取决于你能够承担多少内存而转而去考虑内存瓶颈的问题。

---- 

### 10. 雪花算法
```java
/**
     * 自定义 分布式唯一号 id
     *  1 位 符号位
     * 41 位 时间戳
     * 10 位 工作机器 id
     * 12 位 并发序列号
     *
     *       The distribute unique id is as follows :
     * +---------------++---------------+---------------+-----------------+
     * |     Sign      |     epoch     |    workerId   |     sequence     |
     * |    1 bits     |    41 bits    |    10 bits   |      12 bits      |
     * +---------------++---------------+---------------+-----------------+
     */
```
- 介绍

雪花算法：主要由时间戳、机器码、序列码这三者组成，各个部分有代表的意义。

> 标志位

表示符号位，固定值为`0`，表示正数。

> 时间戳

时间戳代表运行时长，它由`41`比特组成，最高可使用69年，由当前系统时间戳与服务启动当天凌晨时间戳的差值表示。

> 机器码

机器码代表分布式节点，它由`10`比特组成，最高可表示`1024`个机器码，默认采用当前服务主机号`hashCode`、高低位异或和分布式缓存算法生成，机器码生成异常时由`SecureRandom`使用随机种子、`AtomicLong`与`CAS`锁生成，当机器码数量最大时将抛出异常`WorkerIdCantApplyException`。

> 序列号

序列号代码并发量，在同一毫秒内发生作用，即毫秒并发时作为一部分唯一值，它由`12`比特组成，最高可表示`4096`个序列号。

- 应用场景

（1）多服务节点负载实现业务持久化主键唯一

（2）毫秒内请求并发数最高可达到`4095`，可适当改变序列号满足不同场景 

（3）满足基本有序的唯一号`id`，有利于高效索引查询和维护

（4）`id`号前`6`位附加当前时间年月日日志查询，可作为日志记录

而在`RPC`中主要采用雪花算法实现了请求包的唯一识别号，因为`UUID`生成唯一性和时间持续性比雪花算法更好，但它id值是非递增序列，在索引建立和维护时代价更高。

雪花算法生成的`id`基本有序递增，可作为索引值，而且维护成本低，代价是强依赖机器时钟，为了尽可能发挥它的优势和减少不足，对最近的时间内保存了时间戳与序列号，回拨即获取当时序列号，有则自增，无则阻塞恢复到时钟回拨前的时间戳，回拨时间过大抛异常中断，而且服务器重启时小概率可能出现回拨会从而导致`id`值重复的问题。
```properties
cn.fyupeng.redis.server-addr=127.0.0.1:6379
cn.fyupeng.redis.server-auth=true
cn.fyupeng.redis.server-pwd=123456
```
除此以外，超时重试机制，在分布式场景下，第二次重试会通过负载均衡策略负载到其他服务节点，利用雪花算法弥补了分布式场景下的无法解决幂等性的问题。

超时重试采用`Jedis/Lettuce`两种实现缓存的方式，可分别在服务端、客户端配置相应的缓存连接客户端方式：
```properties
cn.fyupeng.redis.server-way=lettuce
cn.fyupeng.redis.client-way=jedis
cn.fyupeng.redis.server-async=true
```
如何选择`JRedisHelper`与`LRedisHelper`呢？

JRedisHelper
- 线程安全
- `synchronized`与`lock`的悲观锁机制
- 不提供线程池
- 连接数为`1`
- 同步操作
- 提供依据主机号缓存和获取机器`id`
- 提供依据请求`id`缓存和获取请求结果

LRedisHelper
- 线程安全
- 提供线程池
- 连接数稳定且由线程池提供
- 异步/同步操作
- 提供依据主机号缓存和获取机器`id`
- 提供依据请求`id`缓存和获取请求结果

>特别提醒

高并发请求不会出现请求号重复的情况，当前最高毫秒级并发`4096`，而超时机制、`LRedisHelper`线程池对连接的超时控制等配置参数还不成熟，具体应用场景可自行下载源码修改参数。

---- 

### 11. 高并发

在`Netty`高性能框架的支持下，有单`Reactor`单线程、单`Reactor`多线程和主从`Reactor`多线程，采用性能最好的主从`Reactor`多线程，优势在于多服务结点（多`channel`情况下）并发处理时，从`workGroup`中每个线程可以处理一个`channel`，实现并行处理，不过在单个`channel`承载高并发下，无法多个线程同时处理事件，因为一个`channel`只能绑定一个线程。

如果多个线程同时处理一个`channel`，将会出现类似`Redis`多线程情况下，用`Jedis`操作出现的安全问题，这里因为多个线程应对一个`channe`l将会使情况变得异常复杂，这里跟`Redis`单线程一样，异曲同工，速度之快在于单线程不用考虑多线程之间的协调性，只要再次分发`channel`到线程池中执行，那个获取到`channel`的线程就可以去读取消息，自行写入返回消息给服务端即可。

这样负责读取`channel`绑定的单线程只需要提交任务到线程池，不需阻塞，即可处理高并发请求。

从代码细节上来讲，读取到`channel`事件，意味着缓存`buffer`已经被读走了，不会影响其他线程继续读取`channel`，当然这里会出现短暂的阻塞，因为读取也需要一定时间，所以不会出现多个任务提交执行出现交叉行为。

这里读取之快又涉及到零拷贝，数据在用户态是不用拷贝的，直接透明使用。

```java
/**
 * NettyServerHandler
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
@Slf4j
public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequest> {

    private static RequestHandler requestHandler;

    /**
     * Lettuce 分布式缓存采用 HESSIAN 序列化方式
     */
    private static CommonSerializer serializer = CommonSerializer.getByCode(CommonSerializer.HESSIAN_SERIALIZER);

    /**
     * netty 服务端采用 线程池处理耗时任务
     */
    private static final EventExecutorGroup group = new DefaultEventExecutorGroup(16);

    @Override
    protected void channelRead0( ChannelHandlerContext ctx, RpcRequest msg ) throws Exception {
        /**
         * 心跳包 只 作为 检测包，不做处理
         */
        if (msg.getHeartBeat()) {
            log.debug("receive hearBeatPackage from customer...");
            return;
        }
        group.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    log.info("server has received request package: {}", msg);

                    // 到了这一步，如果请求包在上一次已经被 服务器成功执行，接下来要做幂等性处理，也就是客户端设置超时重试处理

                    /**
                     * 改良
                     * 使用 Redis 实现分布式缓存
                     *
                     */
                    Object result = null;

                    if ("jedis".equals(redisServerWay) || "default".equals(redisServerWay) || StringUtils.isBlank(
                            redisServerWay)) {
                        if (!JRedisHelper.existsRetryResult(msg.getRequestId())) {
                            log.info("requestId[{}] does not exist, store the result in the distributed cache",
                                    msg.getRequestId());
                            result = requestHandler.handler(msg);
                            if (result != null)
                                JRedisHelper.setRetryRequestResult(msg.getRequestId(), JsonUtils.objectToJson(result));
                            else {
                                JRedisHelper.setRetryRequestResult(msg.getRequestId(), null);
                            }
                        } else {
                            result = JRedisHelper.getForRetryRequestId(msg.getRequestId());
                            if (result != null) {
                                result = JsonUtils.jsonToPojo((String) result, msg.getReturnType());
                            }
                            log.info("Previous results:{} ", result);
                            log.info(" >>> Capture the timeout packet and call the previous result successfully <<< ");
                        }
                    } else {

                        if (LRedisHelper.existsRetryResult(msg.getRequestId()) == 0L) {
                            log.info("requestId[{}] does not exist, store the result in the distributed cache",
                                    msg.getRequestId());
                            result = requestHandler.handler(msg);

                            if ("true".equals(redisServerAsync) && result != null) {
                                LRedisHelper.asyncSetRetryRequestResult(msg.getRequestId(),
                                        serializer.serialize(result));
                            } else {
                                if (result != null)
                                    LRedisHelper.syncSetRetryRequestResult(msg.getRequestId(),
                                            serializer.serialize(result));
                                else {
                                    LRedisHelper.syncSetRetryRequestResult(msg.getRequestId(), null);
                                }
                            }
                        } else {
                            result = LRedisHelper.getForRetryRequestId(msg.getRequestId());
                            if (result != null) {
                                result = serializer.deserialize((byte[]) result, msg.getReturnType());
                            }
                            log.info("Previous results:{} ", result);
                            log.info(" >>> Capture the timeout packet and call the previous result successfully <<< ");
                        }
                    }

                    // 生成 校验码，客户端收到后 会 对 数据包 进行校验
                    if (ctx.channel().isActive() && ctx.channel().isWritable()) {
                        /**
                         * 这里要分两种情况：
                         * 1. 当数据无返回值时，保证 checkCode 与 result 可以检验，客户端 也要判断 result 为 null 时 checkCode 是否也为 null，才能认为非他人修改
                         * 2. 当数据有返回值时，校验 checkCode 与 result 的 md5 码 是否相同
                         */
                        String checkCode = "";
                        // 这里做了 当 data为 null checkCode 为 null，checkCode可作为 客户端的判断 返回值 依据
                        if (result != null) {
                            try {
                                checkCode = new String(DigestUtils.md5(result.toString().getBytes("UTF-8")));
                            } catch (UnsupportedEncodingException e) {
                                log.error("binary stream conversion failure: ", e);
                                //e.printStackTrace();
                            }
                        } else {
                            checkCode = null;
                        }
                        RpcResponse rpcResponse = RpcResponse.success(result, msg.getRequestId(), checkCode);
                        log.info(String.format(
                                "server send back response package {requestId: %s, message: %s, statusCode: %s ]}",
                                rpcResponse.getRequestId(), rpcResponse.getMessage(), rpcResponse.getStatusCode()));
                        ChannelFuture future = ctx.writeAndFlush(rpcResponse);


                    } else {
                        log.info("channel status [active: {}, writable: {}]", ctx.channel().isActive(),
                                ctx.channel().isWritable());
                        log.error("channel is not writable");
                    }
                    /**
                     * 1. 通道关闭后，对于 心跳包 将不可用
                     * 2. 由于客户端 使用了 ChannelProvider 来 缓存 channel，这里关闭后，无法 发挥 channel 缓存的作用
                     */
                    //future.addListener(ChannelFutureListener.CLOSE);
                } finally {
                    ReferenceCountUtil.release(msg);
                }
            }
        });
    }
}
```

当然高并发下还要考虑一个问题，任务处理太慢时，不能让客户端一直阻塞等待，可以设置超时，避免因为服务端某一个任务影响到其他请求的执行，要让出给其他有需要的线程使用，于是引入的超时机制配合分布式缓存，在超时机制下，要么直接将第一次请求后服务端缓存的结果直接返回，要么直接失败，来保证它的一个高并发稳定性。

---- 

### 12. 异常解决
- ServiceNotFoundException

抛出异常`ServiceNotFoundException`

堆栈信息：`service instances size is zero, can't provide service! please start server first!`

正常情况下，一般的错误从报错中可以引导解决。

解决真实服务不存在的情况，导致负载均衡中使用的策略出现异常的情况，修复后会强制抛出`ServiceNotFoundException`，或许大部分情况是服务未启动。

当然，推荐真实服务应该在服务启动器的内层包中，同层可能会不起作用。

除非使用注解注明包名`@ServiceScan("com.fyupeng")`

其他情况下，如出现服务端无反应，而且服务已经成功注册到注册中心，那么你就得检查下服务端与客户端中接口命名的包名是否一致，如不一致，也是无法被自动发现服务从注册中心发现的，这样最常见的报错也是`service instances size is zero`。

- ReceiveResponseException

抛出异常`data in package is modified Exception`

信息摘要算法的实现，使用的是`String`类型的`equals`方法，所以客户端在编写`Service`接口时，如果返回类型不是八大基本类型 + String 类型，也就是复杂对象类型，那么要重写`toString`方法。

不使用`Object`默认的`toString`方法，因为它默认打印信息为`16`位的内存地址，在做校验中，发送的包和请求获取的包是需要重新实例化的，说白了就是深克隆，**必须** 重写`Object`原有`toString`方法。

为了避免该情况发生，建议所有`PoJo`、`VO`类必须重写`toString`方法，其实就是所有真实业务方法返回类型的实体，必须重写`toString`方法。

如返回体有嵌套复杂对象，所有复杂对象均要重写`toString`只要满足不同对象但内容相同的`toString`方法打印信息一致，数据完整性检测才不会误报。

- RegisterFailedException

抛出异常`Failed to register service Exception`

原因是注册中心没有启动或者注册中心地址端口指定不明，或者因为防火墙问题，导致`Nacos`所在服务器的端口访问失败。

使用该框架时，需注意以下两点：

(1) 支持注册本地地址，如 localhost或127.0.0.1，则注册地址会解析成公网地址；

(2) 支持注册内网地址和外网地址，则地址为对应内网地址或外网地址，不会将其解析；

- NotSuchMethodException
抛出异常`java.lang.NoSuchMethodError:  org.slf4j.spi.LocationAwareLogger.log`

出现该异常的原因依赖包依赖了`jcl-over-slf4j`的`jar`包，与`springboot-starter-log4j`中提供的`jcl-over-slf4j`重复了，建议手动删除`rpc-core-1.0.0-jar-with-dependenceies.jar`中`org.apache.commons`包


- DecoderException

抛出异常：`com.esotericsoftware.kryo.KryoException: Class cannot be created (missing no-arg constructor): java.lang.StackTraceElement`

主要是因为`Kryo`序列化和反序列化是通过无参构造反射创建的，所以使用到`Pojo`类，首先必须对其创建无参构造函数，否则将抛出该异常，并且无法正常执行。

- InvocationTargetException

抛出异常：`Serialization trace:stackTrace (java.lang.reflect.InvocationTargetException)`

主要也是反射调用失败，主要原因还是反射执行目标函数失败，缺少相关函数，可能是构造函数或者其他方法参数问题。

- AnnotationMissingException

抛出异常：`cn.fyupeng.exception.AnnotationMissingException`

由打印信息中可知，追踪`AbstractRpcServer`类信息打印
```ruby
cn.fyupeng.net.AbstractRpcServer [main] - mainClassName: jdk.internal.reflect.DirectMethodHandleAccessor
```
如果`mainClassName`不为`@ServiceScan`注解标记所在类名，则需要到包`cn.fyupeng.util.ReflectUtil`下修改或重写`getStackTrace`方法，将没有过滤的包名加进过滤列表即可，这可能与`JDK`的版本有关。

- OutOfMemoryError

抛出异常`java.lang.OutOfMemoryError: Requested array size exceeds VM limit`

基本不可能会抛出该错误，由于考虑到并发请求，可能导致，如果请求包分包，会出现很多问题，所以每次请求只发送一个请求包，如在应用场景需要发送大数据，比如发表文章等等，需要手动去重写使用的序列化类的`serialize`方法

例如：KryoSerializer可以重写`serialize`方法中写缓存的大小，默认为`4096`，超出该大小会很容易报数组越界异常问题。
```java
/**
 * README.CN.md
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
class /**
         * bufferSize: 缓存大小
         */
Output output =new Output( byteArrayOutputStream,100000 )){
}
```

- RetryTimeoutExcepton

抛出异常`cn.fyupeng.exception.AnnotationMissingException`

在启用重试机制后，客户端超过重试次数仍未能成功调用服务，即可认为服务不可用，并抛出超时重试异常。

抛出该异常后，将中断该线程，其线程还未执行的任务将终止，默认不会开启重试机制，则不会抛出该异常。

- InvalidSystemClockException

抛出异常`cn.fyupeng.idworker.exception.InvalidSystemClockException`

雪花算法生成中是有很小概率出现时钟回拨，时间回拨需要解决`id`值重复的问题，故而有可能抛出`InvalidSystemClockException`中断异常，逻辑不可处理异常。

- WorkerIdCantApplyException

抛出异常`cn.fyupeng.idworker.exception.WorkerIdCantApplyException`

雪花算法生成中，借助`IdWorker`生成器生成分布式唯一`id`时，是借助了机器码，当机器码数量生成达到最大值将不可再申请，这时将抛出中断异常`WorkerIdCantApplyException`。

- NoSuchMethodError

抛出异常`io.netty.resolver.dns.DnsNameResolverBuilder.socketChannelType(Ljava/lang/Class;)Lio/netty/resolver/dns/DnsNameResolverBuilder`

整合`SpringBoot`时会覆盖`netty`依赖和`lettuce`依赖，`SpringBoot2.1.2`之前，内含`netty`版本较低，而且`RPC`框架支持兼容`netty-all:4.1.52.Final`及以上，推荐使用`SpringBoot2.3.4.RELEASE`即以上可解决该问题。

- AsyncTimeUnreasonableException

抛出异常`cn.fyupeng.exception.AsyncTimeUnreasonableException`

异步时间设置不合理异常，使用注解@Reference时，字段`asyncTime`必须大于`timeout`，这样才能保证超时时间内不会报异常`java.util.concurrent.TimeoutException`，否则最大超时时间将可能不可达并且会打印`warn`日志，导致触发下一次重试，该做法在`2.0.6`和`2.1.8`中将强制抛出异常终止线程。

与此相同用法的有`RetryTimeoutExcepton`和`RpcTransmissionException`，都会终结任务执行。

- RpcTransmissionException

抛出异常`cn.fyupeng.exception.RpcTransmissionException`

数据传输异常，在协议层解码中抛出的异常，一般是因为解析前的实现类与解析后接收实习类`toString()`方法协议不同导致的，也可能是包被劫持并且发生内容篡改。

内部设计采用`toSring()`方法来，而不进行某一种固定的方式来校验，这让校验有更大的不确定性，以此获得更高的传输安全，当然这种设计可以让开发人员自行设计具有安全性的`toString`方法实现，如不实现，将继承`Object`的内存地址toString打印，由于是通过网络序列化传输的，也就是深克隆方式创建类，服务端的原校验码和待校验一般不同，就会抛该异常，一般都需要重新`toString()`方法。

---- 

### 13. 健壮性（善后工作）

服务端的延时关闭善后工作，能够保证连接的正常关闭。

- TCP 关闭（四次挥手）

```shell
8191	80.172711	192.168.2.185	192.168.2.185	TCP	44	8085 → 52700 [FIN, ACK] Seq=3290 Ack=3566 Win=2616320 Len=0
8190	80.172110	192.168.2.185	192.168.2.185	TCP	44	8085 → 52700 [ACK] Seq=3290 Ack=3566 Win=2616320 Len=0
8191	80.172711	192.168.2.185	192.168.2.185	TCP	44	8085 → 52700 [FIN, ACK] Seq=3290 Ack=3566 Win=2616320 Len=0
8192	80.172751	192.168.2.185	192.168.2.185	TCP	44	52700 → 8085 [ACK] Seq=3566 Ack=3291 Win=2616320 Len=0
```

而且不再出现发送`RST`问题，即接收缓冲区中还有数据未接收，出现的原因为`Netty`自身善后工作出现了问题，即在`future.channel().closeFuture().sync()`该操作执行后，线程终止不会往下执行，即时有`finally`依旧如此，于是使用关闭钩子来自动调用完成连接的正常关闭。

- 关闭钩子

```java
/**
 * ShutdownHook
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class ShutdownHook {

    private static final ShutdownHook shutdownHook = new ClientShutdownHook();

    public static ShutdownHook getShutdownHook() {
        return shutdownHook;
    }

    /**
     * 添加关闭钩子
     * 客户端钩子 跟 服务端钩子分开启动添加，不要放一起
     */
    public void addClearAllHook() {
        log.info("All services will be cancel after shutdown");
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            // 服务端，由服务端钩子执行
            JRedisHelper.remWorkerId(IpUtils.getPubIpAddr());
            log.info("the cache for workId has bean cleared successfully");
            NacosUtils.clearRegistry();
            NettyServer.shutdownAll();
            ThreadPoolFactory.shutdownAll();

            // 客户端，由客户端钩子执行
            ChannelProvider.shutdownAll();
            ThreadPoolFactory.shutdownAll();
            // 其他 善后工作
        }));
    }
}
```

- 使用方法

在服务端或者客户端代理启动时调用

```java
/**
 * NettyServer
 *
 * @author shuigedeng
 * @version 2026.01
 * @since 2025-12-19 09:30:45
 */
public class NettyServer extends AbstractRpcServer {

    @Override
    public void start() {
        /**
         *  封装了 之前 使用的 线程吃 和 任务队列
         *  实现了 ExecutorService 接口
         */
        ShutdownHook.getShutdownHook().addClearAllHook();
    }
}
```

Netty已经提供了优雅关闭，即`bossGroup.shutdownGracefully().sync()`，可将其用静态方法封装起来，交由钩子调用即可。

---- 

### 14. 版本追踪

#### 1.0版本

- [ [#1.0.1](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/1.0.1/pom) ]：解决真实分布式场景下出现的注册服务找不到的逻辑问题；

- [ [#1.0.2](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/1.0.2/pom) ]：解耦注册中心的地址绑定，可到启动器所在工程项目的资源下配置`resource.properties`文件；

- [ [#1.0.4.RELEASE](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/1.0.4.RELEASE/pom) ]：修复`Jar`方式部署项目后注册到注册中心的服务未能被发现的问题，解耦`Jar`包启动配置文件的注入，约束名相同会覆盖项目原有配置信息；

- [ [#1.0.5](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/1.0.5/pom) ]：将心跳机制打印配置默认级别为`trace`，默认日志级别为`info`，需要开启到`logback.xml`启用。

- [ [#1.0.6](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/1.0.6/pom) ]：默认请求包大小为`4096`字节，扩容为`100000`字节，满足日常的`100000`字的数据包，不推荐发送大数据包，如有需求看异常`OutOfMemoryError`说明。

- [ [#1.0.10](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/1.0.10/pom) ]: 修复负载均衡出现`select`失败问题，提供配置中心高可用集群节点注入配置、负载均衡配置、容错自动切换

#### 2.0版本

- [ [#2.0.0](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.0.0/pom) ]：优化`1.0`版本，`2.0`版本问世超时重试机制，使用到幂等性来解决业务损失问题，提高业务可靠性。

- [ [#2.0.1](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.0.1/pom) ]：版本维护

- [ [#2.0.2](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.0.2/pom) ]：修复集群配置中心宕机重试和负载问题

- [ [#2.0.3](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.0.3/pom) ]：提供个性化服务版本号，支持各种场景，如测试和正式场景，让服务具有更好的兼容性，支持版本维护和升级。

- [ [#2.0.4](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.0.4/pom) ]：支持`SPI`机制，接口与实现解耦。

- [ [#2.0.5](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.0.5/pom) ]：`2.0`将长期维护，`2.1`版本中继承`2.0`待解决的问题得到同步解决。

- [ [#2.0.6](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.0.6/pom) ]：整体整改和性能优化。

- [ [#2.0.8](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.0.8/pom) ]: 代码逻辑优化以及预加载优化。

- [ [#2.0.9](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.0.9/pom) ]：修复客户端/服务端未能正常关闭问题，导致对端连接异常终止。

#### 2.1版本

- [ [#2.1.0](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.1.0/pom) ]：引入雪花算法与分布式缓存，`2.0.0`版本仅支持单机幂等性，修复分布式场景失效问题，采用`轮询负载+超时机制`，能高效解决服务超时问题。

- [ [#2.1.1](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.1.1/pom) ]：更改配置信息`cn.fyupeng.client-async`为`cn.fyupeng.server-async`。

- [ [#2.1.3](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.1.3/pom) ]：修复公网获取`403`异常。

- [ [#2.1.5](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.1.5/pom) ]：修复注册中心`group`默认缺省报错异常。

- [ [#2.1.7](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.1.7/pom) ]：修复保存文章正常，读取文章超出边界异常问题、解决防火墙下`netty`无法监听阿里云、腾讯云本地公网地址问题、修复查询为空/无返回值序列化逻辑异常问题、修复分布式缓存特情况下出现的序列化异常现象。

- [ [#2.1.8](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.1.8/pom) ]：整体整改和性能优化。

- [ [#2.1.9](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.1.9/pom) ]：代码逻辑优化以及预加载优化。

- [ [#2.1.10](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.1.10/pom) ]：修复客户端/服务端未能正常关闭问题，导致对端连接异常终止、整合统一的业务线程池，以便后期清理工作。

#### 2.1版本

- [ [#2.2.0](https://search.maven.org/artifact/cn.fyupeng/rpc-netty-framework/2.2.0/pom) ]：支持 扩展 `Bean` 自定义实例化，满足 `Spring` 注解依赖注入和注解切面执行需求、修正注解包名为`annotation`、解决服务注销时未能手动清除服务实例内存、性能小幅度提升、维护并发下轮询策略的不稳定性。


---- 

### 15. 开发说明

RNF 开源 RPC 框架已遵循 Apache License Version 2.0 协议，使用前请遵循以上协议，如有版权纠纷或使用不便请与作者联系！


