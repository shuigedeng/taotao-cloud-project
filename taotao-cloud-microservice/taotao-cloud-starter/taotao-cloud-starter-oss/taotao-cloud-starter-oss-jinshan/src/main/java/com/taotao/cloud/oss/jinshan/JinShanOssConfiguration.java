package com.taotao.cloud.oss.jinshan;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ksyun.ks3.service.Ks3;
import com.ksyun.ks3.service.Ks3Client;
import com.ksyun.ks3.service.Ks3ClientConfig;
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
 * 金山开源软件配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:20
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@EnableConfigurationProperties({JinShanOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "JINSHAN")
public class JinShanOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(JinShanOssConfiguration.class, StarterName.OSS_STARTER);
	}

    public static final String DEFAULT_BEAN_NAME = "jinShanOssClient";

    @Autowired
    private JinShanOssProperties jinShanOssProperties;

    @Bean
	@ConditionalOnMissingBean
    public StandardOssClient jinShanOssClient() {
        Map<String, JinShanOssConfig> ossConfigMap = jinShanOssProperties.getOssConfig();
        if (ossConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, jinShanOssClient(jinShanOssProperties));
        } else {
            String accessKeyId = jinShanOssProperties.getAccessKeyId();
            String accessKeySecret = jinShanOssProperties.getAccessKeySecret();
            Ks3ClientConfig clientConfig = jinShanOssProperties.getClientConfig();
            ossConfigMap.forEach((name, jinShanOssConfig) -> {
                if (ObjectUtil.isEmpty(jinShanOssConfig.getAccessKeyId())) {
                    jinShanOssConfig.setAccessKeyId(accessKeyId);
                }
                if (ObjectUtil.isEmpty(jinShanOssConfig.getAccessKeySecret())) {
                    jinShanOssConfig.setAccessKeySecret(accessKeySecret);
                }
                if (ObjectUtil.isEmpty(jinShanOssConfig.getClientConfig())) {
                    jinShanOssConfig.setClientConfig(clientConfig);
                }
                SpringUtil.registerBean(name, jinShanOssClient(jinShanOssConfig));
            });
        }
        return null;
    }

    public StandardOssClient jinShanOssClient(JinShanOssConfig jinShanOssConfig) {
        return new JinShanOssClient(ks3(jinShanOssConfig), jinShanOssConfig);
    }

    public Ks3 ks3(JinShanOssConfig ossConfig) {
        Ks3ClientConfig clientConfig = ossConfig.getClientConfig();
        return new Ks3Client(ossConfig.getAccessKeyId(), ossConfig.getAccessKeySecret(), clientConfig);
    }
}
