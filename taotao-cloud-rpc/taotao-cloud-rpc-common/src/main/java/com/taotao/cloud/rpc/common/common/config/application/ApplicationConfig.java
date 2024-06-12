package com.taotao.cloud.rpc.common.common.config.application;

/**
 * 应用配置信息
 * （1）服务的应用应该是一个单例。
 * （2）对于用户可以不可见，直接根据 rpc.properties 设置。
 * @author shuigedeng
 * @since 2024.06
 */
public interface ApplicationConfig {

    /**
     * @return 应用名称
     * @since 2024.06
     */
    String name();

    /**
     * @return 环境名称
     * dev test pre_prod prod
     * @since 2024.06
     */
    String env();

}
