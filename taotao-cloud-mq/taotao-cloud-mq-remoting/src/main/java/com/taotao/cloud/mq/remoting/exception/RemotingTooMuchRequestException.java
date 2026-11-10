package com.taotao.cloud.mq.remoting.exception;

/**
 * 请求过多异常（流控）
 */
public class RemotingTooMuchRequestException extends RemotingException {

    private static final long serialVersionUID = 1L;

    public RemotingTooMuchRequestException() {
        super();
    }

    public RemotingTooMuchRequestException(String message) {
        super(message);
    }

    public RemotingTooMuchRequestException(String message, Throwable cause) {
        super(message, cause);
    }

    public RemotingTooMuchRequestException(Throwable cause) {
        super(cause);
    }
}
