# taotao-cloud-project

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

## 2. 总体架构图
![mark](./snapshot/springcloud微服务架构图.jpeg)


## 3. 核心依赖 
依赖 | 版本
---|---
Spring |  5.3.4 
Spring Boot |  2.4.3  
Spring Cloud | 2020.0.1 
Spring Cloud alibaba | 2.2.5.RELEASE  
Spring Security OAuth2 | 5.4.5
Mybatis Plus | 3.4.2
Hutool | 5.5.9
Lombok | 1.18.18
Mysql | 8.0.20
Querydsl | 4.4.0
Swagger | 3.3.0
Knife4j | 3.0.2
Redisson | 3.15.0
Lettuce | 6.0.2.RELEASE
Elasticsearch | 6.8.7
Xxl-job | 2.2.0
EasyCaptcha | 1.6.2

## 4. 演示地址
* **演示地址**： [https://backend.taotaocloud.top](http://192.168.99.130:3000)
  * 账号密码：admin/admin
  * 监控账号密码：admin/admin
  * Grafana账号：admin/admin
  * 任务管理账号密码：admin/admin
  * Sentinel：sentinel/sentinel
* **演示环境有全方位的监控示例：日志监控系统 + APM系统 + GPE系统**
* Github地址： https://github.com/shuigedeng/taotao-cloud-project 
* 前后端分离的企业级微服务架构
* 基于 Spring Cloud Hoxton 、Spring Boot 2.4、 OAuth2 的RBAC权限管理系统  
* 基于数据驱动视图的理念封装 react , antd  
* 提供对常见容器化支持 Docker、Kubernetes、Rancher2 支持  
* 提供 lambda 、stream api 、webflux 的生产实践   
* 提供应用管理，方便第三方系统接入，**支持多租户(应用隔离)**
* 引入组件化的思想实现高内聚低耦合并且高度可配置化
* 注重代码规范，严格控制包依赖，每个工程基本都是最小依赖

## 5. 模块说明
```
taotao-cloud -- 父项目，公共依赖
│  ├─taotao-cloud-auth -- spring-security-oauth2 认证模块[9800]
│  ├─taotao-cloud-code -- 代码生成模块[9000]
│  ├─taotao-cloud-config -- 配置文件模块
│  ├─taotao-cloud-demo -- demo模块
│  │  ├─taotao-cloud-demo-gateway -- 网关demo
│  │  ├─taotao-cloud-demo-youzan -- 有赞demo
│  ├─taotao-cloud-file -- 文件上传模块
│  │  ├─taotao-cloud-file-api -- 文件上传api
│  │  ├─taotao-cloud-file-api -- 文件上传实现[9100]
│  ├─taotao-cloud-gateway -- 网关模块[9900]
│  ├─taotao-cloud-order -- 订单模块
│  │  ├─taotao-cloud-order-api -- 订单管理api
│  │  ├─taotao-cloud-order-api -- 订单管理实现[9600]
│  ├─taotao-cloud-product -- 商品模块
│  │  ├─taotao-cloud-product-api -- 商品管理api
│  │  ├─taotao-cloud-product-api -- 商品管理实现[9500]
│  ├─taotao-cloud-uc -- 用户模块
│  │  ├─taotao-cloud-uc-api -- 用户管理api
│  │  ├─taotao-cloud-uc-api -- 用户管理实现[9700]
│  │─taotao-cloud-starter -- 通用模块
│  │  ├─taotao-cloud-auth-starter -- 封装spring security client通用操作逻辑
│  │  ├─taotao-cloud-common-starter -- 封装通用操作逻辑
│  │  ├─taotao-cloud-data-starter -- 封装数据库通用操作逻辑
│  │  ├─taotao-cloud-elasticsearch-starter -- 封装elasticsearch通用操作逻辑
│  │  ├─taotao-cloud-elk-starter -- 封装elk操作逻辑
│  │  ├─taotao-cloud-job-starter -- 封装定时任务操作逻辑
│  │  ├─taotao-cloud-message-starter -- 封装第三方消息发现操作逻辑
│  │  ├─taotao-cloud-rabbitmq-starter -- 封装rabbitmq操作逻辑
│  │  ├─taotao-cloud-redis-starter -- 封装redis操作逻辑
│  │  ├─taotao-cloud-ribbon-starter -- 封装ribbon通用逻辑
│  │  ├─taotao-cloud-seata-starter -- 封装seata通用逻辑
│  │  ├─taotao-cloud-sentinel-starter -- 封装sentinel通用逻辑
│  │  ├─taotao-cloud-shardingjdbc-starter -- 封装shardingjdbc通用逻辑
│  │  ├─taotao-cloud-social-starter -- 封装第三方登录通用逻辑
│  │  ├─taotao-cloud-swagger-starter -- 封装swagger通用逻辑
```

## 6.开源共建

1. 欢迎提交 [pull request](https://github.com/shuigedeng/taotao-cloud)，注意对应提交对应 `dev` 分支

2. 欢迎提交 [issue](https://github.com/shuigedeng/taotao-cloud/issues)，请写清楚遇到问题的原因、开发环境、复显步骤。

3. 不接受`功能请求`的 [issue](https://github.com/shuigedeng/taotao-cloud/issues)，功能请求可能会被直接关闭。  

4. mail: <a href="981376577@qq.com">981376577@qq.com</a> | <a target="_blank" href="http://wpa.qq.com/msgrd?v=3&uin=3130998334&site=qq&menu=yes"> QQ: 981376577</a>    

<table>
    <tr>
        <td><a target="_blank" href="https://www.aliyun.com/minisite/goods?userCode=dickv1kw&share_source=copy_link"><img width="460px" height="177px" alt="阿里云" src="https://gitee.com/zlt2000/images/raw/master/aly.jpg"/></a></td>
        <td><a target="_blank" href="https://url.cn/e2NLAJ9r"><img width="460px" height="177px"  alt="腾讯云" src="https://gitee.com/zlt2000/images/raw/master/txy.jpg"/></a></td>
    </tr>
</table>

