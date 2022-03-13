package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品相册表
 */
@Entity
@Table(name = GoodsGallery.TABLE_NAME)
@TableName(GoodsGallery.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsGallery.TABLE_NAME, comment = "商品相册表")
public class GoodsGallery extends BaseSuperEntity<GoodsGallery, Long> {

	public static final String TABLE_NAME = "tt_goods_gallery";

	/**
	 * 商品id
	 */
	@Column(name = "goods_id", nullable = false, columnDefinition = "varchar(64) not null comment '商品id'")
	private String goodsId;

	/**
	 * 缩略图路径
	 */
	@Column(name = "thumbnail", nullable = false, columnDefinition = "varchar(64) not null comment '缩略图路径'")
	private String thumbnail;

	/**
	 * 小图路径
	 */
	@Column(name = "small", nullable = false, columnDefinition = "varchar(64) not null comment '小图路径'")
	private String small;

	/**
	 * 原图路径
	 */
	@Column(name = "original", nullable = false, columnDefinition = "varchar(64) not null comment '原图路径'")
	private String original;

	/**
	 * 是否是默认图片1   0没有默认
	 */
	@Column(name = "is_default", nullable = false, columnDefinition = "varchar(64) not null comment '是否是默认图片1   0没有默认'")
	private Integer isDefault;

	/**
	 * 排序
	 */
	@Column(name = "sort", nullable = false, columnDefinition = "int not null comment '排序'")
	private Integer sort;

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getSmall() {
		return small;
	}

	public void setSmall(String small) {
		this.small = small;
	}

	public String getOriginal() {
		return original;
	}

	public void setOriginal(String original) {
		this.original = original;
	}

	public Integer getIsDefault() {
		return isDefault;
	}

	public void setIsDefault(Integer isDefault) {
		this.isDefault = isDefault;
	}

	public Integer getSort() {
		return sort;
	}

	public void setSort(Integer sort) {
		this.sort = sort;
	}
}
