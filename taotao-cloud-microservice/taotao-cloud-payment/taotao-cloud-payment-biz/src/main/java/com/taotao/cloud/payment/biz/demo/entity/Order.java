package com.taotao.cloud.payment.biz.demo.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.io.Serializable;

@Table(name = "pay_order")
public class Order implements Serializable {

	private static final long serialVersionUID = -456050143121335002L;

	@Id
	@Column(name = "id")
	private Integer id;

	@Column(name = "order_no")
	private String orderNo;

	@Column(name = "body")
	private String body;

	@Column(name = "money")
	private String money;

	@Column(name = "status")
	private Integer status;

	@Column(name = "pay_no")
	private String payNo;

	@Column(name = "pay_time")
	private String payTime;

	@Column(name = "add_time")
	private String addTime;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getMoney() {
		return money;
	}

	public void setMoney(String money) {
		this.money = money;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public String getPayNo() {
		return payNo;
	}

	public void setPayNo(String payNo) {
		this.payNo = payNo;
	}

	public String getPayTime() {
		return payTime;
	}

	public void setPayTime(String payTime) {
		this.payTime = payTime;
	}

	public String getAddTime() {
		return addTime;
	}

	public void setAddTime(String addTime) {
		this.addTime = addTime;
	}

}
