package com.taotao.cloud.member.biz.entity;

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
 * 会员店铺收藏表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:42:32
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = MemberStoreCollection.TABLE_NAME)
@TableName(MemberStoreCollection.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberStoreCollection.TABLE_NAME, comment = "会员店铺收藏表")
public class MemberStoreCollection extends BaseSuperEntity<MemberStoreCollection, Long> {

	public static final String TABLE_NAME = "tt_member_store_collection";

	/**
	 * 会员id
	 */
	@Column(name = "member_id", columnDefinition = "bigint not null comment '会员id'")
	private Long memberId;

	/**
	 * 店铺id
	 */
	@Column(name = "store_id", columnDefinition = "bigint not null comment '店铺id'")
	private Long storeId;
}
