package com.taotao.cloud.data.mybatis.plus.datascope.dataPermission.factory;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.ArrayUtil;
import com.fxz.common.dataPermission.annotation.DataPermission;
import com.fxz.common.dataPermission.aop.DataPermissionContextHolder;
import com.fxz.common.dataPermission.rule.DataPermissionRule;
import lombok.RequiredArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 默认的 DataPermissionRuleFactoryImpl 实现类 支持通过 {@link DataPermissionContextHolder} 过滤数据权限
 *
 * @author fxz
 */
@RequiredArgsConstructor
public class DataPermissionRuleFactoryImpl implements DataPermissionRuleFactory {

	/**
	 * 数据权限规则数组
	 */
	private final List<DataPermissionRule> rules;

	@Override
	public List<DataPermissionRule> getDataPermissionRules() {
		return rules;
	}

	/**
	 * mappedStatementId 参数，暂时没有用。以后，可以基于 mappedStatementId + DataPermission 进行缓存
	 * @param mappedStatementId 指定 Mapper 的编号
	 * @return 生效的数据权限规则
	 */
	@Override
	public List<DataPermissionRule> getDataPermissionRule(String mappedStatementId) {
		// 1. 无数据权限
		if (CollUtil.isEmpty(rules)) {
			return Collections.emptyList();
		}
		// 2. 未配置，则默认开启
		DataPermission dataPermission = DataPermissionContextHolder.get();
		if (dataPermission == null) {
			return rules;
		}
		// 3. 已配置，但禁用
		if (!dataPermission.enable()) {
			return Collections.emptyList();
		}

		// 4. 已配置，只选择部分规则
		if (ArrayUtil.isNotEmpty(dataPermission.includeRules())) {
			return rules.stream().filter(rule -> ArrayUtil.contains(dataPermission.includeRules(), rule.getClass()))
					.collect(Collectors.toList());
		}
		// 5. 已配置，只排除部分规则
		if (ArrayUtil.isNotEmpty(dataPermission.excludeRules())) {
			return rules.stream().filter(rule -> !ArrayUtil.contains(dataPermission.excludeRules(), rule.getClass()))
					.collect(Collectors.toList());
		}
		// 6. 已配置，全部规则
		return rules;
	}

}
