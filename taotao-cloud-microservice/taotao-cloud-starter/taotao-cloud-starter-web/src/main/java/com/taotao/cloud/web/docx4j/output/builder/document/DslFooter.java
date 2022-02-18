package com.taotao.cloud.web.docx4j.output.builder.document;

import com.taotao.cloud.web.docx4j.output.OutputConstants;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFFooter;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFTable;

/**
 * 页脚
 */
public class DslFooter extends RichableDocument<DslFooter> {
	private final XWPFFooter footer;

    DslFooter(XWPFFooter footer) {
        this.footer = footer;
    }

    /**
     * 默认居中分页页脚
     * @param left   页码左侧字符串
     * @param center 页码右侧字符串
     * @param right  总页数右侧字符串
     * @return {@link DslFooter}
     */
    public DslFooter page(String left, String center, String right) {
        return
            this.paragraph(p ->
                p.run(left)
                    // 当前页码
                    .more(xp -> xp.getCTP().addNewFldSimple().setInstr(OutputConstants.DOCX_PAGES))
                    .run(center)
                    // 总页码
                    .more(xp -> xp.getCTP().addNewFldSimple().setInstr(OutputConstants.DOCX_NUM_PAGES))
                    .run(right)
                    .more(xp -> xp.setAlignment(ParagraphAlignment.CENTER))
            );
    }

    @Override
    protected XWPFParagraph createParagraph() {
        return this.footer.createParagraph();
    }

    @Override
    protected XWPFTable createTable(int rows, int columns) {
        return this.footer.createTable(rows, columns);
    }
}
