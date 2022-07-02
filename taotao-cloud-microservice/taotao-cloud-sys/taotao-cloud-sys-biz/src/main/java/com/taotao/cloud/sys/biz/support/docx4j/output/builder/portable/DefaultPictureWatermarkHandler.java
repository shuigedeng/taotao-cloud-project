package com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGState;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;
import java.io.File;
import java.io.IOException;
import java.util.stream.IntStream;

/**
 * 默认图片水印
 */
public class DefaultPictureWatermarkHandler extends PdfPageEventHelper {
    /**
     * 水印图片
     */
    private final Image image;
    /**
     * 图片宽度
     */
    private final float width;
    /**
     * 图片高度
     */
    private final float height;
    /**
     * 图片旋转度数
     */
    private final float rotate;

    public DefaultPictureWatermarkHandler(File file) {
        this(file, 0);
    }

    public DefaultPictureWatermarkHandler(File file, float width) {
        this(file, width, 45F);
    }

    public DefaultPictureWatermarkHandler(File file, float width, float rotate) {
        try {
            this.image = Image.getInstance(file.toURI().toURL());
        } catch (IOException | BadElementException e) {
            throw new PortableExportException(e);
        }

        float w = this.image.getWidth(), h = this.image.getHeight();
        // 等宽比调整
        if (width > 0) {
            float scale = width / w;
            w *= scale;
            h *= scale;
        }

        this.width = w;
        this.height = h;

        this.rotate = rotate;
    }

    @Override
    public void onEndPage(PdfWriter writer, Document document) {
        PdfContentByte contentUnder = writer.getDirectContentUnder();
        int count = 10;
        IntStream.range(0, count)
            .boxed()
            .forEach(x ->
                IntStream.range(0, count)
                    .boxed()
                    .forEach(y -> {
                        Image image = Image.getInstance(this.image);
                        image.scaleAbsoluteWidth(this.width);
                        image.scaleAbsoluteHeight(this.height);
                        image.setRotation(this.rotate);
                        image.setAbsolutePosition(x * this.width * 1.5F + 50.5F, y * this.height * 2.5F + 40.0F);

                        PdfGState state = new PdfGState();
                        // 水印图片透明度
                        state.setFillOpacity(0.3F);

                        contentUnder.saveState();
                        contentUnder.setGState(state);
                        try {
                            contentUnder.addImage(image);
                        } catch (DocumentException e) {
                            throw new PortableExportException(e);
                        }
                        contentUnder.restoreState();
                    })
            );
    }
}
