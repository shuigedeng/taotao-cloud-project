package com.taotao.cloud.office.easypoi.easypoi;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author by jueyue on 19-10-9.
 */
public class XmlTest {

	//@Test
	public void test() throws Exception {
		BufferedReader rows = new BufferedReader(new InputStreamReader(
			new FileInputStream("C:\\Users\\jueyue\\Desktop\\山东组织机构.xml")));
		StringBuilder xml = new StringBuilder();
		String str = "";
		while ((str = rows.readLine()) != null) {
			xml.append(str);
		}
		Document doc = Jsoup.parse(xml.toString());
		Elements elements = doc.getElementsByClass("TreeViewUnit_0");
		for (int i = 0; i < elements.size(); i++) {
			System.out.println(elements.get(i).text());
		}

	}
}
