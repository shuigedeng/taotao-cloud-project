# shardingjdbc

## 介绍
本系统由于同一分装Sharding JDBC并扩展功能，为业务提供方便。

关于Sharding jdbc更多详情，请参阅 [shardingjdbc](https://shardingsphere.apache.org/document/current/cn/manual/sharding-jdbc/configuration/config-spring-boot/) 配置文档

## 依赖引用
```java 
<dependency>
	<artifactId>csx-bsf-shardingjdbc</artifactId>
	<groupId>com.yh.csx.bsf</groupId>
	<version>1.7.1-SNAPSHOT</version>
</dependency>
```


## 配置说明
```shell
#启用shardingjdbc
bsf.shardingjdbc.enabled=true
#支持注解读写分析配置
#@MasterOnly 与@SlaveOnly指定主库或从库
bsf.shardingjdbc.aspect.enabled=true
```
主从配置

```shell 
spring.shardingsphere.datasource.names = master-0,slave-1
spring.shardingsphere.datasource.master-0.type = com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.master-0.driver-class-name = com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.master-0.url = jdbc:mysql://10.252.193.28:3306/{database}?useUnicode=true&characterEncoding=UTF8&useSSL=false&allowMultiQueries=true
spring.shardingsphere.datasource.master-0.username = {username}
spring.shardingsphere.datasource.master-0.password = {password}
spring.shardingsphere.datasource.master-0.filters = stat
spring.shardingsphere.datasource.master-0.max-active = 30
spring.shardingsphere.datasource.master-0.initial-size = 5
spring.shardingsphere.datasource.master-0.max-wait = 60000
spring.shardingsphere.datasource.master-0.time-between-eviction-runs-millis = 60000
spring.shardingsphere.datasource.master-0.min-evictable-idle-time-millis = 300000
spring.shardingsphere.datasource.master-0.validation-query = SELECT 'x'
spring.shardingsphere.datasource.master-0.test-while-idle = true
spring.shardingsphere.datasource.master-0.test-on-borrow = false
spring.shardingsphere.datasource.master-0.test-on-return = false
spring.shardingsphere.datasource.master-0.pool-prepared-statements = true
spring.shardingsphere.datasource.master-0.max-open-prepared-statements = 20

spring.shardingsphere.datasource.slave-1.type = com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.slave-1.driver-class-name = com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.slave-1.url = jdbc:mysql://10.252.193.28:3306/{database}?useUnicode=true&characterEncoding=UTF8&useSSL=false&allowMultiQueries=true
spring.shardingsphere.datasource.slave-1.username =  {username}
spring.shardingsphere.datasource.slave-1.password = {password}
spring.shardingsphere.datasource.slave-1.filters = stat
spring.shardingsphere.datasource.slave-1.max-active = 30
spring.shardingsphere.datasource.slave-1.initial-size = 5
spring.shardingsphere.datasource.slave-1.max-wait = 60000
spring.shardingsphere.datasource.slave-1.time-between-eviction-runs-millis = 60000
spring.shardingsphere.datasource.slave-1.min-evictable-idle-time-millis = 300000
spring.shardingsphere.datasource.slave-1.validation-query = SELECT 'x'
spring.shardingsphere.datasource.slave-1.test-while-idle = true
spring.shardingsphere.datasource.slave-1.test-on-borrow = false
spring.shardingsphere.datasource.slave-1.test-on-return = false
spring.shardingsphere.datasource.slave-1.pool-prepared-statements = true
spring.shardingsphere.datasource.slave-1.max-open-prepared-statements = 20


spring.shardingsphere.masterslave.load-balance-algorithm-type = round_robin
spring.shardingsphere.masterslave.name = master-0
spring.shardingsphere.masterslave.master-data-source-name = master-0
spring.shardingsphere.masterslave.slave-data-source-names = slave-1
```
多数据配置
``` shell
spring.shardingsphere.datasource.names = main,other
spring.shardingsphere.datasource.main.type = com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.main.driver-class-name = com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.main.url = jdbc:mysql://10.252.193.28:3306/{database}?useUnicode=true&characterEncoding=UTF8&useSSL=false&allowMultiQueries=true
spring.shardingsphere.datasource.main.username = {username}
spring.shardingsphere.datasource.main.password = {password}
spring.shardingsphere.datasource.main.filters = stat
spring.shardingsphere.datasource.main.max-active = 30
spring.shardingsphere.datasource.main.initial-size = 5
spring.shardingsphere.datasource.main.max-wait = 60000
spring.shardingsphere.datasource.main.time-between-eviction-runs-millis = 60000
spring.shardingsphere.datasource.main.min-evictable-idle-time-millis = 300000
spring.shardingsphere.datasource.main.validation-query = SELECT 'x'
spring.shardingsphere.datasource.main.test-while-idle = true
spring.shardingsphere.datasource.main.test-on-borrow = false
spring.shardingsphere.datasource.main.test-on-return = false
spring.shardingsphere.datasource.main.pool-prepared-statements = true
spring.shardingsphere.datasource.main.max-open-prepared-statements = 20

spring.shardingsphere.datasource.other.type = com.alibaba.druid.pool.DruidDataSource
spring.shardingsphere.datasource.other.driver-class-name = com.mysql.cj.jdbc.Driver
spring.shardingsphere.datasource.other.url = jdbc:mysql://10.252.193.28:3306/{database}?useUnicode=true&characterEncoding=UTF8&useSSL=false&allowMultiQueries=true
spring.shardingsphere.datasource.other.username =  {username}
spring.shardingsphere.datasource.other.password = {password}
spring.shardingsphere.datasource.other.filters = stat
spring.shardingsphere.datasource.other.max-active = 30
spring.shardingsphere.datasource.other.initial-size = 5
spring.shardingsphere.datasource.other.max-wait = 60000
spring.shardingsphere.datasource.other.time-between-eviction-runs-millis = 60000
spring.shardingsphere.datasource.other.min-evictable-idle-time-millis = 300000
spring.shardingsphere.datasource.other.validation-query = SELECT 'x'
spring.shardingsphere.datasource.other.test-while-idle = true
spring.shardingsphere.datasource.other.test-on-borrow = false
spring.shardingsphere.datasource.other.test-on-return = false
spring.shardingsphere.datasource.other.pool-prepared-statements = true
spring.shardingsphere.datasource.other.max-open-prepared-statements = 20
# 多数据源使用时,使用@DataSource注解指定数据源
# 多数据源时，需要配置自定义hint策略算法
spring.shardingsphere.sharding.default-database-strategy.hint.algorithm-class-name=com.yh.csx.bsf.shardingjdbc.DataSourceShardingAlgorithm
```

## 示例代码

```
/**
*  指定主库
*/
@MasterOnly
@Service
public class CustomerService {

	@Autowired
	private CustomerMapper customerMapper;

	/**
	 * 获取客户详细信息
	 * 
	 * @param id
	 * @return
	 */
	@MasterOnly
	public Customer getCustomer(Long id) {
		Customer customer = null;
		customer = customerMapper.selectByPrimaryKey(id);
		customer.setUpdateTime(new Date());

		return customer;
	}
}
```
```
/**
*  指定从库
*/
@SlaveOnly(1)
@Service
public class CustomerService {

	@Autowired
	private CustomerMapper customerMapper;

	/**
	 * 获取客户详细信息
	 * 
	 * @param id
	 * @return
	 */
	@SlaveOnly(1)
	public Customer getCustomer(Long id) {
		Customer customer = null;
		customer = customerMapper.selectByPrimaryKey(id);
		customer.setUpdateTime(new Date());

		return customer;
	}
}
```
```
/**
* 指定数据源main
*/
@DataSource(name="main")
@Service
public class CustomerService {

	@Autowired
	private CustomerMapper customerMapper;

	/**
	 * 获取客户详细信息
	 * 
	 * @param id
	 * @return
	 */
	@DataSource(name="main")
	public Customer getCustomer(Long id) {
		Customer customer = null;
		customer = customerMapper.selectByPrimaryKey(id);
		customer.setUpdateTime(new Date());

		return customer;
	}
}
```
