### Sentinel

```shell
# 普通版
docker-compose -f docker-compose-sentinel.yml -p sentinel up -d

# 监控数据持久化到mysql版
# docker-compose -f docker-compose-sentinel-mysql.yml -p sentinel up -d

# 规则持久化到nacos
# docker-compose -f docker-compose-sentinel-nacos.yml -p sentinel up -d
```

访问地址：[`ip地址:8858`](http://www.zhengqingya.com:8858)
登录账号密码：`sentinel/sentinel`

#### SpringCloud中规则持久化到nacos

```xml
<!-- Sentinel规则持久化至Nacos -->
<dependency>
   <groupId>com.alibaba.csp</groupId>
   <artifactId>sentinel-datasource-nacos</artifactId>
</dependency>
```

```yml
spring:
  application:
    name: demo # 应用名称
  cloud:
    sentinel:
      enabled: true # 自动化配置是否生效
      eager: true   # 禁用控制台懒加载
      web-context-unify: false # 关闭调用链路收敛 => 实现链路流控
      transport:
        dashboard: www.zhengqingya.com:8858 # 控制台地址
        client-ip: ${spring.cloud.client.ip-address} # 获取本机IP地址
        port: 18719 # 启动该服务，会在应用程序的相应服务器上启动HTTP Server，并且该服务器将与Sentinel dashboard进行交互
      # ============== ↓↓↓↓↓↓ 增加规则持久化配置到nacos ↓↓↓↓↓↓ ==============
      datasource:
        # 流控规则
        flow:
          nacos:
            server-addr: ${spring.cloud.nacos.config.server-addr}
            username: ${spring.cloud.nacos.config.username}
            password: ${spring.cloud.nacos.config.password}
            namespace: ${spring.cloud.nacos.config.namespace}
            group-id: sentinel-group
            data-id: ${spring.application.name}-sentinel-flow-rules
            # 规则类型：flow、degrade、param-flow、system、authority
            rule-type: flow
        # 熔断降级
        degrade:
          nacos:
            server-addr: ${spring.cloud.nacos.config.server-addr}
            username: ${spring.cloud.nacos.config.username}
            password: ${spring.cloud.nacos.config.password}
            namespace: ${spring.cloud.nacos.config.namespace}
            group-id: sentinel-group
            data-id: ${spring.application.name}-sentinel-degrade-rules
            rule-type: degrade
        # 热点规则
        param-flow:
          nacos:
            server-addr: ${spring.cloud.nacos.config.server-addr}
            username: ${spring.cloud.nacos.config.username}
            password: ${spring.cloud.nacos.config.password}
            namespace: ${spring.cloud.nacos.config.namespace}
            group-id: sentinel-group
            data-id: ${spring.application.name}-sentinel-param-flow-rules
            rule-type: param-flow
        # 系统规则
        system:
          nacos:
            server-addr: ${spring.cloud.nacos.config.server-addr}
            username: ${spring.cloud.nacos.config.username}
            password: ${spring.cloud.nacos.config.password}
            namespace: ${spring.cloud.nacos.config.namespace}
            group-id: sentinel-group
            data-id: ${spring.application.name}-sentinel-system-rules
            rule-type: system
        # 授权规则
        authority:
          nacos:
            server-addr: ${spring.cloud.nacos.config.server-addr}
            username: ${spring.cloud.nacos.config.username}
            password: ${spring.cloud.nacos.config.password}
            namespace: ${spring.cloud.nacos.config.namespace}
            group-id: sentinel-group
            data-id: ${spring.application.name}-sentinel-authority-rules
            rule-type: authority
```
