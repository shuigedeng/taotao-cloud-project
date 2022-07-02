package com.taotao.cloud.sys.biz.support.docx4j.output.builder.document;

import java.io.File;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.IntStream;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

/**
 * 表格行{@link XWPFTableRow} dsl
 */
public class DslTableRow {
	private final XWPFTableRow row;
	private  int rowIndex;
	private final AtomicInteger cellIndex;

    DslTableRow(XWPFTableRow row, int rowIndex) {
        this.row = row;
        this.rowIndex = rowIndex;
        this.cellIndex = new AtomicInteger();
    }

    /**
     * 添加一个单元格主要方法
     * @param consumer 单元格消费
     * @return {@link DslTableRow}
     */
    public DslTableRow cell(Consumer<DslTableCell> consumer) {
        // 单元格索引
        int index = this.cellIndex.getAndIncrement();
        XWPFTableCell tableCell = this.row.getCell(index);
        DslTableCell dslCell = new DslTableCell(tableCell);
        consumer.accept(dslCell);
        // 列合并
        if (dslCell.colspan > 1) {
            tableCell.getCTTc().addNewTcPr().addNewHMerge().setVal(STMerge.RESTART);
            IntStream.range(index + 1, index + dslCell.colspan)
                .forEach(it ->
                    this.row.getCell(it)
                        .getCTTc()
                        .addNewTcPr()
                        .addNewHMerge()
                        .setVal(STMerge.CONTINUE)
                );
        }

        // 行合并
        if (dslCell.rowspan > 1) {
            tableCell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            IntStream.range(this.rowIndex + 1, this.rowIndex + dslCell.rowspan)
                .forEach(it ->
                    this.row.getTable()
                        .getRow(it)
                        .getCell(index)
                        .getCTTc()
                        .addNewTcPr()
                        .addNewVMerge()
                        .setVal(STMerge.CONTINUE)
                );
        }

        return this;
    }

    /**
     * 单元格添加图片
     * @param file   图片文件
     * @param width  宽度
     * @param height 高度
     * @return {@link DslTableRow}
     */
    public DslTableRow pictureCell(File file, int width, int height) {
        return this.cell(c -> c.paragraph(p -> p.run(r -> r.picture(file, width, height))));
    }

    /**
     * 添加一个表头单元格
     * @param o 单元格对象
     * @return {@link DslRow}
     */
    public DslTableRow headCell(Object o) {
        return this.cell(t -> t.boldText(o));
    }

    /**
     * 添加一个表头单元格
     * @param supplier 表格单元格提供
     * @return {@link DslTableRow}
     */
    public DslTableRow headCell(Supplier<Object> supplier) {
        return this.headCell(supplier.get());
    }

    /**
     * 批量添加表头单元格
     * @param objects 单元格数组
     * @return {@link DslTableRow}
     */
    public DslTableRow headCells(Object... objects) {
        if (Objects.nonNull(objects) && objects.length > 0) {
            for (Object object : objects) {
                this.headCell(object);
            }
        }

        return this;
    }

    /**
     * 批量添加表头单元格
     * @param iterable 迭代器
     * @param function 表头内容生成方法
     * @param <U>      迭代元素类型
     * @return {@link DslTableRow}
     */
    public <U> DslTableRow headCells(Iterable<U> iterable, Function<U, Object> function) {
        if (Objects.nonNull(iterable)) {
            iterable.forEach(u -> this.headCell(function.apply(u)));
        }

        return this;
    }

    /**
     * 批量添加表头单元格
     * @param iterable 表头迭代器
     * @return {@link DslRow}
     */
    public DslTableRow headCells(Iterable<Object> iterable) {
        if (Objects.nonNull(iterable)) {
            iterable.forEach(this::headCell);
        }

        return this;
    }

    /**
     * 添加一个数据单元格
     * @param o 单元格内容对象
     * @return {@link DslCell}
     */
    public DslTableRow dataCell(Object o) {
        return this.cell(c -> c.text(o));
    }

    /**
     * 添加一个数据单元格
     * @param supplier 单元格内容提供
     * @return {@link DslRow}
     */
    public DslTableRow dataCell(Supplier<Object> supplier) {
        return this.dataCell(supplier.get());
    }

    /**
     * 批量添加数据单元格
     * @param suppliers 数据单元格提供数组
     * @return {@link DslTableRow}
     */
    public DslTableRow dataCells(Supplier<?>... suppliers) {
        if (Objects.nonNull(suppliers) && suppliers.length > 0) {
            for (Supplier<?> supplier : suppliers) {
                this.dataCell(supplier.get());
            }
        }

        return this;
    }
}
