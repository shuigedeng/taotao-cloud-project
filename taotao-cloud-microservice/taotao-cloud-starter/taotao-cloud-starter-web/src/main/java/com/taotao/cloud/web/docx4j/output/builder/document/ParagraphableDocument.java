package com.taotao.cloud.web.docx4j.output.builder.document;

import org.apache.poi.xwpf.usermodel.BreakType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import javax.annotation.Generated;
import java.io.File;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * 可分段的
 * {@link org.apache.poi.xwpf.usermodel.XWPFDocument}
 * {@link org.apache.poi.xwpf.usermodel.XWPFHeader}
 * {@link org.apache.poi.xwpf.usermodel.XWPFFooter}
 * {@link org.apache.poi.xwpf.usermodel.XWPFTableCell}
 */
@Generated({})
abstract class ParagraphableDocument<T extends ParagraphableDocument> {
    /**
     * 添加一个段落
     * @param consumer 段落消费
     */
    public T paragraph(Consumer<DslParagraph> consumer) {
        consumer.accept(new DslParagraph(this.createParagraph()));
        return (T) this;
    }

    /**
     * 添加多个段落
     * @param iterable 段落迭代器
     * @param consumer 段落迭代消费
     * @param <U>      段落迭代类型
     * @return {@link T}
     */
    public <U> T paragraphs(Iterable<U> iterable, BiConsumer<U, DslParagraph> consumer) {
        if (Objects.nonNull(iterable)) {
            iterable.forEach(it -> this.paragraph(p -> consumer.accept(it, p)));
        }

        return (T) this;
    }

    /**
     * 添加一个正文段落
     * @param text 正文内容
     * @return {@link T}
     */
    public T textParagraph(String text) {
        return this.paragraph(p -> p.run(text));
    }

    /**
     * 添加图片段落
     * @param file   图片文件
     * @param width  宽度
     * @param height 高度
     * @return {@link T}
     */
    public T pictureParagraph(File file, int width, int height) {
        return this.paragraph(p -> p.run(r -> r.picture(file, width, height)));
    }

    /**
     * 添加一个标题段落 默认左对齐
     * @param text  标题内容
     * @param style 标题样式
     * @return {@link T}
     */
    public T headingParagraph(String text, ParagraphStyle style) {
        return this.headingParagraph(text, style, ParagraphAlignment.LEFT);
    }

    /**
     * 添加一个标题段落
     * @param text      标题内容
     * @param style     标题样式
     * @param alignment 对齐方式
     * @return {@link T}
     */
    public T headingParagraph(String text, ParagraphStyle style, ParagraphAlignment alignment) {
        return
            this.paragraph(p ->
                p.run(text)
                    .more(t -> {
                        t.setStyle(style.id);
                        t.setAlignment(alignment);
                    })
            );
    }

    /**
     * 添加分页符
     * @return {@link T}
     */
    public T pageBreak() {
        return this.paragraph(t -> t.run(r -> r.more(xr -> xr.addBreak(BreakType.PAGE))));
    }

    /**
     * 创建段落
     * @return {@link XWPFParagraph}
     */
    protected abstract XWPFParagraph createParagraph();
}
