package com.taotao.cloud.encrypt.handler.impl;

import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.encrypt.exception.EncryptException;
import com.taotao.cloud.encrypt.handler.SignEncryptHandler;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * 签名实现处理类
 */
public class SignEncryptHandlerImpl implements SignEncryptHandler {
	@Override
	public Object handle(Object proceed, long timeout, TimeUnit timeUnit, String signSecret, Map<Object, Object> jsonMap) throws EncryptException {
		Object sign = jsonMap.get("sign");
		Object timestamp = jsonMap.get("timestamp");
		this.checkParam(sign, timestamp, timeout, timeUnit);
		String digestMd5 = this.getDigest(jsonMap, signSecret, StandardCharsets.UTF_8);
		LogUtil.debug("加密后的字符：" + digestMd5);
		if (!digestMd5.equals(sign)) {
			throw new EncryptException("Illegal request,Decryption failed");
		}
		return proceed;
	}

	private void checkParam(Object sign, Object timestamp, long timeout, TimeUnit timeUnit) {
		if (sign == null) {
			throw new EncryptException("Illegal request,Sign does not exist");
		}
		if (timestamp == null) {
			throw new EncryptException("Illegal request,timestamp does not exist");
		}
		long now = System.currentTimeMillis();
		long timestampLong = Long.parseLong(timestamp.toString());
		if (!((now < timestampLong + timeout) && now >= timestampLong)) {
			throw new EncryptException("非法请求，请求超时");
		}
	}

	private String getDigest(Map<Object, Object> map, String sortSignSecret, Charset charset) {
		StringBuilder sb = new StringBuilder();
		map.entrySet().stream().
				filter(entry -> entry != null && !"sign".equals(entry.getKey())).
				sorted(Comparator.comparing(entry -> entry.getKey().toString())).
				forEach(entry -> {
					sb.append(entry.getKey().toString()).append("=").append(entry.getValue().toString()).append("&");
				});
		sb.append("secret").append("=").append(sortSignSecret);
		return md5Encode(sb.toString());
	}

	private String md5Encode(String plainText) {
		try {
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(plainText.getBytes());
			byte[] b = md.digest();
			int i;
			StringBuilder buf = new StringBuilder("");
			for (int offset = 0; offset < b.length; offset++) {
				i = b[offset];
				if (i < 0) {
					i += 256;
				}
				if (i < 16) {
					buf.append("0");
				}
				buf.append(Integer.toHexString(i));
			}
			return buf.toString();
		} catch (Exception e) {
			throw new EncryptException("md5 encode error");
		}
	}
}
