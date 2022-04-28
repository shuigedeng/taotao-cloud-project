package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.promotion.api.enums.KanJiaStatusEnum;
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
 * 砍价活动参与实体类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:57
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = KanjiaActivity.TABLE_NAME)
@TableName(KanjiaActivity.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = KanjiaActivity.TABLE_NAME, comment = "砍价活动参与实体类")
public class KanjiaActivity extends BaseSuperEntity<KanjiaActivity, Long> {

	public static final String TABLE_NAME = "tt_kanjia_activity";
	/**
	 * 砍价商品id
	 */
	@Column(name = "kanjia_activity_goods_id", columnDefinition = "bigint not null comment '砍价商品id'")
	private Long kanjiaActivityGoodsId;
	/**
	 * 发起砍价活动会员id
	 */
	@Column(name = "member_id", columnDefinition = "bigint not null comment '发起砍价活动会员id'")
	private Long memberId;
	/**
	 * 发起砍价活动会员名称
	 */
	@Column(name = "member_name", columnDefinition = "varchar(255) not null comment '发起砍价活动会员名称'")
	private String memberName;
	/**
	 * 剩余购买金额
	 */
	@Column(name = "surplus_price", columnDefinition = "decimal(10,2) not null default 0 comment '剩余购买金额'")
	private BigDecimal surplusPrice;
	/**
	 * 砍价最低购买金额
	 */
	@Column(name = "purchase_price", columnDefinition = "decimal(10,2) not null default 0 comment '砍价最低购买金额'")
	private BigDecimal purchasePrice;
	/**
	 * 砍价商品skuId
	 */
	@Column(name = "sku_id", columnDefinition = "bigint not null comment '砍价商品skuId'")
	private Long skuId;
	/**
	 * 货品名称
	 */
	@Column(name = "goods_name", columnDefinition = "varchar(255) not null comment '货品名称'")
	private String goodsName;
	/**
	 * 缩略图
	 */
	@Column(name = "thumbnail", columnDefinition = "varchar(255) not null comment '缩略图'")
	private String thumbnail;

	/**
	 * 砍价活动状态
	 * @see KanJiaStatusEnum
	 */
	@Column(name = "status", columnDefinition = "varchar(255) not null comment '砍价活动状态'")
	private String status;
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
			o)) {
			return false;
		}
		KanjiaActivity kanjiaActivity = (KanjiaActivity) o;
		return getId() != null && Objects.equals(getId(), kanjiaActivity.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
