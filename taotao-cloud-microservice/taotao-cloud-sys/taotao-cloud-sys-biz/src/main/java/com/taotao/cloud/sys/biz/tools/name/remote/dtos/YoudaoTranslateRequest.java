package com.taotao.cloud.sys.biz.tools.name.remote.dtos;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class YoudaoTranslateRequest {
    private String from = "auto";
    private String to = "auto";
    private String signType = "v3";
    // 当前时间 单位:秒
    private String curtime;
    private String appKey;
    private String q;
    private String salt;
    private String sign;

    /**
     * 构建一个查询实体
     * @param q
     * @param appKey    在有道官网注册的 key
     * @param appSecret 在有道官网注册的 secret
     */
    public YoudaoTranslateRequest(String q, String appKey, String appSecret) {
        this.q = q;
        this.appKey = appKey;

        // 这里设置全部参数,使用者只需要传要查询的字符串就可以了
        salt = String.valueOf(System.currentTimeMillis());
        curtime = String.valueOf(System.currentTimeMillis() / 1000);
        String signStr = appKey + truncate(q) + salt + curtime + appSecret;

        // 加密 ,使用 sha256 然后 hex , 字符串不使用 utf-8
        byte[] bytes = DigestUtils.sha256(signStr.getBytes());
        sign = Hex.encodeHexString(bytes);
    }

    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 11) + len + q.substring(len - 10, len));
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

	public String getSignType() {
		return signType;
	}

	public void setSignType(String signType) {
		this.signType = signType;
	}

	public String getCurtime() {
		return curtime;
	}

	public void setCurtime(String curtime) {
		this.curtime = curtime;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
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
}
