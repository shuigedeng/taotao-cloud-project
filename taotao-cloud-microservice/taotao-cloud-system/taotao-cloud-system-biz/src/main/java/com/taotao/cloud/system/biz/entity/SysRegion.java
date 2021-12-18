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
package com.taotao.cloud.system.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

/**
 * 地区表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:52:30
 */
@Entity
@Table(name = SysRegion.TABLE_NAME)
@TableName(SysRegion.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SysRegion.TABLE_NAME, comment = "地区表")
public class SysRegion extends SuperEntity<SysRegion,Long> {

	public static final String TABLE_NAME = "uc_sys_region";

	/**
	 * 地区编码
	 */
	@Column(name = "code", unique = true, nullable = false, columnDefinition = "varchar(255) not null comment '地区编码'")
	private String code;

	/**
	 * 地区名称
	 */
	@Column(name = "name", nullable = false, columnDefinition = "varchar(255) not null default '' comment '地区名称'")
	private String name;

	/**
	 * 地区级别（1:省份province,2:市city,3:区县district,4:街道street）
	 */
	@Column(name = "level", nullable = false, columnDefinition = "int not null comment '地区级别（1:省份province,2:市city,3:区县district,4:街道street）'")
	private Integer level;

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
	@Column(name = "lat", columnDefinition = "varchar(255) null comment '城市中心经度'")
	private String lat;

	/**
	 * 地区父节点
	 */
	@Column(name = "parent_id", columnDefinition = "bigint comment '地区父节点'")
	private Long parentId;

	@CreatedDate
	@Column(name = "create_time", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP comment '创建时间'")
	private LocalDateTime createTime;

	@LastModifiedDate
	@Column(name = "last_modified_time", columnDefinition = "TIMESTAMP comment '最后修改时间'")
	private LocalDateTime lastModifiedTime;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Integer getLevel() {
		return level;
	}

	public void setLevel(Integer level) {
		this.level = level;
	}

	public String getCityCode() {
		return cityCode;
	}

	public void setCityCode(String cityCode) {
		this.cityCode = cityCode;
	}

	public String getLng() {
		return lng;
	}

	public void setLng(String lng) {
		this.lng = lng;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public LocalDateTime getLastModifiedTime() {
		return lastModifiedTime;
	}

	public void setLastModifiedTime(LocalDateTime lastModifiedTime) {
		this.lastModifiedTime = lastModifiedTime;
	}

	public SysRegion() {
	}

	public SysRegion(String code, String name, Integer level, String cityCode, String lng,
		String lat, Long parentId, LocalDateTime createTime,
		LocalDateTime lastModifiedTime) {
		this.code = code;
		this.name = name;
		this.level = level;
		this.cityCode = cityCode;
		this.lng = lng;
		this.lat = lat;
		this.parentId = parentId;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}
}
