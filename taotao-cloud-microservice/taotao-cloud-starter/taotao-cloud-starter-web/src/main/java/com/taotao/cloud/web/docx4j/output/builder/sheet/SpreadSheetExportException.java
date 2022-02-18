package com.taotao.cloud.web.docx4j.output.builder.sheet;


import com.taotao.cloud.web.docx4j.output.OutputException;

/**
 * 电子表格导出异常
 */
public class SpreadSheetExportException extends OutputException {
    public SpreadSheetExportException(String message) {
        super(message);
    }

    public SpreadSheetExportException(String message, Throwable t) {
        super(message, t);
    }

    public SpreadSheetExportException(Throwable t) {
        this(t.getMessage(), t);
    }
}
