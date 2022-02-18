package com.taotao.cloud.web.docx4j.output.builder.portable;

import com.itextpdf.text.pdf.PdfPTable;

import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * pdf表格dsl
 */
public class DslTable {
	private final PdfPTable table;

    DslTable(PdfPTable table) {
        this.table = table;
    }

    /**
     * 表格其他设置
     * @param consumer 表格消费
     * @return {@link DslTable}
     */
    public DslTable more(Consumer<PdfPTable> consumer) {
        consumer.accept(this.table);
        return this;
    }

    /**
     * 添加单行数据
     * @param consumer 行消费
     * @return {@link DslTable}
     */
    public DslTable row(Consumer<DslRow> consumer) {
        DslRow row = new DslRow();
        consumer.accept(row);
        // 将行单元格数据依次添加
        row.getCells().forEach(this.table::addCell);

        return this;
    }

    /**
     * 添加多行数据
     * @param iterable 迭代器
     * @param consumer 迭代元素消费
     * @param <U>      迭代元素类型
     * @return {@link DslTable}
     */
    public <U> DslTable rows(Iterable<U> iterable, BiConsumer<U, DslRow> consumer) {
        if (Objects.nonNull(iterable)) {
            iterable.forEach(u -> this.row(t -> consumer.accept(u, t)));
        }

        return this;
    }
}
