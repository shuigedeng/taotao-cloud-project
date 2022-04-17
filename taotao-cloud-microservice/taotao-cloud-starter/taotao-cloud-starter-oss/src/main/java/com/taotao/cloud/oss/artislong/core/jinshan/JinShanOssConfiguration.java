package com.taotao.cloud.oss.artislong.core.jinshan;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.ksyun.ks3.service.Ks3;
import com.ksyun.ks3.service.Ks3Client;
import com.ksyun.ks3.service.Ks3ClientConfig;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.jinshan.model.JinShanOssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConditionalOnClass(Ks3.class)
@EnableConfigurationProperties({JinShanOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.JINSHAN + CharPool.DOT + OssConstant.ENABLE,
        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class JinShanOssConfiguration {

    public static final String DEFAULT_BEAN_NAME = "jinShanOssClient";

    @Autowired
    private JinShanOssProperties jinShanOssProperties;

    @Bean
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
