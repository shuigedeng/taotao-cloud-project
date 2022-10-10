package com.taotao.cloud.order.biz.model.entity.order;

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
import java.io.Serial;
import java.util.Objects;


/**
 * 订单交易投诉通信表
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-28 09:01:29
 */
@Getter
@Setter
@ToString(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = OrderComplaintCommunication.TABLE_NAME)
@TableName(OrderComplaintCommunication.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderComplaintCommunication.TABLE_NAME, comment = "订单交易投诉通信表")
public class OrderComplaintCommunication extends BaseSuperEntity<OrderComplaintCommunication, Long> {

	public static final String TABLE_NAME = "tt_order_complaint_communication";

	@Serial
	private static final long serialVersionUID = -2384351827382795547L;

	/**
	 * 投诉id
	 */
	@Column(name = "complain_id", columnDefinition = "varchar(64) not null comment '投诉id'")
	private Long complainId;
	/**
	 * 对话内容
	 */
	@Column(name = "content", columnDefinition = "varchar(64) not null comment '对话内容'")
	private String content;
	/**
	 * 所属，买家/卖家
	 */
	@Column(name = "owner", columnDefinition = "varchar(64) not null comment '所属，买家/卖家'")
	private String owner;
	/**
	 * 对话所属名称
	 */
	@Column(name = "owner_name", columnDefinition = "varchar(64) not null comment '对话所属名称'")
	private String ownerName;
	/**
	 * 对话所属id,卖家id/买家id
	 */
	@Column(name = "owner_id", columnDefinition = "varchar(64) not null comment '对话所属id,卖家id/买家id'")
	private Long ownerId;

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
			return false;
		}
		OrderComplaintCommunication that = (OrderComplaintCommunication) o;
		return getId() != null && Objects.equals(getId(), that.getId());
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}
}
