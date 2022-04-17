/*
 * Copyright 2002-2021 the original author or authors.
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
package com.taotao.cloud.sys.biz.entity.dict;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.SuperEntity;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * SysDict
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:04
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = Dict.TABLE_NAME)
@TableName(Dict.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Dict.TABLE_NAME, comment = "字典表")
public class Dict extends BaseSuperEntity<Dict, Long> {

	public static final String TABLE_NAME = "tt_dict";

	/**
	 * 字典名称
	 */
	@Column(name = "dict_name", columnDefinition = "varchar(255) not null  comment '字典名称'")
	private String dictName;

	/**
	 * 字典编码
	 */
	@Column(name = "dict_code", unique = true, columnDefinition = "varchar(255) not null comment '字典编码'")
	private String dictCode;

	/**
	 * 描述
	 */
	@Column(name = "description", columnDefinition = "varchar(255) comment '描述'")
	private String description;

	/**
	 * 排序值
	 */
	@Column(name = "sort_num", columnDefinition = "int(11) not null default 0 comment '排序值'")
	private Integer sortNum;

	/**
	 * 备注信息
	 */
	@Column(name = "remark", columnDefinition = "varchar(255) comment '备注信息'")
	private String remark;
}
