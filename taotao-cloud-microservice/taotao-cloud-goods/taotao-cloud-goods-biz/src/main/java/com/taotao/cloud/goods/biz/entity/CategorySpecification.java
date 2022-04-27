package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 商品分类规格关联表
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
@Table(name = CategorySpecification.TABLE_NAME)
@TableName(CategorySpecification.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = CategorySpecification.TABLE_NAME, comment = "商品分类规格关联表")
public class CategorySpecification extends SuperEntity<CategorySpecification, Long> {

	public static final String TABLE_NAME = "tt_category_specification";

	/**
	 * 分类id
	 */
	@Column(name = "category_id", columnDefinition = "bigint not null comment '分类id'")
	private Long categoryId;

	/**
	 * 规格id
	 */
	@Column(name = "specification_id", columnDefinition = "bigint not null comment '规格id'")
	private Long specificationId;

	@Override
	public boolean equals(Object o) {
				if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		CategorySpecification that = (CategorySpecification) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
