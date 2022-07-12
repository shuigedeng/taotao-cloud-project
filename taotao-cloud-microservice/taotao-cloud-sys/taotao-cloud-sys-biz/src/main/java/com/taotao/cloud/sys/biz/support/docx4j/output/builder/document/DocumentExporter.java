package com.taotao.cloud.sys.biz.support.docx4j.output.builder.document;

import com.taotao.cloud.sys.biz.support.docx4j.output.utils.FileUtil;
import com.taotao.cloud.sys.biz.support.docx4j.output.utils.HttpResponseUtil;
import com.taotao.cloud.sys.biz.support.docx4j.output.utils.HttpServletUtil;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.util.IOUtils;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFStyle;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.xmlbeans.XmlException;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.StylesDocument;

/**
 * word文档构造器
 */
public class DocumentExporter extends RichableDocument<DocumentExporter> {
	private final XWPFDocument document;

    private DocumentExporter(XWPFDocument document) {
        // 若文档样式为空则创建样式集
        if (Objects.isNull(document.getStyles())) {
            document.createStyles();
        }

        this.document = document;
        // 添加默认样式
        try {
            StylesDocument stylesDocument =
	            (StylesDocument) StylesDocument.Factory.parse(new File(
		            FileUtil.brotherPath(DocumentExporter.class, "styles.xml")));
            this.document.getStyles().setStyles(stylesDocument.getStyles());
        } catch (XmlException | IOException e) {
            throw new DocumentExportException(e);
        }
    }

    /**
     * 创建word文档
     * @return {@link DocumentExporter}
     */
    public static DocumentExporter create() {
        return new DocumentExporter(new XWPFDocument());
    }

    /**
     * 通过输入流创建word文档
     * @param is {@link InputStream}
     * @return {@link DocumentExporter}
     */
    public static DocumentExporter create(InputStream is) {
        try {
            return new DocumentExporter(new XWPFDocument(is));
        } catch (IOException e) {
            throw new DocumentExportException(e);
        }
    }

    /**
     * 自定义样式
     * @return {@link DocumentExporter}
     */
    public DocumentExporter style(Supplier<XWPFStyle> supplier) {
        this.addStyle(supplier);
        return this;
    }

    /**
     * 添加页眉
     * @param type     页眉类型
     * @param consumer 页眉消费
     * @return {@link DocumentExporter}
     */
    public DocumentExporter header(HeaderFooterType type, Consumer<DslHeader> consumer) {
        consumer.accept(new DslHeader(this.document.createHeader(type)));
        return this;
    }

    /**
     * 添加默认所有页的页眉文本
     * @param text 文本内容
     * @return {@link DocumentExporter}
     */
    public DocumentExporter header(String text) {
        return this.header(HeaderFooterType.DEFAULT, header -> header.textParagraph(text));
    }

    /**
     * 多文档创建 自动分页
     * @param iterable 迭代器
     * @param consumer builder消费
     * @param <U>      迭代元素类型
     * @return {@link DocumentExporter}
     */
    public <U> DocumentExporter documents(Iterable<U> iterable, BiConsumer<U, DocumentExporter> consumer) {
        if (Objects.nonNull(iterable)) {
            Iterator<U> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                consumer.accept(iterator.next(), this);

                // 若存在下一项则添加分页符
                if (iterator.hasNext()) {
                    // 添加自动换页
                    super.pageBreak();
                }
            }
        }

        return this;
    }

    /**
     * 添加页脚
     * @param type     页脚类型
     * @param consumer 页脚消费
     * @return {@link DocumentExporter}
     */
    public DocumentExporter footer(HeaderFooterType type, Consumer<DslFooter> consumer) {
        consumer.accept(new DslFooter(this.document.createFooter(type)));
        return this;
    }

    /**
     * 添加默认所有的页脚文本
     * @param text 文本内容
     * @return {@link DocumentExporter}
     */
    public DocumentExporter footer(String text) {
        return this.footer(HeaderFooterType.DEFAULT, footer -> footer.textParagraph(text));
    }

    /**
     * 将word文档写到servlet输出流并指定文件后缀
     * @param fileName 文件名
     */
    public void writeToServletResponse(String fileName) {
        HttpServletResponse response = HttpServletUtil.getCurrentResponse();
        try {
            // http文件名处理 并固定为docx后缀
            HttpResponseUtil.handleOutputFileName(DocumentFileType.DOCX.fullName(fileName), response);
            this.writeTo(response.getOutputStream(), false);
        } catch (IOException e) {
            throw new DocumentExportException(e);
        }
    }

    /**
     * 将word文档写到给定输出流并关闭流
     * @param outputStream 输出流
     * @param closeable    是否需要关闭输出流
     */
    public void writeTo(OutputStream outputStream, boolean closeable) {
        this.doWrite(outputStream, closeable);
    }

    /**
     * 将word文档写到给定输出流并关闭流
     * @param outputStream 输出流
     */
    public void writeTo(OutputStream outputStream) {
        this.writeTo(outputStream, true);
    }

    /**
     * 为文档添加一个特定样式
     * @param supplier 样式提供
     */
    protected void addStyle(Supplier<XWPFStyle> supplier) {
        this.document.getStyles().addStyle(supplier.get());
    }

    /**
     * 为文档添加一个特定样式
     * @param style {@link XWPFStyle}
     */
    protected void addStyle(XWPFStyle style) {
        this.addStyle(() -> style);
    }

    /**
     * 将word文档写到输出流
     * @param outputStream 输出流
     * @param closeable    是否需要关闭输出流
     */
    protected void doWrite(OutputStream outputStream, boolean closeable) {
        try {
            this.document.write(outputStream);
        } catch (IOException e) {
            throw new DocumentExportException(e);
        } finally {
            // 文档流关闭
            IOUtils.closeQuietly(this.document);
            // 若需要关闭输入流则关闭 如ServletOutputStream则不能关闭
            if (closeable) {
                IOUtils.closeQuietly(outputStream);
            }
        }
    }

    @Override
    protected XWPFParagraph createParagraph() {
        return this.document.createParagraph();
    }

    @Override
    protected XWPFTable createTable(int rows, int columns) {
        return this.document.createTable(rows, columns);
    }
}
