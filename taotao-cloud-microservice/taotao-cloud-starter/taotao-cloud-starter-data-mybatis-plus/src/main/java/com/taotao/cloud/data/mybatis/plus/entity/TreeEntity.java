package com.taotao.cloud.data.mybatis.plus.entity;

import static com.baomidou.mybatisplus.annotation.SqlCondition.LIKE;

import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 包括id、create_time、created_by、updated_by、update_time、label、parent_id、sort_value 字段的表继承的树形实体
 *
 * @author zuihou
 * @date 2019/05/05
 */
@Getter
@Setter
@Accessors(chain = true)
@ToString(callSuper = true)
public class TreeEntity<E, T extends Serializable> extends Entity<T> {

	/**
	 * 名称
	 */
	@NotEmpty(message = "名称不能为空")
	@Size(max = 255, message = "名称长度不能超过255")
	@TableField(value = "label", condition = LIKE)
	protected String label;

	/**
	 * 父ID
	 */
	@TableField(value = "parent_id")
	protected T parentId;

	/**
	 * 排序
	 */
	@TableField(value = "sort_value")
	protected Integer sortValue;


	@TableField(exist = false)
	protected List<E> children;


	/**
	 * 初始化子类
	 */
	public void initChildren() {
		if (getChildren() == null) {
			this.setChildren(new ArrayList<>());
		}
	}
}
