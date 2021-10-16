package com.taotao.cloud.order.api.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import java.time.LocalDateTime;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 售后退款操作记录表
 *
 * @author shuigedeng
 * @since 2020/4/30 15:49
 */
@Entity
@TableName(OrderRefundReqRecord.TABLE_NAME)
@Table(name = OrderRefundReqRecord.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = OrderRefundReqRecord.TABLE_NAME, comment = "售后退款操作记录表")
public class OrderRefundReqRecord extends BaseSuperEntity<Long> {

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
	@Column(name = "ext", columnDefinition = "varchar(32) not null comment '扩展信息'")
	private String ext;

	@Column(name = "req_record_type", columnDefinition = "int not null default 0 comment '记录类型'")
	private Integer reqRecordType;

	/**
	 * 创建时间
	 */
	@Column(name = "create_Date", columnDefinition = "TIMESTAMP comment '创建时间'")
	private LocalDateTime createDate;

	@Override
	public String toString() {
		return "OrderRefundReqRecord{" +
			"orderCode='" + orderCode + '\'' +
			", itemCode='" + itemCode + '\'' +
			", title='" + title + '\'' +
			", remark='" + remark + '\'' +
			", createName='" + createName + '\'' +
			", createId='" + createId + '\'' +
			", createNick='" + createNick + '\'' +
			", ext='" + ext + '\'' +
			", reqRecordType=" + reqRecordType +
			", createDate=" + createDate +
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
		OrderRefundReqRecord that = (OrderRefundReqRecord) o;
		return Objects.equals(orderCode, that.orderCode) && Objects.equals(itemCode,
			that.itemCode) && Objects.equals(title, that.title)
			&& Objects.equals(remark, that.remark) && Objects.equals(createName,
			that.createName) && Objects.equals(createId, that.createId)
			&& Objects.equals(createNick, that.createNick) && Objects.equals(
			ext, that.ext) && Objects.equals(reqRecordType, that.reqRecordType)
			&& Objects.equals(createDate, that.createDate);
	}

	@Override
	public int hashCode() {
		return Objects.hash(super.hashCode(), orderCode, itemCode, title, remark, createName,
			createId,
			createNick, ext, reqRecordType, createDate);
	}

	public String getOrderCode() {
		return orderCode;
	}

	public void setOrderCode(String orderCode) {
		this.orderCode = orderCode;
	}

	public String getItemCode() {
		return itemCode;
	}

	public void setItemCode(String itemCode) {
		this.itemCode = itemCode;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public String getCreateId() {
		return createId;
	}

	public void setCreateId(String createId) {
		this.createId = createId;
	}

	public String getCreateNick() {
		return createNick;
	}

	public void setCreateNick(String createNick) {
		this.createNick = createNick;
	}

	public String getExt() {
		return ext;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public Integer getReqRecordType() {
		return reqRecordType;
	}

	public void setReqRecordType(Integer reqRecordType) {
		this.reqRecordType = reqRecordType;
	}

	public LocalDateTime getCreateDate() {
		return createDate;
	}

	public void setCreateDate(LocalDateTime createDate) {
		this.createDate = createDate;
	}

	public OrderRefundReqRecord(){}
	public OrderRefundReqRecord(String orderCode, String itemCode, String title,
		String remark, String createName, String createId, String createNick, String ext,
		Integer reqRecordType, LocalDateTime createDate) {
		this.orderCode = orderCode;
		this.itemCode = itemCode;
		this.title = title;
		this.remark = remark;
		this.createName = createName;
		this.createId = createId;
		this.createNick = createNick;
		this.ext = ext;
		this.reqRecordType = reqRecordType;
		this.createDate = createDate;
	}

	public static OrderRefundReqRecordBuilder builder() {
		return new OrderRefundReqRecordBuilder();
	}
	public static final class OrderRefundReqRecordBuilder {

		private String orderCode;
		private String itemCode;
		private String title;
		private String remark;
		private String createName;
		private String createId;
		private String createNick;
		private String ext;
		private Integer reqRecordType;
		private LocalDateTime createDate;
		private Long id;
		private Long createBy;
		private Long lastModifiedBy;
		private LocalDateTime createTime;
		private LocalDateTime lastModifiedTime;
		private int version = 1;
		private Boolean delFlag = false;

		private OrderRefundReqRecordBuilder() {
		}



		public OrderRefundReqRecordBuilder orderCode(String orderCode) {
			this.orderCode = orderCode;
			return this;
		}

		public OrderRefundReqRecordBuilder itemCode(String itemCode) {
			this.itemCode = itemCode;
			return this;
		}

		public OrderRefundReqRecordBuilder title(String title) {
			this.title = title;
			return this;
		}

		public OrderRefundReqRecordBuilder remark(String remark) {
			this.remark = remark;
			return this;
		}

		public OrderRefundReqRecordBuilder createName(String createName) {
			this.createName = createName;
			return this;
		}

		public OrderRefundReqRecordBuilder createId(String createId) {
			this.createId = createId;
			return this;
		}

		public OrderRefundReqRecordBuilder createNick(String createNick) {
			this.createNick = createNick;
			return this;
		}

		public OrderRefundReqRecordBuilder ext(String ext) {
			this.ext = ext;
			return this;
		}

		public OrderRefundReqRecordBuilder reqRecordType(Integer reqRecordType) {
			this.reqRecordType = reqRecordType;
			return this;
		}

		public OrderRefundReqRecordBuilder createDate(LocalDateTime createDate) {
			this.createDate = createDate;
			return this;
		}

		public OrderRefundReqRecordBuilder id(Long id) {
			this.id = id;
			return this;
		}

		public OrderRefundReqRecordBuilder createBy(Long createBy) {
			this.createBy = createBy;
			return this;
		}

		public OrderRefundReqRecordBuilder lastModifiedBy(Long lastModifiedBy) {
			this.lastModifiedBy = lastModifiedBy;
			return this;
		}

		public OrderRefundReqRecordBuilder createTime(LocalDateTime createTime) {
			this.createTime = createTime;
			return this;
		}

		public OrderRefundReqRecordBuilder lastModifiedTime(LocalDateTime lastModifiedTime) {
			this.lastModifiedTime = lastModifiedTime;
			return this;
		}

		public OrderRefundReqRecordBuilder version(int version) {
			this.version = version;
			return this;
		}

		public OrderRefundReqRecordBuilder delFlag(Boolean delFlag) {
			this.delFlag = delFlag;
			return this;
		}

		public OrderRefundReqRecord build() {
			OrderRefundReqRecord orderRefundReqRecord = new OrderRefundReqRecord();
			orderRefundReqRecord.setOrderCode(orderCode);
			orderRefundReqRecord.setItemCode(itemCode);
			orderRefundReqRecord.setTitle(title);
			orderRefundReqRecord.setRemark(remark);
			orderRefundReqRecord.setCreateName(createName);
			orderRefundReqRecord.setCreateId(createId);
			orderRefundReqRecord.setCreateNick(createNick);
			orderRefundReqRecord.setExt(ext);
			orderRefundReqRecord.setReqRecordType(reqRecordType);
			orderRefundReqRecord.setCreateDate(createDate);
			orderRefundReqRecord.setId(id);
			orderRefundReqRecord.setCreatedBy(createBy);
			orderRefundReqRecord.setLastModifiedBy(lastModifiedBy);
			orderRefundReqRecord.setCreateTime(createTime);
			orderRefundReqRecord.setLastModifiedTime(lastModifiedTime);
			orderRefundReqRecord.setVersion(version);
			orderRefundReqRecord.setDelFlag(delFlag);
			return orderRefundReqRecord;
		}
	}
}
