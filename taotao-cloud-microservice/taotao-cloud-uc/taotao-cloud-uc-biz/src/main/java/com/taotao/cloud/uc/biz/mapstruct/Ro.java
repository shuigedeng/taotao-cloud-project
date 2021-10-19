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
package com.taotao.cloud.uc.biz.mapstruct;

import javax.persistence.Column;

/**
 * 角色表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2021-10-09 21:52:30
 */
public record Ro<T>(
	@Column(name = "name", nullable = false, columnDefinition = "varchar(32) not null comment '角色名称'")
	String name,
	@Column(name = "code", unique = true, nullable = false, columnDefinition = "varchar(32) not null comment '角色标识'")
	String code,
	@Column(name = "remark", columnDefinition = "varchar(255) comment '备注'")
	String remark,
	@Column(name = "tenant_id", unique = true, columnDefinition = "varchar(32) COMMENT '租户id'")
	String tenantId) {

	public static final String TABLE_NAME = "uc_sys_role";
}

