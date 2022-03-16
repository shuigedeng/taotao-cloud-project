package com.taotao.cloud.sys.biz.entity.verification.entity.dos;

import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.sys.biz.entity.verification.entity.enums.VerificationSourceEnum;
import lombok.Data;

/**
 * 验证码资源维护
 */
@Data
@TableName("li_verification_source")
public class VerificationSource extends BaseEntity {

	private static final long serialVersionUID = 1L;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 名称
	 */
	private String resource;

	/**
	 * "验证码资源类型 SLIDER/SOURCE
	 *
	 * @see VerificationSourceEnum
	 */
	private String type;
}
