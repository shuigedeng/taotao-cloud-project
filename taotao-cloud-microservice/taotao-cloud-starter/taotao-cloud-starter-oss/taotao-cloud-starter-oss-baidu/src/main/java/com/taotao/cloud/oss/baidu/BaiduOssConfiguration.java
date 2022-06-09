package com.taotao.cloud.oss.baidu;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.common.condition.ConditionalOnOssEnabled;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * 百度oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:51
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@EnableConfigurationProperties({BaiduOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "BAIDU")
public class BaiduOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(BaiduOssConfiguration.class, StarterName.OSS_STARTER);
	}

	public static final String DEFAULT_BEAN_NAME = "baiduOssClient";

	@Autowired
	private BaiduOssProperties baiduOssProperties;

	@Bean
	@ConditionalOnMissingBean
	public StandardOssClient baiduOssClient() {
		Map<String, BaiduOssConfig> baiduOssConfigMap = baiduOssProperties.getOssConfig();
		if (baiduOssConfigMap.isEmpty()) {
			SpringUtil.registerBean(DEFAULT_BEAN_NAME, baiduOssClient(baiduOssProperties));
		} else {
			String accessKeyId = baiduOssProperties.getAccessKeyId();
			String secretAccessKey = baiduOssProperties.getSecretAccessKey();
			BosClientConfiguration clientConfig = baiduOssProperties.getClientConfig();
			baiduOssConfigMap.forEach((name, baiduOssConfig) -> {
				if (ObjectUtil.isEmpty(baiduOssConfig.getAccessKeyId())) {
					baiduOssConfig.setAccessKeyId(accessKeyId);
				}
				if (ObjectUtil.isEmpty(baiduOssConfig.getSecretAccessKey())) {
					baiduOssConfig.setSecretAccessKey(secretAccessKey);
				}
				if (ObjectUtil.isEmpty(baiduOssConfig.getClientConfig())) {
					baiduOssConfig.setClientConfig(clientConfig);
				}
				SpringUtil.registerBean(name, baiduOssClient(baiduOssConfig));
			});
		}
		return null;
	}

	public StandardOssClient baiduOssClient(BaiduOssConfig baiduOssConfig) {
		return new BaiduOssClient(bosClient(bosClientConfiguration(baiduOssConfig)), baiduOssConfig);
	}

	public BosClientConfiguration bosClientConfiguration(BaiduOssConfig baiduOssConfig) {
		BosClientConfiguration clientConfig = baiduOssConfig.getClientConfig();
		clientConfig.setCredentials(new DefaultBceCredentials(baiduOssConfig.getAccessKeyId(), baiduOssConfig.getSecretAccessKey()));
		return clientConfig;
	}

	public BosClient bosClient(BosClientConfiguration config) {
		return new BosClient(config);
	}

}
