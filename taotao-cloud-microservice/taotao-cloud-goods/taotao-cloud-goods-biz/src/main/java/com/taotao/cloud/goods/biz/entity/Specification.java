package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 规格项表规格项表
 *
 * @since 2020-02-18 15:18:56
 */
@Entity
@Table(name = Specification.TABLE_NAME)
@TableName(Specification.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Specification.TABLE_NAME, comment = "规格项表")
public class Specification extends BaseSuperEntity<Specification, Long> {

	public static final String TABLE_NAME = "tt_specification";

	/**
	 * 规格名称
	 */
	@Column(name = "spec_name", nullable = false, columnDefinition = "varchar(64) not null comment '会员规格名称ID'")
	private String specName;

	/**
	 * 所属卖家 0属于平台
	 * <p>
	 * 店铺自定义规格暂时废弃 2021-06-23 后续推出新配置方式
	 */
	@Column(name = "store_id", nullable = false, columnDefinition = "varchar(64) not null comment '所属卖家'")
	private String storeId;

	/**
	 * 规格值名字, 《,》分割
	 */
	@Column(name = "spec_value", nullable = false, columnDefinition = "varchar(64) not null comment '规格值名字'")
	private String specValue;

	public String getSpecName() {
		return specName;
	}

	public void setSpecName(String specName) {
		this.specName = specName;
	}

	public String getStoreId() {
		return storeId;
	}

	public void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public String getSpecValue() {
		return specValue;
	}

	public void setSpecValue(String specValue) {
		this.specValue = specValue;
	}
}
