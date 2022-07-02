package com.taotao.cloud.sys.biz.support.docx4j.output.builder.sheet;

import com.taotao.cloud.sys.biz.support.docx4j.output.builder.OutputFileType;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * 电子表格文件类型
 */
public enum SpreadSheetFileType implements OutputFileType {
    /**
     * 2003 excel
     */
    XLS,
    /**
     * 2007及以上excel
     */
    XLSX;

    /**
     * 通过{@link Workbook}类型设置电子表格后缀名
     * @param workbook {@link Workbook}
     * @return {@link SpreadSheetFileType}
     */
    public static SpreadSheetFileType getTypeByWorkbook(Workbook workbook) {
        if (workbook instanceof XSSFWorkbook || workbook instanceof SXSSFWorkbook) {
            return XLSX;
        }

        return XLS;
    }
}
