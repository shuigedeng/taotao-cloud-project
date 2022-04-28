package com.taotao.cloud.oss.artislong.core.ali;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClient;
import com.aliyun.oss.OSSClientBuilder;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.ali.model.AliOssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 阿里oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:11
 */
@Configuration
@ConditionalOnClass(OSSClient.class)
@EnableConfigurationProperties({AliOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.ALI + CharPool.DOT
	+ OssConstant.ENABLE,
	havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class AliOssConfiguration {

	public static final String DEFAULT_BEAN_NAME = "aliOssClient";

	@Autowired
	private AliOssProperties aliOssProperties;

	@Bean
	public StandardOssClient aliOssClient() {
		Map<String, AliOssConfig> aliOssConfigMap = aliOssProperties.getOssConfig();
		if (aliOssConfigMap.isEmpty()) {
			SpringUtil.registerBean(DEFAULT_BEAN_NAME, aliOssClient(aliOssProperties));
		} else {
			String endpoint = aliOssProperties.getEndpoint();
			String accessKeyId = aliOssProperties.getAccessKeyId();
			String accessKeySecret = aliOssProperties.getAccessKeySecret();
			ClientBuilderConfiguration clientConfig = aliOssProperties.getClientConfig();
			aliOssConfigMap.forEach((name, aliOssConfig) -> {
				if (ObjectUtil.isEmpty(aliOssConfig.getEndpoint())) {
					aliOssConfig.setEndpoint(endpoint);
				}
				if (ObjectUtil.isEmpty(aliOssConfig.getAccessKeyId())) {
					aliOssConfig.setAccessKeyId(accessKeyId);
				}
				if (ObjectUtil.isEmpty(aliOssConfig.getAccessKeySecret())) {
					aliOssConfig.setAccessKeySecret(accessKeySecret);
				}
				if (ObjectUtil.isEmpty(aliOssConfig.getClientConfig())) {
					aliOssConfig.setClientConfig(clientConfig);
				}
				SpringUtil.registerBean(name, aliOssClient(aliOssConfig));
			});
		}
		return null;
	}

	public StandardOssClient aliOssClient(AliOssConfig aliOssConfig) {
		return new AliOssClient(ossClient(aliOssConfig), aliOssConfig);
	}

	public OSS ossClient(AliOssConfig aliOssConfig) {
		String securityToken = aliOssConfig.getSecurityToken();
		ClientBuilderConfiguration clientConfiguration = aliOssConfig.getClientConfig();
		if (ObjectUtil.isEmpty(securityToken) && ObjectUtil.isNotEmpty(clientConfiguration)) {
			return new OSSClientBuilder().build(aliOssConfig.getEndpoint(),
				aliOssConfig.getAccessKeyId(),
				aliOssConfig.getAccessKeySecret(), clientConfiguration);
		}
		if (ObjectUtil.isNotEmpty(securityToken) && ObjectUtil.isEmpty(clientConfiguration)) {
			return new OSSClientBuilder().build(aliOssConfig.getEndpoint(),
				aliOssConfig.getAccessKeyId(),
				aliOssConfig.getAccessKeySecret(), securityToken);
		}
		return new OSSClientBuilder().build(aliOssConfig.getEndpoint(),
			aliOssConfig.getAccessKeyId(),
			aliOssConfig.getAccessKeySecret(), securityToken, clientConfiguration);
	}

}
