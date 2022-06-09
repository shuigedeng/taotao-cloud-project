package com.taotao.cloud.oss.qiniu;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
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
 * 气妞妞oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:44
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@EnableConfigurationProperties({QiNiuOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "QINIU")
public class QiNiuOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(QiNiuOssConfiguration.class, StarterName.OSS_STARTER);
	}

    public static final String DEFAULT_BEAN_NAME = "qiNiuOssClient";

    @Autowired
    private QiNiuOssProperties qiNiuOssProperties;

    @Bean
	@ConditionalOnMissingBean
    public StandardOssClient qiNiuOssClient() {
        Map<String, QiNiuOssConfig> qiNiuOssConfigMap = qiNiuOssProperties.getOssConfig();
        if (qiNiuOssConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, qiNiuOssClient(qiNiuOssProperties));
        } else {
            String accessKey = qiNiuOssProperties.getAccessKey();
            String secretKey = qiNiuOssProperties.getSecretKey();
            qiNiuOssConfigMap.forEach((name, qiNiuOssConfig) -> {
                if (ObjectUtil.isEmpty(qiNiuOssConfig.getAccessKey())) {
                    qiNiuOssConfig.setAccessKey(accessKey);
                }
                if (ObjectUtil.isEmpty(qiNiuOssConfig.getSecretKey())) {
                    qiNiuOssConfig.setSecretKey(secretKey);
                }
                SpringUtil.registerBean(name, qiNiuOssClient(qiNiuOssConfig));
            });
        }
        return null;
    }

    private StandardOssClient qiNiuOssClient(QiNiuOssConfig qiNiuOssConfig) {
        Auth auth = auth(qiNiuOssConfig);
        com.qiniu.storage.Configuration configuration = configuration(qiNiuOssConfig);
        UploadManager uploadManager = uploadManager(configuration);
        BucketManager bucketManager = bucketManager(auth, configuration);
        return qiNiuOssClient(auth, uploadManager, bucketManager, qiNiuOssConfig);
    }

    public StandardOssClient qiNiuOssClient(Auth auth, UploadManager uploadManager, BucketManager bucketManager, QiNiuOssConfig qiNiuOssConfig) {
        return new QiNiuOssClient(auth, uploadManager, bucketManager, qiNiuOssConfig);
    }

    public Auth auth(QiNiuOssConfig qiNiuOssConfig) {
        return Auth.create(qiNiuOssConfig.getAccessKey(), qiNiuOssConfig.getSecretKey());
    }

    public UploadManager uploadManager(com.qiniu.storage.Configuration configuration) {
        return new UploadManager(configuration);
    }

    public BucketManager bucketManager(Auth auth, com.qiniu.storage.Configuration configuration) {
        return new BucketManager(auth, configuration);
    }

    public com.qiniu.storage.Configuration configuration(QiNiuOssConfig qiNiuOssConfig) {
        return new com.qiniu.storage.Configuration(qiNiuOssConfig.getRegion().buildRegion());
    }


}
