package com.taotao.cloud.goods.biz.entity;


import com.taotao.cloud.data.jpa.entity.JpaSuperEntity;
import javax.persistence.Column;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 商品销售范围表
 *
 * @author shuigedeng
 * @since 2020/4/30 16:04
 */
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
//@Entity
@Table(name = "tt_product_area")
@org.hibernate.annotations.Table(appliesTo = "tt_product_area", comment = "商品销售范围表")
public class GoodsArea extends JpaSuperEntity {
	/**
	 * 商品id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private String regionJson;
	/**
	 * 商品id
	 */
	@Column(name = "member_id", nullable = false, columnDefinition = "varchar(64) not null comment '会员ID'")
    private int type;

}
