
package com.taotao.cloud.auth.biz.strategy.local;

import com.taotao.cloud.auth.biz.strategy.AbstractStrategyPermissionDetailsService;
import com.taotao.cloud.auth.biz.strategy.user.SysPermission;
import com.taotao.cloud.security.springsecurity.core.domain.HerodotusPermission;
import org.apache.commons.collections4.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 本地权限服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/4/1 18:56
 */
public class HerodotusLocalPermissionDetailsService extends AbstractStrategyPermissionDetailsService {

	private final SysPermissionService sysPermissionService;

	public HerodotusLocalPermissionDetailsService(SysPermissionService sysPermissionService) {
		this.sysPermissionService = sysPermissionService;
	}

	@Override
	public List<HerodotusPermission> findAll() {
		List<SysPermission> authorities = sysPermissionService.findAll();

		if (CollectionUtils.isNotEmpty(authorities)) {
			return toEntities(authorities);
		}

		return new ArrayList<>();
	}
}
