package com.taotao.cloud.report.biz.other;

import static shorturl.server.server.application.util.ConversionUtil.decode;
import static shorturl.server.server.application.util.ConversionUtil.encode;

import org.apache.tomcat.util.codec.binary.Base64;
import org.junit.jupiter.api.Test;
import shorturl.server.server.application.util.DefaultIdWorker;

public class ConversionUtilTest {

	@Test
	public void UtilTest() {
		Base64 base64 = new Base64();
		long l = new DefaultIdWorker().generate();
		System.out.println("id:" + l);
		String encodeStr = encode(l, 11);
		int length = encodeStr.length();
		System.out.println("62进制：" + encodeStr);
		if ((length <= 8)) {
			System.out.println("62进制：" + encodeStr);
		}
		String substring = encodeStr.substring(length - 8, length);
		System.out.println("62进制：" + substring);
		System.out.println("10进制：" + decode(encodeStr));
	}
}
