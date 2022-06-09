/**
 * $Id: WangYiOssConfiguration.java,v 1.0 2022/3/4 9:49 PM chenmin Exp $
 */
package com.taotao.cloud.oss.wangyi;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.netease.cloud.auth.BasicCredentials;
import com.netease.cloud.auth.Credentials;
import com.netease.cloud.services.nos.NosClient;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
import com.taotao.cloud.oss.common.condition.ConditionalOnOssEnabled;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * 王毅oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:44:06
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@ConditionalOnClass(NosClient.class)
@EnableConfigurationProperties({WangYiOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "WANGYI")
public class WangYiOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(WangYiOssConfiguration.class, StarterName.OSS_STARTER);
	}

    public static final String DEFAULT_BEAN_NAME = "wangYiOssClient";

    @Autowired
    private WangYiOssProperties wangYiOssProperties;

    @Bean
	@ConditionalOnMissingBean
    public StandardOssClient wangYiOssClient() {
        Map<String, WangYiOssConfig> wangYiOssConfigMap = wangYiOssProperties.getOssConfig();
        if (wangYiOssConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, wangYiOssClient(wangYiOssProperties));
        } else {
            String endPoint = wangYiOssProperties.getEndpoint();
            String accessKey = wangYiOssProperties.getAccessKey();
            String secretKey = wangYiOssProperties.getSecretKey();
            wangYiOssConfigMap.forEach((name, wangYiOssConfig) -> {
                if (ObjectUtil.isEmpty(wangYiOssConfig.getEndpoint())) {
                    wangYiOssConfig.setEndpoint(endPoint);
                }
                if (ObjectUtil.isEmpty(wangYiOssConfig.getAccessKey())) {
                    wangYiOssConfig.setAccessKey(accessKey);
                }
                if (ObjectUtil.isEmpty(wangYiOssConfig.getSecretKey())) {
                    wangYiOssConfig.setSecretKey(secretKey);
                }
                SpringUtil.registerBean(name, wangYiOssClient(wangYiOssConfig));
            });
        }
        return null;
    }

    public StandardOssClient wangYiOssClient(WangYiOssConfig ossConfig) {
        return new WangYiOssClient(nosClient(ossConfig), ossConfig);
    }

    public NosClient nosClient(WangYiOssConfig ossConfig) {
        Credentials credentials = new BasicCredentials(ossConfig.getAccessKey(), ossConfig.getSecretKey());
        NosClient nosClient = new NosClient(credentials);
        nosClient.setEndpoint(ossConfig.getEndpoint());
        nosClient.setConfiguration(ossConfig.getClientConfig());
        return nosClient;
    }
}
