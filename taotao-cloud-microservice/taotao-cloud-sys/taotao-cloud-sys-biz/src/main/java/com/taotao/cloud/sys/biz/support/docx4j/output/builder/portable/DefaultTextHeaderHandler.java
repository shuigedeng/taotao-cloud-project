package com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 * 默认文本页眉
 */
public class DefaultTextHeaderHandler extends PdfPageEventHelper {
	private final String text;

    public DefaultTextHeaderHandler(String text) {
        this.text = text;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        ColumnText.showTextAligned(
            writer.getDirectContent(),
            Element.ALIGN_CENTER,
            new Phrase(this.text, Fonts.HEADER_FOOTER.font()),
            document.getPageSize().getWidth() / 2.0F,
            document.getPageSize().getHeight() - 10.0F,
            0.0F
        );
    }
}
