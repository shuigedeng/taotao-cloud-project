

package com.taotao.cloud.auth.biz.strategy.remote;

import com.taotao.cloud.auth.biz.strategy.AbstractStrategyPermissionDetailsService;
import com.taotao.cloud.security.springsecurity.core.domain.HerodotusPermission;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>Description: 远程权限服务 </p>
 *
 * @author : gengwei.zheng
 * @date : 2022/4/1 19:01
 */
public class HerodotusRemotePermissionDetailsService extends AbstractStrategyPermissionDetailsService {

//    private final RemoteAuthorityDetailsService remoteAuthorityDetailsService;
//
//    public HerodotusRemotePermissionDetailsService(RemoteAuthorityDetailsService remoteAuthorityDetailsService) {
//        this.remoteAuthorityDetailsService = remoteAuthorityDetailsService;
//    }

	@Override
	public List<HerodotusPermission> findAll() {
//        Result<List<SysPermission>> result = remoteAuthorityDetailsService.findAll();
//        List<SysPermission> authorities = result.getData();
//        if (CollectionUtils.isNotEmpty(authorities)) {
//            return toEntities(authorities);
//        }
		return new ArrayList<>();
	}
}
