package com.taotao.cloud.data.mybatisplus.datascope.dataPermission.dept.service;

import com.fxz.common.core.entity.DeptDataPermissionRespDTO;
import com.fxz.common.security.entity.FxzAuthUser;
import com.fxz.system.feign.RemoteRoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于部门的数据权限 Framework Service
 *
 * @author fxz
 */
@Slf4j
@RequiredArgsConstructor
public class DeptDataPermissionFrameworkService {

	private final RemoteRoleService remoteRoleService;

	/**
	 * 获得登陆用户的部门数据权限
	 * @param loginUser 登陆用户
	 * @return 部门数据权限
	 */
	public DeptDataPermissionRespDTO getDeptDataPermission(FxzAuthUser loginUser) {
		log.info("查询用户:{}的数据权限", loginUser);
		DeptDataPermissionRespDTO respDTO = remoteRoleService.getDataPermission().getData();
		log.info("数据权限是:{}", respDTO);
		return respDTO;
	}

}
