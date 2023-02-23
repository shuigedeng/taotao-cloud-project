package com.taotao.cloud.sys.biz.model.pojo.connect;

import lombok.Data;

/**
 * 微信设置
 */
@Data
public class WechatConnectSettingItem {

	/**
	 * @see ClientType
	 */
	private String clientType;

	private String appId;

	private String appSecret;
}
