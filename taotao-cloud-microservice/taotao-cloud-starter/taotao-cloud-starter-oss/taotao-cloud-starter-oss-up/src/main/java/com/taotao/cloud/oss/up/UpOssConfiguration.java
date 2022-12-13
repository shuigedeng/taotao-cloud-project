package com.taotao.cloud.oss.up;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.condition.ConditionalOnOssEnabled;
import com.taotao.cloud.oss.common.model.SliceConfig;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import com.upyun.ParallelUploader;
import com.upyun.RestManager;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 了开源软件配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:50
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@ConditionalOnBean(RestManager.class)
@EnableConfigurationProperties({UpOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "UP")
public class UpOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(UpOssConfiguration.class, StarterName.OSS_UP_STARTER);
	}

	public static final String DEFAULT_BEAN_NAME = "upOssClient";

	@Autowired
	private UpOssProperties upOssProperties;

	@Bean
	@ConditionalOnMissingBean
	public StandardOssClient upOssClient() {
		Map<String, UpOssConfig> upOssConfigMap = upOssProperties.getOssConfig();
		if (upOssConfigMap.isEmpty()) {
			SpringUtil.registerBean(DEFAULT_BEAN_NAME, upOssClient(upOssProperties));
		} else {
			String userName = upOssProperties.getUserName();
			String password = upOssProperties.getPassword();
			upOssConfigMap.forEach((name, upOssConfig) -> {
				if (ObjectUtil.isEmpty(upOssConfig.getUserName())) {
					upOssConfig.setUserName(userName);
				}
				if (ObjectUtil.isEmpty(upOssConfig.getPassword())) {
					upOssConfig.setPassword(password);
				}
				SpringUtil.registerBean(name, upOssClient(upOssConfig));
			});
		}
		return null;
	}

	private StandardOssClient upOssClient(UpOssConfig upOssConfig) {
		RestManager restManager = restManager(upOssConfig);
		ParallelUploader parallelUploader = parallelUploader(upOssConfig);
		return upOssClient(restManager, parallelUploader, upOssConfig);
	}

	public StandardOssClient upOssClient(RestManager restManager, ParallelUploader parallelUploader,
		UpOssConfig upOssConfig) {
		return new UpOssClient(restManager, parallelUploader, upOssConfig);
	}

	public RestManager restManager(UpOssConfig upOssConfig) {
		RestManager restManager = new RestManager(upOssConfig.getBucketName(),
			upOssConfig.getUserName(), upOssConfig.getPassword());
		// 手动设置超时时间：默认为30秒
		restManager.setTimeout(upOssConfig.getTimeout());
		// 选择最优的接入点
		restManager.setApiDomain(upOssConfig.getApiDomain().toString());
		return restManager;
	}

	public ParallelUploader parallelUploader(UpOssConfig upOssConfig) {
		ParallelUploader parallelUploader = new ParallelUploader(upOssConfig.getBucketName(),
			upOssConfig.getUserName(), upOssConfig.getPassword());

		SliceConfig sliceConfig = upOssConfig.getSliceConfig();
		parallelUploader.setParallel(sliceConfig.getTaskNum());
		parallelUploader.setCheckMD5(true);
		parallelUploader.setTimeout(upOssConfig.getTimeout());
		return parallelUploader;
	}
}
