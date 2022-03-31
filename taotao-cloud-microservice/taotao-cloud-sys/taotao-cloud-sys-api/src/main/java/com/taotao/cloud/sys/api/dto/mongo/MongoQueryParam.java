package com.taotao.cloud.sys.api.dto.mongo;

import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MongoQueryParam {

	/**
	 * 连接名称
	 */
	private String connName;
	/**
	 * 数据库名
	 */
	private String databaseName;
	/**
	 * 集合名称
	 */
	private String collectionName;
	/**
	 * filter json
	 */
	private String filter;
	/**
	 * sort json
	 */
	private String sort;

}
