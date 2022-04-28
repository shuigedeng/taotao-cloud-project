package com.taotao.cloud.oss.artislong.core.qingyun;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.qingyun.model.QingYunOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * 清云操作系统属性
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:42:22
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.QINGYUN)
public class QingYunOssProperties extends QingYunOssConfig implements InitializingBean {

    private Boolean enable = false;

    private Map<String, QingYunOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(QingYunOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, QingYunOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, QingYunOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
