package com.taotao.cloud.office.docx4j.output.builder.sheet;

import cn.wisewe.docx4j.output.builder.sheet.CustomStyleType;
import cn.wisewe.docx4j.output.builder.sheet.SpreadSheetExporter;
import com.taotao.cloud.office.docx4j.output.builder.SpecDataFactory;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.junit.Test;

/**
 * {@link SpreadSheetExporter}单元测试
 */
public class SpreadSheetExporterSpec {
    @Test
    public void empty() throws FileNotFoundException {
        SpreadSheetExporter.create()
            .writeTo(new FileOutputStream(
	            FileUtil.brotherPath(SpreadSheetExporterSpec.class, "empty.xlsx")));
    }

    @Test
    public void validation() throws FileNotFoundException {
        SpreadSheetExporter.create()
            .workbook(wb ->
                wb.sheet(s ->
                    s.row(r -> r.headRedAsterCells("姓名", "性别", "职业"))
                        .listValidation(2, Arrays.asList("男", "女"))
                        .listValidation(3, Arrays.asList("学生", "老师", "流浪汉"))
                )
            )
            .writeTo(new FileOutputStream(
	            FileUtil.brotherPath(SpreadSheetExporterSpec.class, "validation.xlsx")));
    }

    @Test
    public void simple() throws FileNotFoundException {
        SpreadSheetExporter.create()
            .workbook(wb ->
                wb.sheet(s ->
                    // 表头行
                    s.row(r -> r.headCells("姓名", "年龄", "性别"))
                        // 数据行
                        .rows(
                            SpecDataFactory.tableData(),
                            (it, row) -> row.dataCells(it::getName, it::getAge, it::getSex)
                        )
                        // 行列冻结
                        .freeze(1, 1)
                )
            )
            .writeTo(new FileOutputStream(FileUtil.brotherPath(SpreadSheetExporterSpec.class, "simple.xlsx")));
    }

    @Test
    public void diagonal() throws FileNotFoundException {
        SpreadSheetExporter.fastCreate(wb ->
            wb.sheet(s ->
                s.row(r -> r.diagonalDownHeadCell("星期", "节次").diagonalUpHeadCell("左上", "右下"))
                    .row(r -> r.diagonalUpDataCell("节次1", "星期一").diagonalDownDataCell("节次2", "星期二"))
            )
        ).writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "diagonal.xlsx")));
    }

    @Test
    public void customStyle() throws FileNotFoundException {
        SpreadSheetExporter.create()
            // 自定义表头样式
            .customStyle(CustomStyleType.HEAD, wb -> {
                // 表头字体加粗
                Font font = wb.createFont();
                font.setColor(IndexedColors.WHITE.getIndex());
                font.setBold(true);

                // 单元格居中 表头背景色
                CellStyle cellStyle = wb.createCellStyle();
                cellStyle.setFont(font);
                // 设置表头背景色
                cellStyle.setFillForegroundColor(IndexedColors.BLACK.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // 设置表头边线及边线颜色
                BorderStyle borderStyle = BorderStyle.THIN;
                cellStyle.setBorderTop(borderStyle);
                cellStyle.setBorderBottom(borderStyle);
                cellStyle.setBorderLeft(borderStyle);
                cellStyle.setBorderRight(borderStyle);
                short borderColor = IndexedColors.RED.getIndex();
                cellStyle.setTopBorderColor(borderColor);
                cellStyle.setBottomBorderColor(borderColor);
                cellStyle.setLeftBorderColor(borderColor);
                cellStyle.setRightBorderColor(borderColor);
                // 内容对齐方式
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                return cellStyle;
            })
            // 自定义数据单元格样式
            .customStyle(CustomStyleType.DATA, wb -> {
                // 单元格居中 表头背景色
                CellStyle cellStyle = wb.createCellStyle();
                // 设置表头背景色
                cellStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
                cellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                // 设置表头边线及边线颜色
                BorderStyle borderStyle = BorderStyle.THIN;
                cellStyle.setBorderTop(borderStyle);
                cellStyle.setBorderBottom(borderStyle);
                cellStyle.setBorderLeft(borderStyle);
                cellStyle.setBorderRight(borderStyle);
                short borderColor = IndexedColors.RED.getIndex();
                cellStyle.setTopBorderColor(borderColor);
                cellStyle.setBottomBorderColor(borderColor);
                cellStyle.setLeftBorderColor(borderColor);
                cellStyle.setRightBorderColor(borderColor);
                // 内容对齐方式
                cellStyle.setAlignment(HorizontalAlignment.CENTER);
                cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);

                return cellStyle;
            })
            .workbook(wb ->
                wb.sheet(s ->
                    // 表头行
                    s.row(r -> r.headCells("姓名", "年龄", "性别"))
                        // 数据行
                        .rows(
                            SpecDataFactory.tableData(),
                            (it, row) -> row.dataCells(it::getName, it::getAge, it::getSex)
                        )
                        // 行列冻结
                        .freeze(1, 1)
                )
            )
            .writeTo(new FileOutputStream(FileUtil.brotherPath(SpreadSheetExporterSpec.class, "custom-style.xlsx")));
    }

    @Test
    public void dynamicSheet() throws FileNotFoundException {
        SpreadSheetExporter.create()
            .workbook(wb ->
                // 动态sheet
                wb.sheets(SpecDataFactory.tableData(), it -> it.getName() + "的Sheet", (it, s) ->
                    // 表头行
                    s.row(r -> r.headCells("姓名", "年龄", "性别"))
                        .row(r -> r.dataCells(it::getName, it::getAge, it::getSex))
                )
            )
            .writeTo(new FileOutputStream(FileUtil.brotherPath(SpreadSheetExporterSpec.class, "dynamic-sheet.xlsx")));
    }

    @Test
    public void mergeHead() throws FileNotFoundException {
        SpreadSheetExporter.create()
            .workbook(wb ->
                wb.sheet(s ->
                    // 表头行
                    s.row(r ->
                        r.headCell(c -> c.rowspan(2).redAster("姓名")).headCell(c -> c.colspan(2).text("其他信息")))
                        .row(r -> r.headCells("姓名", "年龄", "性别"))
                        // 数据行
                        .rows(
                            SpecDataFactory.tableData(),
                            (it, row) -> row.dataCells(it::getName, it::getAge, it::getSex)
                        )
                )
            )
            .writeTo(new FileOutputStream(FileUtil.brotherPath(SpreadSheetExporterSpec.class, "merge-head.xlsx")));
    }

    @Test
    public void mergeData() throws FileNotFoundException {
        // 将数据按照性别分组 合并处理性别列 模拟sql分组 但不保证列表数据顺序
        Map<String, List<Person>> groupBySex =
            SpecDataFactory.tableData().stream().collect(Collectors.groupingBy(Person::getSex));
        SpreadSheetExporter.fastCreate(wb ->
            wb.sheet(s -> {
                // 表头行
                s.row(r -> r.headCells("姓名", "年龄", "性别"));
                // 按照性别渲染表格
                groupBySex.forEach((key, value) -> {
                    AtomicBoolean merged = new AtomicBoolean();
                    int rowspan = value.size();
                    // 数据行
                    s.rows(value, (t, row) ->
                        row.dataCell(t::getName)
                            .dataCell(t::getAge)
                            .dataCell(c -> {
                                c.text(t::getSex);
                                if (!merged.get()) {
                                    // 只合并第一行
                                    merged.set(Boolean.TRUE);
                                    c.rowspan(rowspan);
                                }
                            })
                    );
                });
            })
        ).writeTo(new FileOutputStream(FileUtil.brotherPath(SpreadSheetExporterSpec.class, "merge-data.xlsx")));
    }

    @Test
    public void picture() throws FileNotFoundException {
        SpreadSheetExporter.create()
            .workbook(wb ->
                wb.sheet(s ->
                    // 表头行
                    s.row(r -> r.headCells("姓名", "年龄", "性别", "图片"))
                        // 数据行
                        .rows(
                            SpecDataFactory.tableData(),
                            (it, row) ->
                                row.dataCells(it::getName, it::getAge, it::getSex)
                                    // 这里为了测试 设置为固定宽度和高度
                                    .pictureCell(it.picture(), 20, 20)
                        )
                        // 行列冻结
                        .freeze(1, 1)
                )
            )
            .writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "picture.xlsx")));
    }
}
