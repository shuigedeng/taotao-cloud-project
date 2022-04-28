
package com.taotao.cloud.common.support.deepcopy;

/**
 * 深复制异常
 *
 * @author shuigedeng
 * @version 2022.04
 * @since 2022-04-27 17:08:17
 */
public class DeepCopyException extends RuntimeException {

    public DeepCopyException() {
    }

    public DeepCopyException(String message) {
        super(message);
    }

    public DeepCopyException(String message, Throwable cause) {
        super(message, cause);
    }

    public DeepCopyException(Throwable cause) {
        super(cause);
    }

    public DeepCopyException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
