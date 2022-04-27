package com.taotao.cloud.order.biz.entity.order;

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
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 订单定时任务处理表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:53
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderWaitEvent.TABLE_NAME)
@Table(name = OrderWaitEvent.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderWaitEvent.TABLE_NAME, comment = "订单定时任务处理表")
public class OrderWaitEvent extends BaseSuperEntity<OrderWaitEvent, Long> {

	public static final String TABLE_NAME = "order_wait_event";

	/**
	 * 事件类型
	 */
	@Column(name = "event_type", columnDefinition = "int not null default 0 comment '事件类型'")
	private Integer eventType = 0;

	/**
	 * 事件状态；1--已处理；0--待处理
	 */
	@Column(name = "event_status", columnDefinition = "int not null default 0 comment '事件状态；1--已处理；0--待处理'")
	private Integer eventStatus = 0;

	/**
	 * 触发时间
	 */
	@Column(name = "trigger_time", columnDefinition = "datetime comment '触发时间'")
	private LocalDateTime triggerTime;

	/**
	 * 事件处理结果
	 */
	@Column(name = "event_result", columnDefinition = "varchar(256) not null comment '事件处理结果'")
	private String eventResult;

	@Column(name = "refund_code", columnDefinition = "varchar(256) not null comment 'refundCode'")
	private String refundCode;

	@Override
	public boolean equals(Object o) {
				if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		OrderWaitEvent that = (OrderWaitEvent) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
