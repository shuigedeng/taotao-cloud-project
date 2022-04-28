package com.taotao.cloud.order.biz.entity.trade;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.common.enums.UserEnum;
import com.taotao.cloud.common.utils.lang.StringUtil;
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
 * 订单日志
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:02:36
 */
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = OrderLog.TABLE_NAME)
@TableName(OrderLog.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderLog.TABLE_NAME, comment = "订单日志")
public class OrderLog extends BaseSuperEntity<OrderLog, Long> {

	public static final String TABLE_NAME = "tt_order_log";

	/**
	 * 订单编号
	 */
	@Column(name = "order_sn", columnDefinition = "varchar(255) not null comment '订单编号'")
	private String orderSn;

	/**
	 * 操作者id(可以是卖家)
	 */
	@Column(name = "operator_id", columnDefinition = "bigint not null comment ' 操作者id(可以是卖家)'")
	private Long operatorId;

	/**
	 * 操作者类型
	 *
	 * @see UserEnum
	 */
	@Column(name = "operator_type", columnDefinition = "varchar(64) not null comment '会员ID'")
	private String operatorType;

	/**
	 * 操作者名称
	 */
	@Column(name = "operator_name", columnDefinition = "varchar(64) not null comment '操作者名称'")
	private String operatorName;

	/**
	 * 日志信息
	 */
	@Column(name = "message", columnDefinition = "text not null comment '日志信息'")
	private String message;

	public String getCreateBy() {
		if (StringUtil.isEmpty(createBy)) {
			return "系统";
		}
		return createBy;
	}

	@Override
	public boolean equals(Object o) {
				if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		OrderLog orderLog = (OrderLog) o;
		return getId() != null && Objects.equals(getId(), orderLog.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
