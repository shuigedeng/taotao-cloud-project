package com.taotao.cloud.store.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 店铺-物流公司设置
 *
 * 
 * @since 2020/11/17 8:01 下午
 */
@Entity
@Table(name = StoreLogistics.TABLE_NAME)
@TableName(StoreLogistics.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = StoreLogistics.TABLE_NAME, comment = "店铺-物流公司设置表")
public class StoreLogistics extends BaseSuperEntity<StoreLogistics, Long> {

	public static final String TABLE_NAME = "li_store_logistics";

	@Column(name = "store_id", nullable = false, columnDefinition = "varchar(64) not null comment '店铺ID'")
	private String storeId;

	@Column(name = "logistics_id", nullable = false, columnDefinition = "varchar(64) not null comment '物流公司ID'")
	private String logisticsId;

}
