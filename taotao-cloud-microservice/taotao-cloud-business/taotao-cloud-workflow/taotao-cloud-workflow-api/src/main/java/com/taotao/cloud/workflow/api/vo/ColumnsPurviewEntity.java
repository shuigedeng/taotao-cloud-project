package com.taotao.cloud.workflow.api.vo;

import java.util.Date;
import lombok.Data;

@Data
public class ColumnsPurviewEntity {

	/**
	 * 主键
	 */
	private String id;
	/**
	 * 列表字段数组
	 */
	private String fieldList;
	/**
	 * 模块ID
	 */
	private String moduleId;

	/**
	 * 排序码
	 */
	private Long sortCode;

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
