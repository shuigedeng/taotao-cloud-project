package com.taotao.cloud.sys.biz.docx4j.output.builder.document;

import com.taotao.cloud.sys.biz.docx4j.input.FileUtil;
import com.taotao.cloud.sys.biz.docx4j.output.builder.Person;
import com.taotao.cloud.sys.biz.docx4j.output.builder.SpecDataFactory;
import com.taotao.cloud.sys.biz.support.docx4j.output.builder.document.DocumentExporter;
import com.taotao.cloud.sys.biz.support.docx4j.output.builder.document.ParagraphStyle;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;
import org.apache.poi.wp.usermodel.HeaderFooterType;
import org.apache.poi.xwpf.usermodel.ParagraphAlignment;
import org.junit.Test;

/**
 * {@link DocumentExporter}单元测试
 */
public class DocumentExporterSpec {

	@Test
	public void empty() throws FileNotFoundException {
		DocumentExporter.create()
			.writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "empty.docx")));
	}

	@Test
	public void simple() throws IOException {
		DocumentExporter.create()
			.headingParagraph("标题一", ParagraphStyle.HEADING_1)
			.headingParagraph("居中标题二", ParagraphStyle.HEADING_2, ParagraphAlignment.CENTER)
			.headingParagraph("居右标题三", ParagraphStyle.HEADING_3, ParagraphAlignment.RIGHT)
			.headingParagraph("标题五", ParagraphStyle.HEADING_5)
			.headingParagraph("标题七", ParagraphStyle.HEADING_7)
			.headingParagraph("标题九", ParagraphStyle.HEADING_9)
			.textParagraph("这是正文这是正文这是正文这是正文这是正文这是正文这是正文这是正文")
			.writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "simple.docx")));
	}

	@Test
	public void breakPage() throws FileNotFoundException {
		List<Person> people = SpecDataFactory.tableData();
		DocumentExporter.create()
			// 多个文档 自动添加分页符
			.documents(people, (it, d) ->
				// 分页文档
				d.headingParagraph(it.getName() + "个人信息", ParagraphStyle.SUB_HEADING)
					.table(2, 3, t ->
						t.row(r -> r.headCells("姓名", "年龄", "性别"))
							.row(r -> r.dataCells(it::getName, it::getAge, it::getSex))
					)
			)
			.writeTo(
				new FileOutputStream(FileUtil.brotherPath(this.getClass(), "break-page.docx")));
	}

	@Test
	public void table() throws FileNotFoundException {
		List<Person> people = SpecDataFactory.tableData();
		DocumentExporter.create()
			.headingParagraph("教职工列表", ParagraphStyle.SUB_HEADING)
			// 需要指定表格行数及列数
			.table(people.size() + 1, 3, t ->
				// 表头行会自动加粗
				t.row(r -> r.headCells("姓名", "年龄", "性别"))
					// 数据行正常
					.rows(people, (p, r) -> r.dataCells(p::getName, p::getAge, p::getSex))
			)
			.writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "table.docx")));
	}

	/**
	 * 合并表格
	 *
	 * @throws FileNotFoundException FileNotFoundException
	 * @see SpreadSheetExporterSpec#mergeHead()
	 * @see SpreadSheetExporterSpec#mergeData()
	 */
	@Test
	public void mergeTable() throws FileNotFoundException {
		List<Person> people = SpecDataFactory.tableData();
		// 将数据按照性别分组 合并处理性别列 模拟sql分组 但不保证列表数据顺序
		Map<String, List<Person>> groupBySex = people.stream()
			.collect(Collectors.groupingBy(Person::getSex));
		DocumentExporter.create()
			.headingParagraph("教职工列表", ParagraphStyle.SUB_HEADING)
			// 需要指定表格行数及列数
			.table(people.size() + 2, 3, t -> {
				// 表头行列合并
				t.row(r ->
						r.cell(c -> c.boldText("姓名").rowspan(2))
							.cell(c -> c.boldText("其他信息").colspan(2))
							// 合并列数据补全
							.headCell("")
					)
					// 合并行的数据需要补全
					.row(r -> r.headCells("姓名", "年龄", "性别"));
				groupBySex.forEach((key, value) -> {
					AtomicBoolean merged = new AtomicBoolean();
					int rowspan = value.size();
					t.rows(value, (it, r) ->
						r.dataCells(it::getName, it::getAge)
							.cell(c -> {
								c.text(it::getSex);
								// 行合并一次
								if (!merged.get()) {
									merged.set(true);
									c.rowspan(rowspan);
								}
							})
					);
				});
			})
			.writeTo(
				new FileOutputStream(FileUtil.brotherPath(this.getClass(), "merge-table.docx")));
	}

	@Test
	public void simpleHeaderAndFooter() throws FileNotFoundException {
		List<Person> people = SpecDataFactory.tableData();
		DocumentExporter.create()
			// 多个文档 自动添加分页符
			.documents(people, (it, d) ->
				// 分页文档
				d.headingParagraph(it.getName() + "个人信息", ParagraphStyle.SUB_HEADING)
					.table(2, 3, t ->
						t.row(r -> r.headCells("姓名", "年龄", "性别"))
							.row(r -> r.dataCells(it::getName, it::getAge, it::getSex))
					)
			)
			.header("我是页眉")
			.footer("我是页脚")
			.writeTo(new FileOutputStream(
				FileUtil.brotherPath(this.getClass(), "simple-header-footer.docx")));
	}

	@Test
	public void complexHeaderAndFooter() throws FileNotFoundException {
		List<Person> people = SpecDataFactory.tableData();
		DocumentExporter.create()
			// 多个文档 自动添加分页符
			.documents(people, (it, d) ->
				// 分页文档
				d.headingParagraph(it.getName() + "个人信息", ParagraphStyle.SUB_HEADING)
					.table(2, 3, t ->
						t.row(r -> r.headCells("姓名", "年龄", "性别"))
							.row(r -> r.dataCells(it::getName, it::getAge, it::getSex))
					)
			)
			.header(HeaderFooterType.DEFAULT, h -> h.text("成都中教智慧职工信息"))
			.footer(HeaderFooterType.DEFAULT, f -> f.page("第", "页/共", "页"))
			.writeTo(new FileOutputStream(
				FileUtil.brotherPath(this.getClass(), "complex-header-footer.docx")));
	}

	@Test
	public void picture() throws FileNotFoundException {
		List<Person> people = SpecDataFactory.tableData();
		DocumentExporter.create()
			.headingParagraph("教职工列表", ParagraphStyle.SUB_HEADING)
			// 需要指定表格行数及列数
			.table(people.size() + 1, 5, t ->
				// 表头行会自动加粗
				t.row(r -> r.headCells("姓名", "年龄", "性别", "图片", "文字图片"))
					// 数据行正常
					.rows(people, (p, r) ->
						r.dataCells(p::getName, p::getAge, p::getSex)
							.pictureCell(new File(FileUtil.rootPath(this.getClass(), "/c.gif")), 20,
								20)
							.cell(c ->
								c.text("我是单元格图片")
									.pictureParagraph(
										new File(FileUtil.rootPath(this.getClass(), "/c.gif")),
										20,
										20
									)
							)
					)
			)
			// 段落图片
			.textParagraph("我是正文图片")
			.pictureParagraph(new File(FileUtil.rootPath(this.getClass(), "/d.bmp")), 400, 150)
			// 页眉图片
			.header(HeaderFooterType.DEFAULT, h ->
				h.textParagraph("我是页眉图片")
					.pictureParagraph(new File(FileUtil.rootPath(this.getClass(), "/b.png")), 20,
						20)
			)
			// 页脚图片
			.footer(HeaderFooterType.DEFAULT, f ->
				f.textParagraph("我是页脚图片")
					.pictureParagraph(new File(FileUtil.rootPath(this.getClass(), "/a.jpg")), 20,
						20)
			)
			.writeTo(new FileOutputStream(FileUtil.brotherPath(this.getClass(), "picture.docx")));
	}
}
