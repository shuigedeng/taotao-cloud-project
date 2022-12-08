package com.taotao.cloud.workflow.api.vo;

import java.util.Date;
import lombok.Data;

@Data
public class AuthorizeEntity {

	/**
	 * 权限主键
	 */
	private String id;

	/**
	 * 项目类型
	 */
	private String itemType;

	/**
	 * 项目主键
	 */
	private String itemId;

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
