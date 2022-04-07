package com.taotao.cloud.sys.biz.entity.sms;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 短信任务
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
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
}
