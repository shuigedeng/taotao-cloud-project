package com.taotao.cloud.mq.remoting.exception;

/**
 * 远程通信超时异常
 */
public class RemotingTimeoutException extends RemotingException {

    private static final long serialVersionUID = 1L;

    public RemotingTimeoutException() {
        super();
    }

    public RemotingTimeoutException(String message) {
        super(message);
    }

    public RemotingTimeoutException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemotingTimeoutException(Throwable cause) {
        super(cause);
    }

    /**
     * 创建超时异常
     *
     * @param addr          地址
     * @param timeoutMillis 超时时间（毫秒）
     */
    public static RemotingTimeoutException newTimeoutException(String addr, long timeoutMillis) {
        return new RemotingTimeoutException(
                String.format("Wait response from %s timeout, %d ms", addr, timeoutMillis));
    }
}
