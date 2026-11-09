package com.taotao.cloud.mq.consistency.ttc.remoting.netty;

/**
 * 系统响应码
 */
public class RemotingSysResponseCode {

    // 成功
    public static final int SUCCESS = 0;

    // 系统错误
    public static final int SYSTEM_ERROR = 1;

    // 系统繁忙
    public static final int SYSTEM_BUSY = 2;

    // 请求代码不支持
    public static final int REQUEST_CODE_NOT_SUPPORTED = 3;

    // 事务失败
    public static final int TRANSACTION_FAILED = 4;
}
