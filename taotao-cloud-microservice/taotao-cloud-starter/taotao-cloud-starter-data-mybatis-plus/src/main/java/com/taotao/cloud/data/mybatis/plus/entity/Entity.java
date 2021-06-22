package com.taotao.cloud.data.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.TableField;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 包括id、create_time、created_by、updated_by、update_time字段的表继承的基础实体
 *
 * @author zuihou
 * @date 2019/05/05
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
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

}
