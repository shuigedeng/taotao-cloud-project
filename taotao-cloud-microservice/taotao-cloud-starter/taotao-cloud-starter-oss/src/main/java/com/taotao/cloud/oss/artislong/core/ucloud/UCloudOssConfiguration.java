package com.taotao.cloud.oss.artislong.core.ucloud;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectApiBuilder;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.http.HttpClient;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.ucloud.model.UCloudOssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @author 陈敏
 * @version UCloudOssConfiguration.java, v 1.1 2022/3/7 0:20 chenmin Exp $
 * Created on 2022/3/7
 */
@Configuration
@ConditionalOnClass(UfileClient.class)
@EnableConfigurationProperties({UCloudOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.UCLOUD + CharPool.DOT + OssConstant.ENABLE,
        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class UCloudOssConfiguration {

    public static final String DEFAULT_BEAN_NAME = "uCloudOssClient";

    @Autowired
    private UCloudOssProperties uCloudOssProperties;

    @Bean
    public StandardOssClient uCloudOssClient() {
        Map<String, UCloudOssConfig> ossConfigMap = uCloudOssProperties.getOssConfig();
        if (ossConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, uCloudOssClient(uCloudOssProperties));
        } else {
            String publicKey = uCloudOssProperties.getPublicKey();
            String privateKey = uCloudOssProperties.getPrivateKey();
            String region = uCloudOssProperties.getRegion();
            String proxySuffix = uCloudOssProperties.getProxySuffix();
            HttpClient.Config clientConfig = uCloudOssProperties.getClientConfig();
            ossConfigMap.forEach((name, ossConfig) -> {
                if (ObjectUtil.isEmpty(ossConfig.getPublicKey())) {
                    ossConfig.setPublicKey(publicKey);
                }
                if (ObjectUtil.isEmpty(ossConfig.getPrivateKey())) {
                    ossConfig.setPrivateKey(privateKey);
                }
                if (ObjectUtil.isEmpty(ossConfig.getRegion())) {
                    ossConfig.setRegion(region);
                }
                if (ObjectUtil.isEmpty(ossConfig.getProxySuffix())) {
                    ossConfig.setProxySuffix(proxySuffix);
                }
                if (ObjectUtil.isEmpty(ossConfig.getClientConfig())) {
                    ossConfig.setClientConfig(clientConfig);
                }
                SpringUtil.registerBean(name, uCloudOssClient(ossConfig));
            });
        }
        return null;
    }

    public StandardOssClient uCloudOssClient(UCloudOssConfig uCloudOssConfig) {
        UfileClient.Config config = new UfileClient.Config(uCloudOssConfig.getClientConfig());
        ObjectAuthorization objectAuthorization = new UfileObjectLocalAuthorization(uCloudOssConfig.getPublicKey(), uCloudOssConfig.getPrivateKey());
        ObjectConfig objectConfig = new ObjectConfig(uCloudOssConfig.getRegion(), uCloudOssConfig.getProxySuffix());
        UfileClient ufileClient = UfileClient.configure(config);
        ObjectApiBuilder objectApiBuilder = new ObjectApiBuilder(ufileClient, objectAuthorization, objectConfig);
        return new UCloudOssClient(ufileClient, objectApiBuilder, uCloudOssConfig);
    }
}
