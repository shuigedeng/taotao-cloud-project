package com.taotao.cloud.sys.biz.support.docx4j.input;

/**
 * 导入异常
 */
public class InputException extends RuntimeException {
    public InputException() {
        super();
    }

    public InputException(String message) {
        super(message);
    }

    public InputException(String message, Throwable t) {
        super(message, t);
    }
}
