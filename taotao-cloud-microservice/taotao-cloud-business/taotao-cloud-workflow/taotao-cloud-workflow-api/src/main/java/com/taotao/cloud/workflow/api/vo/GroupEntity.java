package com.taotao.cloud.workflow.api.vo;

import java.util.Date;
import lombok.Data;

@Data
public class GroupEntity {

	/**
	 * 自然主键
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
	 * 类型
	 */
	private String type;

	/**
	 * 说明
	 */
	private String description;

	/**
	 * 有效标志
	 */
	private Integer enabledMark;

	/**
	 * 排序
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

	/**
	 * 修改时间
	 */
	private Date lastModifyTime;

	/**
	 * 修改用户
	 */
	private String lastModifyUserId;

	/**
	 * 删除时间
	 */
	private Date deleteTime;

	/**
	 * 删除用户
	 */
	private String deleteUserId;

	/**
	 * 删除标志
	 */
	private Integer deleteMark;

}
