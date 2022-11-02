package com.taotao.cloud.pay.wxpay.wx.domain;

import com.taotao.cloud.pay.wxpay.wx.WxPay;
import com.taotao.cloud.pay.wxpay.wx.constants.WxPayConstant;
import com.taotao.cloud.pay.wxpay.wx.enums.RequestSuffix;
import com.taotao.cloud.pay.wxpay.wx.enums.SignType;
import com.taotao.cloud.pay.wxpay.wx.response.WxPayResponse;
import com.taotao.cloud.pay.wxpay.wx.utils.WxPayUtil;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * @author lingting 2021/2/1 10:57
 */
public interface WxDomain {

	/**
	 * 主域名
	 */
	String MAIN1 = "https://api.mch.weixin.qq.com/";

	String MAIN2 = "https://api.weixin.qq.com/";

	/**
	 * 备用域名
	 */
	String BACKUP1 = "https://api2.mch.weixin.qq.com/";

	String BACKUP2 = "https://api2.weixin.qq.com/";

	/**
	 * 发起请求. 根据微信建议,实现类最好拥有主备域名自动切换的功能
	 *
	 * @param params 参数
	 * @param rs     请求后缀
	 * @return java.util.Map<java.lang.String, java.lang.String>
	 */
	default Map<String, String> request(Map<String, String> params, RequestSuffix rs) {
		try {
			String res = "";
			try {
				res = sendRequest(params, rs);
				return WxPayUtil.xmlToMap(res);
			} catch (Exception e) {
				// 用于处理返回值异常情况
				LoggerFactory.getLogger(getClass()).error("微信支付请求失败!返回值:\n {}", res);
				throw e;
			}
		} catch (ParserConfigurationException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (SAXException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 发送请求
	 *
	 * @param params 参数
	 * @param rs     前缀
	 * @return java.lang.String
	 */
	String sendRequest(Map<String, String> params, RequestSuffix rs);

	/**
	 * 获取沙箱环境密钥
	 *
	 * @param wxPay 支付信息
	 * @return com.hccake.extend.pay.wx.response.WxPayResponse
	 */
	default WxPayResponse sandbox(WxPay wxPay) {
		HashMap<String, String> map = new HashMap<>();
		map.put("mch_id", wxPay.getMchId());
		map.put("nonce_str", WxPayUtil.generateNonceStr());
		// 设置签名类型
		map.put(WxPayConstant.FIELD_SIGN_TYPE, SignType.MD5.getStr());
		// 签名
		map.put(WxPayConstant.FIELD_SIGN, WxPayUtil.sign(map, wxPay.getMckKey()));

		return WxPayResponse.of(request(map, RequestSuffix.GETSIGNKEY));
	}

}
