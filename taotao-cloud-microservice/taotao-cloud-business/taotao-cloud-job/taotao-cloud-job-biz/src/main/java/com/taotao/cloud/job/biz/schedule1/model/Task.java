package com.taotao.cloud.job.biz.schedule1.model;


import java.util.Date;

public class Task {

	//id
	private String id;

	//任务名
	private String name;

	/*
		目标字符串
		格式bean.method(params)
		String字符串类型，包含'、boolean布尔类型，等于true或者false
		long长整形，包含L、double浮点类型，包含D、其他类型归类为整形
		aa.aa('String',100L,20.20D)
	 */
	private String invokeTarget;

	//周期(month、week、day、hour、minute、secods)
	private String cycle;
	//cron表达式
	private String cronExpression;

	//执行策略(1手动，2-自动）
	private Integer policy;

	//状态（0正常 1暂停）
	private Integer status;
	/**
	 * 删除标志（0-存在，1-删除）
	 */
	private Integer delFlag;

	//执行情况(1-执行中,2-已暂停)
	private Integer situation;

	//执行版本(每执行一次加一)
	private Integer version;

	//上次执行时间
	private Date lastRunTime;

	//下次执行时间
	private Date nextRunTime;

	private String createBy;

	private Date createTime;

	private String updateBy;

	private Date updateTime;

	//备注
	private String remark;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInvokeTarget() {
		return invokeTarget;
	}

	public void setInvokeTarget(String invokeTarget) {
		this.invokeTarget = invokeTarget;
	}

	public String getCronExpression() {
		return cronExpression;
	}

	public void setCronExpression(String cronExpression) {
		this.cronExpression = cronExpression;
	}

	public Integer getPolicy() {
		return policy;
	}

	public void setPolicy(Integer policy) {
		this.policy = policy;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getDelFlag() {
		return delFlag;
	}

	public void setDelFlag(Integer delFlag) {
		this.delFlag = delFlag;
	}

	public String getCycle() {
		return cycle;
	}

	public void setCycle(String cycle) {
		this.cycle = cycle;
	}

	public Date getNextRunTime() {
		return nextRunTime;
	}

	public void setNextRunTime(Date nextRunTime) {
		this.nextRunTime = nextRunTime;
	}

	public Date getLastRunTime() {
		return lastRunTime;
	}

	public void setLastRunTime(Date lastRunTime) {
		this.lastRunTime = lastRunTime;
	}

	public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public Integer getSituation() {
		return situation;
	}

	public void setSituation(Integer situation) {
		this.situation = situation;
	}
}
