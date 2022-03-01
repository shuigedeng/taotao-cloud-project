package com.taotao.cloud.sys.biz.activiti.pagemodel;

import java.math.BigDecimal;
import java.util.Date;

public class PurchaseTask {
	Integer bussinesskey;
	String applyer;
	String itemlist;
	BigDecimal total;
	String applytime;
	String taskid;
	String taskname;
	String processinstanceid;
	String processdefid;
	public int getBussinesskey() {
		return bussinesskey;
	}
	public void setBussinesskey(int bussinesskey) {
		this.bussinesskey = bussinesskey;
	}
	public String getApplyer() {
		return applyer;
	}
	public void setApplyer(String applyer) {
		this.applyer = applyer;
	}
	public String getItemlist() {
		return itemlist;
	}
	public void setItemlist(String itemlist) {
		this.itemlist = itemlist;
	}
	public BigDecimal getTotal() {
		return total;
	}
	public void setTotal(BigDecimal total) {
		this.total = total;
	}
	
	public String getApplytime() {
		return applytime;
	}
	public void setApplytime(String applytime) {
		this.applytime = applytime;
	}
	public void setBussinesskey(Integer bussinesskey) {
		this.bussinesskey = bussinesskey;
	}
	public String getTaskid() {
		return taskid;
	}
	public void setTaskid(String taskid) {
		this.taskid = taskid;
	}
	public String getTaskname() {
		return taskname;
	}
	public void setTaskname(String taskname) {
		this.taskname = taskname;
	}
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public String getProcessdefid() {
		return processdefid;
	}
	public void setProcessdefid(String processdefid) {
		this.processdefid = processdefid;
	}
	
}
