package com.taotao.cloud.sys.biz.support.docx4j.output.builder.document;

import java.math.BigInteger;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Generated;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.CTTblWidth;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STTblWidth;

/**
 * word可创建段落、表格的类型
 * {@link org.apache.poi.xwpf.usermodel.XWPFFooter}
 * {@link org.apache.poi.xwpf.usermodel.XWPFHeader}
 * {@link org.apache.poi.xwpf.usermodel.XWPFDocument}
 */
@Generated({})
abstract class RichableDocument<T extends RichableDocument> extends ParagraphableDocument<T> {
    /**
     * 添加固定表格
     * @param rows     行数
     * @param columns  列数
     * @param consumer 表格消费
     * @return {@link T}
     */
    @SuppressWarnings("unchecked")
    public T table(int rows, int columns, Consumer<DslTable> consumer) {
        XWPFTable table = this.createTable(rows, columns);
        // 表格宽度自适应
        CTTblWidth tblWidth = table.getCTTbl().addNewTblPr().addNewTblW();
        tblWidth.setType(STTblWidth.DXA);
        tblWidth.setW(BigInteger.valueOf(9072));
        consumer.accept(new DslTable(table));

        return (T) this;
    }

    /**
     * 批量添加表格
     * @param iterable       迭代器
     * @param rowFunction    行生方法
     * @param columnFunction 列生成方法
     * @param consumer       表格消费
     * @param <U>            迭代器内容类型
     * @return {@link T}
     */
    @SuppressWarnings("unchecked")
    public <U> T tables(Iterable<U> iterable, Function<U, Integer> rowFunction, Function<U, Integer> columnFunction,
                        BiConsumer<U, DslTable> consumer) {
        if (Objects.nonNull(iterable)) {
            iterable.forEach(u ->
                this.table(rowFunction.apply(u), columnFunction.apply(u), t -> consumer.accept(u, t))
            );
        }

        return (T) this;
    }

    /**
     * 创建表格
     * @param rows    行数
     * @param columns 列数
     * @return {@link XWPFTable}
     */
    protected abstract XWPFTable createTable(int rows, int columns);
}
