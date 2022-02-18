package com.taotao.cloud.web.docx4j.output.builder.portable;


import com.taotao.cloud.web.docx4j.output.OutputException;

/**
 * pdf导出异常
 */
public class PortableExportException extends OutputException {
    public PortableExportException(String message, Throwable t) {
        super(message, t);
    }

    public PortableExportException(Throwable t) {
        this(t.getMessage(), t);
    }
}
