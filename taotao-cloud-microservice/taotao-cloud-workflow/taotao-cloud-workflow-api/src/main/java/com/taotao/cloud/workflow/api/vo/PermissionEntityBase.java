package com.taotao.cloud.workflow.api.vo;

import java.util.Date;
import lombok.Data;

@Data
public class PermissionEntityBase {

	/**
	 * 主键
	 */
	private String id;

	/**
	 * 名称
	 */
	private String fullName;

	/**
	 * 编码
	 */
	private String enCode;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * 排序码
	 */
	private Long sortCode;

	/**
	 * 扩展属性
	 */
	private String propertyJson;

	/**
	 * 有效标志
	 */
	private Integer enabledMark;

	/**
	 * 创建时间
	 */
	private Date creatorTime;

	/**
	 * 创建用户
	 */
	private String creatorUserId;

	/**
	 * 修改时间
	 */
	private Date lastModifyTime;

	/**
	 * 修改用户
	 */
	private String lastModifyUserId;

	/**
	 * 删除标志
	 */
	private Integer deleteMark;

	/**
	 * 删除时间
	 */
	private Date deleteTime;

	/**
	 * 删除用户
	 */
	private String deleteUserId;

}

