package com.taotao.cloud.disruptor;

import com.taotao.cloud.ip2region.configuration.Ip2regionAutoConfiguration;
import com.taotao.cloud.ip2region.impl.Ip2regionSearcherImpl;
import com.taotao.cloud.ip2region.model.Ip2regionSearcher;
import com.taotao.cloud.ip2region.model.IpInfo;
import com.taotao.cloud.ip2region.properties.Ip2regionProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

/**
 * ip2region 测试
 *
 * @author L.cm
 */
class Ip2regionTest {
	private Ip2regionSearcher searcher;

	@BeforeEach
	public void setup() throws Exception {
		AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
		Ip2regionAutoConfiguration configuration = new Ip2regionAutoConfiguration();
		searcher = configuration.ip2regionSearcher(context, new Ip2regionProperties());
		((Ip2regionSearcherImpl) searcher).afterPropertiesSet();
	}

	@Test
	void getRegion() throws Exception {
		System.out.println(searcher.memorySearch("220.248.12.158"));
		System.out.println(searcher.memorySearch("222.240.36.135"));
		System.out.println(searcher.memorySearch("172.30.13.97"));
		System.out.println(searcher.memorySearch("223.26.64.0"));
		System.out.println(searcher.memorySearch("223.26.128.0"));
		System.out.println(searcher.memorySearch("223.26.67.0"));
		System.out.println(searcher.memorySearch("223.29.220.0"));
		System.out.println(searcher.memorySearch("82.120.124.0"));
	}

	@Test
	void test2() {
		IpInfo ipInfo = searcher.memorySearch("127.0.0.1");
		Assertions.assertNotNull(ipInfo);
	}

}
