package com.taotao.cloud.goods.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

/**
 * 商品品牌表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:55:08
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = Brand.TABLE_NAME)
@TableName(Brand.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Brand.TABLE_NAME, comment = "商品品牌表")
public class Brand extends BaseSuperEntity<Brand, Long> {

	public static final String TABLE_NAME = "tt_brand";

	/**
	 * 品牌名称
	 */
	@Column(name = "name", columnDefinition = "varchar(255) not null comment '品牌名称'")
	private String name;

	/**
	 * 品牌图标
	 */
	@Column(name = "logo", columnDefinition = "varchar(255) not null comment '品牌图标'")
	private String logo;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		Brand brand = (Brand) o;
		return getId() != null && Objects.equals(getId(), brand.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
