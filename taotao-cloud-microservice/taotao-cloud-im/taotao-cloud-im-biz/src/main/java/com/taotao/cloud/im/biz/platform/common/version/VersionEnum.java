package com.taotao.cloud.im.biz.platform.common.version;

import lombok.Getter;

/**
 * 版本枚举值
 */
@Getter
public enum VersionEnum {

    V1_0_0("1.0.0", "初始化版本"),
    V_LAST("9.9.9", "最后的版本"),
    ;

    private final String code;
    private final String info;

    VersionEnum(String code, String info) {
        this.code = code;
        this.info = info;
    }

}
