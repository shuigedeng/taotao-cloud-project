package com.taotao.cloud.member.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 会员商品收藏表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:37:12
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberGoodsCollection.TABLE_NAME)
@TableName(MemberGoodsCollection.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberGoodsCollection.TABLE_NAME, comment = "会员商品收藏表")
public class MemberGoodsCollection extends BaseSuperEntity<MemberGoodsCollection, Long> {

	public static final String TABLE_NAME = "tt_member_goods_collection";

	/**
	 * 会员id
	 */
	@Column(name = "member_id", columnDefinition = "bigint not null comment '会员id'")
	private Long memberId;

	/**
	 * 商品id
	 */
	@Column(name = "sku_id", columnDefinition = "bigint not null comment '商品id'")
	private Long skuId;
}
