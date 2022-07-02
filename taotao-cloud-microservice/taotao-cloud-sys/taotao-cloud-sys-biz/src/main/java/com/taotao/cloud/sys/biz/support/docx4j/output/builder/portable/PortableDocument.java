package com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable;

import com.itextpdf.text.BadElementException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import javax.annotation.Generated;

/**
 * 用于段落、表格构建
 */
@Generated({})
abstract class PortableDocument<T extends PortableDocument> {
    /**
     * 添加单个段落
     * @param consumer 段落消费
     * @return {@link T}
     */
    public T paragraph(Consumer<DslParagraph> consumer) {
        Paragraph paragraph = new Paragraph();
        consumer.accept(new DslParagraph(paragraph));
        // 容器添加段落
        this.addParagraph(paragraph);

        return (T) this;
    }

    /**
     * 动态添加多个段落
     * @param iterable 段落迭代
     * @param consumer 段落迭代消费
     * @param <U>      段落迭代内容类型
     * @return {@link T}
     */
    public <U> T paragraphs(Iterable<U> iterable, BiConsumer<U, DslParagraph> consumer) {
        if (Objects.nonNull(iterable)) {
            iterable.forEach(u -> this.paragraph(p -> consumer.accept(u, p)));
        }

        return (T) this;
    }

    /**
     * 正文段落
     * @param text  正文内容
     * @param fonts 字体
     * @return {@link T}
     */
    public T textParagraph(String text, Fonts fonts) {
        return this.paragraph(p -> p.chunk(text, fonts.font()));
    }

    /**
     * 正文段落
     * @param text 正文段落内容
     * @return {@link T}
     */
    public T textParagraph(String text) {
        return this.paragraph(p -> p.chunk(text));
    }

    /**
     * 添加标题段落
     * @param text      标题内容
     * @param fonts     字体
     * @param alignment 对齐方式
     * @return {@link T}
     */
    public T headingParagraph(String text, Fonts fonts, int alignment) {
        return
            this.paragraph(p ->
                p.chunk(text, fonts.font())
                    .more(op -> {
                        op.setAlignment(alignment);
                        op.setSpacingBefore(fonts.size / 4.0f);
                        op.setSpacingAfter(fonts.size / 3.0f);
                    })
            );
    }

    /**
     * 标题段落 默认左对齐
     * @param text  标题段落内容
     * @param fonts 预定义字体
     * @return {@link T}
     */
    public T headingParagraph(String text, Fonts fonts) {
        return this.headingParagraph(text, fonts, Element.ALIGN_LEFT);
    }

    /**
     * 图片段落添加
     * @param file     图片文件
     * @param consumer 图片处理
     * @return {@link T}
     */
    public T pictureParagraph(File file, Consumer<Image> consumer) {
        Image image;
        try {
            image = Image.getInstance(file.toURI().toURL());
        } catch (BadElementException | IOException e) {
            throw new PortableExportException(e);
        }
        consumer.accept(image);
        this.addImage(image);

        return (T) this;
    }

    /**
     * 图片段落添加
     * @param file 图片文件
     * @return {@link T}
     */
    public T pictureParagraph(File file) {
        return this.pictureParagraph(file, t -> {});
    }

    /**
     * 图片添加
     * @param file  图片文件
     * @param width 指定宽度
     * @return {@link T}
     */
    public T pictureParagraph(File file, float width) {
        return
            this.pictureParagraph(file, image -> {
                float originWidth = image.getWidth();
                float originHeight = image.getHeight();
                // 图片等比例
                image.scaleToFit(width, width / originWidth * originHeight);
            });
    }

    /**
     * 添加一个表格
     * @param columns  表格一行最多列数
     * @param consumer 表格消费
     * @return {@link T}
     */
    public T table(int columns, Consumer<DslTable> consumer) {
        PdfPTable pdfPTable = new PdfPTable(columns);
        // 设置表格行前后间距
        pdfPTable.setSpacingAfter(10);
        pdfPTable.setSpacingBefore(10);
        // 设置表格默认宽度100%
        pdfPTable.setWidthPercentage(100F);

        consumer.accept(new DslTable(pdfPTable));
        this.addTable(pdfPTable);

        return (T) this;
    }

    /**
     * 动态创建多表
     * @param iterable 表迭代器
     * @param function 每个表格列数生成方法
     * @param consumer 每个表格消费
     * @param <U>      迭代器内容类型
     * @return {@link T}
     */
    public <U> T tables(Iterable<U> iterable, Function<U, Integer> function, BiConsumer<U, DslTable> consumer) {
        if (Objects.nonNull(iterable)) {
            iterable.forEach(u -> this.table(function.apply(u), t -> consumer.accept(u, t)));
        }

        return (T) this;
    }

    /**
     * 将段落添加到某个容器
     * @param paragraph 段落实例
     */
    void addParagraph(Paragraph paragraph) {
        this.addElement(paragraph);
    }

    /**
     * 将表格添加到容器
     * @param table 表格实例
     */
    void addTable(PdfPTable table) {
        this.addElement(table);
    }

    /**
     * 将图片添加到容器
     * @param image 图片
     */
    void addImage(Image image) {
        this.addElement(image);
    }

    /**
     * 将任意元素添加到容器
     * @param element {@link Element}
     */
    abstract void addElement(Element element);
}
