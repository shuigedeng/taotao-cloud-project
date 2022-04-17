package com.taotao.cloud.oss.artislong.core.huawei;

import cn.hutool.core.text.CharPool;
import com.taotao.cloud.oss.artislong.constant.OssConstant;
import com.taotao.cloud.oss.artislong.core.huawei.model.HuaweiOssConfig;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.HashMap;
import java.util.Map;

/**
 * @author 陈敏
 * @version HuaWeiOssProperties.java, v 1.1 2021/11/25 9:56 chenmin Exp $
 * Created on 2021/11/25
 */
@ConfigurationProperties(OssConstant.OSS + CharPool.DOT + OssConstant.OssType.HUAWEI)
public class HuaWeiOssProperties extends HuaweiOssConfig implements InitializingBean {

    private Boolean enable = false;
    
    private Map<String, HuaweiOssConfig> ossConfig = new HashMap<>();

    @Override
    public void afterPropertiesSet() {
        if (ossConfig.isEmpty()) {
            this.init();
        } else {
            ossConfig.values().forEach(HuaweiOssConfig::init);
        }
    }

	public Boolean getEnable() {
		return enable;
	}

	public void setEnable(Boolean enable) {
		this.enable = enable;
	}

	public Map<String, HuaweiOssConfig> getOssConfig() {
		return ossConfig;
	}

	public void setOssConfig(
		Map<String, HuaweiOssConfig> ossConfig) {
		this.ossConfig = ossConfig;
	}
}
