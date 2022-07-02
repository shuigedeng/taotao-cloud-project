package com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable;

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.pdf.PdfPageEvent;
import com.itextpdf.text.pdf.PdfWriter;
import com.taotao.cloud.sys.biz.support.docx4j.output.utils.HttpResponseUtil;
import com.taotao.cloud.sys.biz.support.docx4j.output.utils.HttpServletUtil;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.util.IOUtils;

/**
 * Pdf文档构建器
 */
public class PortableExporter extends
		PortableDocument<PortableExporter> {
    /**
     * pdf文档
     */
   private final Document document;
    /**
     * 内存字节流
     */
    private final  ByteArrayOutputStream byteArrayOutputStream;
	private final PdfWriter writer;

    PortableExporter(Document document) throws DocumentException {
        this.document = document;
        this.byteArrayOutputStream = new ByteArrayOutputStream(1024);
        this.writer = PdfWriter.getInstance(this.document, this.byteArrayOutputStream);
    }

    /**
     * 使用指定{@link Document}创建
     * @param supplier {@link Document}提供器
     * @return {@link PortableExporter}
     */
    public static PortableExporter create(Supplier<Document> supplier) {
        try {
            return new PortableExporter(supplier.get());
        } catch (DocumentException e) {
            throw new PortableExportException(e);
        }
    }

    /**
     * 创建builder
     * @return {@link PortableExporter}
     */
    public static PortableExporter create() {
        return PortableExporter.create(Document::new);
    }

    /**
     * 快速构建文档并开启
     * @return {@link PortableExporter}
     */
    public static PortableExporter fastCreate() {
        return PortableExporter.create().open();
    }

    /**
     * 快速构建给定文档并开启
     * @param supplier {@link Document}提供
     * @return {@link PortableExporter}
     */
    public static PortableExporter fastCreate(Supplier<Document> supplier) {
        return PortableExporter.create(supplier).open();
    }

    /**
     * 文档开启
     * @return {@link PortableExporter}
     */
    public PortableExporter open() {
        this.document.open();
        return this;
    }

    /**
     * 添加事件
     * @param event 任意{@link PdfPageEvent}事件
     * @param <T>   事件类型
     * @return {@link PortableExporter}
     */
    public <T extends PdfPageEvent> PortableExporter event(T event) {
        return this.events(Collections.singletonList(event));
    }

    /**
     * 添加多个事件
     * @param events {@link List}
     * @param <T>    事件类型
     * @return {@link PortableExporter}
     */
    public <T extends PdfPageEvent> PortableExporter events(List<T> events) {
        if (Objects.nonNull(events)) {
            events.forEach(this.writer::setPageEvent);
        }

        return this;
    }

    /**
     * 插入分页符
     * @return {@link PortableExporter}
     */
    public PortableExporter pageBreak() {
        this.writer.setPageEmpty(false);
        this.document.newPage();
        return this;
    }

    /**
     * 多文档创建 自动分页
     * @param iterable 迭代器
     * @param consumer builder消费
     * @param <U>      迭代元素类型
     * @return {@link PortableExporter}
     */
    public <U> PortableExporter documents(Iterable<U> iterable, BiConsumer<U, PortableExporter> consumer) {
        if (Objects.nonNull(iterable)) {
            Iterator<U> iterator = iterable.iterator();
            while (iterator.hasNext()) {
                consumer.accept(iterator.next(), this);
                // 自动插入分页符
                if (iterator.hasNext()) {
                    this.pageBreak();
                }
            }
        }

        return this;
    }

    /**
     * 将pdf文档写到servlet输出流
     * @param fileName 下载文件名名称
     */
    public void writeToServletResponse(String fileName) {
        HttpServletResponse response = HttpServletUtil.getCurrentResponse();
        try {
            // http文件名处理
            HttpResponseUtil.handleOutputFileName(PortableFileType.PDF.fullName(fileName), response);

            this.writeTo(response.getOutputStream(), false);
        } catch (IOException e) {
            throw new PortableExportException(e);
        }
    }

    /**
     * 将pdf文档写到给定输出流并关闭流
     * @param outputStream 输出流
     * @param closeable    是否需要关闭输出流
     */
    public void writeTo(OutputStream outputStream, boolean closeable) {
        this.doWrite(outputStream, closeable);
    }

    /**
     * 将pdf文档写到给定输出流并关闭流
     * @param outputStream 输出流
     */
    public void writeTo(OutputStream outputStream) {
        this.writeTo(outputStream, true);
    }

    /**
     * 将pdf文档写到输出流
     * @param outputStream 输出流
     * @param closeable    是否需要关闭输出流
     */
    protected void doWrite(OutputStream outputStream, boolean closeable) {
        try {
            // 若文档为空 添加一页空文档
            if (this.document.getPageNumber() == 0) {
                this.writer.setPageEmpty(false);
                this.document.newPage();
            }

            // 先关闭文档再访问字节输出流
            this.document.close();
            this.writer.flush();
            this.writer.close();

            this.byteArrayOutputStream.writeTo(outputStream);
        } catch (IOException e) {
            throw new PortableExportException(e);
        } finally {
            IOUtils.closeQuietly(this.byteArrayOutputStream);
            // 若需要关闭输入流则关闭 如ServletOutputStream则不能关闭
            if (closeable) {
                IOUtils.closeQuietly(outputStream);
            }
        }
    }

    @Override
    void addElement(Element element) {
        try {
            this.document.add(element);
        } catch (DocumentException e) {
            throw new PortableExportException(e);
        }
    }
}
