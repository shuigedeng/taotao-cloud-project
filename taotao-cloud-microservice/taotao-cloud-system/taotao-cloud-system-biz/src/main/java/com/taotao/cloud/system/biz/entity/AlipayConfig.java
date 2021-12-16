/**
 * Copyright (C) 2018-2020
 * All rights reserved, Designed By www.yixiang.co
 * 注意：
 * 本软件为www.yixiang.co开发研制
 */
package com.taotao.cloud.system.biz.entity;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.taotao.cloud.web.base.entity.SuperEntity;

import java.io.Serializable;

@TableName("alipay_config")
public class AlipayConfig extends SuperEntity<AlipayConfig, Long> implements Serializable {

    /** 主键 */
    @TableId
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    // @Column(name = "id")
    private Long id;


    /** 应用ID */
    // @Column(name = "app_id")
    private String appId;


    /** 编码 */
    // @Column(name = "charset")
    private String charset;


    /** 类型 固定格式json */
    // @Column(name = "format")
    private String format;


    /** 网关地址 */
    // @Column(name = "gateway_url")
    private String gatewayUrl;


    /** 异步回调 */
    // @Column(name = "notify_url")
    private String notifyUrl;


    /** 私钥 */
    // @Column(name = "private_key")
    private String privateKey;


    /** 公钥 */
    // @Column(name = "public_key")
    private String publicKey;


    /** 回调地址 */
    // @Column(name = "return_url")
    private String returnUrl;


    /** 签名方式 */
    // @Column(name = "sign_type")
    private String signType;


    /** 商户号 */
    // @Column(name = "sys_service_provider_id")
    private String sysServiceProviderId;


    public void copy(AlipayConfig source) {
        BeanUtil.copyProperties(source, this, CopyOptions.create().setIgnoreNullValue(true));
    }

	@Override
	public Long getId() {
		return id;
	}

	@Override
	public void setId(Long id) {
		this.id = id;
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getCharset() {
		return charset;
	}

	public void setCharset(String charset) {
		this.charset = charset;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getGatewayUrl() {
		return gatewayUrl;
	}

	public void setGatewayUrl(String gatewayUrl) {
		this.gatewayUrl = gatewayUrl;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getPrivateKey() {
		return privateKey;
	}

	public void setPrivateKey(String privateKey) {
		this.privateKey = privateKey;
	}

	public String getPublicKey() {
		return publicKey;
	}

	public void setPublicKey(String publicKey) {
		this.publicKey = publicKey;
	}

	public String getReturnUrl() {
		return returnUrl;
	}

	public void setReturnUrl(String returnUrl) {
		this.returnUrl = returnUrl;
	}

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getSysServiceProviderId() {
		return sysServiceProviderId;
	}

	public void setSysServiceProviderId(String sysServiceProviderId) {
		this.sysServiceProviderId = sysServiceProviderId;
	}
}
