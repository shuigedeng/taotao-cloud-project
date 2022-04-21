package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 商品分类表
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = Category.TABLE_NAME)
@TableName(Category.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Category.TABLE_NAME, comment = "商品分类表")
public class Category extends BaseSuperEntity<Category, Long> {

	public static final String TABLE_NAME = "tt_category";

	/**
	 * 分类名称
	 */
	@Column(name = "name", columnDefinition = "varchar(255) not null comment '分类名称'")
	private String name;

	/**
	 * 父id, 根节点为0
	 */
	@Column(name = "parent_id", columnDefinition = "bigint not null comment '父id, 根节点为0'")
	private Long parentId;

	/**
	 * 层级, 从0开始
	 */
	@Column(name = "level", columnDefinition = "int not null default 0 comment '层级, 从0开始'")
	private Integer level;

	/**
	 * 排序值
	 */
	@Column(name = "sort_order", columnDefinition = "int not null default 0 comment '排序值'")
	private Integer sortOrder;

	/**
	 * 佣金比例
	 */
	@Column(name = "commission_rate", columnDefinition = "decimal(10,2) not null comment '佣金比例'")
	private BigDecimal commissionRate;

	/**
	 * 分类图标
	 */
	@Column(name = "image", columnDefinition = "varchar(255) not null comment '分类图标'")
	private String image;

	/**
	 * 是否支持频道
	 */
	@Column(name = "support_channel", columnDefinition = "boolean null default false comment '是否支持频道'")
	private Boolean supportChannel;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Category category = (Category) o;
		return getId() != null && Objects.equals(getId(), category.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
