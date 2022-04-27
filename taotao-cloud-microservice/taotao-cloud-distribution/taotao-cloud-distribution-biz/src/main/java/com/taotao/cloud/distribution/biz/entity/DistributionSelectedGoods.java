package com.taotao.cloud.distribution.biz.entity;

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
 * 分销商已选择分销商品表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 14:59:04
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = DistributionSelectedGoods.TABLE_NAME)
@TableName(DistributionSelectedGoods.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = DistributionSelectedGoods.TABLE_NAME, comment = "分销商已选择分销商品表")
public class DistributionSelectedGoods extends BaseSuperEntity<DistributionSelectedGoods, Long> {

	public static final String TABLE_NAME = "tt_distribution_selected_goods";

	/**
	 * 分销员ID
	 */
	@Column(name = "distribution_id", columnDefinition = "bigint not null  comment '分销员ID'")
	private Long distributionId;

	/**
	 * 分销商品ID
	 */
	@Column(name = "distribution_goods_id", columnDefinition = "bigint not null  comment '分销商品ID'")
	private Long distributionGoodsId;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		DistributionSelectedGoods distributionSelectedGoods = (DistributionSelectedGoods) o;
		return getId() != null && Objects.equals(getId(), distributionSelectedGoods.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
