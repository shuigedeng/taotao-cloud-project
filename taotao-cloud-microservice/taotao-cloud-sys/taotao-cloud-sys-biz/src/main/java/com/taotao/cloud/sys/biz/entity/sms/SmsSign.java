package com.taotao.cloud.sys.biz.entity.sms;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.BaseSuperEntity;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * 短信签名
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@Entity
@Table(name = SmsSign.TABLE_NAME)
@TableName(SmsSign.TABLE_NAME)
@org.hibernate.annotations.Table(appliesTo = SmsSign.TABLE_NAME, comment = "短信签名表")
public class SmsSign extends BaseSuperEntity<SmsSign, Long> {

	public static final String TABLE_NAME = "tt_sys_sms_sign";

	@Column(name = "sign_name", columnDefinition = "varchar(2000) not null comment '签名名称'")
	private String signName;

	@Column(name = "sign_source", columnDefinition = "varchar(2000) not null comment '签名来源'")
	private Integer signSource;

	@Column(name = "remark", columnDefinition = "varchar(2000) not null comment '短信签名申请说明'")
	private String remark;

	@Column(name = "business_license", columnDefinition = "varchar(2000) not null comment '营业执照'")
	private String businessLicense;

	@Column(name = "license", columnDefinition = "varchar(2000) not null comment '授权委托书'")
	private String license;

	@Column(name = "sign_status", columnDefinition =
		"int not null default 0 comment '签名审核状态  0：审核中。"
			+ "     * 1：审核通过。"
			+ "     * 2：审核失败，请在返回参数Reason中查看审核失败原因。'")
	private Integer signStatus;

	@Column(name = "reason", columnDefinition = "varchar(2000) not null comment '审核备注'")
	private String reason;
}
