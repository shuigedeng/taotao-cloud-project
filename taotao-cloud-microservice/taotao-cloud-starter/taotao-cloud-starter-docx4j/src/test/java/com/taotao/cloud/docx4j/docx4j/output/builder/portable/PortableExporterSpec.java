package com.taotao.cloud.docx4j.docx4j.output.builder.portable;

import com.itextpdf.text.Element;
import com.taotao.cloud.sys.biz.docx4j.input.FileUtil;
import com.taotao.cloud.sys.biz.docx4j.output.builder.Person;
import com.taotao.cloud.sys.biz.docx4j.output.builder.SpecDataFactory;
import com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable.DefaultPageFooterHandler;
import com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable.DefaultPictureWatermarkHandler;
import com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable.DefaultTextHeaderHandler;
import com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable.DefaultTextWatermarkHandler;
import com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable.Fonts;
import com.taotao.cloud.sys.biz.support.docx4j.output.builder.portable.PortableExporter;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

/**
 * {@link PortableExporter}单元测试
 */
public class PortableExporterSpec {
    @Test
    public void empty() throws FileNotFoundException {
        PortableExporter.fastCreate()
            .writeTo(new FileOutputStream(
	            FileUtil.brotherPath(PortableExporterSpec.class, "empty.pdf")));
    }

    @Test
    public void simple() throws FileNotFoundException {
        PortableExporter.fastCreate()
            .headingParagraph("标题一", Fonts.HEADING_1)
            .headingParagraph("居中的标题二", Fonts.HEADING_2, Element.ALIGN_CENTER)
            .headingParagraph("居右的标题三", Fonts.HEADING_3, Element.ALIGN_RIGHT)
            .headingParagraph("标题五", Fonts.HEADING_5)
            .headingParagraph("标题七", Fonts.HEADING_7)
            .headingParagraph("标题九", Fonts.HEADING_9)
            .textParagraph("这是正文这是正文这是正文这是正文这是正文这是正文这是正第一行")
            .paragraph(p -> p.chunk("这是正文这是正文这是正文这是正文这是正文这是正文这是第二行", Fonts.HEADER_FOOTER.font()))
            .pictureParagraph(new File(FileUtil.rootPath(PortableExporterSpec.class, "/a.jpeg")), 200)
            .pictureParagraph(new File(FileUtil.rootPath(PortableExporterSpec.class, "/a.jpg")), 100)
            .pictureParagraph(new File(FileUtil.rootPath(PortableExporterSpec.class, "/b.png")))
            .pictureParagraph(new File(FileUtil.rootPath(PortableExporterSpec.class, "/c.gif")))
            .pictureParagraph(new File(FileUtil.rootPath(PortableExporterSpec.class, "/d.bmp")))
            .writeTo(new FileOutputStream(FileUtil.brotherPath(PortableExporterSpec.class, "simple.pdf")));
    }

    @Test
    public void breakPage() throws FileNotFoundException {
        PortableExporter.fastCreate()
            // 多个文档 自动添加分页符
            .documents(SpecDataFactory.tableData(), (it, d) ->
                // 分页文档
                d.headingParagraph(it.getName() + "个人信息", Fonts.HEADING_1)
                    .textParagraph(String.format("姓名:%s", it.getName()))
                    .textParagraph(String.format("年龄:%s", it.getAge()))
                    .textParagraph(String.format("性别:%s", it.getSex()))
            )
            .writeTo(new FileOutputStream(FileUtil.brotherPath(PortableExporterSpec.class, "break-page.pdf")));
    }

    @Test
    public void table() throws FileNotFoundException {
        PortableExporter.fastCreate()
            .paragraph(p ->
                p.chunk("教职工列表", Fonts.HEADING_1.font()).more(pp -> pp.setAlignment(Element.ALIGN_CENTER))
            )
            // 需要指定表格列数
            .table(3, t ->
                // 表头
                t.row(r -> r.headCells("姓名", "年龄", "性别"))
                    // 数据单元格
                    .rows(SpecDataFactory.tableData(), (u, r) -> r.dataCells(u::getName, u::getAge, u::getSex))
            )
            .writeTo(new FileOutputStream(FileUtil.brotherPath(PortableExporterSpec.class, "table.pdf")));
    }

    @Test
    public void mergeTable() throws FileNotFoundException {
        List<Person> people = SpecDataFactory.tableData();
        // 将数据按照性别分组 合并处理性别列 模拟sql分组 但不保证列表数据顺序
        Map<String, List<Person>> groupBySex = people.stream().collect(Collectors.groupingBy(
	        Person::getSex));
        PortableExporter.fastCreate()
            .paragraph(p ->
                p.chunk("教职工列表", Fonts.HEADING_1.font()).more(pp -> pp.setAlignment(Element.ALIGN_CENTER))
            )
            // 需要指定表格行数及列数
            .table(3, t -> {
                // 表头行列合并 从左往右从上之下渲染 合并的单元格只渲染一次
                t.row(r -> r.headCell("姓名", c -> c.rowspan(2)).headCell("其他信息", c -> c.colspan(2)))
                    .row(r -> r.headCells("年龄", "性别"));

                // 数据合并
                groupBySex.forEach((key, value) -> {
                    AtomicBoolean merged = new AtomicBoolean();
                    int rowspan = value.size();
                    t.rows(value, (it, r) -> {
                        // 前两列数据
                        r.dataCells(it::getName, it::getAge);
                        // 行合并一次 单元格也只添加一次
                        if (!merged.get()) {
                            merged.set(true);
                            r.dataCell(it::getSex, c -> c.rowspan(rowspan));
                        }
                    });
                });
            })
            .writeTo(new FileOutputStream(FileUtil.brotherPath(PortableExporterSpec.class, "merge-table.pdf")));
    }

    @Test
    public void headerAndFooter() throws FileNotFoundException {
        PortableExporter.create()
            // 页眉事件
            .event(new DefaultTextHeaderHandler("成都中教智汇"))
            // 页脚事件
            .event(new DefaultPageFooterHandler("第", "页/共", "页"))
            // 事件必须在open之间设置
            .open()
            // 多个文档 自动添加分页符
            .documents(SpecDataFactory.tableData(), (it, d) ->
                // 分页文档
                d.headingParagraph(it.getName() + "个人信息", Fonts.HEADING_1)
                    .table(3, t ->
                        t.row(r -> r.headCells("姓名", "年龄", "性别"))
                            .row(r -> r.dataCells(it::getName, it::getAge, it::getSex))
                    )
            )
            .writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "header-footer.pdf")));
    }

    @Test
    public void watermark() throws FileNotFoundException {
        PortableExporter.create()
            // 页眉事件
            .event(new DefaultTextHeaderHandler("成都中教智汇"))
            // 页脚事件
            .event(new DefaultPageFooterHandler("第", "页/共", "页"))
            .event(new DefaultTextWatermarkHandler("成都中教智汇", 28))
            // 事件必须在open之前设置
            .open()
            // 多个文档 自动添加分页符
            .documents(SpecDataFactory.tableData(), (it, d) ->
                // 分页文档
                d.headingParagraph(it.getName() + "个人信息", Fonts.HEADING_1)
                    .table(3, t ->
                        t.row(r -> r.headCells("姓名", "年龄", "性别"))
                            .row(r -> r.dataCells(it::getName, it::getAge, it::getSex))
                    )
            )
            .writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "watermark.pdf")));
    }

    @Test
    public void picture() throws FileNotFoundException {
        PortableExporter.create()
            // 水印图片
            .event(new DefaultPictureWatermarkHandler(new File(FileUtil.rootPath(this.getClass(), "/b.png")), 50))
            // 事件必须在open之前设置
            .open()
            .headingParagraph("教职工列表", Fonts.HEADING_3)
            // 需要指定表格行数及列数
            .table(5, t ->
                // 表头行会自动加粗
                t.row(r -> r.headCells("姓名", "年龄", "性别", "图片", "文字图片"))
                    .rows(SpecDataFactory.tableData(), (p, r) ->
                        r.dataCells(p::getName, p::getAge, p::getSex)
                            // 图片单元格
                            .cell(c -> c.pictureParagraph(p.picture(), 20))
                            // 文字及图片
                            .cell(c -> c.textParagraph("我是单元格图片").pictureParagraph(p.picture(), 20))
                    )
            )
            .writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "picture.pdf")));
    }
}
