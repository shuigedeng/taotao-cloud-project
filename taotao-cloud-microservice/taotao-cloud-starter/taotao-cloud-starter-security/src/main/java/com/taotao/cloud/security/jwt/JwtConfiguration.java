package com.taotao.cloud.security.jwt;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * 认证服务端配置
 *
 * @author zuihou
 * @date 2018/11/20
 */
@EnableConfigurationProperties(value = {
        JwtProperties.class,
})
public class JwtConfiguration {

    @Bean
    public TokenUtil getTokenUtil(JwtProperties authServerProperties) {
        return new TokenUtil(authServerProperties);
    }
}
