package com.github.houbb.rpc.common.config.registry;

import com.github.houbb.rpc.common.config.component.RpcAddress;
import com.github.houbb.rpc.common.config.component.Credential;

import java.util.List;

/**
 * 注册中心配置类
 * @author shuigedeng
 * @since 0.0.6
 */
public interface RegistryConfig {

    /**
     * @return 地址配置列表
     * @since 0.0.6
     */
    List<RpcAddress> addressList();

    /**
     * @return 凭证信息
     * @since 0.0.6
     */
    Credential credential();


}
