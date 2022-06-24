package com.taotao.cloud.logger.mztlog.web.pojo;


import com.taotao.cloud.logger.mztlog.starter.annotation.DiffLogField;

import java.util.Date;
import java.util.List;

public class Order {
	@DiffLogField(name = "订单ID", function = "ORDER")
	private Long orderId;
	@DiffLogField(name = "订单号")
	private String orderNo;
	private String purchaseName;
	private String productName;
	@DiffLogField(name = "创建时间")
	private Date createTime;

	@DiffLogField(name = "创建人")
	private UserDO creator;
	@DiffLogField(name = "更新人")
	private UserDO updater;
	@DiffLogField(name = "列表项", function = "ORDER")
	private List<String> items;

	public static class UserDO {
		@DiffLogField(name = "用户ID")
		private Long userId;
		@DiffLogField(name = "用户姓名")
		private String userName;

		public Long getUserId() {
			return userId;
		}

		public void setUserId(Long userId) {
			this.userId = userId;
		}

		public String getUserName() {
			return userName;
		}

		public void setUserName(String userName) {
			this.userName = userName;
		}
	}

	public Order() {
	}

	public Order(Long orderId, String orderNo, String purchaseName, String productName, Date createTime, UserDO creator, UserDO updater, List<String> items) {
		this.orderId = orderId;
		this.orderNo = orderNo;
		this.purchaseName = purchaseName;
		this.productName = productName;
		this.createTime = createTime;
		this.creator = creator;
		this.updater = updater;
		this.items = items;
	}

	public Long getOrderId() {
		return orderId;
	}

	public void setOrderId(Long orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getPurchaseName() {
		return purchaseName;
	}

	public void setPurchaseName(String purchaseName) {
		this.purchaseName = purchaseName;
	}

	public String getProductName() {
		return productName;
	}

	public void setProductName(String productName) {
		this.productName = productName;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public UserDO getCreator() {
		return creator;
	}

	public void setCreator(UserDO creator) {
		this.creator = creator;
	}

	public UserDO getUpdater() {
		return updater;
	}

	public void setUpdater(UserDO updater) {
		this.updater = updater;
	}

	public List<String> getItems() {
		return items;
	}

	public void setItems(List<String> items) {
		this.items = items;
	}
}
