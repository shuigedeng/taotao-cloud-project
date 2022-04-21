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
package com.taotao.cloud.data.mybatis.plus.datascope;

import java.util.Map;
import java.util.Objects;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 数据权限查询参数 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:14
 */
public class DataScope extends HashMap {

    /**
     * 限制范围的字段名称
     */
    private String scopeFiledName = "dept_id";

    /**
     * 具体的数据范围
     */
    private List<Integer> deptIds = new ArrayList<>();

	/**
	 * 限制范围的字段名称 （除个人外）
	 */
	private String scopeName = "org_id";
	/**
	 * 限制范围为个人时的字段名称
	 */
	private String selfScopeName = "created_by";
	/**
	 * 当前用户ID
	 */
	private Long userId;

	/**
	 * 具体的数据范围
	 */
	private List<Long> orgIds;

	public String getScopeName() {
		return scopeName;
	}

	public void setScopeName(String scopeName) {
		this.scopeName = scopeName;
	}

	public String getSelfScopeName() {
		return selfScopeName;
	}

	public void setSelfScopeName(String selfScopeName) {
		this.selfScopeName = selfScopeName;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public List<Long> getOrgIds() {
		return orgIds;
	}

	public void setOrgIds(List<Long> orgIds) {
		this.orgIds = orgIds;
	}

	public DataScope(int initialCapacity, float loadFactor, String scopeFiledName,
		List<Integer> deptIds) {
		super(initialCapacity, loadFactor);
		this.scopeFiledName = scopeFiledName;
		this.deptIds = deptIds;
	}

	public DataScope(int initialCapacity, String scopeFiledName,
		List<Integer> deptIds) {
		super(initialCapacity);
		this.scopeFiledName = scopeFiledName;
		this.deptIds = deptIds;
	}

	public DataScope(String scopeFiledName, List<Integer> deptIds) {
		this.scopeFiledName = scopeFiledName;
		this.deptIds = deptIds;
	}

	public DataScope(Map m, String scopeFiledName, List<Integer> deptIds) {
		super(m);
		this.scopeFiledName = scopeFiledName;
		this.deptIds = deptIds;
	}

	@Override
	public String toString() {
		return "DataScope{" +
			"scopeFiledName='" + scopeFiledName + '\'' +
			", deptIds=" + deptIds +
			"} " + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		DataScope dataScope = (DataScope) o;
		return Objects.equals(scopeFiledName, dataScope.scopeFiledName)
			&& Objects.equals(deptIds, dataScope.deptIds);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), scopeFiledName, deptIds);
	}

	public String getScopeFiledName() {
		return scopeFiledName;
	}

	public void setScopeFiledName(String scopeFiledName) {
		this.scopeFiledName = scopeFiledName;
	}

	public List<Integer> getDeptIds() {
		return deptIds;
	}

	public void setDeptIds(List<Integer> deptIds) {
		this.deptIds = deptIds;
	}
}
