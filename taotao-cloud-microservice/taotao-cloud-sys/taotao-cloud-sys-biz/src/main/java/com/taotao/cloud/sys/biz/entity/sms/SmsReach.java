package com.taotao.cloud.sys.biz.entity.sms;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 短信任务
 *
 * @author Chopper
 * @since 2021/1/30 4:13 下午
 */
@Entity
@Table(name = SmsReach.TABLE_NAME)
@TableName(SmsReach.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SmsReach.TABLE_NAME, comment = "短信任务表")
public class SmsReach extends BaseSuperEntity<SmsReach, Long> {

	public static final String TABLE_NAME = "tt_sys_sms_reach";

	@Column(name = "sign_name", nullable = false, columnDefinition = "varchar(2000) not null comment '签名名称'")
	private String signName;

	@Column(name = "sms_name", nullable = false, columnDefinition = "varchar(2000) not null comment '模板名称'")
	private String smsName;

	@Column(name = "message_code", nullable = false, columnDefinition = "varchar(2000) not null comment '消息CODE'")
	private String messageCode;

	@Column(name = "context", nullable = false, columnDefinition = "varchar(2000) not null comment '消息内容'")
	private String context;

	@Column(name = "sms_range", nullable = false, columnDefinition = "varchar(2000) not null comment '接收人 1:全部会员，2：选择会员 '")
	private String smsRange;

	@Column(name = "num", nullable = false, columnDefinition = "varchar(2000) not null comment '预计发送条数'")
	private String num;

	public String getSignName() {
		return signName;
	}

	public void setSignName(String signName) {
		this.signName = signName;
	}

	public String getSmsName() {
		return smsName;
	}

	public void setSmsName(String smsName) {
		this.smsName = smsName;
	}

	public String getMessageCode() {
		return messageCode;
	}

	public void setMessageCode(String messageCode) {
		this.messageCode = messageCode;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getSmsRange() {
		return smsRange;
	}

	public void setSmsRange(String smsRange) {
		this.smsRange = smsRange;
	}

	public String getNum() {
		return num;
	}

	public void setNum(String num) {
		this.num = num;
	}
}
