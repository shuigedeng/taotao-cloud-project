package com.taotao.cloud.im.biz.platform.modules.common.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * 读取oss相关配置
 */
@Component
@Data
public class OssConfig {

    @Value("${oss.serverUrl}")
    private String serverUrl;
    @Value("${oss.accessKey}")
    private String accessKey;
    @Value("${oss.secretKey}")
    private String secretKey;
    @Value("${oss.bucketName}")
    private String bucketName;
    @Value("${oss.region}")
    private String region;

}
