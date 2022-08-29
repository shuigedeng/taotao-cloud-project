package com.taotao.cloud.oss.qingyun;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.qingstor.sdk.config.EnvContext;
import com.qingstor.sdk.service.Bucket;
import com.qingstor.sdk.service.QingStor;
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
 * 清云操作系统配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:19
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@EnableConfigurationProperties({QingYunOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "QINGYUN")
public class QingYunOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(QingYunOssConfiguration.class, StarterName.OSS_STARTER);
	}

    public static final String DEFAULT_BEAN_NAME = "qingYunOssClient";

    @Autowired
    private QingYunOssProperties qingYunOssProperties;

    @Bean
	@ConditionalOnMissingBean
    public StandardOssClient qingYunOssClient() {
        Map<String, QingYunOssConfig> ossConfigMap = qingYunOssProperties.getOssConfig();
        if (ossConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, qingYunOssClient(qingYunOssProperties));
        } else {
            String endpoint = qingYunOssProperties.getEndpoint();
            String accessKey = qingYunOssProperties.getAccessKey();
            String accessSecret = qingYunOssProperties.getAccessSecret();
            String zone = qingYunOssProperties.getZone();
            EnvContext.HttpConfig clientConfig = qingYunOssProperties.getClientConfig();
            ossConfigMap.forEach((name, ossConfig) -> {
                if (ObjectUtil.isEmpty(ossConfig.getEndpoint())) {
                    ossConfig.setEndpoint(endpoint);
                }
                if (ObjectUtil.isEmpty(ossConfig.getAccessKey())) {
                    ossConfig.setAccessKey(accessKey);
                }
                if (ObjectUtil.isEmpty(ossConfig.getAccessSecret())) {
                    ossConfig.setAccessSecret(accessSecret);
                }
                if (ObjectUtil.isEmpty(ossConfig.getZone())) {
                    ossConfig.setZone(zone);
                }
                if (ObjectUtil.isEmpty(ossConfig.getClientConfig())) {
                    ossConfig.setClientConfig(clientConfig);
                }
                SpringUtil.registerBean(name, qingYunOssClient(ossConfig));
            });
        }
        return null;
    }

    public StandardOssClient qingYunOssClient(QingYunOssConfig qingYunOssConfig) {
        QingStor qingStor = qingStor(qingYunOssConfig);
        Bucket bucket = qingStor.getBucket(qingYunOssConfig.getBucketName(), qingYunOssConfig.getZone());
        return new QingYunOssClient(qingStor, bucket, qingYunOssConfig);
    }

    public QingStor qingStor(QingYunOssConfig qingYunOssConfig) {
        EnvContext env = new EnvContext(qingYunOssConfig.getAccessKey(), qingYunOssConfig.getAccessSecret());
        env.setHttpConfig(qingYunOssConfig.getClientConfig());
        String endpoint = qingYunOssConfig.getEndpoint();
        if (ObjectUtil.isNotEmpty(endpoint)) {
            env.setEndpoint(endpoint);
        }
        env.setCnameSupport(qingYunOssConfig.getCnameSupport());
        env.setAdditionalUserAgent(qingYunOssConfig.getAdditionalUserAgent());
        env.setVirtualHostEnabled(qingYunOssConfig.getVirtualHostEnabled());
        return new QingStor(env);
    }
}
