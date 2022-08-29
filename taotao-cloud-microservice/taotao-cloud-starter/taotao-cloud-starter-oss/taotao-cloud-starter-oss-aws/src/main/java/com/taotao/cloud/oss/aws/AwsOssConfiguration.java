package com.taotao.cloud.oss.aws;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
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
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.awscore.defaultsmode.DefaultsMode;
import software.amazon.awssdk.services.s3.S3Client;

import java.util.Map;

/**
 * aws oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:39:35
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@EnableConfigurationProperties({AwsOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "AWS")
public class AwsOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(AwsOssConfiguration.class, StarterName.OSS_STARTER);
	}

    public static final String DEFAULT_BEAN_NAME = "awsOssClient";

    @Autowired
    private AwsOssProperties awsOssProperties;

    @Bean
	@ConditionalOnMissingBean
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
