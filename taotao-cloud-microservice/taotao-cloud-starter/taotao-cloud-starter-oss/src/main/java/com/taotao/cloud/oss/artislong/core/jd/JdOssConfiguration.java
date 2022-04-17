package com.taotao.cloud.oss.artislong.core.jd;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.amazonaws.ClientConfiguration;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.transfer.TransferManager;
import com.amazonaws.services.s3.transfer.TransferManagerBuilder;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.jd.model.JdOssConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConditionalOnClass(AmazonS3.class)
@EnableConfigurationProperties({JdOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.JD + CharPool.DOT + OssConstant.ENABLE,
        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class JdOssConfiguration {

    public static final String DEFAULT_BEAN_NAME = "jdOssClient";

    @Autowired
    private JdOssProperties jdOssProperties;

    @Bean
    public StandardOssClient jdOssClient() {
        Map<String, JdOssConfig> jdOssConfigMap = jdOssProperties.getOssConfig();
        if (jdOssConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, jdOssClient(jdOssProperties));
        } else {
            String endpoint = jdOssProperties.getEndpoint();
            String accessKey = jdOssProperties.getAccessKey();
            String secretKey = jdOssProperties.getSecretKey();
            String region = jdOssProperties.getRegion();
            ClientConfiguration clientConfig = jdOssProperties.getClientConfig();
            jdOssConfigMap.forEach((name, jdOssConfig) -> {
                if (ObjectUtil.isEmpty(jdOssConfig.getEndpoint())) {
                    jdOssConfig.setEndpoint(endpoint);
                }
                if (ObjectUtil.isEmpty(jdOssConfig.getAccessKey())) {
                    jdOssConfig.setAccessKey(accessKey);
                }
                if (ObjectUtil.isEmpty(jdOssConfig.getSecretKey())) {
                    jdOssConfig.setSecretKey(secretKey);
                }
                if (ObjectUtil.isEmpty(jdOssConfig.getRegion())) {
                    jdOssConfig.setRegion(region);
                }
                if (ObjectUtil.isEmpty(jdOssConfig.getClientConfig())) {
                    jdOssConfig.setClientConfig(clientConfig);
                }
                SpringUtil.registerBean(name, jdOssClient(jdOssConfig));
            });
        }
        return null;
    }

    private StandardOssClient jdOssClient(JdOssConfig jdOssConfig) {
        ClientConfiguration clientConfig = jdOssConfig.getClientConfig();
        AwsClientBuilder.EndpointConfiguration endpointConfig = endpointConfig(jdOssConfig);
        AWSCredentials awsCredentials = awsCredentials(jdOssConfig);
        AWSCredentialsProvider awsCredentialsProvider = awsCredentialsProvider(awsCredentials);
        AmazonS3 amazonS3 = amazonS3(endpointConfig, clientConfig, awsCredentialsProvider);
        TransferManager transferManager = transferManager(amazonS3);
        return jdOssClient(amazonS3, transferManager, jdOssConfig);
    }

    public StandardOssClient jdOssClient(AmazonS3 amazonS3, TransferManager transferManager, JdOssConfig jdOssConfig) {
        return new JdOssClient(amazonS3, transferManager, jdOssConfig);
    }

    public AwsClientBuilder.EndpointConfiguration endpointConfig(JdOssConfig jdOssConfig) {
        return new AwsClientBuilder.EndpointConfiguration(jdOssConfig.getEndpoint(), jdOssConfig.getRegion());
    }

    public AWSCredentials awsCredentials(JdOssConfig jdOssConfig) {
        return new BasicAWSCredentials(jdOssConfig.getAccessKey(), jdOssConfig.getSecretKey());
    }

    public AWSCredentialsProvider awsCredentialsProvider(AWSCredentials awsCredentials) {
        return new AWSStaticCredentialsProvider(awsCredentials);
    }

    public AmazonS3 amazonS3(AwsClientBuilder.EndpointConfiguration endpointConfig, ClientConfiguration clientConfig,
                             AWSCredentialsProvider awsCredentialsProvider) {
        return AmazonS3Client.builder()
                .withEndpointConfiguration(endpointConfig)
                .withClientConfiguration(clientConfig)
                .withCredentials(awsCredentialsProvider)
                .disableChunkedEncoding()
                .build();
    }

    public TransferManager transferManager(AmazonS3 amazonS3) {
        return TransferManagerBuilder.standard()
                .withS3Client(amazonS3)
                .build();
    }
}
