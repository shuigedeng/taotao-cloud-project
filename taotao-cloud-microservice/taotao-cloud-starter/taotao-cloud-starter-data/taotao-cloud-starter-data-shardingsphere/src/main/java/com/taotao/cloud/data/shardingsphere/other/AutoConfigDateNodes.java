package com.taotao.cloud.data.shardingsphere.other;

import com.taotao.cloud.common.utils.log.LogUtils;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Map;
import javax.sql.DataSource;
import org.apache.commons.lang3.StringUtils;
import org.apache.shardingsphere.driver.jdbc.core.datasource.ShardingSphereDataSource;
import org.apache.shardingsphere.infra.config.rule.RuleConfiguration;
import org.apache.shardingsphere.mode.manager.ContextManager;
import org.apache.shardingsphere.sharding.algorithm.config.AlgorithmProvidedShardingRuleConfiguration;
import org.apache.shardingsphere.sharding.api.config.rule.ShardingTableRuleConfiguration;
import org.apache.shardingsphere.sharding.spi.ShardingAlgorithm;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class AutoConfigDateNodes implements ApplicationRunner {

	private final DataSource dataSource;

	private final static Integer DAY = 15;

	public AutoConfigDateNodes(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	public void run(ApplicationArguments args) {
		LogUtils.info("进行自动更新sharding中物理表配置");
		refreshShardingNodes((ShardingSphereDataSource) dataSource);

	}

	/**
	 * 物理表更新逻辑，可使用定时器每天更新物理表
	 *
	 * @param ds
	 */
	private void refreshShardingNodes(ShardingSphereDataSource ds) {

		// 获取context信息
		ContextManager contextManager = getContextManager(ds);

		Collection<RuleConfiguration> addRuleConfigs = new LinkedList<>();
		// 获取配置的分片信息
		Collection<RuleConfiguration> configurations = contextManager.getMetaDataContexts()
			.getMetaData().getDatabases()
			.get("logic_db").getRuleMetaData().getConfigurations();

		for (RuleConfiguration configuration : configurations) {
			// 处理分片信息
			AlgorithmProvidedShardingRuleConfiguration algorithmProvidedShardingRuleConfiguration = (AlgorithmProvidedShardingRuleConfiguration) configuration;
			Map<String, ShardingAlgorithm> shardingAlgorithms = algorithmProvidedShardingRuleConfiguration
				.getShardingAlgorithms();
			AlgorithmProvidedShardingRuleConfiguration addRuleConfiguration = new AlgorithmProvidedShardingRuleConfiguration();
			Collection<ShardingTableRuleConfiguration> addTableConfigurations = new LinkedList<>();
			// 获取noed列表配置
			for (ShardingTableRuleConfiguration shardingTableRuleConfiguration : algorithmProvidedShardingRuleConfiguration
				.getTables()) {
				// 处理node信息，建表，自动刷新node都在此方法处理
				ShardingTableRuleConfiguration addTableConfiguration = createTableRule(
					shardingTableRuleConfiguration,
					shardingAlgorithms, DAY);
				addTableConfigurations.add(addTableConfiguration);
			}
			addRuleConfiguration.setTables(addTableConfigurations);
			addRuleConfiguration.setAutoTables(
				algorithmProvidedShardingRuleConfiguration.getAutoTables());
			addRuleConfiguration
				.setBindingTableGroups(
					algorithmProvidedShardingRuleConfiguration.getBindingTableGroups());
			addRuleConfiguration.setBroadcastTables(
				algorithmProvidedShardingRuleConfiguration.getBroadcastTables());
			addRuleConfiguration.setDefaultDatabaseShardingStrategy(
				algorithmProvidedShardingRuleConfiguration.getDefaultDatabaseShardingStrategy());
			addRuleConfiguration.setDefaultTableShardingStrategy(
				algorithmProvidedShardingRuleConfiguration.getDefaultTableShardingStrategy());
			addRuleConfiguration.setDefaultKeyGenerateStrategy(
				algorithmProvidedShardingRuleConfiguration.getDefaultKeyGenerateStrategy());
			addRuleConfiguration
				.setDefaultShardingColumn(
					algorithmProvidedShardingRuleConfiguration.getDefaultShardingColumn());
			addRuleConfiguration.setShardingAlgorithms(shardingAlgorithms);
			addRuleConfiguration.setKeyGenerators(
				algorithmProvidedShardingRuleConfiguration.getKeyGenerators());
			addRuleConfigs.add(addRuleConfiguration);
		}
		contextManager.alterRuleConfiguration("logic_db", addRuleConfigs);
		// 将新数据添加进contex中
		setContextManager(ds, contextManager);
	}

	private ContextManager getContextManager(ShardingSphereDataSource dataSource) {
		try {
			Field contextManagerField = dataSource.getClass().getDeclaredField("contextManager");
			contextManagerField.setAccessible(true);
			return (ContextManager) contextManagerField.get(dataSource);
		} catch (Exception e) {
			throw new RuntimeException("系统异常");
		}

	}

	private void setContextManager(ShardingSphereDataSource dataSource, ContextManager manager) {
		try {
			Field contextManagerField = dataSource.getClass().getDeclaredField("contextManager");
			contextManagerField.setAccessible(true);
			contextManagerField.set(dataSource, manager);
		} catch (Exception e) {
			throw new RuntimeException("系统异常");
		}

	}

	private ShardingTableRuleConfiguration createTableRule(
		ShardingTableRuleConfiguration shardingTableRuleConfiguration,
		Map<String, ShardingAlgorithm> shardingAlgorithms, Integer day) {

		ShardingAlgorithm shardingAlgorithm = shardingAlgorithms
			.get(shardingTableRuleConfiguration.getTableShardingStrategy()
				.getShardingAlgorithmName());
		// 获取当前分表使用的自定义类的全路径
		String algorithmClassName = shardingAlgorithm.getProps().getProperty("algorithmClassName");
		if (StringUtils.isBlank(algorithmClassName)) {
			return shardingTableRuleConfiguration;
		}
		// 通过反射执行‘分表自定义类’的构建物理表方法
		String logicTable = shardingTableRuleConfiguration.getLogicTable();
		String actualDataNodes = "";
		try {
			// 反射拿到自定分表类，node节点信息，建表都在此类中处理，反射调用
			Class<?> aClass = Class.forName(algorithmClassName);
			Object o = aClass.newInstance();
			// 构建节点信息
			Method buildNodes = aClass.getMethod("buildNodes", String.class, Integer.class);
			// 执行得物理表的配置结果
			actualDataNodes = (String) buildNodes.invoke(o, logicTable, day);

			// 执行建表逻辑
			Method createTables = aClass.getMethod("createTables", ShardingSphereDataSource.class,
				String.class,
				Integer.class);

			createTables.invoke(o, (ShardingSphereDataSource) dataSource, logicTable, 5);
		} catch (Exception e) {
			throw new RuntimeException();
		}

		if (StringUtils.isBlank(actualDataNodes)) {
			return shardingTableRuleConfiguration;
		}

		ShardingTableRuleConfiguration addTableConfiguration = new ShardingTableRuleConfiguration(
			logicTable,
			actualDataNodes);

		addTableConfiguration.setTableShardingStrategy(
			shardingTableRuleConfiguration.getTableShardingStrategy());
		addTableConfiguration.setDatabaseShardingStrategy(
			shardingTableRuleConfiguration.getDatabaseShardingStrategy());
		addTableConfiguration.setKeyGenerateStrategy(
			shardingTableRuleConfiguration.getKeyGenerateStrategy());
		return addTableConfiguration;

	}

}
