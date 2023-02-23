package com.taotao.cloud.auth.biz.face;

import com.baidu.aip.face.AipFace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * BadiduFaceConfiguration
 *
 * @author shuigedeng
 * @version 2022.03
 * @see https://cloud.baidu.com/
 * @since 2022-03-28 11:28:48
 */
@Configuration
@EnableConfigurationProperties({BaiduFaceProperties.class})
public class BaiduFaceConfiguration {

	@Autowired
	private BaiduFaceProperties baiduFaceProperties;

	@Bean
	public AipFace aipFace() {
		AipFace aipFace = new AipFace(baiduFaceProperties.getAppId(), baiduFaceProperties.getApiKey(), baiduFaceProperties.getSecretKey());
		aipFace.setConnectionTimeoutInMillis(60 * 1000);
		aipFace.setSocketTimeoutInMillis(60 * 1000);
		return aipFace;
	}
}
