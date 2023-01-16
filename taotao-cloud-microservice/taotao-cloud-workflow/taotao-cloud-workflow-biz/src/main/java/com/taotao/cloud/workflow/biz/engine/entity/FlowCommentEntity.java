package com.taotao.cloud.workflow.biz.engine.entity;

import com.alibaba.fastjson.annotation.JSONField;
import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import java.util.Date;
import lombok.Data;

/**
 * 流程评论
 */
@Data
@TableName("flow_comment")
public class FlowCommentEntity extends SuperEntity<FlowCommentEntity, String> {

	/**
	 * 主键
	 */
	@TableId("id")
	private String id;

	/**
	 * 任务主键
	 */
	@TableField("task_id")
	private String taskId;

	/**
	 * 任务主键
	 */
	@TableField("text")
	private String text;

	/**
	 * 任务主键
	 */
	@TableField("image")
	private String image;

	/**
	 * 任务主键
	 */
	@TableField("file")
	@JSONField(name = "file")
	private String fileName;

	/**
	 * 有效标志
	 */
	@TableField("enabled_mark")
	private Integer enabledMark;

	/**
	 * 创建时间
	 */
	@TableField(value = "creator_time", fill = FieldFill.INSERT)
	private Date creatorTime;

	/**
	 * 创建用户
	 */
	@TableField(value = "creator_user_id", fill = FieldFill.INSERT)
	private Long creatorUserId;

	/**
	 * 修改时间
	 */
	@TableField(value = "lastmodify_time", fill = FieldFill.UPDATE)
	private Date lastModifyTime;

	/**
	 * 修改用户
	 */
	@TableField(value = "lastmodify_user_id", fill = FieldFill.UPDATE)
	private String lastModifyUserId;

	/**
	 * 删除标志
	 */
	@TableField("delete_mark")
	private Integer deleteMark;

	/**
	 * 删除时间
	 */
	@TableField("delete_time")
	private Date deleteTime;

	/**
	 * 删除用户
	 */
	@TableField("delete_user_id")
	private String deleteUserId;

}
