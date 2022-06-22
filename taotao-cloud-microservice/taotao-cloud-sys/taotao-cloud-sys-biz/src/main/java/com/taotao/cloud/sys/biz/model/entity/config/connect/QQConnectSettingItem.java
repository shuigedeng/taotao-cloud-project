package com.taotao.cloud.sys.biz.model.entity.config.connect;


import lombok.Data;

/**
 * QQ联合登录具体配置
 */
@Data
public class QQConnectSettingItem {

	private String clientType;

	private String appId;

	private String appKey;

}
