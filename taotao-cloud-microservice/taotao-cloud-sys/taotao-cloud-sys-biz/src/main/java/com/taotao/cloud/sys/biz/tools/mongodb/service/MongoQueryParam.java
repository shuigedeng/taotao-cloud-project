package com.taotao.cloud.sys.biz.tools.mongodb.service;

import com.alibaba.fastjson.JSONObject;
import org.bson.conversions.Bson;

import javax.validation.constraints.NotNull;

public class MongoQueryParam {
    /**
     * 连接名称
     */
    @NotNull
    private String connName;
    /**
     * 数据库名
     */
    @NotNull
    private String databaseName;
    /**
     * 集合名称
     */
    @NotNull
    private String collectionName;
    /**
     * filter json
     */
    private String filter;
    /**
     * sort json
     */
    private String sort;

	public String getConnName() {
		return connName;
	}

	public void setConnName(String connName) {
		this.connName = connName;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	public String getCollectionName() {
		return collectionName;
	}

	public void setCollectionName(String collectionName) {
		this.collectionName = collectionName;
	}

	public String getFilter() {
		return filter;
	}

	public void setFilter(String filter) {
		this.filter = filter;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
}
