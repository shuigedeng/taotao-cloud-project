package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;


/**
 * 积分商品实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:38
 */
@Getter
@Setter
@ToString(callSuper = true)
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = PointsGoods.TABLE_NAME)
@TableName(PointsGoods.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = PointsGoods.TABLE_NAME, comment = "积分商品实体类")
public class PointsGoods extends BasePromotions<PointsGoods, Long> {

	public static final String TABLE_NAME = "tt_points_goods";
	/**
	 * 商品编号
	 */
	@Column(name = "goods_id", columnDefinition = "bigint not null  comment '商品编号'")
	private Long goodsId;
	/**
	 * 商品sku编号
	 */
	@Column(name = "sku_id", columnDefinition = "bigint not null  comment '商品sku编号'")
	private Long skuId;
	/**
	 * 商品名称
	 */
	@Column(name = "goods_name", columnDefinition = "varchar(255) not null  comment '商品名称'")
	private String goodsName;
	/**
	 * 商品原价
	 */
	@Column(name = "original_price", columnDefinition = "decimal(10,2) not null  comment '商品原价'")
	private BigDecimal originalPrice;
	/**
	 * 结算价格
	 */
	@Column(name = "settlement_price", columnDefinition = "decimal(10,2) not null  comment '结算价格'")
	private BigDecimal settlementPrice;
	/**
	 * 积分商品分类编号
	 */
	@Column(name = "points_goods_category_id", columnDefinition = "bigint not null  comment '积分商品分类编号'")
	private Long pointsGoodsCategoryId;
	/**
	 * 分类名称
	 */
	@Column(name = "points_goods_category_name", columnDefinition = "varchar(255) not null  comment '分类名称'")
	private String pointsGoodsCategoryName;
	/**
	 * 缩略图
	 */
	@Column(name = "thumbnail", columnDefinition = "varchar(255) not null  comment '缩略图'")
	private String thumbnail;
	/**
	 * 活动库存数量
	 */
	@Column(name = "active_stock", columnDefinition = "int not null  comment '活动库存数量'")
	private Integer activeStock;
	/**
	 * 兑换积分
	 */
	@Column(name = "points", columnDefinition = "bigint not null  comment '兑换积分'")
	private Long points;
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
			o)) {
			return false;
		}
		PointsGoods pointsGoods = (PointsGoods) o;
		return getId() != null && Objects.equals(getId(), pointsGoods.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
