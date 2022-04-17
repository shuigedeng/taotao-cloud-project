package com.taotao.cloud.oss.artislong.core.qiniu;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.qiniu.storage.BucketManager;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.qiniu.model.QiNiuOssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@EnableConfigurationProperties({QiNiuOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.QINIU + CharPool.DOT + OssConstant.ENABLE,
        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class QiNiuOssConfiguration {

    public static final String DEFAULT_BEAN_NAME = "qiNiuOssClient";

    @Autowired
    private QiNiuOssProperties qiNiuOssProperties;

    @Bean
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
