package com.taotao.cloud.open.client.config;


import com.taotao.cloud.open.client.constant.ClientConstant;
import com.taotao.cloud.open.common.enums.AsymmetricCryEnum;
import com.taotao.cloud.open.common.enums.CryModeEnum;
import com.taotao.cloud.open.common.enums.SymmetricCryEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 开放api客户端配置类，由引入者添加配置
 * <p>
 * yml配置文件中添加openapi配置示例：
 * </p>
 * <pre class="code">
 * openapi:
 *   client:
 *     config:
 *       openApiRefPath: openapi.example.client.openapiclient
 *       baseUrl: http://localhost:8080
 *       selfPrivateKey: ${keys.local.rsa.privateKey}
 *       remotePublicKey: ${keys.remote.rsa.publicKey}
 *       asymmetricCryEnum: RSA
 *       retDecrypt: true
 *       cryModeEnum: SYMMETRIC_CRY
 *       symmetricCryEnum: AES
 *       callerId: "001"
 *       httpConnectionTimeout: 3
 *       httpReadTimeout: 5
 *       enableCompress: true
 *       httpProxyHost: 127.0.0.1
 *       httpProxyPort: 8888
 * </pre>
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:04:30
 */
@Component
@ConfigurationProperties(prefix = "openapi.client.config")
public class OpenApiClientConfig {
    /**
     * 接口所在路径(包名)
     */
    private String openApiRefPath;

    /**
     * openapi基础路径,例如(http://localhost)
     */
    private String baseUrl;

    /**
     * 本系统私钥
     */
    private String selfPrivateKey;

    /**
     * 远程系统的公钥
     */
    private String remotePublicKey;

    /**
     * 非对称加密算法
     */
    private AsymmetricCryEnum asymmetricCryEnum = AsymmetricCryEnum.RSA;

    /**
     * 返回值是否需要解密
     */
    private boolean retDecrypt = true;

    /**
     * 加密模式
     */
    private CryModeEnum cryModeEnum = CryModeEnum.SYMMETRIC_CRY;

    /**
     * 对称加密算法
     */
    private SymmetricCryEnum symmetricCryEnum = SymmetricCryEnum.AES;

    /**
     * 调用者ID
     */
    private String callerId;

    /**
     * HTTP建立连接超时时间（单位秒）
     */
    private int httpConnectionTimeout = ClientConstant.HTTP_CONNECTION_TIMEOUT;

    /**
     * HTTP数据传输超时时间（单位秒）
     */
    private int httpReadTimeout = ClientConstant.HTTP_READ_TIMEOUT;

    /**
     * HTTP请求代理域名
     */
    private String httpProxyHost;

    /**
     * HTTP请求代理端口
     */
    private Integer httpProxyPort;

    /**
     * HTTP传输的数据是否启用压缩
     */
    private boolean enableCompress = false;

	public String getOpenApiRefPath() {
		return openApiRefPath;
	}

	public void setOpenApiRefPath(String openApiRefPath) {
		this.openApiRefPath = openApiRefPath;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	public String getSelfPrivateKey() {
		return selfPrivateKey;
	}

	public void setSelfPrivateKey(String selfPrivateKey) {
		this.selfPrivateKey = selfPrivateKey;
	}

	public String getRemotePublicKey() {
		return remotePublicKey;
	}

	public void setRemotePublicKey(String remotePublicKey) {
		this.remotePublicKey = remotePublicKey;
	}

	public AsymmetricCryEnum getAsymmetricCryEnum() {
		return asymmetricCryEnum;
	}

	public void setAsymmetricCryEnum(AsymmetricCryEnum asymmetricCryEnum) {
		this.asymmetricCryEnum = asymmetricCryEnum;
	}

	public boolean isRetDecrypt() {
		return retDecrypt;
	}

	public void setRetDecrypt(boolean retDecrypt) {
		this.retDecrypt = retDecrypt;
	}

	public CryModeEnum getCryModeEnum() {
		return cryModeEnum;
	}

	public void setCryModeEnum(CryModeEnum cryModeEnum) {
		this.cryModeEnum = cryModeEnum;
	}

	public SymmetricCryEnum getSymmetricCryEnum() {
		return symmetricCryEnum;
	}

	public void setSymmetricCryEnum(SymmetricCryEnum symmetricCryEnum) {
		this.symmetricCryEnum = symmetricCryEnum;
	}

	public String getCallerId() {
		return callerId;
	}

	public void setCallerId(String callerId) {
		this.callerId = callerId;
	}

	public int getHttpConnectionTimeout() {
		return httpConnectionTimeout;
	}

	public void setHttpConnectionTimeout(int httpConnectionTimeout) {
		this.httpConnectionTimeout = httpConnectionTimeout;
	}

	public int getHttpReadTimeout() {
		return httpReadTimeout;
	}

	public void setHttpReadTimeout(int httpReadTimeout) {
		this.httpReadTimeout = httpReadTimeout;
	}

	public String getHttpProxyHost() {
		return httpProxyHost;
	}

	public void setHttpProxyHost(String httpProxyHost) {
		this.httpProxyHost = httpProxyHost;
	}

	public Integer getHttpProxyPort() {
		return httpProxyPort;
	}

	public void setHttpProxyPort(Integer httpProxyPort) {
		this.httpProxyPort = httpProxyPort;
	}

	public boolean isEnableCompress() {
		return enableCompress;
	}

	public void setEnableCompress(boolean enableCompress) {
		this.enableCompress = enableCompress;
	}
}
