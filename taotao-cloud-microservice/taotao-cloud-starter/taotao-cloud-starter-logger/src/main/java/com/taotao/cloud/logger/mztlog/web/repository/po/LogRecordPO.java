package com.taotao.cloud.logger.mztlog.web.repository.po;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.google.common.collect.Lists;
import com.taotao.cloud.logger.mztlog.beans.CodeVariableType;
import com.taotao.cloud.logger.mztlog.beans.LogRecord;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.validator.constraints.Length;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;
import java.util.Map;

@TableName("t_logrecord")
public class LogRecordPO {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;
    /**
     * 租户
     */
    private String tenant;

    /**
     * 保存的操作日志的类型，比如：订单类型、商品类型
     */
    @NotBlank(message = "type required")
    @Length(max = 200, message = "type max length is 200")
    private String type;
    /**
     * 日志的子类型，比如订单的C端日志，和订单的B端日志，type都是订单类型，但是子类型不一样
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
     */
    private String extra;

    private String codeVariable;

    public static LogRecordPO from(LogRecord logRecord) {
        LogRecordPO logRecordPO = new LogRecordPO();
        BeanUtils.copyProperties(logRecord, logRecordPO);
        logRecordPO.setCodeVariable(JSONUtil.toJsonStr(logRecord.getCodeVariable()));
        return logRecordPO;
    }

    public static List<LogRecord> from(List<LogRecordPO> logRecordPOS) {
        List<LogRecord> ret = Lists.newArrayListWithCapacity(logRecordPOS.size());
        for (LogRecordPO logRecordPO : logRecordPOS) {
            ret.add(toLogRecord(logRecordPO));
        }
        return ret;
    }

    private static LogRecord toLogRecord(LogRecordPO logRecordPO) {
        LogRecord logRecord = new LogRecord();
        BeanUtils.copyProperties(logRecordPO, logRecord);
        if (StringUtils.isNotBlank(logRecordPO.getCodeVariable())) {
            Map<CodeVariableType, Object> toBean = JSONUtil.toBean(logRecordPO.getCodeVariable(),
                    new TypeReference<Map<CodeVariableType, Object>>() {
                    }, true);
            logRecord.setCodeVariable(toBean);
        }
        return logRecord;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
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

	public String getCodeVariable() {
		return codeVariable;
	}

	public void setCodeVariable(String codeVariable) {
		this.codeVariable = codeVariable;
	}
}
