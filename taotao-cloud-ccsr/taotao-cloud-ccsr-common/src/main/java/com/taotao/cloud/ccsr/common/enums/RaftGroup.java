package com.taotao.cloud.ccsr.common.enums;

public enum RaftGroup {

    /**
     * 配置中心
     */
    CONFIG_CENTER_GROUP("config_center_group", true),

    /**
     * 服务注册中心 TODO 待实现 -> enable=false
     */
    SERVICE_REGISTER_CENTER_GROUP("service_register_center_group", false);

    private final String name;

    private final boolean enable;

    RaftGroup(String name, boolean enable) {
        this.name = name;
        this.enable = enable;
    }

    public String getName() {
        return name;
    }

    public boolean isEnable() {
        return enable;
    }
}
