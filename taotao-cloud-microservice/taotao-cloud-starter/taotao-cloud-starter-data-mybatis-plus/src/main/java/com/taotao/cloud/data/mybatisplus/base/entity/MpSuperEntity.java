package com.taotao.cloud.data.mybatisplus.base.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * SuperEntity 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:46
 */
public class MpSuperEntity<I extends Serializable> implements Serializable {

	@Serial
	private static final long serialVersionUID = -4603650115461757622L;

	public static final String FIELD_ID = "id";
	public static final String CREATE_TIME = "createTime";
	public static final String CREATED_BY = "createdBy";
	public static final String UPDATE_TIME = "updateTime";
	public static final String UPDATED_BY = "updatedBy";

	@TableId(value = "id", type = IdType.INPUT)
	@NotNull(message = "id不能为空", groups = Update.class)
	protected I id;

	@TableField(value = "create_time", fill = FieldFill.INSERT)
	protected LocalDateTime createTime;

	@TableField(value = "created_by", fill = FieldFill.INSERT)
	protected I createdBy;

	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	protected LocalDateTime updateTime;

	@TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
	protected I updatedBy;

	/**
	 * 保存和缺省验证组
	 */
	public interface Save extends Default {

	}

	/**
	 * 更新和缺省验证组
	 */
	public interface Update extends Default {

	}

	public MpSuperEntity() {
	}

	public MpSuperEntity(I id, LocalDateTime createTime, I createdBy,
		LocalDateTime updateTime, I updatedBy) {
		this.id = id;
		this.createTime = createTime;
		this.createdBy = createdBy;
		this.updateTime = updateTime;
		this.updatedBy = updatedBy;
	}

	public I getId() {
		return id;
	}

	public void setId(I id) {
		this.id = id;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public I getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(I createdBy) {
		this.createdBy = createdBy;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public I getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(I updatedBy) {
		this.updatedBy = updatedBy;
	}
}
