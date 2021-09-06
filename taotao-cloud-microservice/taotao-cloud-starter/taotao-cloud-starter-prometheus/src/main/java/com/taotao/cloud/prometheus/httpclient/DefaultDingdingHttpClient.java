package com.taotao.cloud.prometheus.httpclient;

import com.taotao.cloud.prometheus.model.DingDingNotice;
import com.taotao.cloud.prometheus.model.DingDingResult;
import com.taotao.cloud.prometheus.properties.DingDingNoticeProperty;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.gson.Gson;

import feign.Feign;
import feign.FeignException;
import feign.Logger.Level;
import feign.RequestTemplate;
import feign.Response;
import feign.codec.DecodeException;
import feign.codec.Decoder;
import feign.codec.EncodeException;
import feign.codec.Encoder;
import feign.slf4j.Slf4jLogger;

public class DefaultDingdingHttpClient implements DingdingHttpClient {

	private final DingDingClientFeign clientFeign = Feign.builder().encoder(new GsonEncoder())
			.decoder(new GsonDecoder()).logger(new Slf4jLogger()).logLevel(Level.FULL)
			.target(DingDingClientFeign.class, "https://oapi.dingtalk.com/robot");

	private final Gson gson;

	private final DingDingNoticeProperty dingDingNoticeProperty;

	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * @param gson
	 * @param dingDingNoticeProperty
	 */
	public DefaultDingdingHttpClient(Gson gson, DingDingNoticeProperty dingDingNoticeProperty) {
		this.gson = gson;
		this.dingDingNoticeProperty = dingDingNoticeProperty;
	}

	@Override
	public DingDingResult doSend(DingDingNotice body) {
		logger.debug("发送钉钉请求:" + body);
		Map<String, Object> map = new HashMap<String, Object>();
		if (dingDingNoticeProperty.isEnableSignatureCheck()) {
			long timestamp = System.currentTimeMillis();
			map.put("sign", generateSign(System.currentTimeMillis(), dingDingNoticeProperty.getSignSecret()));
			map.put("timestamp", timestamp);
		}
		return clientFeign.post(dingDingNoticeProperty.getAccessToken(), body, map);
	}

	protected String generateSign(Long timestamp, String sec) {
		String combine = String.format("%d\n%s", timestamp, sec);
		try {
			Mac mac = Mac.getInstance("HmacSHA256");
			mac.init(new SecretKeySpec(sec.getBytes("UTF-8"), "HmacSHA256"));
			byte[] signData = mac.doFinal(combine.getBytes("UTF-8"));
			return Base64.encodeBase64String(signData);
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException | InvalidKeyException e) {
			e.printStackTrace();
		}
		return null;
	}

	class GsonDecoder implements Decoder {

		@Override
		public Object decode(Response response, Type type) throws IOException, DecodeException, FeignException {
			return gson.fromJson(response.body().asReader(StandardCharsets.UTF_8), type);
		}

	}

	class GsonEncoder implements Encoder {

		@Override
		public void encode(Object object, Type bodyType, RequestTemplate template) throws EncodeException {
			template.body(gson.toJson(object).getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8);
		}

	}

}
