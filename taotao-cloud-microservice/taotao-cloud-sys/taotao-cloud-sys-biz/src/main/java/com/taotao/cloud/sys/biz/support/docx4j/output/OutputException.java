package com.taotao.cloud.sys.biz.support.docx4j.output;

/**
 * 导出异常
 */
public class OutputException extends RuntimeException {
    public OutputException() {
        super();
    }

    public OutputException(String message) {
        super(message);
    }

    public OutputException(String message, Throwable t) {
        super(message, t);
    }
}
