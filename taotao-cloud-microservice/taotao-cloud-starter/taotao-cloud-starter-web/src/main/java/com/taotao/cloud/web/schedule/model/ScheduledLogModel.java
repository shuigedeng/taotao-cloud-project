package com.taotao.cloud.web.schedule.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;


/**
 * ScheduledLog
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-09 17:04:41
 */
public class ScheduledLogModel implements Serializable {

	private static final long serialVersionUID = 2525367910036678105L;

	private String superScheduledName;

	private Date statrDate;

	private Date endDate;

	private Exception exception;

	private Long executionTime;

	private Boolean isSuccess;

	private ScheduledJobModel scheduledJobModel;

	private String fileName;

	public String getSuperScheduledName() {
		return superScheduledName;
	}

	public void setSuperScheduledName(String superScheduledName) {
		this.superScheduledName = superScheduledName;
	}

	public Date getStatrDate() {
		return statrDate;
	}

	public void setStatrDate(Date statrDate) {
		this.statrDate = statrDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public Exception getException() {
		return exception;
	}

	public void setException(Exception exception) {
		this.exception = exception;
	}

	public Long getExecutionTime() {
		return executionTime;
	}

	public void setExecutionTime(Long executionTime) {
		this.executionTime = executionTime;
	}

	public Boolean getSuccess() {
		return isSuccess;
	}

	public void setSuccess(Boolean success) {
		isSuccess = success;
	}

	public ScheduledJobModel getScheduledSource() {
		return scheduledJobModel;
	}

	public void setScheduledSource(ScheduledJobModel scheduledJobModel) {
		this.scheduledJobModel = scheduledJobModel;
	}

	public String getFileName() {
		return fileName;
	}

	public void generateFileName() {
		this.fileName =
			superScheduledName + DateTimeFormatter.ofPattern("yyyyMMdd").format(LocalDate.now());
	}

	public void computingTime() {
		this.executionTime = this.getEndDate().getTime() - this.statrDate.getTime();
	}
}
