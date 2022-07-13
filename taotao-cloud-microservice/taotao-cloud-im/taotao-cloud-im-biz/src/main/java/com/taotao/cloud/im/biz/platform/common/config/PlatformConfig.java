package com.taotao.cloud.im.biz.platform.common.config;

import com.platform.common.core.EnumUtils;
import com.platform.common.enums.YesOrNoEnum;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

/**
 * 读取项目相关配置
 */
@Component
@Configuration
@ConfigurationProperties(prefix = "platform")
public class PlatformConfig {

    /**
     * token超时时间（分钟）
     */
    public static Integer TIMEOUT;

    /**
     * 是否开启短信
     */
    public static YesOrNoEnum SMS;

    /**
     * 上传路径
     */
    public static String UPLOAD_PATH;

    @Value("${platform.timeout}")
    public void setTokenTimeout(Integer timeout) {
        PlatformConfig.TIMEOUT = timeout;
    }

    @Value("${platform.sms:N}")
    public void setSms(String sms) {
        PlatformConfig.SMS = EnumUtils.toEnum(YesOrNoEnum.class, sms, YesOrNoEnum.NO);
    }

    @Value("${platform.uploadPath}")
    public void setUploadPath(String uploadPath) {
        PlatformConfig.UPLOAD_PATH = uploadPath;
    }

}
