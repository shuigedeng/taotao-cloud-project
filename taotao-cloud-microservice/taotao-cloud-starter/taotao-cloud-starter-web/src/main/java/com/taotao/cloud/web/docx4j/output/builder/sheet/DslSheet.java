package com.taotao.cloud.web.docx4j.output.builder.sheet;

import com.taotao.cloud.web.docx4j.output.OutputConstants;
import org.apache.commons.collections4.map.HashedMap;
import org.apache.poi.ss.SpreadsheetVersion;
import org.apache.poi.ss.usermodel.DataValidation;
import org.apache.poi.ss.usermodel.DataValidationConstraint;
import org.apache.poi.ss.usermodel.DataValidationHelper;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddressList;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.stream.IntStream;

/**
 * {@link Sheet} dsl
 */
public class DslSheet {
	private  final Sheet sheet;
    /**
     * 行号
     */
    private final AtomicInteger rowNumber;
    /**
     * 行数据缓存
     */
    private final Map<Integer, DslRow> rows;
    /**
     * 缓存最大列数
     */
    private  int maxCells = 0;
    /**
     * 保持所有列宽度保持最大列宽度一致
     */
    private  boolean keepMaxColumnWidth = false;

    DslSheet(Sheet sheet) {
        this.sheet = sheet;
        // 若是空sheet 则从0开始 否则从已有sheet的下一行开始
        this.rowNumber = new AtomicInteger(this.sheet.getLastRowNum() + 1);
        this.rows = new HashedMap<>(16);
    }

    /**
     * 表格冻结行列
     * @param column 冻结列数
     * @param row    冻结行数
     * @return {@link DslSheet}
     */
    public DslSheet freeze(int column, int row) {
        this.sheet.createFreezePane(column, row);
        return this;
    }

    /**
     * 默认冻结第一行及第一列
     * @return {@link DslSheet}
     */
    public DslSheet freeze() {
        return this.freeze(1, 1);
    }

    /**
     * 单行添加
     * @param consumer 行消费
     * @return {@link DslSheet}
     */
    public DslSheet row(Consumer<DslRow> consumer) {
        DslRow row = this.getOrCreateRow(this.rowNumber.getAndIncrement());
        consumer.accept(row);
        // 更新最大列数
        this.maxCells = Integer.max(this.maxCells, row.getRow().getLastCellNum());

        return this;
    }

    /**
     * 多行数据添加
     * @param iterable 行迭代内容
     * @param consumer 迭代消费
     * @param <T>      迭代内容类型
     * @return {@link DslSheet}
     */
    public <T> DslSheet rows(Iterable<T> iterable, BiConsumer<T, DslRow> consumer) {
        if (Objects.nonNull(iterable)) {
            iterable.forEach(it -> this.row(row -> consumer.accept(it, row)));
        }

        return this;
    }

    /**
     * 自定义校验器
     * @param function 校验器生成方法
     * @return {@link DslSheet}
     */
    public DslSheet validation(BiFunction<DataValidationHelper, SpreadsheetVersion, DataValidation> function) {
        this.sheet.addValidationData(
            function.apply(
                this.sheet.getDataValidationHelper(),
                this.sheet.getWorkbook().getSpreadsheetVersion()
            )
        );
        return this;
    }

    /**
     * 列表校验
     * @param startRow 开始行
     * @param column   校验列
     * @param data     列表数据
     * @return {@link DslSheet}
     */
    public DslSheet listValidation(int startRow, int column, List<String> data) {
        // 列表为空不添加校验
        if (Objects.isNull(data) || data.isEmpty()) {
            return this;
        }

        return
            this.validation((helper, version) -> {
                // list校验
                DataValidationConstraint constraint = helper.createExplicitListConstraint(data.toArray(new String[0]));
                // excel版本支持最大行数
                int maxRows = version.getMaxRows();
                // 校验规则
                DataValidation validation = helper.createValidation(
                    constraint,
                    new CellRangeAddressList(startRow - 1, maxRows - 1, column - 1, column - 1)
                );
                validation.setEmptyCellAllowed(true);
                validation.setSuppressDropDownArrow(true);
                validation.setShowErrorBox(true);
                validation.setErrorStyle(DataValidation.ErrorStyle.STOP);

                return validation;
            });
    }

    /**
     * 列表校验 默认从第二行开始
     * @param column 校验列
     * @param data   列表数据
     * @return {@link DslSheet}
     */
    public DslSheet listValidation(int column, List<String> data) {
        // 默认从第2行开始
        return this.listValidation(2, column, data);
    }

    /**
     * 设置sheet列与最大列宽度一致
     * @return {@link DslSheet}
     */
    public DslSheet keepMaxColumnWidth() {
        this.keepMaxColumnWidth = true;

        return this;
    }

    /**
     * 单元格自适应
     */
    protected void doAutoColumnFit() {
        try {
            // 没有数据行或列
            if (this.rows.isEmpty() || this.maxCells <= 0) {
                return;
            }

            // 计算列最大宽度
            int maxWidth = DslCell.COLUMN_WIDTH.get().values().stream().mapToInt(Integer::intValue).max().orElse(5);

            IntStream.range(0, this.maxCells)
                .forEach(it -> {
                    // 列宽至少宽5个字符
                    int width = this.keepMaxColumnWidth
                        ? maxWidth :
                        Optional.ofNullable(DslCell.COLUMN_WIDTH.get())
                            // 若无数据设置 则赋值为默认宽度
                            .map(m -> m.get(it))
                            .filter(w -> w >= 5)
                            .orElse(5);
                    // 使一列最大宽度为100个字符 并多出2个字符保持美观
                    int fitWidth = Integer.min(width, 100) + 2;
                    this.sheet.setColumnWidth(it, fitWidth * 256);

                    // 设置斜线单元格 自动补全空格
                    Optional.ofNullable(DslCell.DIAGONAL_COLUMNS.get())
                        .map(m -> m.get(it))
                        .ifPresent(s ->
                            s.forEach(c -> {
                                String text = c.cell.getStringCellValue();
                                int index = text.indexOf(OutputConstants.BREAK_LINE);
                                if (index < 0) {
                                    return;
                                }

                                String left = text.substring(0, index), right = text.substring(index + 1);
                                // 自动补齐空格
                                if (c.diagonalUp) {
                                    int len = fitWidth - DslCell.width(right);
                                    // 右上至左下斜线
                                    c.cell.setCellValue(left + OutputConstants.BREAK_LINE + padding(len) + right);
                                } else {
                                    int len = fitWidth - DslCell.width(left);
                                    // 左上至右下斜线
                                    c.cell.setCellValue(padding(len) + left + OutputConstants.BREAK_LINE + right);
                                }
                            })
                        );
                });
        } finally {
            DslCell.removeThreadLocal();
        }
    }

    /**
     * 通过行号获取或者创建行数据
     * @param rowNumber 行号
     * @return {@link DslRow}
     */
    protected DslRow getOrCreateRow(int rowNumber) {
        return
            Optional.ofNullable(this.rows.get(rowNumber))
                .orElseGet(() -> {
                    DslRow row = new DslRow(this.sheet.createRow(rowNumber));
                    this.rows.put(rowNumber, row);

                    return row;
                });
    }

    /**
     * 空格填充
     * @param len 填充字节数
     * @return 带斜线的单元格自动填充空格
     */
    protected static String padding(int len) {
        // 预留一个字节长度
        len -= 1;
        // 多余2个字节使用双字节空格填充
        String padding = String.join(
            OutputConstants.EMPTY,
            Collections.nCopies(len / 2, OutputConstants.DOUBLE_BYTE_SPACE)
        );
        // 剩余字节使用单字节空格填充
        if ((len & 1) > 0) {
            padding += OutputConstants.SPACE;
        }

        return padding;
    }
}
