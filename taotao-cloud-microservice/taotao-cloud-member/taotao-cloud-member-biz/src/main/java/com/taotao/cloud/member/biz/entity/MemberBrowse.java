package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * 会员商品浏览表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:35:23
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberBrowse.TABLE_NAME)
@TableName(MemberBrowse.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberBrowse.TABLE_NAME, comment = "会员商品浏览表")
public class MemberBrowse extends BaseSuperEntity<MemberBrowse, Long> {

	public static final String TABLE_NAME = "tt_member_browse";

	/**
	 * 会员ID
	 */
	@Column(name = "member_id", columnDefinition = "bigint not null comment '会员ID'")
	private Long memberId;

	/**
	 * 商品ID
	 */
	@Column(name = "goods_id", columnDefinition = "bigint not null comment '商品ID'")
	private Long goodsId;

	/**
	 * 规格ID
	 */
	@Column(name = "sku_id", columnDefinition = "bigint not null comment '规格ID'")
	private Long skuId;
}
