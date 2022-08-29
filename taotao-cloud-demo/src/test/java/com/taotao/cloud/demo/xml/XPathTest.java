package com.taotao.cloud.demo.xml;

import com.taotao.cloud.common.utils.common.XmlUtils;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;

/**
 * Created by L.cm on 2016/5/13.
 */
public class XPathTest {

	@Test
	public void test001() {
		String xml =
			"<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>\n" +
				"<bookstore>\n" +
				"<book>\n" +
				"<title lang=\"xxx\">Harry Potter</title>\n" +
				"<price>29.99</price>\n" +
				"</book>\n" +
				"<book>\n" +
				"<title lang=\"eng\">Learning XML</title>\n" +
				"<price>39.95</price>\n" +
				"</book>\n" +
				"</bookstore>";

		XmlUtils xmlUtils = XmlUtils.safe(xml);
		String title1 = xmlUtils.getString("//book[1]/title");
		Assert.assertEquals(title1, "Harry Potter");

		String titleLang = xmlUtils.getString("//book[2]/title/@lang");
		Assert.assertEquals(titleLang, "eng");

		Number price1 = xmlUtils.getNumber("//book[1]/price");
		System.out.println(price1.doubleValue());

		Node node = xmlUtils.getNode("//book[2]/title");
		String titleLang2 = xmlUtils.getString(node, "@lang");
		Assert.assertEquals(titleLang2, "eng");

		Assert.assertEquals(titleLang, titleLang2);

		boolean isEn = xmlUtils.getBoolean("//book[1]/title/@lang=\"eng\"");
		System.out.println(isEn);
	}
}
