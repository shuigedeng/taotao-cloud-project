#Apollo配置使用
Apollo（阿波罗）是携程框架部门研发的分布式配置中心，能够集中化管理应用不同环境、不同集群的配置，配置修改后能够实时推送到应用端，并且具备规范的权限、流程治理等特性，适用于微服务配置管理场景。[更多详情](https://github.com/ctripcorp/apollo)

本系统用于统一封装客户端，简化业务引入使用。

## 依赖配置
```java 
<dependency>
	<artifactId>csx-bsf-apollo</artifactId>
	<groupId>com.yh.csx.bsf</groupId>
	<version>1.7.1-SNAPSHOT</version>
</dependency>
```
## 配置说明
```
#启用开关
bsf.apollo.enabled=true
spring.application.name=
#应用程序ID，必填
app.id=bsf-demo
bsf.env=dev
#apollo服务器地址，必填
apollo.meta=
apollo.bootstrap.enabled=
apollo.bootstrap.namespaces=
#热加载开关
apollo.bootstrap.eagerLoad.enabled=
```