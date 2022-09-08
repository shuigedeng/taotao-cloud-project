package com.taotao.cloud.data.mybatisplus.datascope.dataPermission.dept.config;

import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.dept.rule.DeptDataPermissionRule;
import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.dept.rule.DeptDataPermissionRuleCustomizer;
import com.taotao.cloud.data.mybatisplus.datascope.dataPermission.dept.service.DeptDataPermissionFrameworkService;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 系统内置基于部门的数据权限 AutoConfiguration
 *
 * @author fxz
 */
@AutoConfiguration
@ConditionalOnBean(value = { DeptDataPermissionFrameworkService.class})
public class DeptDataPermissionAutoConfiguration {

	@Bean
	public DeptDataPermissionRule deptDataPermissionRule(DeptDataPermissionFrameworkService service,
                                                         List<DeptDataPermissionRuleCustomizer> customizers) {
		// 创建 DeptDataPermissionRule 对象
		DeptDataPermissionRule rule = new DeptDataPermissionRule(service);
		// 补全表配置
		customizers.forEach(customizer -> customizer.customize(rule));
		return rule;
	}

}
