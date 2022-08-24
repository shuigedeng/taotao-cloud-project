package com.taotao.cloud.sign.properties;

import com.taotao.cloud.sign.advice.SignAspect;
import java.util.Arrays;
import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;

/**
 * 数字证书配置
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-06 14:43:19
 */
@RefreshScope
@ConfigurationProperties(prefix = SignProperties.PREFIX)
public class SignProperties {

	public static final String PREFIX = "taotao.cloud.web.sign";

	private boolean enabled = false;

	/**
	 * 数字证书自定义key
	 */
	private String key;

	/**
	 * 自定义数字证书 过滤值 默认为"token","sign","dataSecret"
	 */
	private List<String> ignore = Arrays.asList(SignAspect.TOKEN_HEADER, SignAspect.SIGN_HEADER,
		SignAspect.DATA_SECRET_HEADER);

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public List<String> getIgnore() {
		return ignore;
	}

	public void setIgnore(List<String> ignore) {
		this.ignore = ignore;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
}
