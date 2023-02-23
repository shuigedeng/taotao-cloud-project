package com.taotao.cloud.workflow.api.vo;

import java.util.Date;
import lombok.Data;

@Data
public class UserRelationEntity {

	/**
	 * 关系主键
	 */
	private String id;

	/**
	 * 用户主键
	 */
	private String userId;

	/**
	 * 对象类型
	 */
	private String objectType;

	/**
	 * 对象主键
	 */
	private String objectId;

	/**
	 * 排序码
	 */
	private Long sortCode;

	/**
	 * 创建时间
	 */
	private Date creatorTime;

	/**
	 * 创建用户
	 */
	private String creatorUserId;
}
