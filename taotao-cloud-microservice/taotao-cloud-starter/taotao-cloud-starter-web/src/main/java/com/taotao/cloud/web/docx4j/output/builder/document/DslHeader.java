package com.taotao.cloud.web.docx4j.output.builder.document;

import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFHeader;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

/**
 * 页眉
 */
public class DslHeader extends RichableDocument<DslHeader> {
	private final XWPFHeader header;

    DslHeader(XWPFHeader header) {
        this.header = header;
    }

    /**
     * 默认居中页眉文本
     * @param text 文本内容
     * @return {@link DslHeader}
     */
    public DslHeader text(String text) {
        return this.paragraph(p -> p.run(text).more(xp -> xp.setAlignment(ParagraphAlignment.CENTER)));
    }

    @Override
    protected XWPFParagraph createParagraph() {
        return this.header.createParagraph();
    }

    @Override
    protected XWPFTable createTable(int rows, int columns) {
        return this.header.createTable(rows, columns);
    }
}
