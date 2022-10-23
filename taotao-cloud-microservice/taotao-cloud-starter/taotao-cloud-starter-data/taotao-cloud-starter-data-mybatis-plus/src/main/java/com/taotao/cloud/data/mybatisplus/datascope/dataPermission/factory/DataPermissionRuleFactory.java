package com.taotao.cloud.data.mybatisplus.datascope.dataPermission.factory;


import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.rule.DataPermissionRule;

import java.util.List;

/**
 * 数据权限规则工厂接口 管理容器中配置的数据权限规则
 */
public interface DataPermissionRuleFactory {

	/**
	 * 获取生效的数据权限规则
	 *
	 * @return 生效的数据权限规则数组
	 */
	List<DataPermissionRule> getDataPermissionRule();

}
