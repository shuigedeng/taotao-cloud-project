package com.taotao.cloud.canal.client.exception;

/**
 * canal 操作的异常类
 *
 * @author 阿导
 * @CopyRight 萬物皆導
 * @created 2018/5/28 16:39
 * @Modified_By 阿导 2018/5/28 16:39
 */
public class CanalClientException extends RuntimeException {
    
    /**
     * 默认构造方法
     *
     * @author 阿导
     * @time 2018/5/28 16:39
     * @CopyRight 万物皆导
     * @param
     * @return
     */
    public CanalClientException() {
    }
    /**
     *  带错误信息的构造方法
     *
     * @author 阿导
     * @time 2018/5/28 16:39
     * @CopyRight 万物皆导
     * @param message
     * @return
     */
    public CanalClientException(String message) {
        super(message);
    }
    
    /**
     * 带错误信息和其造成原因的构造方法
     *
     * @author 阿导
     * @time 2018/5/28 16:39
     * @CopyRight 万物皆导
     * @param  message
     * @param cause
     * @return
     */
    public CanalClientException(String message, Throwable cause) {
        super(message, cause);
    }
    
    /**
     * 带造成错误信息的构造方法
     *
     * @author 阿导
     * @time 2018/5/28 16:43
     * @CopyRight 万物皆导
     * @param cause
     * @return
     */
    public CanalClientException(Throwable cause) {
        super(cause);
    }
    
    /**
     *
     *
     * @author 阿导
     * @time 2018/5/28 16:43
     * @CopyRight 万物皆导
     * @param message
     * @param cause
     * @param enableSuppression
     * @param writableStackTrace
     * @return
     */
    public CanalClientException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
