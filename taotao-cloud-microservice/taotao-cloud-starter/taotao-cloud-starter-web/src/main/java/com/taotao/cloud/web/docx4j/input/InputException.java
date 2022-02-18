package com.taotao.cloud.web.docx4j.input;

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
