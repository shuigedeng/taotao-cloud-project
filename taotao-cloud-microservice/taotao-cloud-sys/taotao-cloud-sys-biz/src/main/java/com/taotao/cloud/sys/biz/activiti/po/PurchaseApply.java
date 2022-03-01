package com.taotao.cloud.sys.biz.activiti.po;

import java.math.BigDecimal;
import java.util.Date;

import org.activiti.engine.task.Task;

import com.alibaba.fastjson.annotation.JSONField;

public class PurchaseApply {
	Integer id;
	String itemlist;
	BigDecimal total;
	String applytime;
	String applyer;
	Task task;
	
	String processinstanceid;
	
	String activityid;
	
	String state;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
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
	public String getApplyer() {
		return applyer;
	}
	public void setApplyer(String applyer) {
		this.applyer = applyer;
	}
	public Task getTask() {
		return task;
	}
	public void setTask(Task task) {
		this.task = task;
	}
	
	public String getProcessinstanceid() {
		return processinstanceid;
	}
	public void setProcessinstanceid(String processinstanceid) {
		this.processinstanceid = processinstanceid;
	}
	public String getActivityid() {
		return activityid;
	}
	public void setActivityid(String activityid) {
		this.activityid = activityid;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	
}
