/*
 *    Copyright (c) 2018-2025, lengleng All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the pig4cloud.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 * Author: lengleng (wangiegie@gmail.com)
 */
package com.taotao.cloud.oss.config;

import com.taotao.cloud.common.constant.StarterName;
import com.taotao.cloud.common.utils.LogUtil;
import com.taotao.cloud.oss.controller.OssEndpoint;
import com.taotao.cloud.oss.core.OssTemplate;
import com.taotao.cloud.oss.props.OssProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * AWS自动配置类
 * @author lengleng
 * @author 858695266
 * @link https://github.com/pig-mesh/oss-spring-boot-starter
 * @since 1.0.0
 */
@Configuration
public class OssConfiguration implements InitializingBean {

	@Override
	public void afterPropertiesSet() throws Exception {
		LogUtil.started(OssConfiguration.class, StarterName.OSS_STARTER);
	}

    @Autowired
    @SuppressWarnings("all")
    private RedisTemplate<String, Object> redisTemplate;

    @Autowired
    @SuppressWarnings("all")
    private OssProperties properties;

    @Bean
    @ConditionalOnMissingBean(OssTemplate.class)
    @ConditionalOnProperty(name = "oss.enable", havingValue = "true", matchIfMissing = true)
    public OssTemplate ossTemplate() {
	    LogUtil.started(OssTemplate.class, StarterName.OSS_STARTER);

        return new OssTemplate(properties);
    }

    @Bean
    @ConditionalOnProperty(name = "oss.info", havingValue = "true")
    public OssEndpoint ossEndpoint(OssTemplate template) {
	    LogUtil.started(OssEndpoint.class, StarterName.OSS_STARTER);

        return new OssEndpoint(template);
    }

    @Bean
    @RefreshScope
    public OssProperties ossProperties(){
	    LogUtil.started(OssProperties.class, StarterName.OSS_STARTER);

        //ComponentConstant.OSS_DEFAULT
        return (OssProperties) redisTemplate.opsForValue().get("");
    }
}
