package com.taotao.cloud.rpc.common.common.config.registry.impl;

import com.taotao.cloud.rpc.common.common.config.component.RpcAddress;
import com.taotao.cloud.rpc.common.common.config.component.Credential;
import com.taotao.cloud.rpc.common.common.config.registry.RegistryConfig;

import java.util.List;

/**
 * 注册中心配置类
 * @author shuigedeng
 * @since 0.0.6
 */
public class DefaultRegistryConfig implements RegistryConfig {

    /**
     * 地址配置列表
     * @since 0.0.6
     */
    private List<RpcAddress> rpcAddressList;

    /**
     * 凭证信息
     * @since 0.0.6
     */
    private Credential credential;

    @Override
    public List<RpcAddress> addressList() {
        return rpcAddressList;
    }

    public DefaultRegistryConfig addressList(List<RpcAddress> rpcAddressList) {
        this.rpcAddressList = rpcAddressList;
        return this;
    }

    @Override
    public Credential credential() {
        return credential;
    }

    public DefaultRegistryConfig credential(Credential credential) {
        this.credential = credential;
        return this;
    }
}
