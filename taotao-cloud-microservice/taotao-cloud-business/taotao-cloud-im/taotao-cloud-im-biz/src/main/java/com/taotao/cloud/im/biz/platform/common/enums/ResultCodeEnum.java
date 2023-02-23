package com.taotao.cloud.im.biz.platform.common.enums;

import lombok.Getter;

/**
 * 返回码枚举
 */
@Getter
public enum ResultCodeEnum {

    /**
     * 操作成功
     */
    SUCCESS(200, "操作成功"),
    /**
     * 未授权
     */
    UNAUTHORIZED(401, "token失效，请重新登录"),
    /**
     * 资源/服务未找到
     */
    NOT_FOUND(404, "路径不存在，请检查路径是否正确"),
    /**
     * 操作失败
     */
    FAIL(500, "系统异常，请联系管理员"),
    /**
     * 版本号
     */
    VERSION(601, "版本过低，请升级"),
    ;

    private final Integer code;
    private final String info;

    ResultCodeEnum(Integer code, String info) {
        this.code = code;
        this.info = info;
    }

}
