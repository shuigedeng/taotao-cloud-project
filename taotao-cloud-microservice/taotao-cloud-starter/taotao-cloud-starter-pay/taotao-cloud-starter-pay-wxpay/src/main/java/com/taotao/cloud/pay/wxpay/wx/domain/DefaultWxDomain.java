package com.taotao.cloud.pay.wxpay.wx.domain;

import cn.hutool.http.HttpRequest;
import com.taotao.cloud.pay.wxpay.wx.enums.RequestSuffix;
import com.taotao.cloud.pay.wxpay.wx.utils.WxPayUtil;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

/**
 * 微信域名管理
 *
 * @author lingting 2021/1/26 16:05
 */
public class DefaultWxDomain implements WxDomain {

	private static final String FLAG = "/";

	/**
	 * 是否使用沙箱
	 */
	private final boolean sandbox;

	private DefaultWxDomain(boolean sandbox) {
		this.sandbox = sandbox;
	}

	public static DefaultWxDomain of(boolean sandbox) {
		return new DefaultWxDomain(sandbox);
	}

	@Override
	public String sendRequest(Map<String, String> params, RequestSuffix rs) {
		// 获取请求地址
		try {
			String url = getUrl(rs.getSuffix());
			HttpRequest post = HttpRequest.post(url).header("Content-Type", "text/xml")
				.body(WxPayUtil.mapToXml(params));
			return post.execute().body();
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (TransformerException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 根据微信的建议, 这里后续需要加上主备切换的功能
	 *
	 * @return java.lang.String
	 */
	public String getDomain() {
		return MAIN1;
	}

	public String getUrl(String suffix) {
		if (suffix.startsWith(FLAG)) {
			suffix = suffix.substring(1);
		}

		if (sandbox) {
			return getDomain() + "sandboxnew/pay/" + suffix;
		}
		return getDomain() + "pay/" + suffix;
	}

}
