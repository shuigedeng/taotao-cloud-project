package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 浏览历史
 *
 * @since 2020/11/17 7:22 下午
 */
@Entity
@Table(name = FootPrint.TABLE_NAME)
@TableName(FootPrint.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = FootPrint.TABLE_NAME, comment = "浏览历史表")
public class FootPrint extends BaseSuperEntity<FootPrint, Long> {

	public static final String TABLE_NAME = "li_foot_print";

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员ID'")
	private String memberId;

	@Column(name = "goods_id", nullable = false, columnDefinition = "varchar(32) not null comment '商品ID'")
	private String goodsId;

	@Column(name = "sku_id", nullable = false, columnDefinition = "varchar(32) not null comment '规格ID'")
	private String skuId;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
}
