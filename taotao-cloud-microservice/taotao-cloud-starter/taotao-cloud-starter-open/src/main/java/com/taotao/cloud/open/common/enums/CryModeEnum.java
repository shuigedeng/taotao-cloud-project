package com.taotao.cloud.open.common.enums;

/**
 * 加密模式
 *
 * @author wanghuidong
 * 时间： 2022/7/10 19:17
 */
public enum CryModeEnum {

    /**
     * 未知(用于注解上配置)
     */
    UNKNOWN,

    /**
     * 不加密（仅对参数进行签名与验证）
     */
    NONE,

    /**
     * 非对称加密（对参数和返回值使用非对称加密）
     */
    ASYMMETRIC_CRY,

    /**
     * 对称加密（对参数和返回值使用对称加密，对称加密密钥则用非对称加密后传输）
     */
    SYMMETRIC_CRY
}
