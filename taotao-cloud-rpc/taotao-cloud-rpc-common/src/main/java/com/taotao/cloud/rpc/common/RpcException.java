package com.taotao.cloud.rpc.common;

/**
 * RPC 异常
 * <p>
 * 统一的 RPC 调用异常，携带错误码便于客户端针对性处理。
 */
public class RpcException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 错误码
     */
    private final int code;

    public RpcException(int code, String message) {
        super(message);
        this.code = code;
    }

    public RpcException(int code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }

    public RpcException(int code, Throwable cause) {
        super(cause);
        this.code = code;
    }

    public int getCode() {
        return code;
    }

    // ====================== 错误码常量 ======================

    /** 未知错误 */
    public static final int UNKNOWN_EXCEPTION = 0;
    /** 网络层异常（连接失败/IO 错误） */
    public static final int NETWORK_EXCEPTION = 1;
    /** 超时（连接超时/读取超时） */
    public static final int TIMEOUT_EXCEPTION = 2;
    /** 业务异常 */
    public static final int BIZ_EXCEPTION = 3;
    /** 禁止访问（鉴权/Token 失败） */
    public static final int FORBIDDEN_EXCEPTION = 4;
    /** 没有可用 Provider */
    public static final int NO_PROVIDER_EXCEPTION = 5;
    /** 序列化异常 */
    public static final int SERIALIZATION_EXCEPTION = 6;
    /** 协议解析异常（魔数/版本不匹配） */
    public static final int PROTOCOL_EXCEPTION = 7;
    /** 重连异常 */
    public static final int RECONNECT_EXCEPTION = 8;
    /** 限流/过载 */
    public static final int LIMIT_EXCEPTION = 9;
    /** 配置异常 */
    public static final int CONFIG_EXCEPTION = 10;

    // ====================== 工厂方法 ======================

    public static RpcException timeout(String msg) {
        return new RpcException(TIMEOUT_EXCEPTION, msg);
    }

    public static RpcException timeout(String msg, Throwable cause) {
        return new RpcException(TIMEOUT_EXCEPTION, msg, cause);
    }

    public static RpcException network(String msg) {
        return new RpcException(NETWORK_EXCEPTION, msg);
    }

    public static RpcException network(String msg, Throwable cause) {
        return new RpcException(NETWORK_EXCEPTION, msg, cause);
    }

    public static RpcException noProvider(String msg) {
        return new RpcException(NO_PROVIDER_EXCEPTION, msg);
    }

    public static RpcException serialization(String msg, Throwable cause) {
        return new RpcException(SERIALIZATION_EXCEPTION, msg, cause);
    }

    public static RpcException serialization(String msg) {
        return new RpcException(SERIALIZATION_EXCEPTION, msg);
    }

    public static RpcException protocol(String msg) {
        return new RpcException(PROTOCOL_EXCEPTION, msg);
    }

    public static RpcException biz(String msg) {
        return new RpcException(BIZ_EXCEPTION, msg);
    }

    public static RpcException biz(String msg, Throwable cause) {
        return new RpcException(BIZ_EXCEPTION, msg, cause);
    }

    public static RpcException forbidden(String msg) {
        return new RpcException(FORBIDDEN_EXCEPTION, msg);
    }

    public static RpcException limit(String msg) {
        return new RpcException(LIMIT_EXCEPTION, msg);
    }

    public static RpcException config(String msg) {
        return new RpcException(CONFIG_EXCEPTION, msg);
    }
}
