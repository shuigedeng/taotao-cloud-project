package com.taotao.cloud.oss.ucloud;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.ucloud.ufile.UfileClient;
import cn.ucloud.ufile.api.object.ObjectApiBuilder;
import cn.ucloud.ufile.api.object.ObjectConfig;
import cn.ucloud.ufile.auth.ObjectAuthorization;
import cn.ucloud.ufile.auth.UfileObjectLocalAuthorization;
import cn.ucloud.ufile.http.HttpClient;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
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
 * ucloud oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:29
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@ConditionalOnClass(UfileClient.class)
@EnableConfigurationProperties({UCloudOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "UCLOUD")
public class UCloudOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(UCloudOssConfiguration.class, StarterName.OSS_STARTER);
	}

    public static final String DEFAULT_BEAN_NAME = "uCloudOssClient";

    @Autowired
    private UCloudOssProperties uCloudOssProperties;

    @Bean
	@ConditionalOnMissingBean
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
