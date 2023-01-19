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
package com.taotao.cloud.sys.biz.model.entity.dict;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import jakarta.annotation.PreDestroy;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.PostLoad;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;
import org.hibernate.Hibernate;

/**
 * 字典表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:10:04
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = Dict.TABLE_NAME)
@TableName(Dict.TABLE_NAME)
@EntityListeners({Dict.DictEntityListener.class})
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

	@Builder
	public Dict(Long id, LocalDateTime createTime, Long createBy, LocalDateTime updateTime,
		Long updateBy, Integer version, Boolean delFlag, String dictName, String dictCode,
		String description, Integer sortNum, String remark) {
		super(id, createTime, createBy, updateTime, updateBy, version, delFlag);
		this.dictName = dictName;
		this.dictCode = dictCode;
		this.description = description;
		this.sortNum = sortNum;
		this.remark = remark;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Dict dict = (Dict) o;
		return getId() != null && Objects.equals(getId(), dict.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}


	public static class DictEntityListener {

		/**
		 * 在新实体持久化之前（添加到EntityManager）
		 *
		 * @param object 对象
		 * @since 2022-10-21 11:59:54
		 */
		@PrePersist
		public void prePersist(Object object) {
			LogUtils.info(" DictEntityListener prePersis: {}", object);
		}

		/**
		 * 在数据库中存储新实体（在commit或期间flush）
		 *
		 * @param object 对象
		 * @since 2022-10-21 11:59:54
		 */
		@PostPersist
		public void postPersist(Object object) {
			LogUtils.info("DictEntityListener postPersist: {}", object);
		}

		/**
		 * 从数据库中检索实体后。
		 *
		 * @param object 对象
		 * @since 2022-10-21 11:59:55
		 */
		@PostLoad
		public void postLoad(Object object) {
			LogUtils.info("DictEntityListener postLoad: {}", object);
		}

		/**
		 * 当一个实体被识别为被修改时EntityManager
		 *
		 * @param object 对象
		 * @since 2022-10-21 11:59:54
		 */
		@PreUpdate
		public void preUpdate(Object object) {
			LogUtils.info("DictEntityListener preUpdate: {}", object);
		}


		/**
		 * 更新数据库中的实体（在commit或期间flush）
		 *
		 * @param object 对象
		 * @since 2022-10-21 11:59:54
		 */
		@PostUpdate
		public void postUpdate(Object object) {
			LogUtils.info("DictEntityListener postUpdate: {}", object);
		}


		/**
		 * 在EntityManager中标记要删除的实体时
		 *
		 * @param object 对象
		 * @since 2022-10-21 11:59:54
		 */
		@PreRemove
		public void preRemove(Object object) {
			LogUtils.info("DictEntityListener preRemove: {}", object);
		}

		/**
		 * 从数据库中删除实体（在commit或期间flush）
		 *
		 * @param object 对象
		 * @since 2022-10-21 11:59:55
		 */
		@PostRemove
		public void postRemove(Object object) {
			LogUtils.info("DictEntityListener postRemove: {}", object);
		}

		/**
		 * 前摧毁
		 *
		 * @param object 对象
		 * @since 2022-10-21 11:59:54
		 */
		@PreDestroy
		public void preDestroy(Object object) {
			LogUtils.info("DictEntityListener preDestroy: {}", object);
		}


	}

}

