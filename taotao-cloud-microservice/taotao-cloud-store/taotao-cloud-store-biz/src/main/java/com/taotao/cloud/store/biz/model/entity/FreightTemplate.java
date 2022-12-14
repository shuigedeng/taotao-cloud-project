package com.taotao.cloud.store.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.store.api.enums.FreightTemplateEnum;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import com.taotao.cloud.web.base.entity.JpaEntityListener;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Table;
import java.util.Objects;

/**
 * 运费模板表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-12 21:25:04
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = FreightTemplate.TABLE_NAME)
@TableName(FreightTemplate.TABLE_NAME)
@EntityListeners({JpaEntityListener.class})
@org.hibernate.annotations.Table(appliesTo = FreightTemplate.TABLE_NAME, comment = "运费模板表")
public class FreightTemplate extends BaseSuperEntity<FreightTemplate, Long> {

	public static final String TABLE_NAME = "tt_freight_template";

	@Column(name = "store_id", columnDefinition = "bigint not null comment '店铺ID'")
	private Long storeId;

	@Column(name = "name", columnDefinition = "varchar(32) not null comment '模板名称'")
	private String name;

	/**
	 * @see FreightTemplateEnum
	 */
	@Column(name = "pricing_method", columnDefinition = "varchar(32) not null comment '计价方式：按件、按重量 WEIGHT,NUM,FREE'")
	private String pricingMethod;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		FreightTemplate dict = (FreightTemplate) o;
		return getId() != null && Objects.equals(getId(), dict.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
