package com.taotao.cloud.mq.remoting.exception;

/**
 * 远程通信异常基类
 */
public class RemotingException extends Exception {

    private static final long serialVersionUID = 1L;

    public RemotingException() {
        super();
    }

    public RemotingException(String message) {
        super(message);
    }

    public RemotingException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemotingException(Throwable cause) {
        super(cause);
    }
}
