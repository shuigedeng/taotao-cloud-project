package com.taotao.cloud.order.biz.entity.order;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.*;

/**
 * 售后退款操作记录表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:49
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@TableName(OrderRefundReqRecord.TABLE_NAME)
@Table(name = OrderRefundReqRecord.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderRefundReqRecord.TABLE_NAME, comment = "售后退款操作记录表")
public class OrderRefundReqRecord extends BaseSuperEntity<OrderRefundReqRecord,Long> {

	public static final String TABLE_NAME = "order_refund_req_record";

	@Column(name = "order_code", columnDefinition = "varchar(32) not null comment '订单编码'")
	private String orderCode;

	@Column(name = "item_code", columnDefinition = "varchar(32) not null comment '订单编码'")
	private String itemCode;

	/**
	 * 标题
	 */
	@Column(name = "title", columnDefinition = "varchar(32) not null comment '标题'")
	private String title;

	/**
	 * 备注
	 */
	@Column(name = "remark", columnDefinition = "varchar(3200) null comment '备注'")
	private String remark;

	/**
	 * 操作人名称
	 */
	@Column(name = "create_name", columnDefinition = "varchar(32) not null comment '操作人名称'")
	private String createName;

	/**
	 * 操作人id
	 */
	@Column(name = "create_id", columnDefinition = "varchar(32) not null comment '操作人id'")
	private String createId;

	/**
	 * 操作人昵称
	 */
	@Column(name = "create_nick", columnDefinition = "varchar(32) not null comment '操作人昵称'")
	private String createNick;

	/**
	 * 扩展信息
	 */
	@Column(name = "ext", columnDefinition = "text null comment '扩展信息'")
	private String ext;

	@Column(name = "req_record_type", columnDefinition = "int not null default 0 comment '记录类型'")
	private Integer reqRecordType;

	/**
	 * 创建时间
	 */
	@Column(name = "create_Date", columnDefinition = "TIMESTAMP comment '创建时间'")
	private LocalDateTime createDate;
}
