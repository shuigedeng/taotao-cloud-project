package com.taotao.cloud.oss.ftp;

import cn.hutool.extra.ftp.Ftp;
import cn.hutool.extra.spring.SpringUtil;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtil;
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

import java.util.Map;

/**
 * ftp操作系统配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:40:06
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@EnableConfigurationProperties({FtpOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "FTP")
public class FtpOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(FtpOssConfiguration.class, StarterName.OSS_STARTER);
	}

    public static final String DEFAULT_BEAN_NAME = "ftpOssClient";

    @Autowired
    private FtpOssProperties ftpOssProperties;

    @Bean
	@ConditionalOnMissingBean
    public StandardOssClient ftpOssClient() {
        Map<String, FtpOssConfig> ftpOssConfigMap = ftpOssProperties.getOssConfig();
        if (ftpOssConfigMap.isEmpty()) {
            SpringUtil.registerBean(DEFAULT_BEAN_NAME, ftpOssClient(ftpOssProperties));
        } else {
            ftpOssConfigMap.forEach((name, ftpOssConfig) -> SpringUtil.registerBean(name, ftpOssClient(ftpOssConfig)));
        }
        return null;
    }

    public StandardOssClient ftpOssClient(FtpOssConfig ftpOssConfig) {
        return new FtpOssClient(ftp(ftpOssConfig), ftpOssConfig);
    }

    public Ftp ftp(FtpOssConfig ftpOssConfig) {
        Ftp ftp = new Ftp(ftpOssConfig, ftpOssConfig.getMode());
        ftp.setBackToPwd(ftpOssConfig.isBackToPwd());
        return ftp;
    }

}
