package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 商品计量单位表
 */
@Entity
@Table(name = GoodsUnit.TABLE_NAME)
@TableName(GoodsUnit.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsUnit.TABLE_NAME, comment = "商品计量单位表")
public class GoodsUnit extends BaseSuperEntity<GoodsUnit, Long> {

	public static final String TABLE_NAME = "tt_goods_unit";

	/**
	 * 计量单位名称
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '计量单位名称'")
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
