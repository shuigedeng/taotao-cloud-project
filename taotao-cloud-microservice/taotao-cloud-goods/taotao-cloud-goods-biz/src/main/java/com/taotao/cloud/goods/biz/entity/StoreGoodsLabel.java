package com.taotao.cloud.goods.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 店铺商品分类表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-14 21:50:58
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = StoreGoodsLabel.TABLE_NAME)
@TableName(StoreGoodsLabel.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = StoreGoodsLabel.TABLE_NAME, comment = "店铺商品标签表")
public class StoreGoodsLabel extends BaseSuperEntity<StoreGoodsLabel, Long> {

	public static final String TABLE_NAME = "tt_store_goods_label";

	/**
	 * 店铺ID
	 */
	@Column(name = "store_id", columnDefinition = "bigint not null comment '店铺ID'")
	private Long storeId;

	/**
	 * 店铺商品分类名称
	 */
	@Column(name = "label_name", columnDefinition = "varchar(255) not null comment '店铺商品分类名称'")
	private String labelName;

	/**
	 * 店铺商品分类排序
	 */
	@Column(name = "sort_order", columnDefinition = "int not null comment '店铺商品分类排序'")
	private Integer sortOrder;

	/**
	 * 父id, 根节点为0
	 */
	@Column(name = "parent_id", columnDefinition = "bigint not null comment '父id, 根节点为0'")
	private Long parentId;

	/**
	 * 层级, 从0开始
	 */
	@Column(name = "level", columnDefinition = "int not null comment '层级, 从0开始'")
	private Integer level;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		StoreGoodsLabel that = (StoreGoodsLabel) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
