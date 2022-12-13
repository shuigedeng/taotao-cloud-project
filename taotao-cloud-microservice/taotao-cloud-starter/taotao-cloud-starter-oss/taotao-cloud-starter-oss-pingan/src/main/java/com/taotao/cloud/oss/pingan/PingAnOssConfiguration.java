package com.taotao.cloud.oss.pingan;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pingan.radosgw.sdk.config.ObsClientConfig;
import com.pingan.radosgw.sdk.service.RadosgwService;
import com.pingan.radosgw.sdk.service.RadosgwServiceFactory;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.condition.ConditionalOnOssEnabled;
import com.taotao.cloud.oss.common.exception.OssException;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 平安oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:08
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@EnableConfigurationProperties({PingAnOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "PINGAN")
public class PingAnOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(PingAnOssConfiguration.class, StarterName.OSS_PINGAN_STARTER);
	}

	public static final String DEFAULT_BEAN_NAME = "pingAnOssClient";

	@Autowired
	private PingAnOssProperties pingAnOssProperties;

	@Bean
	@ConditionalOnMissingBean
	public StandardOssClient pingAnOssClient() {
		Map<String, PingAnOssConfig> ossConfigMap = pingAnOssProperties.getOssConfig();
		if (ossConfigMap.isEmpty()) {
			SpringUtil.registerBean(DEFAULT_BEAN_NAME, pingAnOssClient(pingAnOssProperties));
		} else {
			String userAgent = pingAnOssProperties.getUserAgent();
			String obsUrl = pingAnOssProperties.getObsUrl();
			String obsAccessKey = pingAnOssProperties.getObsAccessKey();
			String obsSecret = pingAnOssProperties.getObsSecret();
			String domainName = pingAnOssProperties.getDomainName();
			ossConfigMap.forEach((name, ossConfig) -> {
				if (ObjectUtil.isEmpty(ossConfig.getUserAgent())) {
					ossConfig.setUserAgent(userAgent);
				}
				if (ObjectUtil.isEmpty(ossConfig.getObsUrl())) {
					ossConfig.setObsUrl(obsUrl);
				}
				if (ObjectUtil.isEmpty(ossConfig.getObsAccessKey())) {
					ossConfig.setObsAccessKey(obsAccessKey);
				}
				if (ObjectUtil.isEmpty(ossConfig.getObsSecret())) {
					ossConfig.setObsSecret(obsSecret);
				}
				if (ObjectUtil.isEmpty(ossConfig.getDomainName())) {
					ossConfig.setDomainName(domainName);
				}
				SpringUtil.registerBean(name, pingAnOssClient(ossConfig));
			});
		}
		return null;
	}

	public StandardOssClient pingAnOssClient(PingAnOssConfig pingAnOssConfig) {
		return new PingAnOssClient(pingAnOssConfig, radosgwService(pingAnOssConfig));
	}

	public RadosgwService radosgwService(PingAnOssConfig pingAnOssConfig) {
		ObsClientConfig oc = new ObsClientConfig() {
			@Override
			public String getUserAgent() {
				return pingAnOssConfig.getUserAgent();
			}

			@Override
			public String getObsUrl() {
				return pingAnOssConfig.getObsUrl();
			}

			@Override
			public String getObsAccessKey() {
				return pingAnOssConfig.getObsAccessKey();
			}

			@Override
			public String getObsSecret() {
				return pingAnOssConfig.getObsSecret();
			}

			@Override
			public boolean isRepresentPathInKey() {
				return pingAnOssConfig.getRepresentPathInKey();
			}
		};

		try {
			String domainName = pingAnOssConfig.getDomainName();
			if (ObjectUtil.isEmpty(domainName)) {
				return RadosgwServiceFactory.getFromConfigObject(oc);
			} else {
				return RadosgwServiceFactory.getFromConfigObject(oc, domainName);
			}
		} catch (Exception e) {
			throw new OssException(e);
		}
	}
}
