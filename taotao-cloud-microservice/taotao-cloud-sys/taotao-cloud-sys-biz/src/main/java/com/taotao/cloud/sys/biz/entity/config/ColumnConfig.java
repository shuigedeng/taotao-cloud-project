/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.taotao.cloud.sys.biz.entity.config;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.sys.biz.utils.GenUtil;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 字段配置表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-15 09:20:23
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = ColumnConfig.TABLE_NAME)
@TableName(ColumnConfig.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = ColumnConfig.TABLE_NAME, comment = "字段配置表")
public class ColumnConfig extends BaseSuperEntity<ColumnConfig, Long> {

	public static final String TABLE_NAME = "tt_column_config";

	@Column(name = "table_name", columnDefinition = "varchar(64) not null comment '表名称'")
	private String tableName;

	/**
	 * 数据库字段名称
	 */
	@Column(name = "column_name", columnDefinition = "varchar(64) not null comment '数据库字段名称'")
	private String columnName;

	/**
	 * 数据库字段类型
	 */
	@Column(name = "column_type", columnDefinition = "varchar(64) not null comment '数据库字段类型'")
	private String columnType;

	/**
	 * 数据库字段键类型
	 */
	@Column(name = "key_type", columnDefinition = "varchar(64) not null comment '数据库字段键类型'")
	private String keyType;

	/**
	 * 字段额外的参数
	 */
	@Column(name = "extra", columnDefinition = "varchar(128) not null comment '字段额外的参数'")
	private String extra;

	/**
	 * 数据库字段描述
	 */
	@Column(name = "remark", columnDefinition = "varchar(256) not null comment '数据库字段描述'")
	private String remark;

	/**
	 * 必填
	 */
	@Column(name = "not_null", columnDefinition = "boolean default false comment '必填'")
	private Boolean notNull;

	/**
	 * 是否在列表显示
	 */
	@Column(name = "list_show", columnDefinition = "boolean default false comment '是否在列表显示'")
	private Boolean listShow;

	/**
	 * 是否表单显示
	 */
	@Column(name = "form_show", columnDefinition = "boolean default false comment '是否表单显示'")
	private Boolean formShow;

	/**
	 * 表单类型
	 */
	@Column(name = "form_type", columnDefinition = "varchar(64) not null comment '表单类型'")
	private String formType;

	/**
	 * 查询 1:模糊 2：精确
	 */
	@Column(name = "query_type", columnDefinition = "varchar(64) not null comment '查询 1:模糊 2：精确'")
	private String queryType;

	/**
	 * 字典名称
	 */
	@Column(name = "dict_name", columnDefinition = "varchar(64) not null comment '字典名称'")
	private String dictName;

	/**
	 * 日期注解
	 */
	@Column(name = "date_annotation", columnDefinition = "varchar(64) not null comment '日期注解'")
	private String dateAnnotation;


	public ColumnConfig(String tableName, String columnName, Boolean notNull, String columnType,
		String remark, String keyType, String extra) {
		this.tableName = tableName;
		this.columnName = columnName;
		this.columnType = columnType;
		this.keyType = keyType;
		this.extra = extra;
		this.notNull = notNull;
		if (GenUtil.PK.equalsIgnoreCase(keyType) && GenUtil.EXTRA.equalsIgnoreCase(extra)) {
		    this.notNull = false;
		}
		this.remark = remark;
		this.listShow = true;
		this.formShow = true;
	}
}
