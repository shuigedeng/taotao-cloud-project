package com.taotao.cloud.sys.biz.support.docx4j.input.builder.sheet;


import com.taotao.cloud.sys.biz.support.docx4j.input.InputException;

/**
 * 电子表格导入异常
 */
public class SpreadSheetImportException extends InputException {
    public SpreadSheetImportException(String message) {
        super(message);
    }

    public SpreadSheetImportException(String message, Throwable t) {
        super(message, t);
    }

    public SpreadSheetImportException(Throwable t) {
        this(t.getMessage(), t);
    }
}
