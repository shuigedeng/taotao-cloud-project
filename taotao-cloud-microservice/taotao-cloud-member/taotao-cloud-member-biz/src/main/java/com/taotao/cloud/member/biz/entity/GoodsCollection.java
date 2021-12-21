package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员商品收藏
 *
 * @since 2020/11/18 3:31 下午
 */
@Entity
@Table(name = GoodsCollection.TABLE_NAME)
@TableName(GoodsCollection.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsCollection.TABLE_NAME, comment = "会员商品收藏表")
public class GoodsCollection extends BaseSuperEntity<GoodsCollection, Long> {

	public static final String TABLE_NAME = "li_goods_collection";

	@Schema(description = "会员id")
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员id'")
	private String memberId;

	@Column(name = "sku_id", nullable = false, columnDefinition = "varchar(32) not null comment '商品id'")
	private String skuId;

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
