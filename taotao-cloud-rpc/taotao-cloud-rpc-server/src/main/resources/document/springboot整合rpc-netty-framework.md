一个分布式微服务RPC框架 | [返回](/document/README.CN.md)

## 使用效果：

1. 用户访问客户端：GET http://127.0.0.1:8081/user/hello?name="张三来访"

![image-20221020170500139](https://yupeng-tuchuang.oss-cn-shenzhen.aliyuncs.com/image-20221020170500139.png)

1. 浏览器访问客户端：

![image-20221020170622580](https://yupeng-tuchuang.oss-cn-shenzhen.aliyuncs.com/image-20221020170622580.png)

服务端接收情况：

![image-20221020170428236](https://yupeng-tuchuang.oss-cn-shenzhen.aliyuncs.com/image-20221020170428236.png)

服务端负载注册服务：

![image-20221020170833644](https://yupeng-tuchuang.oss-cn-shenzhen.aliyuncs.com/image-20221020170833644.png)

上面的实现就好比客户端只拿到服务端的api接口，加上配置中心地址即可调用远程服务！

## 1. 创建工程

创建两个工程，一个作为服务端`SpringBoot`、一个作为客户端`SpringBoot`，同时作为后端接口服务

创建Maven工程的时候推荐使用父子工程依赖，而且要注意子模块之间的相互依赖关系，其中：

父模块（`root`项目）：负责管理`SpringBoot`版本、统一版本、`JDK`版本、日志依赖

```xml
<profiles>
    <profile>
        <id>jdk1.8</id>
        <activation>
            <activeByDefault>true</activeByDefault>
            <jdk>1.8</jdk>
        </activation>
        <properties>
            <maven-compiler-source>1.8</maven-compiler-source>
            <maven-compiler-target>1.8</maven-compiler-target>
            <maven-copiler-compilerVersion>1.8</maven-copiler-compilerVersion>
        </properties>
    </profile>
</profiles>

<parent>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-parent</artifactId>
<version>2.1.2.RELEASE</version>
<relativePath/>
</parent>
<dependencies>
<!-- 与 logbakc 整合 -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <version>1.18.10</version>
</dependency>
<!-- 日志框架 -->
<dependency>
    <groupId>ch.qos.logback</groupId>
    <artifactId>logback-classic</artifactId>
    <version>1.2.3</version>
</dependency>
</dependencies>
```

主模块（启动类所在模块）应配置`maven`打包插件，`SpringBootStarterWeb`，`rpc-core`和依赖`Service`/`Controller`模块，客户端只有`Service`模块，服务端只有`Controller`模块

```xml
<dependencies>

    <dependency>
        <groupId>cn.fyupeng</groupId>
        <artifactId>springboot-rpc-service</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>

    <dependency>
        <groupId>cn.fyupeng</groupId>
        <artifactId>rpc-core</artifactId>
        <version>2.0.0</version>
    </dependency>

    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-webmvc</artifactId>
    </dependency>
</dependencies>

<build>
<plugins>
    <plugin>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-maven-plugin</artifactId>
    </plugin>
</plugins>
</build>
```

`Service`/`Controller`模块：依赖`api`模块和`common`模块，客户端请求服务端调用服务，当然没有`Service`模块，让主模块依赖`Controller`模块，`Controller`模块还要与主模块一样依赖`SpringBootStarterWeb`和`rpc-core`

```xml
<dependencies>
    <dependency>
        <groupId>cn.fyupeng</groupId>
        <artifactId>springboot-rpc-common</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
    <dependency>
        <groupId>cn.fyupeng</groupId>
        <artifactId>springboot-rpc-api</artifactId>
        <version>1.0-SNAPSHOT</version>
    </dependency>
</dependencies>
```

`Common`模块，依赖`rpc-common`模块

```xml
<dependencies>
    <dependency>
        <groupId>cn.fyupeng</groupId>
        <artifactId>rpc-common</artifactId>
        <version>2.0.0</version>
    </dependency>
</dependencies>
```

项目在准备测试服务端的自动发现服务功能时，要保证`cn.fyupeng.@Service`注解类能够被扫描，使用`cn.fyupeng.util.ReflectUtil`类即可。

```java
/**
 * Test
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
public class Test {

    public static void main( String[] args ) throws IOException {
        Set<Class<?>> classes = ReflectUtil.getClasses("cn.fyupeng");
        for (Class<?> aClass : classes) {
            System.out.println(aClass);
        }
    }
}
```

## 2. 客户端

### 2.1 编写启动器

新建`cn.fyupeng`包，包下新建启动器类

```java
/**
 * SpringBootClientStarter
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@SpringBootApplication
@ComponentScan(basePackages = {"cn.fyupeng", "org.utils"})
public class SpringBootClientStarter {

    public static void main( String[] args ) {
        SpringApplication.run(SpringBootClientStarter.class, args);
    }
}
```

### 2.2 编写配置文件

```properties
# 单机模式
cn.fyupeng.nacos.register-addr=192.168.10.1:8848
# 集群模式
cn.fyupeng.nacos.cluster.use=true
cn.fyupeng.nacos.cluster.load-balancer=round
cn.fyupeng.nacos.cluster.nodes=192.168.10.1:8847|192.168.10.1:8848;192.168.10.1:8849
```

### 2.3 编写自定义配置类

### 2.4 编写api

注意与客户端包名完全相同

```java
package cn.fyupeng.service;

/**
 * @Auther: fyp
 * @since: 2022/10/19
 * @Description: HelloWorld接口
 * @Package: cn.fyupeng.cn.fyupeng.controller
 * @Version: 1.0
 */
public interface HelloService {
    String sayHello(String name);
}
```

### 2.5 编写控制器

`@PostConstruct`注解不要与`@Autowire`公用，因为`@Autowire`是本地依赖的，而我`@PostConstruct`会在该变量使用前调用，不过需要自行去实现，我的实现是远程依赖。

而`@Reference`没有依赖注入的功能，只有在超时重试才需要标记上！

`@PostContruct`与`@Autowire`在`51cto`博客中有所讲解，请自行到我的`github`主页`get`

```java
package cn.fyupeng.controller;

import cn.fyupeng.annotation.Reference;
import cn.fyupeng.loadbalancer.RandomLoadBalancer;
import cn.fyupeng.net.netty.client.NettyClient;
import cn.fyupeng.proxy.RpcClientProxy;
import cn.fyupeng.serializer.CommonSerializer;
import cn.fyupeng.service.HelloService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.utils.JSONResult;

import javax.annotation.PostConstruct;


/**
 * @Auther: fyp
 * @since: 2022/10/19
 * @Description: HelloWorld控制器
 * @Package: cn.fyupeng.cn.fyupeng.controller
 * @Version: 1.0
 */

@RequestMapping("/user")
@RestController
public class HelloController {

    //@Autowired
    //private HelloService helloService;

    private static final RandomLoadBalancer randomLoadBalancer = new RandomLoadBalancer();
    private static final NettyClient nettyClient = new NettyClient(randomLoadBalancer, CommonSerializer.KRYO_SERIALIZER);
    private static final RpcClientProxy rpcClientProxy = new RpcClientProxy(nettyClient);

    @Reference(retries = 5, timeout = 600, asyncTime = 3000)
    //@Autowired
    private HelloService helloService;

    @PostConstruct
    public void init() {
        helloService = rpcClientProxy.getProxy(HelloService.class, HelloController.class);
    }

    @GetMapping("/hello")
    public JSONResult sayHello(String name) {
        return JSONResult.ok(helloService.sayHello(name));
    }

}

```

## 3. 服务端

### 3.1 编写配置类

新建`cn.fyupeng.config`包，在包下新建资源配置类，用于注入绑定端口

- ResourceConfig 配置类
```java
/**
 * ResourceConfig
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Configuration
@ConfigurationProperties(prefix = "cn.fyupeng.config")
//不使用默认配置文件application.properties和application.yml
@PropertySource("classpath:resource.properties")
public class ResourceConfig {

    private int serverPort;

    public int getServerPort() {
        return serverPort;
    }

    public void setServerPort( int serverPort ) {
        this.serverPort = serverPort;
    }
}
```

## 3.2 编写工具类

- SpringContextUtil 工具类

实现 RPC 回调 Spring 注解依赖注入和注解切面的关键。

```java
package cn.fyupeng.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @Auther: fyp
 * @since: 2023/2/23
 * @Description: Spring容器上下文工具类
 * @Package: cn.fyupeng.util
 * @Version: 1.0
 */
@Component
public class SpringContextUtil implements ApplicationContextAware {
    // Spring 上下文 对象
    private static ApplicationContext applicationContext;

    /**
     * 实现 ApplicationContextAware 接口的 回调方法，设置上下文环境
     * @param applicationContext
     * @throws BeansException
     */
    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringContextUtil.applicationContext = applicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    /**
     * 获取对象
     *
     * @param name
     * @return Object
     * @throws BeansException
     */
    public static Object getBean(String name) throws BeansException {
        return applicationContext.getBean(name);
    }

    public static Object getBean(String name, Class cla) throws BeansException {
        return applicationContext.getBean(name, cla);
    }

}

```

### 3.3 编写启动器

```java
/**
 * RpcServerStarter
 *
 * @author shuigedeng
 * @version 2026.02
 * @since 2025-12-19 09:30:45
 */
@Slf4j
@ServiceScan
@SpringBootApplication
@ComponentScan(basePackages = {"cn.fyupeng", "org.utils"})
public class RpcServerStarter implements CommandLineRunner {

    @Autowired
    private ResourceConfig resourceConfig;

    @PostConstruct
    public void init() {
        Map<String, String> resourceLoaders = ResourceLoadUtils.load("resource.properties");
        if (resourceLoaders != null) {
            String serverAddr = resourceLoaders.get("cn.fyupeng.config.server-addr");
            String[] addrArray = serverAddr.split(":");
            resourceConfig.setServerIp(addrArray[0]);
            resourceConfig.setServerPort(Integer.parseInt(addrArray[1]));
        }
    }

    public static void main( String[] args ) {
        SpringApplication.run(RpcServerStarter.class, args);
    }

    @Override
    public void run( String... args ) throws Exception {
        //这里也可以添加一些业务处理方法，比如一些初始化参数等
        while (true) {
            NettyServer nettyServer = null;
            try {
                // 2.2.0 以下版本
                //nettyServer = new NettyServer(resourceConfig.getServerIp(), resourceConfig.getServerPort(), SerializerCode.KRYO.getCode());
                // 2.2.0 及以上版本支持
                /**
                 * 实现将 容器对象 放到 自管理容器中
                 */
                nettyServer = new NettyServer(resourceConfig.getServerIp(), resourceConfig.getServerPort(),
                        SerializerCode.HESSIAN.getCode()) {

                    @Override
                    public Object newInstance( String fullName, String simpleName, String firstLowCaseName,
                            Class<?> clazz ) throws InstantiationException, IllegalAccessException {
                        return SpringContextUtil.getBean(firstLowCaseName, clazz);
                    }
                };
            } catch (RpcException e) {
                e.printStackTrace();
            }
            log.info("Service bind in port with " + resourceConfig.getServerPort() + " and start successfully!");
            nettyServer.start();
            log.error("RegisterAndLoginService is died，Service is restarting....");
        }
    }

}

```

### 3.4 编写配置文件

注意`config/resource.properties`与资源目录下的`resource.properties`不能同时公用，前者优先级高于后者

```properties
# 用于启动 jar 包地址
cn.fyupeng.config.server-addr=192.168.10.1:8082

# 用于配置中心单机
cn.fyupeng.nacos.register-addr=192.168.2.185:8848
# 用于配置中心集群
cn.fyupeng.nacos.cluster.use=true
cn.fyupeng.nacos.cluster.load-balancer=round
cn.fyupeng.nacos.cluster.nodes=192.168.2.185:8847|192.168.2.185:8848;192.168.2.185:8849
```
当然`Netty`启动会独自开启端口，而且无需启动`Web`服务，整合`SpringBoot`仅仅用到用Jar包启动和使用`Spring`容器管理，把`Tomcat`默认占用端口关了，便于启动多服务服务实现横向扩展

新建`application.properties`
```properties
spring.main.web-application-type=none
spring.main.allow-bean-definition-overriding=true
```
### 3.5 编写api

注意与客户端包名完全相同

```java
package cn.fyupeng.service;

/**
 * @Auther: fyp
 * @since: 2022/10/19
 * @Description: HelloWorld接口
 * @Package: cn.fyupeng.cn.fyupeng.controller
 * @Version: 1.0
 */
public interface HelloService {
    String sayHello(String name);
}
```

### 3.6 编写业务

注意`Service`注解为`cn.fyupeng.service.HelloService`

```java
package cn.fyupeng.service.impl;


import cn.fyupeng.annotation.Service;
import cn.fyupeng.service.HelloService;

/**
 * @Auther: fyp
 * @since: 2022/10/19
 * @Description: HelloWorld实现类
 * @Package: cn.fyupeng.cn.fyupeng.controller.impl
 * @Version: 1.0
 */

@Service
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String name) {
        return "hello, my name is " + name;
    }
}

```



为了使用配置文件注入来启动服务对应的端口
