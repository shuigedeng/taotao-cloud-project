package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;


/**
 * 商品分类品牌关联表
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = CategoryBrand.TABLE_NAME)
@TableName(CategoryBrand.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CategoryBrand.TABLE_NAME, comment = "商品分类品牌关联表")
public class CategoryBrand extends SuperEntity<CategoryBrand, Long> {

	public static final String TABLE_NAME = "tt_category_brand";

	/**
	 * 分类id
	 */
	@Column(name = "category_id", columnDefinition = "bigint not null comment '分类id'")
	private Long categoryId;

	/**
	 * 品牌id
	 */
	@Column(name = "brand_id", columnDefinition = "bigint not null comment '品牌id'")
	private Long brandId;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		CategoryBrand that = (CategoryBrand) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
