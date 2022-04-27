package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
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
 * 商品相册表
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
@Table(name = GoodsGallery.TABLE_NAME)
@TableName(GoodsGallery.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsGallery.TABLE_NAME, comment = "商品相册表")
public class GoodsGallery extends BaseSuperEntity<GoodsGallery, Long> {

	public static final String TABLE_NAME = "tt_goods_gallery";

	/**
	 * 商品id
	 */
	@Column(name = "goods_id", columnDefinition = "bigint not null comment '商品id'")
	private Long goodsId;

	/**
	 * 缩略图路径
	 */
	@Column(name = "thumbnail", columnDefinition = "varchar(255) not null comment '缩略图路径'")
	private String thumbnail;

	/**
	 * 小图路径
	 */
	@Column(name = "small", columnDefinition = "varchar(255) not null comment '小图路径'")
	private String small;

	/**
	 * 原图路径
	 */
	@Column(name = "original", columnDefinition = "varchar(255) not null comment '原图路径'")
	private String original;

	/**
	 * 是否是默认图片1  0没有默认
	 */
	@Column(name = "is_default", columnDefinition = "int not null default 0 comment '是否是默认图片1  0没有默认'")
	private Integer isDefault;

	/**
	 * 排序
	 */
	@Column(name = "sort", columnDefinition = "int not null default 0 comment '排序'")
	private Integer sort;

	@Override
	public boolean equals(Object o) {
				if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		GoodsGallery that = (GoodsGallery) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
