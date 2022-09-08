package com.taotao.cloud.data.mybatisplus.datascope.dataPermission.dept.service;

import org.springframework.boot.autoconfigure.AutoConfiguration;

/**
 * 基于部门的数据权限 Framework Service
 *
 * @author fxz
 */
@AutoConfiguration
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
