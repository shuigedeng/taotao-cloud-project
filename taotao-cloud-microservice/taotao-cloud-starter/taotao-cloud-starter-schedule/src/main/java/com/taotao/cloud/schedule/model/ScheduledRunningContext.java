package com.taotao.cloud.schedule.model;

import java.io.Serializable;

/**
 * ScheduledRunningContext
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:16:15
 */
public class ScheduledRunningContext implements Serializable {

	private static final long serialVersionUID = 2525367910036678105L;

	private Boolean callOff = false;

	private String callOffRemark;

	public Boolean getCallOff() {
		return callOff;
	}

	public void setCallOff(Boolean callOff) {
		this.callOff = callOff;
	}

	public String getCallOffRemark() {
		return callOffRemark;
	}

	public void setCallOffRemark(String callOffRemark) {
		this.callOffRemark = callOffRemark;
	}
}
