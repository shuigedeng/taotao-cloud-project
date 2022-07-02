package com.taotao.cloud.sys.biz.support.docx4j.output.builder.document;


import com.taotao.cloud.sys.biz.support.docx4j.output.OutputException;

/**
 * word导出异常
 */
public class DocumentExportException extends OutputException {
    public DocumentExportException(String message) {
        super(message);
    }

    public DocumentExportException(String message, Throwable t) {
        super(message, t);
    }

    public DocumentExportException(Throwable t) {
        this(t.getMessage(), t);
    }
}
