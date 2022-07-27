package com.taotao.cloud.web.version;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;


/**
 * RESTful API接口版本定义自动配置属性
 *
 * @author shuigedeng
 * @version 2022.06
 * @since 2022-07-27 20:20:50
 */
@ConfigurationProperties("taotao.cloud.web.api-version")
public class ApiVersionProperties {
	
	/**
	 * 是否启用 <code style="color:red">RESTful API接口版本控制</code>
	 * <p>
	 * 默认：true
	 */
	private boolean enabled = true;
	
	/**
	 * 最小版本号，小于该版本号返回版本过时。
	 */
	private double minimumVersion;
    
	/**
	 * {@link RequestMapping} 版本占位符，如下所示：
	 * <p>/{version}/user
	 * <p>/user/{version}
	 */
	private String versionPlaceholder = "{version}";

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	public double getMinimumVersion() {
		return minimumVersion;
	}

	public void setMinimumVersion(double minimumVersion) {
		this.minimumVersion = minimumVersion;
	}

	public String getVersionPlaceholder() {
		return versionPlaceholder;
	}

	public void setVersionPlaceholder(String versionPlaceholder) {
		this.versionPlaceholder = versionPlaceholder;
	}
}
