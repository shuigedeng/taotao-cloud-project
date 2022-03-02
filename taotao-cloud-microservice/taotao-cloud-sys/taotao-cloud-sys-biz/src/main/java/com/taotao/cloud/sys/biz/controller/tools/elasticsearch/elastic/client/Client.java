package com.taotao.cloud.sys.biz.controller.tools.elasticsearch.elastic.client;

import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class Client {
	
	@Value("${es.url}")
	private String esUrl;
	
	@Bean
	RestHighLevelClient configRestHighLevelClient() throws Exception {

		String[] esUrlArr = esUrl.split(",");

		List<HttpHost> httpHosts = new ArrayList<>();
		for(String es : esUrlArr){
			String[] esUrlPort = es.split(":");
			httpHosts.add(new HttpHost(esUrlPort[0], Integer.parseInt(esUrlPort[1]), "http"));
		}

		return new RestHighLevelClient(
		        RestClient.builder(httpHosts.toArray(new HttpHost[0]))
				);
	}
}
