package com.taotao.cloud.oss.huawei;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.obs.services.ObsClient;
import com.obs.services.ObsConfiguration;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
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
 * 华魏oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:40:24
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@EnableConfigurationProperties({HuaWeiOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "HUAWEI")
public class HuaWeiOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(HuaWeiOssConfiguration.class, StarterName.OSS_STARTER);
	}

    public static final String DEFAULT_BEAN_NAME = "huaWeiOssClient";

    @Autowired
    private HuaWeiOssProperties huaWeiOssProperties;

    @Bean
	@ConditionalOnMissingBean
    public StandardOssClient huaWeiOssClient() {
        Map<String, HuaweiOssConfig> huaweiOssConfigMap = huaWeiOssProperties.getOssConfig();
        if (huaweiOssConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, huaWeiOssClient(huaWeiOssProperties));
        } else {
            String accessKey = huaWeiOssProperties.getAccessKey();
            String secretKey = huaWeiOssProperties.getSecretKey();
            ObsConfiguration clientConfig = huaWeiOssProperties.getClientConfig();
            huaweiOssConfigMap.forEach((name, huaweiOssConfig) -> {
                if (ObjectUtil.isEmpty(huaweiOssConfig.getAccessKey())) {
                    huaweiOssConfig.setAccessKey(accessKey);
                }
                if (ObjectUtil.isEmpty(huaweiOssConfig.getSecretKey())) {
                    huaweiOssConfig.setSecretKey(secretKey);
                }
                if (ObjectUtil.isEmpty(huaweiOssConfig.getClientConfig())) {
                    huaweiOssConfig.setClientConfig(clientConfig);
                }
                SpringUtil.registerBean(name, huaWeiOssClient(huaweiOssConfig));
            });
        }
        return null;
    }

    public StandardOssClient huaWeiOssClient(HuaweiOssConfig huaweiOssConfig) {
        return new HuaWeiOssClient(obsClient(huaweiOssConfig), huaweiOssConfig);
    }

    public ObsClient obsClient(HuaweiOssConfig huaweiOssConfig) {
        return new ObsClient(huaweiOssConfig.getAccessKey(), huaweiOssConfig.getSecretKey(), huaweiOssConfig.getClientConfig());
    }
}
