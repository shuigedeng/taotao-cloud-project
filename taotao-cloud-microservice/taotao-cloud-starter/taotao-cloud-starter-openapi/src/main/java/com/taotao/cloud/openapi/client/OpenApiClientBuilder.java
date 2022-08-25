package com.taotao.cloud.openapi.client;


import com.taotao.cloud.openapi.client.constant.ClientConstant;
import com.taotao.cloud.openapi.common.enums.AsymmetricCryEnum;
import com.taotao.cloud.openapi.common.enums.CryModeEnum;
import com.taotao.cloud.openapi.common.enums.SymmetricCryEnum;

/**
 * OpenApiClient构造器
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:05:45
 */
public class OpenApiClientBuilder {

	/**
	 * openapi基础路径,例如(http://localhost)
	 */
	private final String baseUrl;

	/**
	 * 本系统私钥
	 */
	private final String selfPrivateKey;

	/**
	 * 远程系统的公钥
	 */
	private final String remotePublicKey;

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
	private final String callerId;

	/**
	 * 接口名
	 */
	private String api;

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
	 * 是否对HTTP传输的数据启用压缩
	 */
	private boolean enableCompress = false;

	/**
	 * 构造器
	 *
	 * @param baseUrl         openapi接口基础路径
	 * @param selfPrivateKey  本系统私钥
	 * @param remotePublicKey 远程系统公钥
	 * @param callerId        调用者ID
	 */
	public OpenApiClientBuilder(String baseUrl, String selfPrivateKey, String remotePublicKey,
		String callerId) {
		this.baseUrl = baseUrl;
		this.selfPrivateKey = selfPrivateKey;
		this.remotePublicKey = remotePublicKey;
		this.callerId = callerId;
	}

	/**
	 * 构造器
	 *
	 * @param baseUrl         openapi接口基础路径
	 * @param selfPrivateKey  本系统私钥
	 * @param remotePublicKey 远程系统公钥
	 * @param callerId        调用者ID
	 * @param api             API接口名
	 */
	public OpenApiClientBuilder(String baseUrl, String selfPrivateKey, String remotePublicKey,
		String callerId, String api) {
		this.baseUrl = baseUrl;
		this.selfPrivateKey = selfPrivateKey;
		this.remotePublicKey = remotePublicKey;
		this.callerId = callerId;
		this.api = api;
	}

	/**
	 * 设置API接口名
	 *
	 * @param api API接口名
	 * @return builder对象
	 */
	public OpenApiClientBuilder api(String api) {
		this.api = api;
		return this;
	}

	/**
	 * 设置非对称加密算法
	 *
	 * @param asymmetricCryEnum 非对称加密算法
	 * @return builder对象
	 */
	public OpenApiClientBuilder asymmetricCry(AsymmetricCryEnum asymmetricCryEnum) {
		this.asymmetricCryEnum = asymmetricCryEnum;
		return this;
	}

	/**
	 * 设置返回值是否需要解密
	 *
	 * @param retDecrypt 返回值是否需要解密
	 * @return builder对象
	 */
	public OpenApiClientBuilder retDecrypt(boolean retDecrypt) {
		this.retDecrypt = retDecrypt;
		return this;
	}

	/**
	 * 设置加密模式
	 *
	 * @param cryModeEnum 加密模式
	 * @return builder对象
	 */
	public OpenApiClientBuilder cryModeEnum(CryModeEnum cryModeEnum) {
		this.cryModeEnum = cryModeEnum;
		return this;
	}

	/**
	 * 设置对称加密算法
	 *
	 * @param symmetricCryEnum 对称加密算法
	 * @return builder对象
	 */
	public OpenApiClientBuilder symmetricCry(SymmetricCryEnum symmetricCryEnum) {
		this.symmetricCryEnum = symmetricCryEnum;
		return this;
	}

	/**
	 * 设置HTTP建立连接超时时间（单位秒）
	 *
	 * @param httpConnectionTimeout 连接超时时间（单位秒）
	 * @return builder对象
	 */
	public OpenApiClientBuilder httpConnectionTimeout(int httpConnectionTimeout) {
		this.httpConnectionTimeout = httpConnectionTimeout;
		return this;
	}

	/**
	 * 设置HTTP数据传输超时时间（单位秒）
	 *
	 * @param httpReadTimeout HTTP数据传输超时时间（单位秒）
	 * @return builder对象
	 */
	public OpenApiClientBuilder httpReadTimeout(int httpReadTimeout) {
		this.httpReadTimeout = httpReadTimeout;
		return this;
	}

	/**
	 * 设置HTTP请求代理域名
	 *
	 * @param httpProxyHost HTTP请求代理域名
	 * @return builder对象
	 */
	public OpenApiClientBuilder httpProxyHost(String httpProxyHost) {
		this.httpProxyHost = httpProxyHost;
		return this;
	}

	/**
	 * 设置HTTP请求代理端口
	 *
	 * @param httpProxyPort HTTP请求代理端口
	 * @return builder对象
	 */
	public OpenApiClientBuilder httpProxyPort(Integer httpProxyPort) {
		this.httpProxyPort = httpProxyPort;
		return this;
	}

	/**
	 * 设置是否对数据进行压缩
	 *
	 * @param enableCompress 是否对数据进行压缩
	 * @return builder对象
	 */
	public OpenApiClientBuilder enableCompress(boolean enableCompress) {
		this.enableCompress = enableCompress;
		return this;
	}

	/**
	 * 构建一个OpenClientApi对象
	 *
	 * @return OpenClientApi对象
	 */
	public OpenApiClient build() {
		OpenApiClient client = new OpenApiClient(
			baseUrl, selfPrivateKey, remotePublicKey, asymmetricCryEnum,
			retDecrypt, cryModeEnum, symmetricCryEnum, callerId, api,
			httpConnectionTimeout, httpReadTimeout, httpProxyHost, httpProxyPort, enableCompress);
		return client;
	}
}
