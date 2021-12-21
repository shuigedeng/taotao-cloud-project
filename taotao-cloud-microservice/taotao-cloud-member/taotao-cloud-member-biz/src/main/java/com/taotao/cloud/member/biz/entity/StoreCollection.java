package com.taotao.cloud.member.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 会员店铺收藏
 *
 * @since 2020/11/18 3:32 下午
 */
@Entity
@Table(name = StoreCollection.TABLE_NAME)
@TableName(StoreCollection.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = StoreCollection.TABLE_NAME, comment = "会员店铺收藏表")
public class StoreCollection extends BaseSuperEntity<StoreCollection, Long> {

	public static final String TABLE_NAME = "li_store_collection";

	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(32) not null comment '会员id'")
	private String memberId;

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
