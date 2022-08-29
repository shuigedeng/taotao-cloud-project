package com.taotao.cloud.oss.tencent;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.qcloud.cos.COSClient;
import com.qcloud.cos.ClientConfig;
import com.qcloud.cos.auth.BasicCOSCredentials;
import com.qcloud.cos.auth.COSCredentials;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.condition.ConditionalOnOssEnabled;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import java.util.Map;

/**
 * 腾讯oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:13
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@ConditionalOnClass(COSClient.class)
@EnableConfigurationProperties({TencentOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "TENCENT")
public class TencentOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(TencentOssConfiguration.class, StarterName.OSS_STARTER);
	}


	public static final String DEFAULT_BEAN_NAME = "tencentOssClient";

    @Autowired
    private TencentOssProperties tencentOssProperties;

    @Bean
	@ConditionalOnMissingBean
    public StandardOssClient tencentOssClient() {
        Map<String, TencentOssConfig> tencentOssConfigMap = tencentOssProperties.getOssConfig();
        if (tencentOssConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, tencentOssClient(tencentOssProperties));
        } else {
            String secretId = tencentOssProperties.getSecretId();
            String secretKey = tencentOssProperties.getSecretKey();
            ClientConfig clientConfig = tencentOssProperties.getClientConfig();
            tencentOssConfigMap.forEach((name, tencentOssConfig) -> {
                if (ObjectUtil.isEmpty(tencentOssConfig.getSecretId())) {
                    tencentOssConfig.setSecretId(secretId);
                }
                if (ObjectUtil.isEmpty(tencentOssConfig.getSecretKey())) {
                    tencentOssConfig.setSecretKey(secretKey);
                }
                if (ObjectUtil.isEmpty(tencentOssConfig.getClientConfig())) {
                    tencentOssConfig.setClientConfig(clientConfig);
                }
                SpringUtil.registerBean(name, tencentOssClient(tencentOssConfig));
            });
        }
        return null;
    }

    private StandardOssClient tencentOssClient(TencentOssConfig tencentOssConfig) {
        ClientConfig clientConfig = tencentOssConfig.getClientConfig();
        COSCredentials cosCredentials = cosCredentials(tencentOssConfig);
        COSClient cosClient = cosClient(cosCredentials, clientConfig);
        return tencentOssClient(cosClient, tencentOssConfig);
    }

    public StandardOssClient tencentOssClient(COSClient cosClient, TencentOssConfig tencentOssConfig) {
        return new TencentOssClient(cosClient, tencentOssConfig);
    }

    public COSCredentials cosCredentials(TencentOssConfig tencentOssConfig) {
        return new BasicCOSCredentials(tencentOssConfig.getSecretId(), tencentOssConfig.getSecretKey());
    }

    public COSClient cosClient(COSCredentials cred, ClientConfig clientConfig) {
        return new COSClient(cred, clientConfig);
    }
}
