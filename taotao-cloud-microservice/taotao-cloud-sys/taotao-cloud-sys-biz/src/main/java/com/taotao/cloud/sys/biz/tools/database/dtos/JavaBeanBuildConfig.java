package com.taotao.cloud.sys.biz.tools.database.dtos;

import com.taotao.cloud.sys.biz.tools.database.service.meta.dtos.ActualTableName;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

public class JavaBeanBuildConfig {
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
     * 需要构建的数据表列表
     */
    private List<ActualTableName> tables = new ArrayList<>();

    /**
     * 是否添加 lombok
     */
    private boolean lombok;
    /**
     * 是否添加 swagger
     */
    private boolean swagger2;
    /**
     * 是否添加 jpa
     */
    private boolean persistence;
    /**
     * 是否实现 serializer
     */
    private boolean serializer;
    /**
     * 父类
     */
    private String supperClass;
    /**
     * 排除列
     */
    private List<String> exclude = new ArrayList<>();
    /**
     * 重命名策略名称
     */
    private String renameStrategy;
    /**
     * 包名
     */
    @NotNull
    private String packageName;

    public JavaBeanBuildConfig() {
    }

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

	public boolean isLombok() {
		return lombok;
	}

	public void setLombok(boolean lombok) {
		this.lombok = lombok;
	}

	public boolean isSwagger2() {
		return swagger2;
	}

	public void setSwagger2(boolean swagger2) {
		this.swagger2 = swagger2;
	}

	public boolean isPersistence() {
		return persistence;
	}

	public void setPersistence(boolean persistence) {
		this.persistence = persistence;
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

	public String getRenameStrategy() {
		return renameStrategy;
	}

	public void setRenameStrategy(String renameStrategy) {
		this.renameStrategy = renameStrategy;
	}

	public String getPackageName() {
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
}
