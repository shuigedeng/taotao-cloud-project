## csx-bsf-bigdata 介绍
为了解决大表分析查询问题，引入clickhouse数据库。

## Clickhouse介绍
ClickHouse是一个用于联机分析（OLAP）的列式数据库管理系统（DBMS）.更多介绍请参阅[官网](https://clickhouse.tech/)

**OLAP场景的关键特征：**

* 大多数是读请求
* 数据总是以相当大的批(> 1000 rows)进行写入
* 不修改已添加的数据
* 每次查询都从数据库中读取大量的行，但是同时又仅需要少量的列
* 宽表，即每个表包含着大量的列
* 较少的查询(通常每台服务器每秒数百个查询或更少)
* 对于简单查询，允许延迟大约50毫秒
* 列中的数据相对较小： 数字和短字符串(例如，每个URL 60个字节)
* 处理单个查询时需要高吞吐量（每个服务器每秒高达数十亿行）
* 事务不是必须的
* 对数据一致性要求低
* 每一个查询除了一个大表外都很小
* 查询结果明显小于源数据，换句话说，数据被过滤或聚合后能够被盛放在单台服务器的内存中

## 依赖引入
BSF已经集成了clickhouse模块，不需单独依赖引入。
```java
<dependency>
	<artifactId>csx-bsf-bigdata</artifactId>
	<groupId>com.yh.csx.bsf</groupId>
	<version>1.7.5-SNAPSHOT</version>	
</dependency>

```
## 数据源配置

```shell
# bigdata configuration
# enable clickhouse function
bsf.clickhouse.enabled=true
# clickhouse datasouce configuration
spring.datasource.clickhouse.type=com.alibaba.druid.pool.DruidDataSource
spring.datasource.clickhouse.username=default
spring.datasouce.clickhouse.password=
spring.datasource.clickhouse.url=jdbc:clickhouse://10.252.193.21:8123/default
spring.datasource.clickhouse.driver-class-name=ru.yandex.clickhouse.ClickHouseDriver
```

## 技术原理

基于Spring Boot的AtuoConfiguration注入clickhosue 数据源。
```java
 @Bean("clickHouseDataSource")
    @ConfigurationProperties("spring.datasource.clickhouse")
    public DataSource clickHouseDataSource(){
        var type = ReflectionUtils.classForName(PropertyUtils.getPropertyCache("spring.datasource.clickhouse.type",""));
        return DataSourceBuilder.create().type(type).build();
    }
```

## 示例代码

```java
/**
 * Clickhouse dbhelper
 * @author Robin.Wang
 * @Date 2020-05-11
 */
@Slf4j
@Service
public class ClickhouseDBHelper {

	@Autowired
	DataSource dataSource;
	StragtegyProvider stragtegyProvider = new StragtegyProvider();

	/**
	 * 判断库是否存在
	 * 
	 * @param table 库名.表名
	 * @return boolean
	 * @throws Exception
	 */
	public boolean createDatabase(String database) throws Exception {
		Assert.hasText(database, "table can't be null");
		String sql = "create database if not exists " + database;
		return executeQuery(sql);
	}

	/**
	 * 判断表是否存在
	 * 
	 * @param table 库名.表名
	 * @return boolean
	 * @throws Exception
	 */
	public boolean existsTable(String table) throws Exception {
		Assert.hasText(table, "table can't be null");
		String sql = "exists " + table;
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement prepareStatement = conn.prepareStatement(sql);
			ResultSet ret = prepareStatement.executeQuery();

			if (ret.next()) {
				Boolean b = ret.getBoolean("result");
				return b;
			}
			return false;
		} catch (Exception e) {
			log.error("Exception {}", e);
			throw e;
		}

	}

	/**
	 * 创建数据表
	 * 
	 * @param table        库名.表名
	 * @param List<Column> 数据列
	 * @return boolean
	 * @throws Exception
	 */
	public boolean createTable(String table, List<ClickHouseColumn> columns) throws Exception {
		Set<String> ids= new HashSet<String>();
		for(ClickHouseColumn c:columns) {
			if(c.isKey()) {
				ids.add(c.getName());
			}
		}
		return createTable(table, columns, ids, 8192);
	}

	/**
	 * 创建数据表
	 * 
	 * @param schema       数据库名
	 * @param table        表名
	 * @param List<Column> 数据列
	 * @param Set<String>  主键
	 * @param granularity  索引粒度
	 * @return boolean
	 * @throws Exception
	 */
	public boolean createTable(String table, List<ClickHouseColumn> columns, Set<String> ids, Integer granularity)
			throws Exception {
		Assert.notEmpty(columns, "columns can't be emptry");
		StringBuilder sb = new StringBuilder();
		sb.append("create table ").append(table).append("(");
		for (ClickHouseColumn column : columns) {

			sb.append(column.getName()).append(" ");
			if (column.getType().equals(ClickHouseDataType.Decimal)
					|| column.getType().equals(ClickHouseDataType.Decimal32)
					|| column.getType().equals(ClickHouseDataType.Decimal64)
					|| column.getType().equals(ClickHouseDataType.Decimal128)) {
				if (column.isNull()) {
					sb.append("Nullable(").append(column.getType().toString()).append("(32,10)").append(")");
				} else {
					sb.append(column.getType().toString()).append("(32,10)");
				}
			} else {
				if (column.isNull()) {
					sb.append("Nullable(").append(column.getType().toString()).append(")");
				} else {
					sb.append(column.getType().toString());
				}
			}
			sb.append(",");
		}
		if (!sb.toString().contains("auto_id")) {
			sb.append("auto_id  Int64,");
		}
		if (!sb.toString().contains("auto_create_date")) {
			sb.append("auto_create_date Date default now(),");
		}
		sb.delete(sb.length() - 1, sb.length());
		sb.append(")").append(" ENGINE = MergeTree(auto_create_date,");
		if (ids != null && ids.size() > 0) {
			sb.append("(").append(String.join(",", ids)).append("),");
		} else {
			sb.append("( auto_id ),");
		}
		sb.append(granularity > 0 ? granularity : 8192).append(");");
		return executeQuery(sb.toString());
	}

	/**
	 * 如果库表不存在，则创建数据库
	 * 
	 * @param database 数据库
	 * @param table    数据表
	 * @param columns  数据列
	 * @param ids      主键字段名
	 */
	public boolean createTableIfNotExists(String database, String table, List<ClickHouseColumn> columns,
			Set<String> ids) throws Exception {
		if (StringUtils.hasLength(database)) {
			this.createDatabase(database);
		}
		if (!existsTable(database + "." + table)) {
			return createTable(database + "." + table, columns, ids, 8192);
		}
		return true;
	}

	/**
	 * 查询表字段
	 * 
	 * @param table 库名.表名
	 * @return Map<String,String> 表列
	 */
	public Map<String, String> describeTable(String table) {
		Map<String, String> columns = new HashMap<String, String>();
		String sql = "describe " + table;
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement prepareStatement = conn.prepareStatement(sql);
			ResultSet ret = prepareStatement.executeQuery();
			while (ret.next()) {
				columns.put(ret.getString("name"), ret.getString("type"));
			}
		} catch (Exception e) {
			log.error("Exception {}", e);
		}
		return columns;
	}

	/**
	 * 清空表
	 * 
	 * @param table 库名.表名
	 * @return boolean
	 * @throws Exception
	 * 
	 */
	public boolean truncatTable(String table) throws Exception {
		String sql = "truncate table " + table;
		return executeQuery(sql);
	}

	/**
	 * 删除表
	 * 
	 * @param table 库名.表名
	 * @return boolean
	 * @throws Exception
	 * 
	 */
	public boolean dropTable(String table) throws Exception {
		if (existsTable(table)) {
			String sql = "drop table " + table;
			return executeQuery(sql);
		}
		return true;
	}

	/**
	 * 重命名表名
	 * 
	 * @param oldName 原表名
	 * @param newName 新表名
	 * @return boolean
	 * @throws Exception
	 */
	public boolean renameTable(String oldName, String newName) throws Exception {
		String sql = "rename table " + oldName + " to " + newName;
		return executeQuery(sql);
	}

	/**
	 * 插入数据
	 * 
	 * @param table 库名.表名
	 * @param data  数据
	 * @return boolean
	 * @throws Exception
	 */
	public boolean insert(String table, List<ClickHouseColumn> columns) throws Exception {
		StringBuilder sb = new StringBuilder();
		sb.append("insert into ").append(table).append("(");
		for (int i = 0; i < columns.size(); i++) {
			sb.append(columns.get(i).getName()).append(",");
		}
		sb.append("auto_id");
		sb.append(") ").append("values (");
		for (int i = 0; i < columns.size(); i++) {
			sb.append(ClickhouseTypeUtil.perseValue(columns.get(i).getType(), columns.get(i).getValue()))
					.append(",");
		}
		sb.append(GlobalIDUtils.nextId());
		sb.append(")");
		return executeQuery(sb.toString());
	}

	/**
	 * 执行查询
	 * 
	 * @throws Exception
	 */
	public boolean executeQuery(String sql) throws Exception {
		Assert.hasText(sql, "SQL can't not be blank!");
		log.info(sql);
		try (Connection conn = dataSource.getConnection()) {
			PreparedStatement prepareStatement = conn.prepareStatement(sql);
			prepareStatement.executeQuery();
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

}

```

By 王志斌