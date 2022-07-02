package com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.GrayColor;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.util.stream.IntStream;

/**
 * 默认文字水印
 */
public class DefaultTextWatermarkHandler extends PdfPageEventHelper {
    /**
     * 水印文本
     */
    private final String text;
    /**
     * 文本旋转度数
     */
    private final float rotate;
    /**
     * 字体设置
     */
    private final Font font;

    public DefaultTextWatermarkHandler(String text) {
        this(text, 32);
    }

    public DefaultTextWatermarkHandler(String text, float size) {
        // 默认文字旋转45°
        this(text, size, 45);
    }

    public DefaultTextWatermarkHandler(String text, float size, float rotate) {
        this.text = text;
        this.rotate = rotate;
        // 设置水印字体颜色
        this.font = Fonts.font(size, Font.BOLD);
        // 字体颜色透明度
        this.font.setColor(new GrayColor(0.9F));
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        int count = 10;
        IntStream.range(0, count)
            .boxed()
            .forEach(x ->
                IntStream.range(0, count)
                    .boxed()
                    .forEach(y ->
                        ColumnText.showTextAligned(
                            writer.getDirectContentUnder(),
                            Element.ALIGN_CENTER,
                            new Phrase(this.text, this.font),
                            (50.5F + x * 250.0F),
                            (40.0F + y * 150.0F),
                            this.rotate
                        )
                    )
            );
    }
}
