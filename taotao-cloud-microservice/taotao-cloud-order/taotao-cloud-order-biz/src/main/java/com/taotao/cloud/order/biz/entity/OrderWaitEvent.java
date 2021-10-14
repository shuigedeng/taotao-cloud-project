package com.taotao.cloud.order.biz.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 订单定时任务处理表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:53
 */
@Entity
@TableName(OrderWaitEvent.TABLE_NAME)
@Table(name = OrderWaitEvent.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderWaitEvent.TABLE_NAME, comment = "订单定时任务处理表")
public class OrderWaitEvent extends BaseSuperEntity<Long> {

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
	@Column(name = "trigger_time", columnDefinition = "TIMESTAMP comment '触发时间'")
	private LocalDateTime triggerTime;

	@Override
	public String toString() {
		return "OrderWaitEvent{" +
			"eventType=" + eventType +
			", eventStatus=" + eventStatus +
			", triggerTime=" + triggerTime +
			", eventResult='" + eventResult + '\'' +
			", refundCode='" + refundCode + '\'' +
			"} " + super.toString();
	}

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
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}
		OrderWaitEvent that = (OrderWaitEvent) o;
		return Objects.equals(eventType, that.eventType) && Objects.equals(
			eventStatus, that.eventStatus) && Objects.equals(triggerTime,
			that.triggerTime) && Objects.equals(eventResult, that.eventResult)
			&& Objects.equals(refundCode, that.refundCode);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), eventType, eventStatus, triggerTime, eventResult,
			refundCode);
	}

	public Integer getEventType() {
		return eventType;
	}

	public void setEventType(Integer eventType) {
		this.eventType = eventType;
	}

	public Integer getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(Integer eventStatus) {
		this.eventStatus = eventStatus;
	}

	public LocalDateTime getTriggerTime() {
		return triggerTime;
	}

	public void setTriggerTime(LocalDateTime triggerTime) {
		this.triggerTime = triggerTime;
	}

	public String getEventResult() {
		return eventResult;
	}

	public void setEventResult(String eventResult) {
		this.eventResult = eventResult;
	}

	public String getRefundCode() {
		return refundCode;
	}

	public void setRefundCode(String refundCode) {
		this.refundCode = refundCode;
	}

	public OrderWaitEvent() {
	}

	public OrderWaitEvent(Integer eventType, Integer eventStatus, LocalDateTime triggerTime,
		String eventResult, String refundCode) {
		this.eventType = eventType;
		this.eventStatus = eventStatus;
		this.triggerTime = triggerTime;
		this.eventResult = eventResult;
		this.refundCode = refundCode;
	}

	public static OrderWaitEventBuilder builder() {
		return new OrderWaitEventBuilder();
	}

	public static final class OrderWaitEventBuilder {

		private Integer eventType = 0;
		private Integer eventStatus = 0;
		private LocalDateTime triggerTime;
		private String eventResult;
		private String refundCode;
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private OrderWaitEventBuilder() {
		}


		public OrderWaitEventBuilder eventType(Integer eventType) {
			this.eventType = eventType;
			return this;
		}

		public OrderWaitEventBuilder eventStatus(Integer eventStatus) {
			this.eventStatus = eventStatus;
			return this;
		}

		public OrderWaitEventBuilder triggerTime(LocalDateTime triggerTime) {
			this.triggerTime = triggerTime;
			return this;
		}

		public OrderWaitEventBuilder eventResult(String eventResult) {
			this.eventResult = eventResult;
			return this;
		}

		public OrderWaitEventBuilder refundCode(String refundCode) {
			this.refundCode = refundCode;
			return this;
		}

		public OrderWaitEventBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public OrderWaitEventBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public OrderWaitEventBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public OrderWaitEventBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public OrderWaitEventBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public OrderWaitEventBuilder version(int version) {
			this.version = version;
			return this;
		}

		public OrderWaitEventBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public OrderWaitEvent build() {
			OrderWaitEvent orderWaitEvent = new OrderWaitEvent();
			orderWaitEvent.setEventType(eventType);
			orderWaitEvent.setEventStatus(eventStatus);
			orderWaitEvent.setTriggerTime(triggerTime);
			orderWaitEvent.setEventResult(eventResult);
			orderWaitEvent.setRefundCode(refundCode);
			orderWaitEvent.setId(id);
			orderWaitEvent.setCreatedBy(createBy);
			orderWaitEvent.setLastModifiedBy(lastModifiedBy);
			orderWaitEvent.setCreateTime(createTime);
			orderWaitEvent.setLastModifiedTime(lastModifiedTime);
			orderWaitEvent.setVersion(version);
			orderWaitEvent.setDelFlag(delFlag);
			return orderWaitEvent;
		}
	}
}
