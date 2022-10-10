package com.taotao.cloud.message.biz.model.pojo;

import java.io.Serializable;
import lombok.Data;

/**
 * 提现配置
 */
@Data
public class WithdrawalSetting implements Serializable {

	private static final long serialVersionUID = -3872782530832122976L;
	/**
	 * 提现是否需要申请
	 */
	private Boolean apply;
}
