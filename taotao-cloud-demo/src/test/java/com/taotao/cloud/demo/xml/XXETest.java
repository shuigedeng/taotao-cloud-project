package com.taotao.cloud.demo.xml;

import com.taotao.cloud.common.utils.common.XmlUtils;
import org.junit.Test;
import org.xml.sax.SAXParseException;

public class XXETest {

	@Test(expected = SAXParseException.class)
	public void test1() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"   <!DOCTYPE c [\n" +
			"       <!ENTITY file SYSTEM \"file:///etc/passwd\">\n" +
			"   ]>\n" +
			"   <c>&file;</c>";

		XmlUtils helper = XmlUtils.safe(xml);
		System.out.println(helper.getString("c"));
	}

	@Test
	public void test2() {
		String xml = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n" +
			"   <!DOCTYPE c [\n" +
			"       <!ENTITY file SYSTEM \"file:///etc/passwd\">\n" +
			"   ]>\n" +
			"   <c>&file;</c>";

		// 注意：windows 下找不到文件会报错
		try {
			XmlUtils helper = XmlUtils.unsafe(xml);
			System.out.println(helper.getString("c"));
		} catch (Throwable e) {
			e.printStackTrace();
		}

	}
}
