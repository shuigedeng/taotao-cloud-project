package com.taotao.cloud.oss.sftp;

import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.extra.ssh.Sftp;
import com.jcraft.jsch.ChannelSftp;
import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.log.LogUtils;
import com.taotao.cloud.oss.common.condition.ConditionalOnOssEnabled;
import com.taotao.cloud.oss.common.propeties.OssProperties;
import com.taotao.cloud.oss.common.service.StandardOssClient;
import java.util.Map;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * sftp oss配置
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:59
 */
@AutoConfiguration
@ConditionalOnOssEnabled
@ConditionalOnClass(ChannelSftp.class)
@EnableConfigurationProperties({SftpOssProperties.class})
@ConditionalOnProperty(prefix = OssProperties.PREFIX, name = "type", havingValue = "SFTP")
public class SftpOssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtils.started(SftpOssConfiguration.class, StarterName.OSS_SFTP_STARTER);
	}

	public static final String DEFAULT_BEAN_NAME = "sftpOssClient";

	@Autowired
	private SftpOssProperties sftpOssProperties;

	@Bean
	@ConditionalOnMissingBean
	public StandardOssClient sftpOssClient() {
		Map<String, SftpOssConfig> sftpOssConfigMap = sftpOssProperties.getOssConfig();
		if (sftpOssConfigMap.isEmpty()) {
			SpringUtil.registerBean(DEFAULT_BEAN_NAME, sftpOssClient(sftpOssProperties));
		} else {
			sftpOssConfigMap.forEach((name, sftpOssConfig) -> SpringUtil.registerBean(name,
				sftpOssClient(sftpOssConfig)));
		}
		return null;
	}

	public StandardOssClient sftpOssClient(SftpOssConfig sftpOssConfig) {
		return new SftpOssClient(sftp(sftpOssConfig), sftpOssConfig);
	}

	public Sftp sftp(SftpOssConfig sftpOssConfig) {
		return new Sftp(sftpOssConfig);
	}

}
