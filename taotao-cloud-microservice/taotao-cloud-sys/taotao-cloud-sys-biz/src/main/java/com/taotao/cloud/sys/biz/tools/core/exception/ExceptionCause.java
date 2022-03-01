package com.taotao.cloud.sys.biz.tools.core.exception;




import com.taotao.cloud.sys.biz.tools.core.dtos.ResponseDto;
import java.text.MessageFormat;

public interface ExceptionCause<T extends Exception> {
    T exception(Object... args);

    ResponseDto result();

    MessageFormat getMessageFormat();
}
