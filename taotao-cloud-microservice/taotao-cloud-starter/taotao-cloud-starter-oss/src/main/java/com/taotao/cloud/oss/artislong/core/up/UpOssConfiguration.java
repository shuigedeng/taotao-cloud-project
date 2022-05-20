package com.taotao.cloud.oss.artislong.core.up;

import cn.hutool.core.text.CharPool;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.StandardOssClient;
import com.taotao.cloud.oss.artislong.core.up.model.UpOssConfig;
import com.taotao.cloud.oss.artislong.model.SliceConfig;
import com.upyun.ParallelUploader;
import com.upyun.RestManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * 了开源软件配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:43:50
 */
@AutoConfiguration
@ConditionalOnClass(RestManager.class)
@EnableConfigurationProperties({UpOssProperties.class})
@ConditionalOnProperty(prefix = OssConstant.OSS, name = OssConstant.OssType.UP + CharPool.DOT + OssConstant.ENABLE,
        havingValue = OssConstant.DEFAULT_ENABLE_VALUE)
public class UpOssConfiguration {

    public static final String DEFAULT_BEAN_NAME = "upOssClient";

    @Autowired
    private UpOssProperties upOssProperties;

    @Bean
    public StandardOssClient upOssClient() {
        Map<String, UpOssConfig> upOssConfigMap = upOssProperties.getOssConfig();
        if (upOssConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, upOssClient(upOssProperties));
        } else {
            String userName = upOssProperties.getUserName();
            String password = upOssProperties.getPassword();
            upOssConfigMap.forEach((name, upOssConfig) -> {
                if (ObjectUtil.isEmpty(upOssConfig.getUserName())) {
                    upOssConfig.setUserName(userName);
                }
                if (ObjectUtil.isEmpty(upOssConfig.getPassword())) {
                    upOssConfig.setPassword(password);
                }
                SpringUtil.registerBean(name, upOssClient(upOssConfig));
            });
        }
        return null;
    }

    private StandardOssClient upOssClient(UpOssConfig upOssConfig) {
        RestManager restManager = restManager(upOssConfig);
        ParallelUploader parallelUploader = parallelUploader(upOssConfig);
        return upOssClient(restManager, parallelUploader, upOssConfig);
    }

    public StandardOssClient upOssClient(RestManager restManager, ParallelUploader parallelUploader, UpOssConfig upOssConfig) {
        return new UpOssClient(restManager, parallelUploader, upOssConfig);
    }

    public RestManager restManager(UpOssConfig upOssConfig) {
        RestManager restManager = new RestManager(upOssConfig.getBucketName(), upOssConfig.getUserName(), upOssConfig.getPassword());
        // 手动设置超时时间：默认为30秒
        restManager.setTimeout(upOssConfig.getTimeout());
        // 选择最优的接入点
        restManager.setApiDomain(upOssConfig.getApiDomain().toString());
        return restManager;
    }

    public ParallelUploader parallelUploader(UpOssConfig upOssConfig) {
        ParallelUploader parallelUploader = new ParallelUploader(upOssConfig.getBucketName(), upOssConfig.getUserName(), upOssConfig.getPassword());

        SliceConfig sliceConfig = upOssConfig.getSliceConfig();
        parallelUploader.setParallel(sliceConfig.getTaskNum());
        parallelUploader.setCheckMD5(true);
        parallelUploader.setTimeout(upOssConfig.getTimeout());
        return parallelUploader;
    }
}
