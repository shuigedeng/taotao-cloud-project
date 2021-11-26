# taotao-cloud-starter-zookeeper 组件

## 说明
- 提供zookeeper的客户端连接，模板操作
- 提供基于zookeeper的分布式锁
- 提供基于zookeeper的分布式的id生成器
- 提供基于zookeeper的分布式的主备选举

## dependencies
### maven
```xml
<dependency>
  <groupId>io.github.shuigedeng</groupId>
  <artifactId>taotao-cloud-starter-zookeeper</artifactId>
  <version>2021.11</version>
</dependency>
```

### gradle
```groovy
compile("io.github.shuigedeng:taotao-cloud-starter-zookeeper:2021.11")
```

## 配置
| 配置项                         | 默认值 | 说明                                        |
| ------------------------------ | ------ | ----------------------------------------- |
| taotao.cloud.zookeeper.enabled               | false   | 开启zookeeper自动配置                                 |
| taotao.cloud.zookeeper.connectString         | 127.0.0.1:2181   | zk连接集群，多个用逗号隔开                  |
| taotao.cloud.zookeeper.sessionTimeout        | 15000  | 会话超时时间(毫秒)        |
| taotao.cloud.zookeeper.connectionTimeout     | 15000  | 连接超时时间(毫秒) |
| taotao.cloud.zookeeper.baseSleepTime         | 2000  | 初始重试等待时间(毫秒)                   |
| taotao.cloud.zookeeper.maxRetries        | 10  | 重试最大次数            |
| taotao.cloud.zookeeper.lock.enabled |     false   | 开启zookeeper分布式锁自动配置                           |

## 注解
可以使用 `@XssCleanIgnore` 注解对方法和类级别进行忽略。

## 自定义 xss 清理
如果内置的 xss 清理规则不满足需求，可以自己实现 `XssCleaner`，注册成 Spring bean 即可。

### 1. 注册成 Spring bean
```java
@Bean
public XssCleaner xssCleaner() {
    return new MyXssCleaner();
}
```

### 2. MyXssCleaner
```java
public class MyXssCleaner implements XssCleaner {

	@Override
	public String clean(String html) {
		Document.OutputSettings settings = new Document.OutputSettings()
			// 1. 转义用最少的规则，没找到关闭的方法
			.escapeMode(Entities.EscapeMode.xhtml)
			// 2. 保留换行
			.prettyPrint(false);
		// 注意会被转义
		String escapedText = Jsoup.clean(html, "", XssUtil.HtmlWhitelist.INSTANCE, settings);
		// 3. 反转义
		return Entities.unescape(escapedText);
	}

}
```
