package com.taotao.cloud.data.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

/**
 * SuperEntity 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:46
 */
public class SuperEntity<T> implements Serializable {

	public static final String FIELD_ID = "id";
	public static final String CREATE_TIME = "createTime";
	public static final String CREATE_TIME_COLUMN = "create_time";
	public static final String CREATED_BY = "createdBy";
	public static final String CREATED_BY_COLUMN = "created_by";

	private static final long serialVersionUID = -4603650115461757622L;

	@TableId(value = "id", type = IdType.INPUT)
	@NotNull(message = "id不能为空", groups = Update.class)
	protected T id;

	@TableField(value = "create_time", fill = FieldFill.INSERT)
	protected LocalDateTime createTime;

	@TableField(value = "created_by", fill = FieldFill.INSERT)
	protected T createdBy;

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

	public SuperEntity() {
	}

	public SuperEntity(T id, LocalDateTime createTime, T createdBy) {
		this.id = id;
		this.createTime = createTime;
		this.createdBy = createdBy;
	}

	@Override
	public String toString() {
		return "SuperEntity{" +
			"id=" + id +
			", createTime=" + createTime +
			", createdBy=" + createdBy +
			'}';
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SuperEntity<?> that = (SuperEntity<?>) o;
		return Objects.equals(id, that.id) && Objects.equals(createTime,
			that.createTime) && Objects.equals(createdBy, that.createdBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, createTime, createdBy);
	}

	public T getId() {
		return id;
	}

	public void setId(T id) {
		this.id = id;
	}

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}

	public T getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(T createdBy) {
		this.createdBy = createdBy;
	}
}
