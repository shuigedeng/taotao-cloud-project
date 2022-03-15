package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 商品分类品牌关联表
 */
@Entity
@Table(name = CategoryBrand.TABLE_NAME)
@TableName(CategoryBrand.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CategoryBrand.TABLE_NAME, comment = "商品分类品牌关联表")
public class CategoryBrand extends BaseSuperEntity<CategoryBrand, Long> {

	public static final String TABLE_NAME = "tt_category_brand";

	/**
	 * 分类id
	 */
	@Column(name = "category_id", nullable = false, columnDefinition = "varchar(64) not null comment '分类id'")
	private String categoryId;

	/**
	 * 品牌id
	 */
	@Column(name = "brand_id", nullable = false, columnDefinition = "varchar(64) not null comment '品牌id'")
	private String brandId;

	public CategoryBrand(String categoryId, String brandId) {
		this.categoryId = categoryId;
		this.brandId = brandId;
	}

	public String getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(String categoryId) {
		this.categoryId = categoryId;
	}

	public String getBrandId() {
		return brandId;
	}

	public void setBrandId(String brandId) {
		this.brandId = brandId;
	}
}
