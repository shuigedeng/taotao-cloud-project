package com.taotao.cloud.common.enums;

public enum SwitchEnum {

    /**
     * 开关
     */
    OPEN("开启"), CLOSE("关闭");

    private String description;

    SwitchEnum(String description) {
        this.description = description;
    }

    public String description() {
        return description;
    }


}
