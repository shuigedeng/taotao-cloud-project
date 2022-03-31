package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.promotion.api.enums.KanJiaStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 砍价活动参与实体类
 *
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = KanjiaActivity.TABLE_NAME)
@TableName(KanjiaActivity.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = KanjiaActivity.TABLE_NAME, comment = "砍价活动参与实体类")
public class KanjiaActivity extends BaseSuperEntity<KanjiaActivity, Long> {

	public static final String TABLE_NAME = "li_kanjia_activity";

	@Column(name = "kanjia_activity_goods_id", nullable = false, columnDefinition = "varchar(64) not null comment '砍价商品id'")
	private String kanjiaActivityGoodsId;

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '发起砍价活动会员id'")
	private String memberId;

	@Column(name = "member_name", nullable = false, columnDefinition = "varchar(64) not null comment '发起砍价活动会员名称'")
	private String memberName;

	@Column(name = "surplus_price", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '剩余购买金额'")
	private BigDecimal surplusPrice;

	@Column(name = "purchase_price", nullable = false, columnDefinition = "decimal(10,2) not null default 0 comment '砍价最低购买金额'")
	private BigDecimal purchasePrice;

	@Column(name = "sku_id", nullable = false, columnDefinition = "varchar(64) not null comment '砍价商品skuId'")
	private String skuId;

	@Column(name = "goods_name", nullable = false, columnDefinition = "varchar(64) not null comment '货品名称'")
	private String goodsName;

	@Column(name = "thumbnail", nullable = false, columnDefinition = "varchar(64) not null comment '缩略图'")
	private String thumbnail;

	/**
	 * @see KanJiaStatusEnum
	 */
	@Column(name = "status", nullable = false, columnDefinition = "varchar(64) not null comment '砍价活动状态'")
	private String status;
}
