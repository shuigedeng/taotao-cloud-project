package com.taotao.cloud.web.docx4j.output.builder.document;

import com.taotao.cloud.web.docx4j.output.utils.StringConverterUtil;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;

import java.util.Optional;
import java.util.function.Supplier;

/**
 * 表格单元格{@link XWPFTableCell}dsl
 */
public class DslTableCell extends ParagraphableDocument<DslTableCell> {
	private final XWPFTableCell cell;

    public int rowspan = 1;

	public int colspan = 1;

    DslTableCell(XWPFTableCell cell) {
        this.cell = cell;
    }

    /**
     * 设置单元格内容
     * @param o 单元格内容对象
     * @return {@link DslTableCell}
     */
    public DslTableCell text(Object o) {
        return super.textParagraph(StringConverterUtil.convert(o));
    }

    /**
     * 设置单元格内容
     * @param supplier 单元格内容提供器
     * @return {@link DslTableCell}
     */
    public DslTableCell text(Supplier<Object> supplier) {
        return this.text(supplier.get());
    }

    /**
     * 单元格内容加粗
     * @param o 单元格内容对象
     * @return {@link DslTableCell}
     */
    public DslTableCell boldText(Object o) {
        return super.paragraph(p -> p.run(t -> t.text(StringConverterUtil.convert(o)).more(r -> r.setBold(true))));
    }

    /**
     * 单元格内容加粗
     * @param supplier 单元格内容提供器
     * @return {@link DslTableCell}
     */
    public DslTableCell boldText(Supplier<Object> supplier) {
        return this.boldText(supplier.get());
    }

    /**
     * 合并列
     * @param colSpan 合并列数
     * @return {@link DslTableCell}
     */
    public DslTableCell colspan(int colSpan) {
        if (colSpan > 1) {
            this.colspan = colSpan;
        }
        return this;
    }

    /**
     * 合并行
     * @param rowspan 合并行数
     * @return {@link DslTableCell}
     */
    public DslTableCell rowspan(int rowspan) {
        if (rowspan > 1) {
            this.rowspan = rowspan;
        }

        return this;
    }


    @Override
    public DslTableCell headingParagraph(String text, ParagraphStyle style) {
        // 不支持标题风格的段落 若使用该方法默认使用文本
        return super.textParagraph(text);
    }

    @Override
    protected XWPFParagraph createParagraph() {
        // 修复第一个单元格自动添加段落问题
        return
            Optional.ofNullable(this.cell.getParagraphs())
                .filter(CollectionUtils::isNotEmpty)
                .map(it -> it.get(0))
                .orElseGet(this.cell::addParagraph);
    }
}
