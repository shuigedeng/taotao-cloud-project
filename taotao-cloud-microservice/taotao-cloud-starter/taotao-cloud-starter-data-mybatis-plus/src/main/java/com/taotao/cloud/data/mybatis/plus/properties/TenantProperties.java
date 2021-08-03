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
package com.taotao.cloud.data.mybatis.plus.properties;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 多租户Properties
 *
 * @author shuigedeng
 * @version 1.0.0
 * @since 2020/5/2 11:19
 */
@RefreshScope
@ConfigurationProperties(prefix = TenantProperties.PREFIX)
public class TenantProperties {

	public static final String PREFIX = "taotao.cloud.data.mybatis-plus.tenant";

	/**
	 * 是否开启多租户
	 */
	private Boolean enabled = false;

	/**
	 * 是否开启数据权限
	 */
	private Boolean dataScope = false;

	/**
	 * 配置不进行多租户隔离的sql 需要配置mapper的全路径如：com.central.user.mapper.SysUserMapper.findList
	 */
	private List<String> ignoreSqlList = new ArrayList<>();

	/**
	 * 需要排除的多租户的表
	 */
	private List<String> ignoreTables = Arrays.asList(
		"mate_sys_menu",
		"mate_sys_dict",
		"mate_sys_client",
		"mate_sys_tenant",
		"mate_sys_role_permission",
		"mate_sys_config",
		"mate_sys_data_source",
		"mate_sys_attachment");

	/**
	 * 多租户字段名称
	 */
	private String column = "tenant_id";

	/**
	 * 排除不进行租户隔离的sql 样例全路径：vip.mate.system.mapper.UserMapper.findList
	 */
	private List<String> ignoreSqls = new ArrayList<>();

	public Boolean getEnabled() {
		return enabled;
	}

	public Boolean getDataScope() {
		return dataScope;
	}

	public void setDataScope(Boolean dataScope) {
		this.dataScope = dataScope;
	}

	public List<String> getIgnoreSqls() {
		return ignoreSqls;
	}

	public void setIgnoreSqls(List<String> ignoreSqls) {
		this.ignoreSqls = ignoreSqls;
	}

	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}

	public List<String> getIgnoreTables() {
		return ignoreTables;
	}

	public void setIgnoreTables(List<String> ignoreTables) {
		this.ignoreTables = ignoreTables;
	}

	public List<String> getIgnoreSqlList() {
		return ignoreSqlList;
	}

	public void setIgnoreSqlList(List<String> ignoreSqlList) {
		this.ignoreSqlList = ignoreSqlList;
	}

	public String getColumn() {
		return column;
	}

	public void setColumn(String column) {
		this.column = column;
	}
}
