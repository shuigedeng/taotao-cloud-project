package com.taotao.cloud.workflow.api.vo;

import lombok.Data;

@Data
public class RoleEntity extends PermissionEntityBase {

	/**
	 * 角色类型
	 */
	private String type;

	/**
	 * 全局标识
	 */
	private Integer globalMark;

}
