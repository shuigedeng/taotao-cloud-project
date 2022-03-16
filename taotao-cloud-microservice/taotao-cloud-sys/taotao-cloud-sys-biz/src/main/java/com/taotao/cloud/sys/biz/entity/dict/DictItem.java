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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 字典项表
 * // @SQLDelete(sql = "update sys_dict_item set del_flag = 1 where id = ?")
 * // @Where(clause ="del_flag = 1")
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:09:21
 */
@Entity
@Table(name = DictItem.TABLE_NAME)
@TableName(DictItem.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = DictItem.TABLE_NAME, comment = "字典项表")
public class DictItem extends BaseSuperEntity<DictItem,Long> {

	public static final String TABLE_NAME = "tt_dict_item";

	/**
	 * 字典id
	 *
	 * @see Dict
	 */
	@Column(name = "dict_id", nullable = false, columnDefinition = "bigint not null comment '字典id'")
	private Long dictId;

	/**
	 * 字典项文本
	 */
	@Column(name = "item_text", nullable = false, columnDefinition = "varchar(2000) not null comment '字典项文本'")
	private String itemText;

	/**
	 * 字典项值
	 */
	@Column(name = "item_value", nullable = false, columnDefinition = "varchar(2000) not null comment '字典项文本'")
	private String itemValue;

	/**
	 * 描述
	 */
	@Column(name = "description", columnDefinition = "varchar(255) comment '描述'")
	private String description;

	/**
	 * 状态 0不启用 1启用
	 */
	@Column(name = "status", nullable = false, columnDefinition = "int NOT NULL DEFAULT 1 comment ' 状态 0不启用 1启用'")
	private Integer status;

	/**
	 * 排序值
	 */
	@Column(name = "sort_num", columnDefinition = "int not null default 1 comment '排序值'")
	private Integer sortNum = 1;

	public DictItem() {
	}


	public Long getDictId() {
		return dictId;
	}

	public void setDictId(Long dictId) {
		this.dictId = dictId;
	}

	public String getItemText() {
		return itemText;
	}

	public void setItemText(String itemText) {
		this.itemText = itemText;
	}

	public String getItemValue() {
		return itemValue;
	}

	public void setItemValue(String itemValue) {
		this.itemValue = itemValue;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getSortNum() {
		return sortNum;
	}

	public void setSortNum(Integer sortNum) {
		this.sortNum = sortNum;
	}
}
