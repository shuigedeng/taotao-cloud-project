package com.taotao.cloud.workflow.api.vo;

import lombok.Data;

@Data
public class OrganizeEntity extends PermissionEntityBase {

	/**
	 * 机构上级
	 */
	private String parentId;

	/**
	 * 机构分类
	 */
	private String category;

	/**
	 * 机构主管
	 */
	private String manager;

	/**
	 * 父级组织
	 */
	private String organizeIdTree;

}
