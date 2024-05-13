package com.taotao.cloud.rpc.common.common.util;

/**
 * IP 工具類
 * @since 0.2.0
 */
public final class IpUtils {

    /**
     * 构建对应的 ip:port 结果
     * @param ip 地址
     * @param port 端口
     * @return 结果
     * @since 0.2.0
     */
    public static String ipPort(String ip, int port) {
        return ip+":"+port;
    }
}
