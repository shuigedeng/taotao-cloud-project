# ELK

## 介绍
ELK 在最近两年迅速崛起，成为机器数据分析，或者说实时日志处理领域，开源界的第一选择。
ELK的搭建，请参阅[ELK安装部署文档]((https://elkguide.elasticsearch.cn/) )

## 依赖引入

```java 
<dependency>
	<artifactId>csx-bsf-elk</artifactId>
	<groupId>com.yh.csx.bsf</groupId>
	<version>1.7.1-SNAPSHOT</version>
</dependency>
```

## 配置说明

```shell
## bsf elk 集成
#elk服务的开关,非必须，默认true
#bsf.elk.enabled=false 
#elk的服务地址
bsf.elk.destinations= 
#启动web相关的功能
bsf.elk.web.enabled=true 
#启动web的出参入参，依赖bsf.elk.web.enabled=true
bsf.elk.web.controller.aspect.enabled=true 
#启动日志统计及监控，依赖bsf.elk.web.enabled=true
bsf.health.log.statistic.enabled=true	
```