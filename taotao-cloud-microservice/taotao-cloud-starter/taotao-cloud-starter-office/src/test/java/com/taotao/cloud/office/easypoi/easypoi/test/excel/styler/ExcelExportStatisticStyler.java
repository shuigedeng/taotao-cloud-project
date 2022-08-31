package com.taotao.cloud.office.easypoi.easypoi.test.excel.styler;

import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.ss.usermodel.*;

import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerDefaultImpl;

/**
 * Excel 自定义styler 的例子
 * @author JueYue
 *   2015年3月29日 下午9:04:41
 */
public class ExcelExportStatisticStyler extends ExcelExportStylerDefaultImpl {

    private CellStyle numberCellStyle;

    public ExcelExportStatisticStyler(Workbook workbook) {
        super(workbook);
        createNumberCellStyler();
    }

    private void createNumberCellStyler() {
        numberCellStyle = workbook.createCellStyle();
        numberCellStyle.setAlignment(HorizontalAlignment.CENTER);
        numberCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
        numberCellStyle.setDataFormat((short) BuiltinFormats.getBuiltinFormat("0.00"));
        numberCellStyle.setWrapText(true);
    }

    @Override
    public CellStyle getStyles(boolean noneStyler, ExcelExportEntity entity) {
        if (entity != null
            && (entity.getName().contains("int") || entity.getName().contains("double"))) {
            return numberCellStyle;
        }
        return super.getStyles(noneStyler, entity);
    }

}
