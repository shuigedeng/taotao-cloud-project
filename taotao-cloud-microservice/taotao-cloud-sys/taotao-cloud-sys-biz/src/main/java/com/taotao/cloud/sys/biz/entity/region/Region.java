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
package com.taotao.cloud.sys.biz.entity.region;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 地区表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:52:30
 */
@Data
@EqualsAndHashCode(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = Region.TABLE_NAME)
@TableName(Region.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Region.TABLE_NAME, comment = "地区表")
public class Region extends BaseSuperEntity<Region, Long> {

	public static final String TABLE_NAME = "tt_region";

	/**
	 * 地区父节点
	 */
	@Column(name = "parent_id", columnDefinition = "bigint comment '地区父节点'")
	private Long parentId;

	/**
	 * 地区编码
	 */
	@Column(name = "code", columnDefinition = "varchar(255) not null comment '地区编码'")
	private String code;

	/**
	 * 地区名称
	 */
	@Column(name = "name", columnDefinition = "varchar(255) not null default '' comment '地区名称'")
	private String name;

	/**
	 * 地区级别（1:省份province,2:市city,3:区县district,4:街道street） "行政区划级别" + "country:国家" +
	 * "province:省份（直辖市会在province和city显示）" + "city:市（直辖市会在province和city显示）" + "district:区县" +
	 * "street:街道"
	 */
	@Column(name = "level", columnDefinition = "varchar(255) null comment '地区级别（1:省份province,2:市city,3:区县district,4:街道street）'")
	private String level;

	/**
	 * 城市编码
	 */
	@Column(name = "city_code", columnDefinition = "varchar(255) null comment '城市编码'")
	private String cityCode;

	/**
	 * 城市中心经度
	 */
	@Column(name = "lng", columnDefinition = "varchar(255) null comment '城市中心经度'")
	private String lng;

	/**
	 * 城市中心纬度
	 */
	@Column(name = "lat", columnDefinition = "varchar(255) null comment '城市中心纬度'")
	private String lat;

	@Column(name = "path", columnDefinition = "varchar(255) null comment '行政地区路径，类似：1，2，3'")
	private String path;

	@Column(name = "order_num", columnDefinition = "int not null default 0 comment '排序'")
	private Integer orderNum;

}
