package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员商品收藏表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:37:12
 */
@Entity
@Table(name = MemberGoodsCollection.TABLE_NAME)
@TableName(MemberGoodsCollection.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberGoodsCollection.TABLE_NAME, comment = "会员商品收藏表")
public class MemberGoodsCollection extends BaseSuperEntity<MemberGoodsCollection, Long> {

	public static final String TABLE_NAME = "tt_member_goods_collection";

	/**
	 * 会员id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员id'")
	private String memberId;

	/**
	 * 商品id
	 */
	@Column(name = "sku_id", nullable = false, columnDefinition = "varchar(32) not null comment '商品id'")
	private String skuId;


	public MemberGoodsCollection(){}

	public MemberGoodsCollection(String memberId, String skuId) {
		this.memberId = memberId;
		this.skuId = skuId;
	}

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getSkuId() {
		return skuId;
	}

	public void setSkuId(String skuId) {
		this.skuId = skuId;
	}
}
