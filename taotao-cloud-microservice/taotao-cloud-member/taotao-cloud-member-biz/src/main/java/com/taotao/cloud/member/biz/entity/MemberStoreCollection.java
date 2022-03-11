package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 会员店铺收藏表
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-03-11 15:42:32
 */
@Entity
@Table(name = MemberStoreCollection.TABLE_NAME)
@TableName(MemberStoreCollection.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = MemberStoreCollection.TABLE_NAME, comment = "会员店铺收藏表")
public class MemberStoreCollection extends BaseSuperEntity<MemberStoreCollection, Long> {

	public static final String TABLE_NAME = "tt_member_store_collection";
	/**
	 * 会员id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员id'")
	private String memberId;

	/**
	 * 店铺id
	 */
	@Column(name = "store_id", nullable = false, columnDefinition = "varchar(32) not null comment '店铺id'")
	private String storeId;

	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}
}
