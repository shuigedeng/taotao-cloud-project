package com.taotao.cloud.web.docx4j.output.builder.compression;


import com.taotao.cloud.web.docx4j.output.OutputException;

/**
 * 压缩文件异常
 */
public class CompressionExportException extends OutputException {
    public CompressionExportException(String message) {
        super(message);
    }

    public CompressionExportException(String message, Throwable t) {
        super(message, t);
    }

    public CompressionExportException(Throwable t) {
        this(t.getMessage(), t);
    }
}
