package com.taotao.cloud.oss.minio;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.taotao.cloud.oss.common.condition.ConditionalOnOssEnabled;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import io.minio.MinioClient;
import io.minio.http.HttpUtils;
import okhttp3.OkHttpClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * minio oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:41:54
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@EnableConfigurationProperties({MinioOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "MINIO")
public class MinioOssConfiguration implements InitializingBean {

    public static final String DEFAULT_BEAN_NAME = "minioOssClient";

    @Autowired
    private MinioOssProperties minioOssProperties;

	@Bean
	@ConditionalOnMissingBean
    public StandardOssClient minioOssClient(MinioOssConfig minioOssConfig) {
        return new MinioOssClient(minioClient(minioOssConfig), minioOssConfig);
    }

    public MinioClient minioClient(MinioOssConfig minioOssConfig) {
        MinioOssClientConfig clientConfig = minioOssConfig.getClientConfig();
        OkHttpClient okHttpClient = HttpUtils.newDefaultHttpClient(
                clientConfig.getConnectTimeout(), clientConfig.getWriteTimeout(), clientConfig.getReadTimeout());
        return MinioClient.builder()
                .endpoint(minioOssConfig.getEndpoint())
                .credentials(minioOssConfig.getAccessKey(), minioOssConfig.getSecretKey())
                .httpClient(okHttpClient)
                .build();
    }

	@Override
	public void afterPropertiesSet() throws Exception {
		Map<String, MinioOssConfig> minioOssConfigMap = minioOssProperties.getOssConfig();
		if (minioOssConfigMap.isEmpty()) {
			SpringUtil.registerBean(DEFAULT_BEAN_NAME, minioOssClient(minioOssProperties));
		} else {
			String endpoint = minioOssProperties.getEndpoint();
			String accessKey = minioOssProperties.getAccessKey();
			String secretKey = minioOssProperties.getSecretKey();
			MinioOssClientConfig clientConfig = minioOssProperties.getClientConfig();
			minioOssConfigMap.forEach((name, minioOssConfig) -> {
				if (ObjectUtil.isEmpty(minioOssConfig.getEndpoint())) {
					minioOssConfig.setEndpoint(endpoint);
				}
				if (ObjectUtil.isEmpty(minioOssConfig.getAccessKey())) {
					minioOssConfig.setAccessKey(accessKey);
				}
				if (ObjectUtil.isEmpty(minioOssConfig.getSecretKey())) {
					minioOssConfig.setSecretKey(secretKey);
				}
				if (ObjectUtil.isEmpty(minioOssConfig.getClientConfig())) {
					minioOssConfig.setClientConfig(clientConfig);
				}
				SpringUtil.registerBean(name, minioOssClient(minioOssConfig));
			});
		}
	}
}
