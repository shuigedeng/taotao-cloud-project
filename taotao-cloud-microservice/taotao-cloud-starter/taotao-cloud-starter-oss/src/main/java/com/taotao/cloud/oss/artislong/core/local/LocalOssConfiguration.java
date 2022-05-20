package com.taotao.cloud.oss.artislong.core.local;

import cn.hutool.core.text.CharPool;
import cn.hutool.extra.spring.SpringUtil;
import com.aliyun.oss.OSSClient;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.local.model.LocalOssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 本地操作系统配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:31
 */
@AutoConfiguration
@ConditionalOnClass(OSSClient.class)
@EnableConfigurationProperties({LocalOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.LOCAL + CharPool.DOT + OssConstant.ENABLE,
        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class LocalOssConfiguration {

    public static final String DEFAULT_BEAN_NAME = "localOssClient";

    @Autowired
    private LocalOssProperties localProperties;

    @Bean
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
