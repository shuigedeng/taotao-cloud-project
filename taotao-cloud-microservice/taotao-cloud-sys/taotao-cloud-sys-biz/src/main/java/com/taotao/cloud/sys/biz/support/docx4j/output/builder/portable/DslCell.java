package com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable;

import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPCell;
import java.util.function.Consumer;

/**
 * pdf表格单元格dsl
 */
public class DslCell extends
		PortableDocument<DslCell> {
	private final PdfPCell cell;

    DslCell(PdfPCell cell) {
        this.cell = cell;
    }

    /**
     * 单元格设置
     * @param consumer
     * @return
     */
    public DslCell more(Consumer<PdfPCell> consumer) {
        consumer.accept(this.cell);
        return this;
    }

    /**
     * 合并列
     * @param colspan 合并列数
     * @return {@link DslCell}
     */
    public DslCell colspan(int colspan) {
        this.cell.setColspan(colspan);
        return this;
    }

    /**
     * 合并行
     * @param rowspan 合并行数
     * @return {@link DslCell}
     */
    public DslCell rowspan(int rowspan) {
        this.cell.setRowspan(rowspan);
        return this;
    }

    @Override
    void addElement(Element element) {
        this.cell.addElement(element);
    }
}
