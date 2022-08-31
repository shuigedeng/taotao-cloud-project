package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;

public class ManySheetOneSyler extends ExcelExportStylerDefaultImpl {

    private static CellStyle stringSeptailStyle;

    private static CellStyle stringNoneStyle;

    public ManySheetOneSyler(Workbook workbook) {
        super(workbook);
    }

    @Override
    public CellStyle stringSeptailStyle(Workbook workbook, boolean isWarp) {
        if (stringSeptailStyle == null) {
            stringSeptailStyle = workbook.createCellStyle();
            stringSeptailStyle.setAlignment(HorizontalAlignment.CENTER);
            stringSeptailStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            stringSeptailStyle.setDataFormat(STRING_FORMAT);
            stringSeptailStyle.setWrapText(true);
        }
        return stringSeptailStyle;
    }

    @Override
    public CellStyle stringNoneStyle(Workbook workbook, boolean isWarp) {
        if (stringNoneStyle == null) {
            stringNoneStyle = workbook.createCellStyle();
            stringNoneStyle.setAlignment(HorizontalAlignment.CENTER);
            stringNoneStyle.setVerticalAlignment(VerticalAlignment.CENTER);
            stringNoneStyle.setDataFormat(STRING_FORMAT);
            stringNoneStyle.setWrapText(true);
        }
        return stringNoneStyle;
    }

}
