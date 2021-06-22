package com.taotao.cloud.data.mybatis.plus.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import java.time.LocalDateTime;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 包括id、create_time、created_by字段的表继承的基础实体
 *
 * @author zuihou
 * @date 2019/05/05
 */
@Getter
@Setter
@Accessors(chain = true)
@NoArgsConstructor
@AllArgsConstructor
@ToString(callSuper = true)
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
}
