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
 * 规格项表规格项表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-20 16:59:38
 * @since 2020-02-18 15:18:56
 */
@Getter
@Setter
@ToString(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = Specification.TABLE_NAME)
@TableName(Specification.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = Specification.TABLE_NAME, comment = "规格项表")
public class Specification extends BaseSuperEntity<Specification, Long> {

	public static final String TABLE_NAME = "tt_specification";

	/**
	 * 规格名称
	 */
	@Column(name = "spec_name", columnDefinition = "varchar(255) not null comment '会员规格名称ID'")
	private String specName;

	/**
	 * 所属卖家 0属于平台
	 * <p>
	 * 店铺自定义规格暂时废弃 2021-06-23 后续推出新配置方式
	 */
	@Column(name = "store_id", columnDefinition = "bigint not null comment '所属卖家'")
	private Long storeId;

	/**
	 * 规格值名字, 《,》分割
	 */
	@Column(name = "spec_value", columnDefinition = "varchar(1024) not null comment '规格值名字'")
	private String specValue;

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
		Specification that = (Specification) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
