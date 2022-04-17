package com.taotao.cloud.oss.artislong.core.pingan;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.pingan.radosgw.sdk.config.ObsClientConfig;
import com.pingan.radosgw.sdk.service.RadosgwService;
import com.pingan.radosgw.sdk.service.RadosgwServiceFactory;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.pingan.model.PingAnOssConfig;
import com.taotao.cloud.oss.artislong.exception.OssException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConditionalOnClass(RadosgwService.class)
@EnableConfigurationProperties({PingAnOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.PINGAN + CharPool.DOT + OssConstant.ENABLE,
        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class PingAnOssConfiguration {

    public static final String DEFAULT_BEAN_NAME = "pingAnOssClient";

    @Autowired
    private PingAnOssProperties pingAnOssProperties;

    @Bean
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
            public String getUserAgent() {
                return pingAnOssConfig.getUserAgent();
            }
            public String getObsUrl() {
                return pingAnOssConfig.getObsUrl();
            }
            public String getObsAccessKey() {
                return pingAnOssConfig.getObsAccessKey();
            }
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
