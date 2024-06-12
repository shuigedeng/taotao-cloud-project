package com.taotao.cloud.rpc.common.common.config.registry;

import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import com.taotao.cloud.rpc.common.common.config.component.Credential;

import java.util.List;

/**
 * 注册中心配置类
 * @author shuigedeng
 * @since 2024.06
 */
public interface RegistryConfig {

    /**
     * @return 地址配置列表
     * @since 2024.06
     */
    List<RpcAddress> addressList();

    /**
     * @return 凭证信息
     * @since 2024.06
     */
    Credential credential();


}
