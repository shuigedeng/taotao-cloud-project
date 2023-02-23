package com.taotao.cloud.im.biz.platform.common.constant;

/**
 * 头部常量
 */
public class HeadConstant {

    /**
     * 令牌
     */
    public static final String TOKEN_KEY = "Authorization";

    /**
     * 版本号
     */
    public static final String VERSION = "version";

    /**
     * 设备类型
     */
    public static final String DEVICE = "device";

    /**
     * 设备
     */
    private static final String DEVICE_PREFIX = DEVICE + "=";

    /**
     * 设备android
     */
    public static final String ANDROID = DEVICE_PREFIX + "android";

    /**
     * 设备ios
     */
    public static final String IOS = DEVICE_PREFIX + "ios";

}
