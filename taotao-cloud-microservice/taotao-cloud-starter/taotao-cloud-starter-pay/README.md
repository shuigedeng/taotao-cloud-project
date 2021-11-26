# 支付聚合spring boot组件
pay-spring-boot-starter 是一个基于spring-boot实现自动化配置的支付对接， **让你真正做到一行代码实现支付聚合**， 让你可以不用理解支付怎么对接，只需要专注你的业务


### 特性
      1. 项目第三方依赖极少，依托于spring boot与pay-java，项目精简，不用担心项目迁移问题
      2. 一行代码解决配置，一行代码发起支付，一行代码处理回调并且业务与支付完全隔离
      3. 项目扩展性极强极灵活，组件中暴露大量接口，实现对应接口重写加入spring容器即可覆盖全部功能
      4. 引入pay-java具体支付组件即可激活某一支付功能，代码可以不用任何修改即可使用

---
全能第三方支付对接pay-spring-boot-starter开发工具包.优雅的轻量级支付模块集成支付对接支付整合（微信,支付宝,银联,友店,富友,跨境支付paypal,payoneer(P卡派安盈)易极付）app,扫码,网页支付刷卡付条码付刷脸付转账服务商模式、支持多种支付类型多支付账户，支付与业务完全剥离，简单几行代码即可实现支付，简单快速完成支付模块的开发



#### 本项目在以下代码托管网站
* 码云：https://gitee.com/egzosn/pay-spring-boot-starter-parent
* GitHub：https://github.com/egzosn/pay-spring-boot-starter-parent

#### 软件架构
spring-boot  pay-java-parent

#### 本项目基础实现 pay-java-parent 全能第三方支付对接Java开发工具包.优雅的轻量级支付模块集成支付对接支付整合（微信,支付宝,银联,友店,富友,跨境支付paypal,payoneer(P卡派安盈)易极付）app,扫码,网页支付刷卡付条码付刷脸付转账服务商模式、支持多种支付类型多支付账户，支付与业务完全剥离，简单几行代码即可实现支付，简单快速完成支付模块的开发，可轻松嵌入到任何系统里 目前仅是一个开发工具包（即SDK）
* 码云：https://gitee.com/egzosn/pay-java-parent
* GitHub：https://github.com/egzosn/pay-java-parent


#### 使用教程

1. 引入 pay-spring-boot-starter
```xml
    <dependency>
        <groupId>com.egzosn</groupId>
        <artifactId>pay-spring-boot-starter</artifactId>
        <version>1.0.2-b</version>
    </dependency>
```
2. 引入 你需要对接的基于`pay-java-parent`支付开发包,具体支付模块 "{module-name}" 为具体的支付渠道的模块名 pay-java-ali，pay-java-wx等

```xml
    <dependency>
        <groupId>com.egzosn</groupId>
        <artifactId>{module-name}</artifactId>
        <version>2.14.3-b2</version>
    </dependency>

```
3. 编写一个基于com.egzosn.pay.spring.boot.core.PayServiceConfigurer的子类
```
    3.1.  并将其加入spring容器中
    3.2.  对PayServiceConfigurer的子类所实现的方法进行配置数据来源，目前提供两种方式`jdbc`与`inMemory`

```


4. 使用： 在你需要用到的类中注入 `com.egzosn.pay.spring.boot.core.MerchantPayServiceManager`  这个类是一个支付相关的操作类，拿到该类的引用即可对任意支付进行操作

#### 使用案例详情查看[pay-spring-boot-starter-demo](pay-spring-boot-starter-demo?dir=1&filepath=pay-spring-boot-starter-demo)


###### 支付教程

* [基础模块支付宝微信讲解](https://gitee.com/egzosn/pay-java-parent/wikis/Home)
* [微信V3，查看demo/WxV3PayController](https://gitee.com/egzosn/pay-java-parent/blob/develop/pay-java-demo/)
* [微信合并支付，查看demo/WxV3CombinePayController](https://gitee.com/egzosn/pay-java-parent/blob/develop/pay-java-demo/)
* [微信分账，查看demo/WxV3ProfitSharingController](https://gitee.com/egzosn/pay-java-parent/blob/develop/pay-java-demo/)
* [银联](https://gitee.com/egzosn/pay-java-parent/blob/develop/pay-java-union?dir=1&filepath=pay-java-union)
* [payoneer](https://gitee.com/egzosn/pay-java-parent/blob/develop/pay-java-payoneer?dir=1&filepath=pay-java-payoneer)
* [paypal,查看demo/PayPalV2PayController](https://gitee.com/egzosn/pay-java-parent/blob/develop/pay-java-demo/)
* [友店微信](https://gitee.com/egzosn/pay-java-parent/blob/develop/pay-java-wx-youdian?dir=1&filepath=pay-java-youdian)
* [富友](https://gitee.com/egzosn/pay-java-parent/blob/develop/pay-java-fuiou?dir=1&filepath=pay-java-fuiou)


作者公众号
![公众号](https://egzosn.gitee.io/pay-java-parent/gzh.png "gzh.png")

E-Mail：egan@egzosn.com

**QQ群：**


1. pay-java(1群): 542193977(已满)
2. pay-java(2群)：766275051


微信群:
![微信群](https://egzosn.gitee.io/pay-java-parent/wx.jpg "wx.jpg")




#### 参与贡献

1. Fork 本项目
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request


也感谢各大友友同学帮忙进行接口测试

非常欢迎和感谢对本项目发起Pull Request的同学，不过本项目基于git flow开发流程，因此在发起Pull Request的时候请选择develop分支。

