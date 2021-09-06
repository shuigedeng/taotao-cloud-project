package com.taotao.cloud.data.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import java.util.Objects;
import jdk.nashorn.internal.objects.annotations.Getter;
import jdk.nashorn.internal.objects.annotations.Setter;


/**
 * Entity 
 *
 * @author shuigedeng
 * @version 2021.9
 * @since 2021-09-04 07:40:40
 */
public class Entity<T> extends SuperEntity<T> {

	public static final String UPDATE_TIME = "updateTime";
	public static final String UPDATED_BY = "updatedBy";
	public static final String UPDATE_TIME_COLUMN = "update_time";
	public static final String UPDATED_BY_COLUMN = "updated_by";
	private static final long serialVersionUID = 5169873634279173683L;

	@TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
	protected LocalDateTime updateTime;

	@TableField(value = "updated_by", fill = FieldFill.INSERT_UPDATE)
	protected T updatedBy;

	public Entity(T id, LocalDateTime createTime, T createdBy, LocalDateTime updateTime,
		T updatedBy) {
		super(id, createTime, createdBy);
		this.updateTime = updateTime;
		this.updatedBy = updatedBy;
	}

	public Entity() {
	}

	@Override
	public String toString() {
		return "Entity{" +
			"updateTime=" + updateTime +
			", updatedBy=" + updatedBy +
			"} " + super.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		Entity<?> entity = (Entity<?>) o;
		return Objects.equals(updateTime, entity.updateTime) && Objects.equals(
			updatedBy, entity.updatedBy);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), updateTime, updatedBy);
	}

	public Entity(LocalDateTime updateTime, T updatedBy) {
		this.updateTime = updateTime;
		this.updatedBy = updatedBy;
	}

	public LocalDateTime getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(LocalDateTime updateTime) {
		this.updateTime = updateTime;
	}

	public T getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(T updatedBy) {
		this.updatedBy = updatedBy;
	}
}
