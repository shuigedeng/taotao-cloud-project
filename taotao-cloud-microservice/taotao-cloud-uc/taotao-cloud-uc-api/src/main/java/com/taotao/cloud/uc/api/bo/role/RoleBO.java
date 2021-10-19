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
package com.taotao.cloud.uc.api.bo.role;

import io.swagger.v3.oas.annotations.media.Schema;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 角色查询对象
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 15:23:58
 */
public class RoleBO implements Serializable {

	@Serial
	private static final long serialVersionUID = 5126530068827085130L;

	/**
	 * id
	 */
	private Long id;
	/**
	 * 角色名称
	 */
	private String name;
	/**
	 * 角色code
	 */
	private String code;
	/**
	 * 备注
	 */
	private String remark;
	/**
	 * 创建时间
	 */
	private LocalDateTime createTime;
	/**
	 * 最后修改时间
	 */
	private LocalDateTime lastModifiedTime;

	public RoleBO() {
	}

	public RoleBO(Long id, String name, String code, String remark, LocalDateTime createTime,
		LocalDateTime lastModifiedTime) {
		this.id = id;
		this.name = name;
		this.code = code;
		this.remark = remark;
		this.createTime = createTime;
		this.lastModifiedTime = lastModifiedTime;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
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
}
