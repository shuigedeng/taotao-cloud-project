package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 分类绑定参数组表
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = CategoryParameterGroup.TABLE_NAME)
@TableName(CategoryParameterGroup.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CategoryParameterGroup.TABLE_NAME, comment = "分类绑定参数组表")
public class CategoryParameterGroup extends BaseSuperEntity<CategoryParameterGroup, Long> {

	public static final String TABLE_NAME = "li_category_parameter_group";

	/**
	 * 参数组名称
	 */
	@Column(name = "group_name", columnDefinition = "varchar(255) not null comment '参数组名称'")
	private String groupName;

	/**
	 * 关联分类id
	 */
	@Column(name = "category_id", columnDefinition = "bigint not null comment '关联分类id'")
	private Long categoryId;

	/**
	 * 排序
	 */
	@Column(name = "sort_order", columnDefinition = "int not null default 0 comment '排序'")
	private Integer sortOrder;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		CategoryParameterGroup that = (CategoryParameterGroup) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
