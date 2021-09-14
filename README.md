[comment]: <> (<p align="center"><font size=120><strong>taotao-cloud-project</strong></font></p>)

# **taotao-cloud-project**

----

<p align="center">
  <img src='https://img.shields.io/badge/license-Apache%202-green' alt='License'/>
  <img src="https://img.shields.io/badge/Spring-5.3.4-red" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Boot-2.4.3-orange" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud-2020.0.1-yellowgreen" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud%20alibaba-2.2.5.RELEASE-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Elasticsearch-6.8.7-green" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Mybatis%20Plus-3.4.2-yellow" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Knife4j-3.0.2-brightgreen" alt="Downloads"/>
</p>

## 1. 如果您觉得有帮助，请点右上角 "Star" 支持一下谢谢

**taotao cloud project** 创建这个仓库的目的就是工作以来的技术总结和技术沉淀(业余时间进行开发) **仓库代码中不涉及公司任何业务代码** 主要包括如下几部分

- **大数据模块** 集成日志数据处理和分析、用户行为分析、推荐系统、离线/流式计算、数据仓库、数据湖等大数据处理
  

- **微服务模块** 基于spring cloud alibab微服务基础脚手架框架,用于基础服务的集成和跟业务无关的基础技术集成, 
  提供大量的starter作为技术底层支持,同时基础框架集中统一优化中间件相关服务及使用,
  提供高性能,更方便的基础服务接口及工具，完全可以在实际工作中使用
  

- **前端模块** 主要使用react进行前端开发、集成以taro为主的多端合一框架。以react antd 框架进行快速后台管理平台开发
  

- **python模块** 主要是集成了Django的web开发、家庭自动化框架原理的分析

总之基于Spring Cloud Alibaba的微服务架构。旨在提供技术框架的基础能力的封装，减少开发工作，只关注业务

## 2. springcloud微服务架构图
![mark](./snapshot/springcloud微服务架构图.jpeg)


## 3. springcloud微服务分层图
![mark](./snapshot/springcloud微服务分层图.png)


## 4. dependencies
Gradle:
```
dependencyManagement{
  imports {
    mavenBom "io.github.shuigedeng:taotao-cloud-dependencies:2021.9.1"
  }
}

api "io.github.shuigedeng:taotao-cloud-starter-web"
```

Maven:
```
<dependentyManagement>
  <dependencies>
    <dependency>
      <groupId>io.github.shuigedeng</groupId>
      <artifactId>taotao-cloud-dependencies</artifactId>
      <version>2021.9.1</version>
      <type>pom</type>
      <scope>import</scope>
    </dependency>
  </dependencies>
</dependentyManagement>


<dependencies>
    <dependency>
      <groupId>io.github.shuigedeng</groupId>
      <artifactId>taotao-cloud-starter-web</artifactId>
    </dependency>
</dependencies>
```


## 5. 核心依赖 
依赖 | 版本
---|---
Spring |  5.3.4 
Spring Boot |  2.5.4
Spring Cloud | 2020.0.3
Spring Cloud alibaba | 2021.1  
Spring Security OAuth2 | 5.4.5
Mybatis Plus | 3.4.2
Hutool | 5.5.9
Mysql | 8.0.20
Querydsl | 4.4.0
Swagger | 3.3.0
Knife4j | 3.0.2
Redisson | 3.15.0
Lettuce | 6.0.2.RELEASE
Elasticsearch | 6.8.7
Xxl-job | 2.2.0
EasyCaptcha | 1.6.2
Guava | 29.0-jre

## 6. 演示地址
* taotao cloud 首页: [https://taotaocloud.top](https://taotaocloud.top)
* 博客系统地址: [https://blog.taotaocloud.top](https://blog.taotaocloud.top)
* 大屏监控系统地址: [https://datav.taotaocloud.top](https://datav.taotaocloud.top)
* 后台管理地址: [https://backend.taotaocloud.top](https://backend.taotaocloud.top)
* 账号密码：admin/admin
* 监控账号密码：admin/admin
* Grafana账号：admin/admin
* 任务管理账号密码：admin/admin
* Sentinel：sentinel/sentinel

## 7. 功能特点

* 微服务技术框架: 前后端分离的企业级微服务架构、主要针对解决微服务和业务开发时常见的**非功能性需求** 
* 主体框架：采用最新的Spring Boot 2.5.1、Spring Cloud 2020.0.3、Spring Cloud Alibaba 2.2.5.RELEASE版本进行设计
* 统一注册：支持Nacos作为注册中心，实现多配置、分群组、分命名空间、多业务模块的注册和发现功能
* 统一认证：统一Oauth2认证协议，采用jwt的方式，实现统一认证，完备的RBAC权限管理、数据权限处理、网关统一鉴权、灰度发布
* 业务监控：利用Spring Boot Admin 监控各个独立服务的运行状态
* 日志分析：集成kafka、ELK、prometheus实时监控日志(请求日志、系统日志、数据变更日志、用户日志)
* 分布式事务：集成spring cloud alibaba seata分布式事务处理
* 业务熔断：采用spring cloud alibaba Sentinel实现业务熔断处理，避免服务之间出现雪崩
* 链路追踪：自定义traceId的方式，实现简单的链路追踪功能、集成skywalking、sleuth、zipkin链路监控
* 分布式任务：集成xxl-job分布式定时任务处理
* 内部调用：集成了Feign和Dubbo两种模式支持内部调用，并且可以实现无缝切换
* 身份注入：通过注解的方式，实现用户登录信息的快速注入
* 在线文档：通过接入Knife4j，实现在线API文档的查看与调试
* 消息中心：集成消息中间件RocketMQ、kafka，对业务进行异步处理
* 业务分离：采用前后端分离的框架设计，前端采用react antd脚手架快速开放
* 多租户功能：集成Mybatis Plus、jpa,实现saas多租户功能  
* 容器化支持: Docker、Kubernetes、Rancher2 支持  
* webflux支持: lambda、stream api、webflux 的生产实践
* 开放平台: 提供应用管理，方便第三方系统接入，**支持多租户(应用隔离)**
* 组件化: 引入组件化的思想实现高内聚低耦合并且高度可配置化
* 代码规范: 注重代码规范，严格控制包依赖

> PS: 借鉴了其他开源项目

## 8. 模块说明
```
taotao-cloud-project -- 父项目
│  ├─taotao-cloud-bigdata -- 大数据模块
│  │  ├─taotao-cloud-bigdata-azkaban -- azkaban模块
│  │  ├─taotao-cloud-bigdata-clickhouse -- clickhouse模块
│  │  ├─taotao-cloud-bigdata-datax -- datax模块
│  │  ├─taotao-cloud-bigdata-elasticsearch -- elasticsearch模块
│  │  ├─taotao-cloud-bigdata-flink -- flink模块
│  │  ├─taotao-cloud-bigdata-flum -- flum模块
│  │  ├─taotao-cloud-bigdata-hadoop -- hadoop模块
│  │  ├─taotao-cloud-bigdata-hbase -- hbase模块
│  │  ├─taotao-cloud-bigdata-hive -- hive模块
│  │  ├─taotao-cloud-bigdata-hudi -- hudi模块
│  │  ├─taotao-cloud-bigdata-impala -- impala模块
│  │  ├─taotao-cloud-bigdata-kafka -- kafka模块
│  │  ├─taotao-cloud-bigdata-kudu -- kudu模块
│  │  ├─taotao-cloud-bigdata-openresty -- openresty模块
│  │  ├─taotao-cloud-bigdata-phoenix -- phoenix模块
│  │  ├─taotao-cloud-bigdata-spark -- spark模块
│  │  ├─taotao-cloud-bigdata-storm -- storm模块
│  │  ├─taotao-cloud-bigdata-tez -- tez模块
│  │  ├─taotao-cloud-bigdata-trino -- trino模块
│  │  ├─taotao-cloud-bigdata-zookeeper -- zookeeper模块
│  ├─taotao-cloud-container -- 容器模块
│  │  ├─taotao-cloud-docker -- docker模块
│  │  ├─taotao-cloud-kubernetes -- kubernetes模块
│  ├─taotao-cloud-demo -- demo模块
│  │  ├─taotao-cloud-demo-kafka -- kafka模块
│  │  ├─taotao-cloud-demo-mqtt -- mqtt模块
│  │  ├─taotao-cloud-demo-rocketmq -- rocketmq模块
│  │  ├─taotao-cloud-demo-seata -- seata模块
│  │  ├─taotao-cloud-demo-sharding-jdbc -- sharding-jdbc模块
│  │  ├─taotao-cloud-demo-sso -- sso模块
│  │  ├─taotao-cloud-demo-transaction -- transaction模块
│  │  ├─taotao-cloud-demo-youzan -- youzan模块
│  ├─taotao-cloud-dependencies -- 全局公共依赖模块
│  ├─taotao-cloud-java -- java模块
│  │  ├─taotao-cloud-concurrent  -- concurrent模块
│  │  ├─taotao-cloud-javaee -- javaee模块
│  │  ├─taotao-cloud-javase -- javase模块
│  │  ├─taotao-cloud-javaweb -- javaweb模块
│  ├─taotao-cloud-microservice -- 微服务模块
│  │  ├─taotao-cloud-aftersale  -- aftersale模块
│  │  ├─taotao-cloud-bulletin  -- bulletin模块
│  │  ├─taotao-cloud-cart  -- cart模块
│  │  ├─taotao-cloud-coupon  -- coupon模块
│  │  ├─taotao-cloud-customer  -- customer模块
│  │  ├─taotao-cloud-dfs  -- dfs模块
│  │  ├─taotao-cloud-backend  -- backend模块
│  │  ├─taotao-cloud-front  -- front模块
│  │  ├─taotao-cloud-log -- log模块
│  │  ├─taotao-cloud-logistics  -- logistics模块
│  │  ├─taotao-cloud-mail  -- mail模块
│  │  ├─taotao-cloud-member  -- member模块
│  │  ├─taotao-cloud-news  -- news模块
│  │  ├─taotao-cloud-oauth2  -- oauth2模块
│  │  ├─taotao-cloud-operation  -- operation模块
│  │  ├─taotao-cloud-order  -- order模块
│  │  ├─taotao-cloud-pay -- pay模块
│  │  ├─taotao-cloud-product  -- product模块
│  │  ├─taotao-cloud-recommend  -- recommend模块
│  │  ├─taotao-cloud-report  -- report模块
│  │  ├─taotao-cloud-search  -- search模块
│  │  ├─taotao-cloud-seckill   -- seckill模块
│  │  ├─taotao-cloud-settlement  -- settlement模块
│  │  ├─taotao-cloud-sms  -- sms模块
│  │  ├─taotao-cloud-stock  -- stock模块
│  │  ├─taotao-cloud-uc  -- uc模块
│  │  ├─taotao-cloud-starter  -- starter模块
│  ├─taotao-cloud-netty -- netty模块
│  ├─taotao-cloud-offline -- 离线数据分析模块
│  │  ├─taotao-cloud-offline-warehouse  -- 数据仓库模块
│  │  ├─taotao-cloud-offline-weblog -- web日志分析模块
│  ├─taotao-cloud-opencv -- opencv模块
│  ├─taotao-cloud-plugin -- 插件模块
│  │  ├─taotao-cloud-gradle-plugin  -- gradle-plugin模块
│  │  ├─taotao-cloud-idea-plugin -- idea-plugin模块
│  ├─taotao-cloud-processor -- java processor模块
│  ├─taotao-cloud-python -- python模块
│  │  ├─taotao-cloud-django  -- django模块
│  │  ├─taotao-cloud-oldboy  -- oldboy模块
│  │  ├─taotao-cloud-scrapy  -- scrapy模块
│  │  ├─taotao-cloud-spider  -- spider模块
│  │  ├─taotao-cloud-tornado  -- tornado模块
│  ├─taotao-cloud-reactive -- spring web响应式模块
│  ├─taotao-cloud-realtime -- 实时数据分析模块
│  │  ├─taotao-cloud-realtime-datalake  -- 数据湖模块
│  │  ├─taotao-cloud-realtime-mall -- 商城日志分析模块
│  │  ├─taotao-cloud-realtime-recommend -- 实时推荐模块
│  │  ├─taotao-cloud-realtime-travel -- 实时旅游模块
│  ├─taotao-cloud-rpc -- rpc模块
│  ├─taotao-cloud-scala -- scala模块
│  ├─taotao-cloud-spring-native -- spring native模块
│  ├─taotao-cloud-spring-source -- spring 源码模块
│  ├─taotao-cloud-spring-standlone -- 单项目模块
│  ├─taotao-cloud-web -- 前端模块
│  │  ├─taotao-cloud-backend  -- 后台管理模块
│  │  ├─taotao-cloud-datav -- 大屏模块
│  │  ├─taotao-cloud-electron -- pc端app模块
│  │  ├─taotao-cloud-front -- 商城前端模块
│  │  ├─taotao-cloud-mall  -- 商城小程序模块
│  │  ├─taotao-cloud-music -- 音乐小程序模块
```

## 9.开源共建

1. 欢迎提交 [pull request](https://github.com/shuigedeng/taotao-cloud-project)，注意对应提交对应 `dev` 分支

2. 欢迎提交 [issue](https://github.com/shuigedeng/taotao-cloud-project/issues)，请写清楚遇到问题的原因、开发环境、复显步骤。

3. 不接受`功能请求`的 [issue](https://github.com/shuigedeng/taotao-cloud-project/issues)，功能请求可能会被直接关闭。  

4. mail: <a href="981376577@qq.com">981376577@qq.com</a> | <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3130998334&site=qq&menu=yes"> QQ: 981376577</a>    

## 10.参与贡献

开发: 目前个人独立开放

## 11.项目截图
<table>
    <tr>
        <td><img alt="调度任务中心" src="snapshot/project/1.png"/></td>
        <td><img alt="nacos服务注册" src="snapshot/project/2.png"/></td>
    </tr>
	<tr>
        <td><img alt="granfana页面" src="snapshot/project/3.png"/></td>
        <td><img alt="prometheus页面" src="snapshot/project/4.png"/></td>
    </tr>
	<tr>
        <td><img alt="skywalking页面" src="snapshot/project/5.png"/></td>
        <td><img alt="sentinel页面" src="snapshot/project/6.png"/></td>
    </tr>
    <tr>
        <td><img alt="kibana页面" src="snapshot/project/7.png"/></td>
        <td><img alt="zipkin页面" src="snapshot/project/8.png"/></td>
    </tr>
    <tr>
        <td><img alt="springadmin页面" src="snapshot/project/9.png"/></td>
        <td><img alt="knife4j页面" src="snapshot/project/10.png"/></td>
    </tr>
    <tr>
        <td><img alt="swagger页面" src="snapshot/project/11.png"/></td>
        <td><img alt="arthas页面" src="snapshot/project/12.png"/></td>
    </tr>

[comment]: <> (    <tr>)

[comment]: <> (        <td><img alt="日志中心02" src="https://gitee.com/zlt2000/images/raw/master/%E6%97%A5%E5%BF%97%E4%B8%AD%E5%BF%8302.png"/></td>)

[comment]: <> (        <td><img alt="慢查询sql" src="https://gitee.com/zlt2000/images/raw/master/%E6%85%A2%E6%9F%A5%E8%AF%A2sql.png"/></td>)

[comment]: <> (    </tr>)

[comment]: <> (    <tr>)

[comment]: <> (        <td><img alt="nacos-discovery" src="https://gitee.com/zlt2000/images/raw/master/nacos-discovery.png"/></td>)

[comment]: <> (        <td><img alt="应用吞吐量监控" src="https://gitee.com/zlt2000/images/raw/master/%E5%BA%94%E7%94%A8%E5%90%9E%E5%90%90%E9%87%8F%E7%9B%91%E6%8E%A7.png"/></td>)

[comment]: <> (    </tr>)
</table>
