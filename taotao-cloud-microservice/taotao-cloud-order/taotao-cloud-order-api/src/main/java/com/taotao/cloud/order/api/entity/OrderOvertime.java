package com.taotao.cloud.order.api.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 订单超时信息表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:44
 */
@Entity
@TableName(OrderOvertime.TABLE_NAME)
@Table(name = OrderOvertime.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderOvertime.TABLE_NAME, comment = "订单超时信息表")
public class OrderOvertime extends BaseSuperEntity<Long> {

	public static final String TABLE_NAME = "order_overtime";

    /**
     * 收货人姓名
     */
    @Column(name = "receiver_name", columnDefinition = "varchar(32) not null comment '收货人姓名'")
    private String receiverName;

    /**
     * 收货人电话
     */
    @Column(name = "receiver_phone", columnDefinition = "varchar(32) not null comment '收货人电话'")
    private String receiverPhone;

    /**
     * 支付时间--支付成功后的时间
     */
    @Column(name = "pay_success_time", columnDefinition = "TIMESTAMP comment '支付时间--支付成功后的时间'")
    private LocalDateTime paySuccessTime;

    /**
     * 超时类型
     */
    @Column(name = "type", columnDefinition = "int not null default 0 comment '超时类型 0-未支付超时 1-未处理售后超时'")
    private Integer type;

    /**
     * 超时时间
     */
    @Column(name = "over_time", columnDefinition = "TIMESTAMP not null comment '超时时间'")
    private LocalDateTime overTime;

	@Override
	public String toString() {
		return "OrderOvertime{" +
			"receiverName='" + receiverName + '\'' +
			", receiverPhone='" + receiverPhone + '\'' +
			", paySuccessTime=" + paySuccessTime +
			", type=" + type +
			", overTime=" + overTime +
			"} " + super.toString();
	}

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
		OrderOvertime that = (OrderOvertime) o;
		return Objects.equals(receiverName, that.receiverName) && Objects.equals(
			receiverPhone, that.receiverPhone) && Objects.equals(paySuccessTime,
			that.paySuccessTime) && Objects.equals(type, that.type)
			&& Objects.equals(overTime, that.overTime);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), receiverName, receiverPhone, paySuccessTime, type,
			overTime);
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverPhone() {
		return receiverPhone;
	}

	public void setReceiverPhone(String receiverPhone) {
		this.receiverPhone = receiverPhone;
	}

	public LocalDateTime getPaySuccessTime() {
		return paySuccessTime;
	}

	public void setPaySuccessTime(LocalDateTime paySuccessTime) {
		this.paySuccessTime = paySuccessTime;
	}

	public Integer getType() {
		return type;
	}

	public void setType(Integer type) {
		this.type = type;
	}

	public LocalDateTime getOverTime() {
		return overTime;
	}

	public void setOverTime(LocalDateTime overTime) {
		this.overTime = overTime;
	}

	public  OrderOvertime(){}

	public OrderOvertime(String receiverName, String receiverPhone,
		LocalDateTime paySuccessTime, Integer type, LocalDateTime overTime) {
		this.receiverName = receiverName;
		this.receiverPhone = receiverPhone;
		this.paySuccessTime = paySuccessTime;
		this.type = type;
		this.overTime = overTime;
	}

	public static OrderOvertimeBuilder builder() {
		return new OrderOvertimeBuilder();
	}

	public static final class OrderOvertimeBuilder {

		private String receiverName;
		private String receiverPhone;
		private LocalDateTime paySuccessTime;
		private Integer type;
		private LocalDateTime overTime;
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private OrderOvertimeBuilder() {
		}



		public OrderOvertimeBuilder receiverName(String receiverName) {
			this.receiverName = receiverName;
			return this;
		}

		public OrderOvertimeBuilder receiverPhone(String receiverPhone) {
			this.receiverPhone = receiverPhone;
			return this;
		}

		public OrderOvertimeBuilder paySuccessTime(LocalDateTime paySuccessTime) {
			this.paySuccessTime = paySuccessTime;
			return this;
		}

		public OrderOvertimeBuilder type(Integer type) {
			this.type = type;
			return this;
		}

		public OrderOvertimeBuilder overTime(LocalDateTime overTime) {
			this.overTime = overTime;
			return this;
		}

		public OrderOvertimeBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public OrderOvertimeBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public OrderOvertimeBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public OrderOvertimeBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public OrderOvertimeBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public OrderOvertimeBuilder version(int version) {
			this.version = version;
			return this;
		}

		public OrderOvertimeBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public OrderOvertime build() {
			OrderOvertime orderOvertime = new OrderOvertime();
			orderOvertime.setReceiverName(receiverName);
			orderOvertime.setReceiverPhone(receiverPhone);
			orderOvertime.setPaySuccessTime(paySuccessTime);
			orderOvertime.setType(type);
			orderOvertime.setOverTime(overTime);
			orderOvertime.setId(id);
			orderOvertime.setCreatedBy(createBy);
			orderOvertime.setLastModifiedBy(lastModifiedBy);
			orderOvertime.setCreateTime(createTime);
			orderOvertime.setLastModifiedTime(lastModifiedTime);
			orderOvertime.setVersion(version);
			orderOvertime.setDelFlag(delFlag);
			return orderOvertime;
		}
	}
}
