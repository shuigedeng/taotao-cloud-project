package com.taotao.cloud.ws;

import com.taotao.cloud.ws.netty.NettyServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@SpringBootApplication
public class TaoTaoCloudWsApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(TaoTaoCloudWsApplication.class, args);

		//启动服务端
		NettyServer nettyServer = new NettyServer();
		nettyServer.run();
	}

	@Bean
	public CorsFilter corsFilter() {
		System.out.println("sdfasdf");
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedOriginPattern(CorsConfiguration.ALL);
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
		urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
		return new CorsFilter(urlBasedCorsConfigurationSource);
	}

}
