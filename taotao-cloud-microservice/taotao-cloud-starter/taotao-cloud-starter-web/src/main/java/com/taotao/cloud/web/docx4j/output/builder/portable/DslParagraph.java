package com.taotao.cloud.web.docx4j.output.builder.portable;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;

import java.io.File;
import java.io.IOException;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * pdf段落dsl
 */
public class DslParagraph {
	private final Paragraph paragraph;

    DslParagraph(Paragraph paragraph) {
        this.paragraph = paragraph;
    }

    /**
     * 段落更多设置
     * @param consumer 段落设置消费
     * @return {@link DslParagraph}
     */
    public DslParagraph more(Consumer<Paragraph> consumer) {
        consumer.accept(this.paragraph);

        return this;
    }

    /**
     * 段落文本添加
     * @param supplier 段落文本提供
     * @return {@link DslParagraph}
     */
    public DslParagraph chunk(Supplier<Chunk> supplier) {
        this.paragraph.add(supplier.get());
        return this;
    }

    /**
     * 添加自定义字体chunk
     * @param text 文本内容
     * @param font 字体定义
     * @return {@link DslParagraph}
     */
    public DslParagraph chunk(String text, Font font) {
        this.chunk(() -> new Chunk(text, font));

        return this;
    }

    /**
     * 正文chunk添加
     * @param text 正文内容
     * @return {@link DslParagraph}
     */
    public DslParagraph chunk(String text) {
        return this.chunk(text, Fonts.NORMAL.font());
    }

    /**
     * 段落多文本添加
     * @param supplier 多文本提供
     * @return {@link DslParagraph}
     */
    public DslParagraph phrase(Supplier<Phrase> supplier) {
        this.paragraph.add(supplier.get());
        return this;
    }

    /**
     * 添加自定义字体phrase
     * @param text 文本内容
     * @param font 字体定义
     * @return {@link DslParagraph}
     */
    public DslParagraph phrase(String text, Font font) {
        this.phrase(() -> new Phrase(text, font));

        return this;
    }

    /**
     * 正文phrase添加
     * @param text 正文内容
     * @return {@link DslParagraph}
     */
    public DslParagraph phrase(String text) {
        return this.phrase(text, Fonts.NORMAL.font());
    }

    /**
     * 段落添加图片
     * @param supplier 图片提供
     * @return {@link DslParagraph}
     */
    public DslParagraph picture(Supplier<Image> supplier) {
        this.paragraph.add(supplier.get());
        return this;
    }

    /**
     * 段落多天图片
     * @param file     图片文件
     * @param consumer 图片处理
     * @return {@link DslParagraph}
     */
    public DslParagraph picture(File file, Consumer<Image> consumer) {
        return this.picture(() -> {
            try {
                Image image = Image.getInstance(file.toURI().toURL());
                consumer.accept(image);
                return image;
            } catch (BadElementException | IOException e) {
                throw new PortableExportException(e);
            }
        });
    }
}
