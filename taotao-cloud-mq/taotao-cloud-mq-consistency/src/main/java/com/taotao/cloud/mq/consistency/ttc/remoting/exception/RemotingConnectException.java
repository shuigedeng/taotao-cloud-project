package com.taotao.cloud.mq.consistency.ttc.remoting.exception;

/**
 * 客户端连接到远端失败（对标 RocketMQ RemotingConnectException）.
 */
public class RemotingConnectException extends RemotingException {

    private static final long serialVersionUID = 1L;

    public RemotingConnectException(String addr) {
        super("connect to " + addr + " failed");
    }

    public RemotingConnectException(String addr, Throwable cause) {
        super("connect to " + addr + " failed", cause);
    }

    public static RemotingConnectException newConnectException(String addr, Throwable cause) {
        return new RemotingConnectException(addr, cause);
    }
}
