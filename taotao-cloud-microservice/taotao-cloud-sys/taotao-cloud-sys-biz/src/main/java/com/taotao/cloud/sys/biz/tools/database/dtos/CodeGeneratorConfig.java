package com.taotao.cloud.sys.biz.tools.database.dtos;


import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;
import java.util.ArrayList;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 项目代码生成配置
 */
public class CodeGeneratorConfig {

	/**
	 * 生成路径
	 */
	private String filePath;
	/**
	 * 项目名称
	 */
	private String projectName;
	/**
	 * 作者
	 */
	private String author;

	/**
	 * maven 配置
	 */
	@Valid
	private MavenConfig mavenConfig;
	/**
	 * 数据源配置
	 */
	@Valid
	private DataSourceConfig dataSourceConfig;
	/**
	 * 包路径配置
	 */
	@Valid
	private PackageConfig packageConfig;
	/**
	 * 全局配置
	 */
	@Valid
	private GlobalConfig globalConfig;
	/**
	 * 其它特性配置
	 */
	@Valid
	private FetureConfig fetureConfig;

	public static class MavenConfig {

		/**
		 * groupId
		 */
		@NotNull
		private String groupId;
		/**
		 * artifactId
		 */
		@NotNull
		private String artifactId;
		/**
		 * version
		 */
		@NotNull
		private String version = "1.0-SNAPSHOT";
		/**
		 * springBootVersion
		 */
		private String springBootVersion = "2.0.5.RELEASE";

		public String getGroupId() {
			return groupId;
		}

		public void setGroupId(String groupId) {
			this.groupId = groupId;
		}

		public String getArtifactId() {
			return artifactId;
		}

		public void setArtifactId(String artifactId) {
			this.artifactId = artifactId;
		}

		public String getVersion() {
			return version;
		}

		public void setVersion(String version) {
			this.version = version;
		}

		public String getSpringBootVersion() {
			return springBootVersion;
		}

		public void setSpringBootVersion(String springBootVersion) {
			this.springBootVersion = springBootVersion;
		}
	}

	public static class DataSourceConfig {

		/**
		 * 连接名
		 */
		@NotNull
		private String connName;
		/**
		 * 数据库 catalog
		 */
		private String catalog;
		/**
		 * 需要生成的数据表配置
		 */
		private List<ActualTableName> tables = new ArrayList<>();

		public String getConnName() {
			return connName;
		}

		public void setConnName(String connName) {
			this.connName = connName;
		}

		public String getCatalog() {
			return catalog;
		}

		public void setCatalog(String catalog) {
			this.catalog = catalog;
		}

		public List<ActualTableName> getTables() {
			return tables;
		}

		public void setTables(List<ActualTableName> tables) {
			this.tables = tables;
		}
	}

	public static class PackageConfig {

		/**
		 * 基础包
		 */
		private String parent;
		/**
		 * mapper 包路径
		 */
		private String mapper;
		/**
		 * service 包路径
		 */
		private String service;
		/**
		 * controller 包路径
		 */
		private String controller;

		/**
		 * entity 包路径
		 */
		private String entity;
		/**
		 * vo 包路径
		 */
		private String vo;
		/**
		 * dto 包路径
		 */
		private String dto;
		/**
		 * param 包路径
		 */
		private String param;

		public String getParent() {
			return parent;
		}

		public void setParent(String parent) {
			this.parent = parent;
		}

		public String getMapper() {
			return mapper;
		}

		public void setMapper(String mapper) {
			this.mapper = mapper;
		}

		public String getService() {
			return service;
		}

		public void setService(String service) {
			this.service = service;
		}

		public String getController() {
			return controller;
		}

		public void setController(String controller) {
			this.controller = controller;
		}

		public String getEntity() {
			return entity;
		}

		public void setEntity(String entity) {
			this.entity = entity;
		}

		public String getVo() {
			return vo;
		}

		public void setVo(String vo) {
			this.vo = vo;
		}

		public String getDto() {
			return dto;
		}

		public void setDto(String dto) {
			this.dto = dto;
		}

		public String getParam() {
			return param;
		}

		public void setParam(String param) {
			this.param = param;
		}
	}

	/**
	 * entity 配置 可以支持 swagger2 , lombok , persistenceApi
	 */
	public static class GlobalConfig {

		private boolean swagger2;
		private boolean lombok;
		private boolean persistence;
		/**
		 * id 列上的注解
		 */
		private String idAnnotation;
		private boolean serialVersionUID;
		/**
		 * 是否要实现序列化
		 */
		private boolean serializer;
		/**
		 * 实体超类
		 */
		private String supperClass;
		/**
		 * 排除列
		 */
		private List<String> exclude;
		/**
		 * 日期相关属性加 json 格式注解
		 */
		private boolean dateFormat;
		/**
		 * 忽略输出的字段列表
		 */
		private List<String> jsonIgnores = new ArrayList<>();

		/**
		 * mapper.xml 配置,基础列
		 */
		private boolean baseColumnList;
		/**
		 * mapper.xml 配置,BaseMap
		 */
		private boolean baseResultMap;

		/**
		 * 重命名策略
		 */
		private String renameStrategy;

		/**
		 * tkmbatisBaseMap
		 */
		private String mappers;

		public boolean isSwagger2() {
			return swagger2;
		}

		public void setSwagger2(boolean swagger2) {
			this.swagger2 = swagger2;
		}

		public boolean isLombok() {
			return lombok;
		}

		public void setLombok(boolean lombok) {
			this.lombok = lombok;
		}

		public boolean isPersistence() {
			return persistence;
		}

		public void setPersistence(boolean persistence) {
			this.persistence = persistence;
		}

		public String getIdAnnotation() {
			return idAnnotation;
		}

		public void setIdAnnotation(String idAnnotation) {
			this.idAnnotation = idAnnotation;
		}

		public boolean isSerialVersionUID() {
			return serialVersionUID;
		}

		public void setSerialVersionUID(boolean serialVersionUID) {
			this.serialVersionUID = serialVersionUID;
		}

		public boolean isSerializer() {
			return serializer;
		}

		public void setSerializer(boolean serializer) {
			this.serializer = serializer;
		}

		public String getSupperClass() {
			return supperClass;
		}

		public void setSupperClass(String supperClass) {
			this.supperClass = supperClass;
		}

		public List<String> getExclude() {
			return exclude;
		}

		public void setExclude(List<String> exclude) {
			this.exclude = exclude;
		}

		public boolean isDateFormat() {
			return dateFormat;
		}

		public void setDateFormat(boolean dateFormat) {
			this.dateFormat = dateFormat;
		}

		public List<String> getJsonIgnores() {
			return jsonIgnores;
		}

		public void setJsonIgnores(List<String> jsonIgnores) {
			this.jsonIgnores = jsonIgnores;
		}

		public boolean isBaseColumnList() {
			return baseColumnList;
		}

		public void setBaseColumnList(boolean baseColumnList) {
			this.baseColumnList = baseColumnList;
		}

		public boolean isBaseResultMap() {
			return baseResultMap;
		}

		public void setBaseResultMap(boolean baseResultMap) {
			this.baseResultMap = baseResultMap;
		}

		public String getRenameStrategy() {
			return renameStrategy;
		}

		public void setRenameStrategy(String renameStrategy) {
			this.renameStrategy = renameStrategy;
		}

		public String getMappers() {
			return mappers;
		}

		public void setMappers(String mappers) {
			this.mappers = mappers;
		}
	}

	private static class FetureConfig {

		/**
		 * 是否配置定时任务
		 */
		private boolean schedule;
		/**
		 * 是否配置线程池
		 */
		private boolean threadPool;
		/**
		 * 输入输出
		 */
		private boolean inputOutput;

		/**
		 * redis
		 */
		private boolean redis;
		/**
		 * mongo
		 */
		private boolean mongo;

		/**
		 * kafka
		 */
		private boolean kafka;
		/**
		 * rocketmq
		 */
		private boolean rocketmq;
		/**
		 * rabbitmq
		 */
		private boolean rabbitmq;

		/**
		 * mysql
		 */
		private boolean mysql;
		/**
		 * postgresql
		 */
		private boolean postgresql;

		public boolean isSchedule() {
			return schedule;
		}

		public void setSchedule(boolean schedule) {
			this.schedule = schedule;
		}

		public boolean isThreadPool() {
			return threadPool;
		}

		public void setThreadPool(boolean threadPool) {
			this.threadPool = threadPool;
		}

		public boolean isInputOutput() {
			return inputOutput;
		}

		public void setInputOutput(boolean inputOutput) {
			this.inputOutput = inputOutput;
		}

		public boolean isRedis() {
			return redis;
		}

		public void setRedis(boolean redis) {
			this.redis = redis;
		}

		public boolean isMongo() {
			return mongo;
		}

		public void setMongo(boolean mongo) {
			this.mongo = mongo;
		}

		public boolean isKafka() {
			return kafka;
		}

		public void setKafka(boolean kafka) {
			this.kafka = kafka;
		}

		public boolean isRocketmq() {
			return rocketmq;
		}

		public void setRocketmq(boolean rocketmq) {
			this.rocketmq = rocketmq;
		}

		public boolean isRabbitmq() {
			return rabbitmq;
		}

		public void setRabbitmq(boolean rabbitmq) {
			this.rabbitmq = rabbitmq;
		}

		public boolean isMysql() {
			return mysql;
		}

		public void setMysql(boolean mysql) {
			this.mysql = mysql;
		}

		public boolean isPostgresql() {
			return postgresql;
		}

		public void setPostgresql(boolean postgresql) {
			this.postgresql = postgresql;
		}
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public MavenConfig getMavenConfig() {
		return mavenConfig;
	}

	public void setMavenConfig(
		MavenConfig mavenConfig) {
		this.mavenConfig = mavenConfig;
	}

	public DataSourceConfig getDataSourceConfig() {
		return dataSourceConfig;
	}

	public void setDataSourceConfig(
		DataSourceConfig dataSourceConfig) {
		this.dataSourceConfig = dataSourceConfig;
	}

	public PackageConfig getPackageConfig() {
		return packageConfig;
	}

	public void setPackageConfig(
		PackageConfig packageConfig) {
		this.packageConfig = packageConfig;
	}

	public GlobalConfig getGlobalConfig() {
		return globalConfig;
	}

	public void setGlobalConfig(
		GlobalConfig globalConfig) {
		this.globalConfig = globalConfig;
	}

	public FetureConfig getFetureConfig() {
		return fetureConfig;
	}

	public void setFetureConfig(
		FetureConfig fetureConfig) {
		this.fetureConfig = fetureConfig;
	}
}
