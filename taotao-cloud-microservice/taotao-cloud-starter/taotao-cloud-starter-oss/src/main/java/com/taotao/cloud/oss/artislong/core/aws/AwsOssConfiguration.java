package com.taotao.cloud.oss.artislong.core.aws;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.aws.constant.AwsRegion;
import com.taotao.cloud.oss.artislong.core.aws.model.AwsOssClientConfig;
import com.taotao.cloud.oss.artislong.core.aws.model.AwsOssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.awscore.defaultsmode.DefaultsMode;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Map;

/**
 * @author 陈敏
 * @version AwsOssConfiguration.java, v 1.0 2022/4/1 18:02 chenmin Exp $
 * Created on 2022/4/1
 */
@Configuration
@ConditionalOnClass(S3Client.class)
@EnableConfigurationProperties({AwsOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.AWS + CharPool.DOT + OssConstant.ENABLE,
        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class AwsOssConfiguration {

    public static final String DEFAULT_BEAN_NAME = "awsOssClient";

    @Autowired
    private AwsOssProperties awsOssProperties;

    @Bean
    public StandardOssClient awsOssClient() {
        Map<String, AwsOssConfig> ossConfigMap = awsOssProperties.getOssConfig();
        if (ossConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, awsOssClient(awsOssProperties));
        } else {
            String accessKeyId = awsOssProperties.getAccessKeyId();
            String secretAccessKey = awsOssProperties.getSecretAccessKey();
            DefaultsMode mode = awsOssProperties.getMode();
            AwsRegion region = awsOssProperties.getRegion();
            ossConfigMap.forEach((name, ossConfig) -> {
                if (ObjectUtil.isEmpty(ossConfig.getAccessKeyId())) {
                    ossConfig.setAccessKeyId(accessKeyId);
                }
                if (ObjectUtil.isEmpty(ossConfig.getSecretAccessKey())) {
                    ossConfig.setSecretAccessKey(secretAccessKey);
                }
                if (ObjectUtil.isEmpty(ossConfig.getMode())) {
                    ossConfig.setMode(mode);
                }
                if (ObjectUtil.isEmpty(ossConfig.getRegion())) {
                    ossConfig.setRegion(region);
                }
                SpringUtil.registerBean(name, ossConfig);
            });
        }
        return null;
    }

    public StandardOssClient awsOssClient(AwsOssConfig ossConfig) {
        return new AwsOssClient(s3Client(ossConfig), ossConfig);
    }

    public S3Client s3Client(AwsOssConfig ossConfig) {
        AwsOssClientConfig clientConfig = ossConfig.getClientConfig();
        return S3Client.builder().credentialsProvider(() -> new AwsCredentials() {
                    @Override
                    public String accessKeyId() {
                        return ossConfig.getAccessKeyId();
                    }

                    @Override
                    public String secretAccessKey() {
                        return ossConfig.getSecretAccessKey();
                    }
                }).region(ossConfig.getRegion().getRegion())
                .serviceConfiguration(builder -> builder
                        .accelerateModeEnabled(clientConfig.getAccelerateModeEnabled())
                        .checksumValidationEnabled(clientConfig.getChecksumValidationEnabled())
                        .multiRegionEnabled(clientConfig.getMultiRegionEnabled())
                        .chunkedEncodingEnabled(clientConfig.getChunkedEncodingEnabled())
                        .pathStyleAccessEnabled(clientConfig.getPathStyleAccessEnabled())
                        .useArnRegionEnabled(clientConfig.getUseArnRegionEnabled())
                )
                .fipsEnabled(clientConfig.getFipsEnabled())
                .dualstackEnabled(clientConfig.getDualstackEnabled()).build();
    }
}
