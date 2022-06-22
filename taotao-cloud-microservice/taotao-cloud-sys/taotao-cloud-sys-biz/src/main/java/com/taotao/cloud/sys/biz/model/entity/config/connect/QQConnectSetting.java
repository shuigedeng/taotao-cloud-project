package com.taotao.cloud.sys.biz.model.entity.config.connect;

import java.util.List;
import lombok.Data;

/**
 * QQ联合登录设置
 */
@Data
public class QQConnectSetting {

	/**
	 * qq联合登陆配置
	 */
	List<QQConnectSettingItem> qqConnectSettingItemList;

}
