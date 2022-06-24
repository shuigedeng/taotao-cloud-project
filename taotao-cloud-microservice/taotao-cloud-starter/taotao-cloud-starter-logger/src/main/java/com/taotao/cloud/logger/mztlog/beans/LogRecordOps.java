package com.taotao.cloud.logger.mztlog.beans;

public class LogRecordOps {
    private String successLogTemplate;
    private String failLogTemplate;
    private String operatorId;
    private String type;
    private String bizNo;
    private String subType;
    private String extra;
    private String condition;
    private boolean isBatch;



	public String getSuccessLogTemplate() {
		return successLogTemplate;
	}

	public void setSuccessLogTemplate(String successLogTemplate) {
		this.successLogTemplate = successLogTemplate;
	}

	public String getFailLogTemplate() {
		return failLogTemplate;
	}

	public void setFailLogTemplate(String failLogTemplate) {
		this.failLogTemplate = failLogTemplate;
	}

	public String getOperatorId() {
		return operatorId;
	}

	public void setOperatorId(String operatorId) {
		this.operatorId = operatorId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getBizNo() {
		return bizNo;
	}

	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public String getCondition() {
		return condition;
	}

	public void setCondition(String condition) {
		this.condition = condition;
	}

	public boolean isBatch() {
		return isBatch;
	}

	public void setBatch(boolean batch) {
		isBatch = batch;
	}

	public static LogRecordOpsBuilder builder() {
		return new LogRecordOpsBuilder();
	}

	public static final class LogRecordOpsBuilder {
		private String successLogTemplate;
		private String failLogTemplate;
		private String operatorId;
		private String type;
		private String bizNo;
		private String subType;
		private String extra;
		private String condition;
		private boolean isBatch;

		private LogRecordOpsBuilder() {
		}



		public LogRecordOpsBuilder successLogTemplate(String successLogTemplate) {
			this.successLogTemplate = successLogTemplate;
			return this;
		}

		public LogRecordOpsBuilder failLogTemplate(String failLogTemplate) {
			this.failLogTemplate = failLogTemplate;
			return this;
		}

		public LogRecordOpsBuilder operatorId(String operatorId) {
			this.operatorId = operatorId;
			return this;
		}

		public LogRecordOpsBuilder type(String type) {
			this.type = type;
			return this;
		}

		public LogRecordOpsBuilder bizNo(String bizNo) {
			this.bizNo = bizNo;
			return this;
		}

		public LogRecordOpsBuilder subType(String subType) {
			this.subType = subType;
			return this;
		}

		public LogRecordOpsBuilder extra(String extra) {
			this.extra = extra;
			return this;
		}

		public LogRecordOpsBuilder condition(String condition) {
			this.condition = condition;
			return this;
		}

		public LogRecordOpsBuilder isBatch(boolean isBatch) {
			this.isBatch = isBatch;
			return this;
		}

		public LogRecordOps build() {
			LogRecordOps logRecordOps = new LogRecordOps();
			logRecordOps.setSuccessLogTemplate(successLogTemplate);
			logRecordOps.setFailLogTemplate(failLogTemplate);
			logRecordOps.setOperatorId(operatorId);
			logRecordOps.setType(type);
			logRecordOps.setBizNo(bizNo);
			logRecordOps.setSubType(subType);
			logRecordOps.setExtra(extra);
			logRecordOps.setCondition(condition);
			logRecordOps.isBatch = this.isBatch;
			return logRecordOps;
		}
	}
}
