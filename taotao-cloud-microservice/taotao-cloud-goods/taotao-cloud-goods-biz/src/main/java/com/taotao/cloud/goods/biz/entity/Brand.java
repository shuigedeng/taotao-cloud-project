package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 商品品牌表
 */
@Data
@EqualsAndHashCode(callSuper = true)
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
}
