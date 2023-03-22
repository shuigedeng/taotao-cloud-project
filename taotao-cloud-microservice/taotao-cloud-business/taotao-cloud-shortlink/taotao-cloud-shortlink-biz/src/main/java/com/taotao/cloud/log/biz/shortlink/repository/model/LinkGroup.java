package com.taotao.cloud.log.biz.shortlink.repository.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * This is Description
 *
 * @since 2022/05/03
 */

/**
 * link_group
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@TableName(value = "short_link.link_group")
public class LinkGroup implements Serializable {

	/**
	 * id
	 */
	@TableId(value = "id", type = IdType.AUTO)
	private Long id;

	/**
	 * 分组名
	 */
	@TableField(value = "title")
	private String title;

	/**
	 * 账号唯一编号
	 */
	@TableField(value = "account_no")
	private Long accountNo;

	/**
	 * 创建时间
	 */
	@TableField(value = "create_time")
	private LocalDateTime createTime;

	/**
	 * 更新时间
	 */
	@TableField(value = "update_time")
	private LocalDateTime updateTime;

	/**
	 * 逻辑删除：0=否、1=是
	 */
	@TableField(value = "is_deleted")
	private Integer isDeleted;

	private static final long serialVersionUID = 1L;

	public static final String COL_ID = "id";

	public static final String COL_TITLE = "title";

	public static final String COL_ACCOUNT_NO = "account_no";

	public static final String COL_CREATE_TIME = "create_time";

	public static final String COL_UPDATE_TIME = "update_time";

	public static final String COL_IS_DELETED = "is_deleted";
}
