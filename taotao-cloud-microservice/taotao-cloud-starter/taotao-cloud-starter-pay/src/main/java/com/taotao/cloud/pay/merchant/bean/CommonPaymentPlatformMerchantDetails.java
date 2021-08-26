package com.taotao.cloud.pay.merchant.bean;

import com.egzosn.pay.common.api.BasePayConfigStorage;
import com.egzosn.pay.common.api.PayService;
import com.egzosn.pay.common.bean.CertStoreType;
import com.egzosn.pay.common.http.HttpConfigStorage;
import com.taotao.cloud.pay.PayConfigurerAdapter;
import com.taotao.cloud.pay.builders.MerchantDetailsServiceBuilder;
import com.taotao.cloud.pay.merchant.PaymentPlatform;
import com.taotao.cloud.pay.merchant.PaymentPlatformMerchantDetails;
import com.taotao.cloud.pay.merchant.PaymentPlatformServiceAdapter;
import com.taotao.cloud.pay.provider.merchant.platform.PaymentPlatforms;
import java.io.IOException;
import java.io.InputStream;

/**
 * 公共的支付商户信息列表
 *
 * @author egan
 * <pre>
 * email egzosn@gmail.com
 * date   2019/4/9 19:39.
 * </pre>
 */
public class CommonPaymentPlatformMerchantDetails extends BasePayConfigStorage implements
	PaymentPlatformMerchantDetails, PaymentPlatformServiceAdapter,
	PayConfigurerAdapter<MerchantDetailsServiceBuilder> {

	private String detailsId;
	private String appId;
	private String mchId;
	private String seller;
	private String subAppId;
	private String subMchId;
	/**
	 * 证书（PKCS12）
	 */
	private Object keystore;


	/**
	 * 公钥证书
	 */
	private Object keyPublicCert;
	/**
	 * 证书，这里针对双证书校验， 银联的根级证书
	 */
	private Object keyCert;
	/**
	 * 应用私钥证书，rsa_private pkcs8格式 生成签名时使用
	 */
	private String keyPrivateCertPwd;

	/**
	 * 证书存储类型
	 */
	private CertStoreType certStoreType;
	/**
	 * 商户对应的支付服务
	 */
	private volatile PayService payService;
	/**
	 * 商户平台
	 */
	private volatile PaymentPlatform platform;

	private MerchantDetailsServiceBuilder builder;
	/**
	 * HTTP请求配置
	 */
	private HttpConfigStorage httpConfigStorage;


	/**
	 * 外部调用者使用，链式的做法
	 *
	 * @return 返回对应外部调用者
	 */
	@Override
	public MerchantDetailsServiceBuilder and() {
		return getBuilder();
	}

	/**
	 * 获取构建器
	 *
	 * @return 构建器
	 */
	@Override
	public MerchantDetailsServiceBuilder getBuilder() {
		return builder;
	}

	public CommonPaymentPlatformMerchantDetails(MerchantDetailsServiceBuilder builder) {
		this();
		this.builder = builder;
	}

	public CommonPaymentPlatformMerchantDetails() {

	}

	/**
	 * 获取支付平台
	 *
	 * @return 支付平台
	 */
	@Override
	public PaymentPlatform getPaymentPlatform() {
		return platform;
	}

	/**
	 * 初始化服务
	 *
	 * @return 支付商户服务适配器
	 */
	@Override
	public PaymentPlatformServiceAdapter initService() {
		platform = PaymentPlatforms.getPaymentPlatform(getPayType());
		if (null == payService) {
			payService = platform.getPayService(this, getHttpConfigStorage());
		}
		return this;
	}

	/**
	 * 获取支付平台对应的支付服务
	 *
	 * @return 支付服务
	 */
	@Override
	public PayService getPayService() {
		return payService;
	}

	/**
	 * 获取HTTP请求配置
	 *
	 * @return HTTP请求配置
	 */
	@Override
	public HttpConfigStorage getHttpConfigStorage() {
		return httpConfigStorage;
	}

	public void setHttpConfigStorage(HttpConfigStorage httpConfigStorage) {
		this.httpConfigStorage = httpConfigStorage;
	}

	public void setDetailsId(String detailsId) {
		this.detailsId = detailsId;
	}

	/**
	 * 获取支付商户id
	 *
	 * @return 支付商户id
	 */
	@Override
	public String getDetailsId() {
		return detailsId;
	}


	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	@Override
	@Deprecated
	public String getAppid() {
		return appId;
	}

	/**
	 * 应用id 纠正名称
	 *
	 * @return 应用id
	 */
	@Override
	public String getAppId() {
		return appId;
	}

	@Override
	public String getPid() {
		return mchId;
	}

	@Override
	public String getSeller() {
		return seller;
	}

	public void setSeller(String seller) {
		this.seller = seller;
	}

	public String getSubAppId() {
		return subAppId;
	}

	public void setSubAppId(String subAppId) {
		this.subAppId = subAppId;
	}

	public String getSubMchId() {
		return subMchId;
	}

	public void setSubMchId(String subMchId) {
		this.subMchId = subMchId;
	}


	/**
	 * 设置私钥证书
	 *
	 * @param keystore 私钥证书地址 或者证书内容字符串 私钥证书密码 {@link #setKeyPrivateCertPwd(String)}
	 */
	public void setKeystore(String keystore) {
		super.setKeyPrivate(keystore);
		this.keystore = keystore;
	}

	/**
	 * 设置私钥证书
	 *
	 * @param keystore 私钥证书信息流 私钥证书密码 {@link #setKeyPrivateCertPwd(String)}
	 */
	public void setKeystore(InputStream keystore) {
		this.keystore = keystore;
	}

	public InputStream getKeystoreInputStream() throws IOException {
		return certStoreType.getInputStream(keystore);
	}

	public Object getKeystore() {
		return keystore;
	}

	public String getKeystorePwd() {
		return getKeyPrivateCertPwd();
	}

	public void setKeystorePwd(String keystorePwd) {
		setKeyPrivateCertPwd(keystorePwd);
	}

	/**
	 * 获取私钥证书密码
	 *
	 * @return 私钥证书密码
	 */
	public String getKeyPrivateCertPwd() {
		return keyPrivateCertPwd;
	}

	public void setKeyPrivateCertPwd(String keyPrivateCertPwd) {
		this.keyPrivateCertPwd = keyPrivateCertPwd;
	}


	public String getKeyPublicCert() {
		return (String) keyPublicCert;
	}

	/**
	 * 设置公钥证书
	 *
	 * @param keyPublicCert 证书信息或者证书路径
	 */
	public void setKeyPublicCert(String keyPublicCert) {
		setKeyPublic(keyPublicCert);
		this.keyPublicCert = keyPublicCert;
	}

	/**
	 * 设置公钥证书
	 *
	 * @param keyPublicCert 证书文件
	 */
	public void setKeyPublicCert(InputStream keyPublicCert) {
		this.keyPublicCert = keyPublicCert;
	}

	public Object getKeyCert() {
		return keyCert;
	}


	/**
	 * 公钥证书，这里针对双证书校验， 银联的根级证书
	 *
	 * @param keyCert 证书信息或者证书路径
	 */
	public void setKeyCert(String keyCert) {
		this.keyCert = keyCert;
	}

	/**
	 * 公钥证书，这里针对双证书校验， 银联的根级证书
	 *
	 * @param keyCert 证书文件
	 */
	public void setKeyCert(InputStream keyCert) {
		this.keyCert = keyCert;
	}

	public InputStream getKeyPublicCertInputStream() throws IOException {
		return certStoreType.getInputStream(keyPublicCert);
	}

	public InputStream getKeyCertInputStream() throws IOException {
		return certStoreType.getInputStream(keyCert);
	}

	/**
	 * 证书存储类型
	 *
	 * @return 证书存储类型
	 */
	public CertStoreType getCertStoreType() {
		return certStoreType;
	}

	public void setCertStoreType(CertStoreType certStoreType) {
		this.certStoreType = certStoreType;
	}


}
