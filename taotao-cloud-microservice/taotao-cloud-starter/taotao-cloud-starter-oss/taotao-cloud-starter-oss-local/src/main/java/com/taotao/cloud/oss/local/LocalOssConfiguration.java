package com.taotao.cloud.oss.local;

import cn.hutool.extra.spring.SpringUtil;
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
 * 本地操作系统配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:31
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@EnableConfigurationProperties({LocalOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "LOCAL")
public class LocalOssConfiguration  implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(LocalOssConfiguration.class, StarterName.OSS_STARTER);
	}

    public static final String DEFAULT_BEAN_NAME = "localOssClient";

    @Autowired
    private LocalOssProperties localProperties;

    @Bean
	@ConditionalOnMissingBean
    public StandardOssClient localOssClient() {
        Map<String, LocalOssConfig> localOssConfigMap = localProperties.getOssConfig();
        if (localOssConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, localOssClient(localProperties));
        } else {
            localOssConfigMap.forEach((name, localOssConfig) -> SpringUtil.registerBean(name, localOssClient(localOssConfig)));
        }
        return null;
    }

    public StandardOssClient localOssClient(LocalOssConfig localOssConfig) {
        return new LocalOssClient(localOssConfig);
    }
}
