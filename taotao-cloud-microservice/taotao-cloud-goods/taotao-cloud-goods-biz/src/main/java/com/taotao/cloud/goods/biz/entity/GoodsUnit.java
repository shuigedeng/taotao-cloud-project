package com.taotao.cloud.goods.biz.entity;

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
 * 商品计量单位表
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = GoodsUnit.TABLE_NAME)
@TableName(GoodsUnit.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = GoodsUnit.TABLE_NAME, comment = "商品计量单位表")
public class GoodsUnit extends BaseSuperEntity<GoodsUnit, Long> {

	public static final String TABLE_NAME = "tt_goods_unit";

	/**
	 * 计量单位名称
	 */
	@Column(name = "name", columnDefinition = "varchar(255) not null comment '计量单位名称'")
	private String name;
}
