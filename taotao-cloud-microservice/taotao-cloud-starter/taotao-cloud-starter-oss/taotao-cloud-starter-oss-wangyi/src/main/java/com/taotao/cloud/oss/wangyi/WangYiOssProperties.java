/**
 * $Id: WangYiOssProperties.java,v 1.0 2022/3/4 9:51 PM chenmin Exp $
 */
package com.taotao.cloud.oss.wangyi;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

import java.util.HashMap;
import java.util.Map;

/**
 * 王毅oss属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:44:08
 */
@RefreshScope
@ConfigurationProperties(WangYiOssProperties.PREFIX)
public class WangYiOssProperties extends WangYiOssConfig implements InitializingBean {

	public static final String PREFIX = "taotao.cloud.oss.wangyi";

    private Map<String, WangYiOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(WangYiOssConfig::init);
        }
    }

	public Map<String, WangYiOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, WangYiOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
