package com.taotao.cloud.sys.biz.api.controller.tools.core.dtos.param;

import lombok.Data;

@Data
public class ConnectParam {
    private String host;
    private int port;

    public static final int DEFAULT_SESSION_TIMEOUT = 30000;
    public static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
    public static final int DEFAULT_MAX_ATTEMPTS = 5;

    private int sessionTimeout = DEFAULT_SESSION_TIMEOUT;
    private int connectionTimeout = DEFAULT_CONNECTION_TIMEOUT;
    private int maxAttempts = DEFAULT_MAX_ATTEMPTS;

    /**
     * 获取连接字符串
     * @return
     */
    public String getConnectString(){
        return host+":"+port;
    }

    /**
     * 获取 http 协议的地址
     * @return
     */
    public String httpConnectString(){return "http://"+host+":"+port;}
}
