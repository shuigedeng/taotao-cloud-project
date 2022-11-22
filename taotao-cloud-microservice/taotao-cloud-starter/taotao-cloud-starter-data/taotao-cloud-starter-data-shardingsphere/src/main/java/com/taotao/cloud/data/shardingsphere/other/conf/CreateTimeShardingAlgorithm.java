package com.taotao.cloud.data.shardingsphere.other.conf;


import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;

/**
 * @author GDS
 * <p>
 * 跟据创建时间分表的接口
 */

public interface CreateTimeShardingAlgorithm {

	/**
	 * 将日期转换为分表的后缀格式
	 *
	 * @param date
	 * @return
	 */
	String buildNodesSuffix(LocalDate date);

	/**
	 * 分表的阶梯方式，物理表每次递增规则,返回date的后一天或一个月或一年
	 *
	 * @param date
	 * @return
	 */
	LocalDate buildNodesAfterDate(LocalDate date);

	/**
	 * 分表的阶梯方式，物理表每次递减规则,返回date的前一天或一个月或一年
	 *
	 * @param date
	 * @return
	 */
	LocalDate buildNodesBeforeDate(LocalDate date);

	/**
	 * 构建可用表的nodes
	 *
	 * @param tableName 表名
	 * @param count     需要表的数量
	 * @return 物理表的集合，用，号拼接
	 */
	default String buildNodes(String tableName, Integer count) {
		List<String> tableNameList = new ArrayList<>();

		LocalDate today = LocalDate.now();

		for (int i = 0; i < count; i++) {
			tableNameList.add("db0." + tableName + "_${'" + buildNodesSuffix(today) + "'}");
			today = buildNodesBeforeDate(today);
		}
		return StringUtils.join(tableNameList, ",");
	}

	default void createTables(ShardingSphereDataSource dataSource, String tableName,
		Integer count) {

		try (Connection connection = dataSource.getConnection(); Statement statement = connection.createStatement()) {

			LocalDate today = LocalDate.now();
			String oldTableName = "";
			String newTableName = "";
			for (int i = 0; i < count; i++) {
				oldTableName = tableName + "_" + buildNodesSuffix(today);
				today = buildNodesAfterDate(today);
				newTableName = tableName + "_" + buildNodesSuffix(today);
				statement.execute(
					"create table IF NOT EXISTS `" + newTableName + "` like  `" + oldTableName
						+ "`");
			}
		} catch (SQLException throwables) {
			throw new RuntimeException("建表失败");
		}

	}

}
