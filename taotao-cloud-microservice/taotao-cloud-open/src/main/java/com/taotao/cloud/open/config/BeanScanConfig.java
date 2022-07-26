package com.taotao.cloud.open.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * 豆扫描配置
 *
 * @author shuigedeng
 * @version 2022.07
 * @since 2022-07-26 10:10:54
 */
@ComponentScans(value = {@ComponentScan(value = "openapi.server.sdk")})
@EntityScan(basePackages = {"openapi.server.sdk"})
@Configuration
public class BeanScanConfig implements EnvironmentAware {

    @Override
    public void setEnvironment(Environment environment) {
        System.out.println("############################ init openapi-server-sdk BeanScanConfig ##############################");
    }
}
