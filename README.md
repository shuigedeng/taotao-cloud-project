#  taotao-cloud-platform

<p align="center">
  <img src='hhttps://img.shields.io/badge/license-Apache%202-green' alt='License'/>
  <img src="https://img.shields.io/badge/Spring%20Boot-2.2.6.RELEASE-orange" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud-Hoxton.SR3-red" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Spring%20Cloud%20Alibaba-2.2.1.RELEASE-blue" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Elasticsearch-6.8.7-brightgreen" alt="Downloads"/>
  <img src="https://img.shields.io/badge/Knife4j-2.0.2-yellowgreen" alt="Downloads"/>
</p>

## 如果您觉得有帮助，请点右上角 "Star" 支持一下谢谢
&nbsp;
## 总体架构图
![mark](https://gitee.com/zlt2000/images/raw/master/springcloud%E5%BE%AE%E6%9C%8D%E5%8A%A1%E6%9E%B6%E6%9E%84%E5%9B%BE.jpg)

&nbsp;
## 核心依赖 
依赖 | 版本
---|---
Spring Boot |  2.2.6.RELEASE  
Spring Cloud | Hoxton.SR8  
Spring Cloud alibaba | 2.2.1.RELEASE  
Spring Security OAuth2 | 2.3.6
Mybatis Plus | 3.3.1
Hutool | 5.3.5
Swagger | 2.9.2
Knife4j | 2.0.2
Xxl-job | 2.2.0
EasyCaptcha | 1.6.2

&nbsp;
## 演示地址
* **演示地址**： [http://192.168.99.130:3000](http://192.168.99.130:3000)
  * 账号密码：admin/admin
  * 监控账号密码：admin/admin
  * Grafana账号：admin/admin
  * 任务管理账号密码：admin/admin
  * Sentinel：sentinel/sentinel
* **演示环境有全方位的监控示例：日志监控系统 + APM系统 + GPE系统**
* Github地址： https://github.com/shuigedeng/taotao-cloud 
* 前后端分离的企业级微服务架构
* 基于 Spring Cloud Hoxton 、Spring Boot 2.3、 OAuth2 的RBAC权限管理系统  
* 基于数据驱动视图的理念封装 react , antd  
* 提供对常见容器化支持 Docker、Kubernetes、Rancher2 支持  
* 提供 lambda 、stream api 、webflux 的生产实践   
* 提供应用管理，方便第三方系统接入，**支持多租户(应用隔离)**
* 引入组件化的思想实现高内聚低耦合并且高度可配置化
* 注重代码规范，严格控制包依赖，每个工程基本都是最小依赖

&nbsp;
## 4. 模块说明
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

#### 开源共建

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

