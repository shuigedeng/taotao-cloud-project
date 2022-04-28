package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.promotion.api.enums.PromotionsApplyStatusEnum;
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
import java.math.BigDecimal;
import java.util.Objects;

/**
 * 秒杀活动申请实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:20
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = SeckillApply.TABLE_NAME)
@TableName(SeckillApply.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SeckillApply.TABLE_NAME, comment = "秒杀活动申请实体类")
public class SeckillApply extends BaseSuperEntity<SeckillApply, Long> {

	public static final String TABLE_NAME = "tt_seckill_apply";
	/**
	 * 活动id
	 */
	@Column(name = "seckill_id", columnDefinition = "bigint not null  comment '活动id'")
	private Long seckillId;
	/**
	 * 时刻
	 */
	@Column(name = "time_line", columnDefinition = "int not null  comment '时刻'")
	private Integer timeLine;
	/**
	 * skuID
	 */
	@Column(name = "sku_id", columnDefinition = "bigint not null  comment 'skuID'")
	private Long skuId;
	/**
	 * 商品名称
	 */
	@Column(name = "goods_name", columnDefinition = "varchar(255) not null  comment '商品名称'")
	private String goodsName;
	/**
	 * 商家id
	 */
	@Column(name = "store_id", columnDefinition = "bigint not null  comment '商家id'")
	private Long storeId;
	/**
	 * 商家名称
	 */
	@Column(name = "store_name", columnDefinition = "varchar(255) not null  comment '商家名称'")
	private String storeName;
	/**
	 * 价格
	 */
	@Column(name = "price", columnDefinition = "decimal(10,2) not null  comment '价格'")
	private BigDecimal price;
	/**
	 * 促销数量
	 */
	@Column(name = "quantity", columnDefinition = "int not null  comment '促销数量'")
	private Integer quantity;

	/**
	 * 状态
	 *
	 * @see PromotionsApplyStatusEnum
	 */
	@Column(name = "promotion_apply_status", columnDefinition = "varchar(255) not null  comment 'APPLY(申请), PASS(通过), REFUSE(拒绝)'")
	private String promotionApplyStatus;
	/**
	 * 驳回原因
	 */
	@Column(name = "fail_reason", columnDefinition = "varchar(255) not null  comment '驳回原因'")
	private String failReason;
	/**
	 * 已售数量
	 */
	@Column(name = "sales_num", columnDefinition = "int not null  comment '已售数量'")
	private Integer salesNum;
	/**
	 * 商品原始价格
	 */
	@Column(name = "original_price", columnDefinition = "decimal(10,2) not null  comment '商品原始价格'")
	private BigDecimal originalPrice;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
			o)) {
			return false;
		}
		SeckillApply seckillApply = (SeckillApply) o;
		return getId() != null && Objects.equals(getId(), seckillApply.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
