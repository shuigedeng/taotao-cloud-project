package com.taotao.cloud.workflow.api.vo;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

@Data
public class OrganizeRelationEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * 自然主键
	 */
	private String id;

	/**
	 *
	 */
	private String organizeId;

	/**
	 * 对象类型（岗位：position、角色：role）
	 */
	private String objectType;

	/**
	 * 对象主键
	 */
	private String objectId;

	/**
	 * 创建时间
	 */
	private Date creatorTime;

	/**
	 * 创建用户
	 */
	private String creatorUserId;

}
