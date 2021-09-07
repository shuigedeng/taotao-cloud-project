package com.taotao.cloud.prometheus.properties;

import com.taotao.cloud.prometheus.enums.DingdingTextType;
import com.taotao.cloud.prometheus.model.DingDingMarkdownNotice;
import com.taotao.cloud.prometheus.model.DingDingNotice;
import com.taotao.cloud.prometheus.model.DingDingTextNotice;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "prometheus.dingding")
public class DingDingNoticeProperties {

	/**
	 * 是否开启钉钉通知
	 */
	private boolean enabled = false;

	/**
	 * 电话信息
	 */
	private String[] phoneNum;

	/**
	 * 钉钉机器人的accessToken
	 */
	private String accessToken;

	/**
	 * 是否开启验签
	 */
	private boolean enableSignatureCheck;

	/**
	 * 验签秘钥
	 */
	private String signSecret;

	/**
	 * 钉钉通知文本类型
	 */
	private DingdingTextType dingdingTextType = DingdingTextType.TEXT;

	/**
	 * @return the enabled
	 */
	public boolean isEnabled() {
		return enabled;
	}

	/**
	 * @param enabled the enabled to set
	 */
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	/**
	 * @return the phoneNum
	 */
	public String[] getPhoneNum() {
		return phoneNum;
	}

	/**
	 * @param phoneNum the phoneNum to set
	 */
	public void setPhoneNum(String[] phoneNum) {
		this.phoneNum = phoneNum;
	}

	/**
	 * @return the accessToken
	 */
	public String getAccessToken() {
		return accessToken;
	}

	/**
	 * @param accessToken the accessToken to set
	 */
	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	/**
	 * @return the enableSignatureCheck
	 */
	public boolean isEnableSignatureCheck() {
		return enableSignatureCheck;
	}

	/**
	 * @param enableSignatureCheck the enableSignatureCheck to set
	 */
	public void setEnableSignatureCheck(boolean enableSignatureCheck) {
		this.enableSignatureCheck = enableSignatureCheck;
	}

	/**
	 * @return the signSecret
	 */
	public String getSignSecret() {
		return signSecret;
	}

	/**
	 * @param signSecret the signSecret to set
	 */
	public void setSignSecret(String signSecret) {
		this.signSecret = signSecret;
	}

	/**
	 * @return the dingdingTextType
	 */
	public DingdingTextType getDingdingTextType() {
		return dingdingTextType;
	}

	/**
	 * @param dingdingTextType the dingdingTextType to set
	 */
	public void setDingdingTextType(DingdingTextType dingdingTextType) {
		this.dingdingTextType = dingdingTextType;
	}

	public DingDingNotice generateDingdingNotice(String msg, String title) {
		switch (dingdingTextType) {
		case MARKDOWN:
			return new DingDingMarkdownNotice(msg, title, phoneNum);
		case TEXT:
			return new DingDingTextNotice(msg, phoneNum);
		}
		// never happen;
		return null;
	}

}
