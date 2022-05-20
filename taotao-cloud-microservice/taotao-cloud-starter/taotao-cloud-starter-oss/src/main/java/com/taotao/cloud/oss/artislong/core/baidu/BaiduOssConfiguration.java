package com.taotao.cloud.oss.artislong.core.baidu;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.baidubce.auth.DefaultBceCredentials;
import com.baidubce.services.bos.BosClient;
import com.baidubce.services.bos.BosClientConfiguration;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.baidu.model.BaiduOssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 百度oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:51
 */
@AutoConfiguration
@ConditionalOnClass(BosClient.class)
@EnableConfigurationProperties({BaiduOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.BAIDU + CharPool.DOT + OssConstant.ENABLE,
        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class BaiduOssConfiguration {

    public static final String DEFAULT_BEAN_NAME = "baiduOssClient";

    @Autowired
    private BaiduOssProperties baiduOssProperties;

    @Bean
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
