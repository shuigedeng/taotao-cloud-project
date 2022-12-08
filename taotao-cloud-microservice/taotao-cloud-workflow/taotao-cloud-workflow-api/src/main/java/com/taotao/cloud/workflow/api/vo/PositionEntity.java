package com.taotao.cloud.workflow.api.vo;

import lombok.Data;

@Data
public class PositionEntity extends PermissionEntityBase {

	/**
	 * 岗位类型
	 */
	private String type;

	/**
	 * 机构主键
	 */
	private String organizeId;
}
