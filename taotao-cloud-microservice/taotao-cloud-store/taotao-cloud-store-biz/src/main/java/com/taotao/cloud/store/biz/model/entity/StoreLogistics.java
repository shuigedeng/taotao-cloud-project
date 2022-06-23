package com.taotao.cloud.store.biz.model.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.AbstractListener;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.util.Objects;

/**
 * 店铺-物流公司设置
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = StoreLogistics.TABLE_NAME)
@TableName(StoreLogistics.TABLE_NAME)
@EntityListeners({AbstractListener.class})
@org.hibernate.annotations.Table(appliesTo = StoreLogistics.TABLE_NAME, comment = "店铺-物流公司设置表")
public class StoreLogistics extends BaseSuperEntity<StoreLogistics, Long> {

	public static final String TABLE_NAME = "tt_store_logistics";

	@Column(name = "store_id", columnDefinition = "varchar(64) not null comment '店铺ID'")
	private String storeId;

	@Column(name = "logistics_id", columnDefinition = "varchar(64) not null comment '物流公司ID'")
	private String logisticsId;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		StoreLogistics dict = (StoreLogistics) o;
		return getId() != null && Objects.equals(getId(), dict.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
