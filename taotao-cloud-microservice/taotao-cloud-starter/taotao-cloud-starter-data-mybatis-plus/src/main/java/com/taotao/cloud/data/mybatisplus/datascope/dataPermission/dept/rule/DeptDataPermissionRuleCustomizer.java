package com.taotao.cloud.data.mybatisplus.datascope.dataPermission.dept.rule;

/**
 * 数据权限规则的自定义配置接口 用于对数据权限的规则列进行配置Ω
 *
 *
 */
@FunctionalInterface
public interface DeptDataPermissionRuleCustomizer {

	/**
	 * 自定义该权限规则
	 * <p/>
	 * 1. 调用 {@link DeptDataPermissionRule#addDeptColumn(Class, String)} 方法，配置基于部门列的过滤规则
	 * <p/>
	 * 2. 调用 {@link DeptDataPermissionRule#addUserColumn(Class, String)} 方法，配置基于用户列的过滤规则
	 * @param rule 权限规则
	 */
	void customize(DeptDataPermissionRule rule);

}
