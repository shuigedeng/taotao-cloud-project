package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 商品分类规格关联表
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = CategorySpecification.TABLE_NAME)
@TableName(CategorySpecification.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CategorySpecification.TABLE_NAME, comment = "商品分类规格关联表")
public class CategorySpecification extends BaseSuperEntity<CategorySpecification, Long> {

	public static final String TABLE_NAME = "tt_category_specification";

	/**
	 * 分类id
	 */
	@Column(name = "category_id", nullable = false, columnDefinition = "varchar(64) not null comment '分类id'")
	private String categoryId;

	/**
	 * 规格id
	 */
	@Column(name = "specification_id", nullable = false, columnDefinition = "varchar(64) not null comment '规格id'")
	private String specificationId;
}
