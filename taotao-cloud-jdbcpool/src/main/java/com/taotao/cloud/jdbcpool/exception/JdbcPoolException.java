package com.taotao.cloud.jdbcpool.exception;

/**
 * @author shuigedeng
 * @since 1.0.0
 */
public class JdbcPoolException extends RuntimeException {

    public JdbcPoolException() {
    }

    public JdbcPoolException(String message) {
        super(message);
    }

    public JdbcPoolException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcPoolException(Throwable cause) {
        super(cause);
    }

    public JdbcPoolException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
