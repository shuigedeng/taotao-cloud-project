package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 会员商品浏览表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:35:23
 */
@Entity
@Table(name = MemberBrowse.TABLE_NAME)
@TableName(MemberBrowse.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberBrowse.TABLE_NAME, comment = "会员商品浏览表")
public class MemberBrowse extends BaseSuperEntity<MemberBrowse, Long> {

	public static final String TABLE_NAME = "tt_member_browse";
	/**
	 * 会员ID
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员ID'")
	private String memberId;
	/**
	 * 商品ID
	 */
	@Column(name = "goods_id", nullable = false, columnDefinition = "varchar(32) not null comment '商品ID'")
	private String goodsId;
	/**
	 * 规格ID
	 */
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
