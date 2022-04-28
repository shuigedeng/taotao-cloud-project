package com.taotao.cloud.promotion.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Objects;

/**
 * 积分商品分类
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 16:24:35
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = PointsGoodsCategory.TABLE_NAME)
@TableName(PointsGoodsCategory.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = PointsGoodsCategory.TABLE_NAME, comment = "积分商品分类")
public class PointsGoodsCategory extends BaseSuperEntity<PointsGoodsCategory, Long> {

	public static final String TABLE_NAME = "tt_points_goods_category";
	/**
	 * 分类名称
	 */
	@Column(name = "name", columnDefinition = "varchar(255) not null  comment '分类名称'")
    private String name;
	/**
	 * 父id, 根节点为0
	 */
	@Column(name = "parent_id", columnDefinition = "bigint not null  comment '父id, 根节点为0'")
    private Long parentId;
	/**
	 * 层级, 从0开始
	 */
	@Column(name = "level", columnDefinition = "int not null  comment '层级, 从0开始'")
    private Integer level;
	/**
	 * 排序值
	 */
	@Column(name = "sort_order", columnDefinition = "int not null  comment '排序值'")
    private Integer sortOrder;
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(
			o)) {
			return false;
		}
		PointsGoodsCategory pointsGoodsCategory = (PointsGoodsCategory) o;
		return getId() != null && Objects.equals(getId(), pointsGoodsCategory.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
