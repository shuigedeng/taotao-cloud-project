package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.promotion.api.enums.KanJiaStatusEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 砍价活动参与实体类
 *
 */
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


	public String getKanjiaActivityGoodsId() {
		return kanjiaActivityGoodsId;
	}

	public void setKanjiaActivityGoodsId(String kanjiaActivityGoodsId) {
		this.kanjiaActivityGoodsId = kanjiaActivityGoodsId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getMemberName() {
		return memberName;
	}

	public void setMemberName(String memberName) {
		this.memberName = memberName;
	}

	public BigDecimal getSurplusPrice() {
		return surplusPrice;
	}

	public void setSurplusPrice(BigDecimal surplusPrice) {
		this.surplusPrice = surplusPrice;
	}

	public BigDecimal getPurchasePrice() {
		return purchasePrice;
	}

	public void setPurchasePrice(BigDecimal purchasePrice) {
		this.purchasePrice = purchasePrice;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public String getThumbnail() {
		return thumbnail;
	}

	public void setThumbnail(String thumbnail) {
		this.thumbnail = thumbnail;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
