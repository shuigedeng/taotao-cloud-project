package com.taotao.cloud.sys.biz.support.docx4j.output.builder.sheet;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.model.StylesTable;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorder;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTBorderPr;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTColor;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.CTXf;
import org.openxmlformats.schemas.spreadsheetml.x2006.main.STBorderStyle;

/**
 * 单元格样式工具
 */
interface CellStyleUtil {

    /**
     * 设置单元格边框样式
     * @param cellStyle 单元格样式
     * @param color     边框颜色
     */
    static void setCellBorderStyle(CellStyle cellStyle, short color) {
        cellStyle.setBorderTop(BorderStyle.THIN);
        cellStyle.setBorderBottom(BorderStyle.THIN);
        cellStyle.setBorderLeft(BorderStyle.THIN);
        cellStyle.setBorderRight(BorderStyle.THIN);
        cellStyle.setTopBorderColor(color);
        cellStyle.setBottomBorderColor(color);
        cellStyle.setLeftBorderColor(color);
        cellStyle.setRightBorderColor(color);
    }

    /**
     * 默认表头字体
     * @param workbook {@link Workbook}
     * @return {@link Font}
     */
    static Font defaultHeadFont(Workbook workbook) {
        // 表头字体加粗
        Font font = workbook.createFont();
        font.setBold(true);

        return font;
    }

    /**
     * 创建默认表头单元格样式
     * @param workbook {@link Workbook}
     * @return {@link CellStyle}
     */
    static CellStyle defaultHeadStyle(Workbook workbook) {
        // 表头字体加粗
        Font font = defaultHeadFont(workbook);

        // 单元格居中 表头背景色
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setFont(font);
        // 设置表头背景色
        cellStyle.setFillForegroundColor(IndexedColors.PALE_BLUE.getIndex());
        cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        // 设置表头边线及边线颜色
        // 设置边框样式
        CellStyleUtil.setCellBorderStyle(cellStyle, IndexedColors.BLACK.index);
        // 内容对齐方式
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        return cellStyle;
    }

    /**
     * 创建默认数据单元格样式
     * @param workbook {@link Workbook}
     * @return {@link CellStyle}
     */
    static CellStyle defaultDataStyle(Workbook workbook) {
        // 单元格居中
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setAlignment(HorizontalAlignment.CENTER);
        cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

        // 设置边框样式
        CellStyleUtil.setCellBorderStyle(cellStyle, IndexedColors.BLACK.index);

        // 自动换行仅对数据支持
        cellStyle.setWrapText(true);

        return cellStyle;
    }

    /**
     * 斜线样式
     * @param workbook     {@link Workbook}
     * @param baseStyle    基础样式
     * @param isDiagonalUp 斜线方向
     * @return {@link CellStyle}
     */
    static CellStyle diagonalStyle(Workbook workbook, CellStyle baseStyle, boolean isDiagonalUp) {
        // 单元格居中 表头背景色
        CellStyle cellStyle = workbook.createCellStyle();
        cellStyle.cloneStyleFrom(baseStyle);
        // 拆分单元格左对齐
        cellStyle.setAlignment(HorizontalAlignment.LEFT);

        StylesTable stylesTable;
        if (workbook instanceof SXSSFWorkbook) {
            stylesTable = ((SXSSFWorkbook) workbook).getXSSFWorkbook().getStylesSource();
        } else if (workbook instanceof XSSFWorkbook) {
            stylesTable = ((XSSFWorkbook) workbook).getStylesSource();
        } else {
            throw new SpreadSheetExportException("excel version not support diagonal cell");
        }

        CTXf coreXf = ((XSSFCellStyle) cellStyle).getCoreXf();

        CTBorder ctBorder;
        if (coreXf.getApplyBorder()) {
            long borderId = coreXf.getBorderId();
            XSSFCellBorder cellBorder = stylesTable.getBorderAt((int) borderId);
            ctBorder = (CTBorder) cellBorder.getCTBorder().copy();
        } else {
            ctBorder = (CTBorder) CTBorder.Factory.newInstance();
        }
        CTBorderPr ctBorderPr = ctBorder.isSetDiagonal() ? ctBorder.getDiagonal() : ctBorder.addNewDiagonal();
        CTColor ctColor = (CTColor) CTColor.Factory.newInstance();
        ctColor.setIndexed(IndexedColors.BLACK.index);
        ctBorderPr.setColor(ctColor);
        ctBorderPr.setStyle(STBorderStyle.THIN);

        if (isDiagonalUp) {
            ctBorder.setDiagonalUp(true);
        } else {
            ctBorder.setDiagonalDown(true);
        }

        ctBorder.setDiagonal(ctBorderPr);
        int borderId = stylesTable.putBorder(new XSSFCellBorder(ctBorder));
        coreXf.setBorderId(borderId);
        coreXf.setApplyBorder(true);

        // 自动换行仅对数据支持
        cellStyle.setWrapText(true);

        return cellStyle;
    }
}
