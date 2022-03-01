package com.taotao.cloud.sys.biz.tools.name.remote.dtos;

import org.apache.commons.codec.digest.DigestUtils;

public class BaiduTranslateRequest {
    private String from = "zh";
    private String to = "en";
    private String q;
    private String salt;
    private String sign;
    private String appid;

    public BaiduTranslateRequest(String q, String appid,String secret) {
        this.q = q;
        this.appid = appid;

        salt = String.valueOf(System.currentTimeMillis());
        sign = DigestUtils.md5Hex(appid + q + salt + secret);
    }

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getQ() {
		return q;
	}

	public void setQ(String q) {
		this.q = q;
	}

	public String getSalt() {
		return salt;
	}

	public void setSalt(String salt) {
		this.salt = salt;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}
}
