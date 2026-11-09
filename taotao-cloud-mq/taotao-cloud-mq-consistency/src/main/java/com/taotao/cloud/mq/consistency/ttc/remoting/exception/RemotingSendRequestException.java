package com.taotao.cloud.mq.consistency.ttc.remoting.exception;

/**
 * 发送请求异常
 */
public class RemotingSendRequestException extends RemotingException {

    private static final long serialVersionUID = 1L;

    public RemotingSendRequestException() {
        super();
    }

    public RemotingSendRequestException(String message) {
        super(message);
    }

    public RemotingSendRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemotingSendRequestException(Throwable cause) {
        super(cause);
    }

    /**
     * 创建发送请求异常
     *
     * @param addr  地址
     * @param cause 原因
     */
    public static RemotingSendRequestException newSendRequestException(String addr, Throwable cause) {
        return new RemotingSendRequestException(
                String.format("Send request to %s failed", addr), cause);
    }
}
