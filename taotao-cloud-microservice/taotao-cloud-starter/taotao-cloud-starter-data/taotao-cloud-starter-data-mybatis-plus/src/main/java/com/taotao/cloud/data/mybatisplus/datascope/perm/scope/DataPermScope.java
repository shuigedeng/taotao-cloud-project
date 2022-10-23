package com.taotao.cloud.data.mybatisplus.datascope.perm.scope;

import com.taotao.cloud.data.mybatisplus.datascope.perm.code.DataScopeEnum;

import java.util.Set;

/**
* 数据权限范围参数
*/
public class DataPermScope {

    /**
     * 范围类型 自身,部门,人员,部门和人员
     */
    private DataScopeEnum scopeType;

    /** 对应部门ID集合 */
    private Set<Long> deptScopeIds;

    /** 对应用户ID集合 */
    private Set<Long> UserScopeIds;

	public DataScopeEnum getScopeType() {
		return scopeType;
	}

	public void setScopeType(DataScopeEnum scopeType) {
		this.scopeType = scopeType;
	}

	public Set<Long> getDeptScopeIds() {
		return deptScopeIds;
	}

	public void setDeptScopeIds(Set<Long> deptScopeIds) {
		this.deptScopeIds = deptScopeIds;
	}

	public Set<Long> getUserScopeIds() {
		return UserScopeIds;
	}

	public void setUserScopeIds(Set<Long> userScopeIds) {
		UserScopeIds = userScopeIds;
	}
}
