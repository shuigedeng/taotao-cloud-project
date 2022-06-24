package com.taotao.cloud.logger.mztlog.beans;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

public class LogRecord {
	/**
	 * id
	 */
	private Serializable id;
	/**
	 * 租户
	 */
	private String tenant;

	/**
	 * 保存的操作日志的类型，比如：订单类型、商品类型
	 *
	 * @since 2.0.0 从 prefix 修改为了type
	 */
	@NotBlank(message = "type required")
	@Length(max = 200, message = "type max length is 200")
	private String type;
	/**
	 * 日志的子类型，比如订单的C端日志，和订单的B端日志，type都是订单类型，但是子类型不一样
	 *
	 * @since 2.0.0 从 category 修改为 subtype
	 */
	private String subType;

	/**
	 * 日志绑定的业务标识
	 */
	@NotBlank(message = "bizNo required")
	@Length(max = 200, message = "bizNo max length is 200")
	private String bizNo;
	/**
	 * 操作人
	 */
	@NotBlank(message = "operator required")
	@Length(max = 63, message = "operator max length 63")
	private String operator;

	/**
	 * 日志内容
	 */
	@NotBlank(message = "opAction required")
	@Length(max = 511, message = "operator max length 511")
	private String action;
	/**
	 * 记录是否是操作失败的日志
	 */
	private boolean fail;
	/**
	 * 日志的创建时间
	 */
	private Date createTime;
	/**
	 * 日志的额外信息
	 *
	 * @since 2.0.0 从detail 修改为extra
	 */
	private String extra;

	/**
	 * 打印日志的代码信息
	 * CodeVariableType 日志记录的ClassName、MethodName
	 */
	private Map<CodeVariableType, Object> codeVariable;

	public Serializable getId() {
		return id;
	}

	public void setId(Serializable id) {
		this.id = id;
	}

	public String getTenant() {
		return tenant;
	}

	public void setTenant(String tenant) {
		this.tenant = tenant;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSubType() {
		return subType;
	}

	public void setSubType(String subType) {
		this.subType = subType;
	}

	public String getBizNo() {
		return bizNo;
	}

	public void setBizNo(String bizNo) {
		this.bizNo = bizNo;
	}

	public String getOperator() {
		return operator;
	}

	public void setOperator(String operator) {
		this.operator = operator;
	}

	public String getAction() {
		return action;
	}

	public void setAction(String action) {
		this.action = action;
	}

	public boolean isFail() {
		return fail;
	}

	public void setFail(boolean fail) {
		this.fail = fail;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public Map<CodeVariableType, Object> getCodeVariable() {
		return codeVariable;
	}

	public void setCodeVariable(Map<CodeVariableType, Object> codeVariable) {
		this.codeVariable = codeVariable;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		LogRecord logRecord = (LogRecord) o;
		return fail == logRecord.fail && Objects.equals(id, logRecord.id) && Objects.equals(tenant, logRecord.tenant) && Objects.equals(type, logRecord.type) && Objects.equals(subType, logRecord.subType) && Objects.equals(bizNo, logRecord.bizNo) && Objects.equals(operator, logRecord.operator) && Objects.equals(action, logRecord.action) && Objects.equals(createTime, logRecord.createTime) && Objects.equals(extra, logRecord.extra) && Objects.equals(codeVariable, logRecord.codeVariable);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, tenant, type, subType, bizNo, operator, action, fail, createTime, extra, codeVariable);
	}

	@Override
	public String toString() {
		return "LogRecord{" +
			"id=" + id +
			", tenant='" + tenant + '\'' +
			", type='" + type + '\'' +
			", subType='" + subType + '\'' +
			", bizNo='" + bizNo + '\'' +
			", operator='" + operator + '\'' +
			", action='" + action + '\'' +
			", fail=" + fail +
			", createTime=" + createTime +
			", extra='" + extra + '\'' +
			", codeVariable=" + codeVariable +
			'}';
	}

	public static LogRecordBuilder builder() {
		return new LogRecordBuilder();
	}

	public static final class LogRecordBuilder {
		private Serializable id;
		private String tenant;
		private String type;
		private String subType;
		private String bizNo;
		private String operator;
		private String action;
		private boolean fail;
		private Date createTime;
		private String extra;
		private Map<CodeVariableType, Object> codeVariable;

		private LogRecordBuilder() {
		}

		public LogRecordBuilder id(Serializable id) {
			this.id = id;
			return this;
		}

		public LogRecordBuilder tenant(String tenant) {
			this.tenant = tenant;
			return this;
		}

		public LogRecordBuilder type(String type) {
			this.type = type;
			return this;
		}

		public LogRecordBuilder subType(String subType) {
			this.subType = subType;
			return this;
		}

		public LogRecordBuilder bizNo(String bizNo) {
			this.bizNo = bizNo;
			return this;
		}

		public LogRecordBuilder operator(String operator) {
			this.operator = operator;
			return this;
		}

		public LogRecordBuilder action(String action) {
			this.action = action;
			return this;
		}

		public LogRecordBuilder fail(boolean fail) {
			this.fail = fail;
			return this;
		}

		public LogRecordBuilder createTime(Date createTime) {
			this.createTime = createTime;
			return this;
		}

		public LogRecordBuilder extra(String extra) {
			this.extra = extra;
			return this;
		}

		public LogRecordBuilder codeVariable(Map<CodeVariableType, Object> codeVariable) {
			this.codeVariable = codeVariable;
			return this;
		}

		public LogRecord build() {
			LogRecord logRecord = new LogRecord();
			logRecord.setId(id);
			logRecord.setTenant(tenant);
			logRecord.setType(type);
			logRecord.setSubType(subType);
			logRecord.setBizNo(bizNo);
			logRecord.setOperator(operator);
			logRecord.setAction(action);
			logRecord.setFail(fail);
			logRecord.setCreateTime(createTime);
			logRecord.setExtra(extra);
			logRecord.setCodeVariable(codeVariable);
			return logRecord;
		}
	}
}
