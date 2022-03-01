package com.taotao.cloud.sys.biz.tools.core.service.data.regex.exception;

public class RegexpIllegalException extends Exception {

    public RegexpIllegalException() {
        super();
    }

    public RegexpIllegalException(String message) {
        super(message);
    }

    public RegexpIllegalException(String regexp, int index) {
        super(String.format("Invalid regular expression: %s, Index: %d", regexp, index));
    }
}
