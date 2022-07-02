package com.taotao.cloud.sys.biz.support.docx4j.input.builder.compression;


import com.taotao.cloud.sys.biz.support.docx4j.input.InputException;

/**
 * 压缩包导入异常
 */
public class CompressionImportException extends InputException {
    public CompressionImportException(String message) {
        super(message);
    }

    public CompressionImportException(String message, Throwable t) {
        super(message, t);
    }

    public CompressionImportException(Throwable t) {
        this(t.getMessage(), t);
    }
}
