package com.taotao.cloud.office.convert;

import cn.hutool.core.io.FileUtil;
import com.aspose.words.Document;
import com.aspose.words.SaveFormat;
import com.taotao.cloud.office.convert.config.Constants;
import com.taotao.cloud.office.convert.util.MyFileUtil;
import com.youbenzi.md2.export.FileFactory;
import gui.ava.html.parser.HtmlParser;
import gui.ava.html.parser.HtmlParserImpl;
import gui.ava.html.renderer.ImageRenderer;
import gui.ava.html.renderer.ImageRendererImpl;
import org.junit.Test;

import java.io.File;
import java.util.List;

/**
 * <p>
 * 小测试$
 * </p>
 *
 * @description
 * @since 2020/7/10$ 11:02$
 */
public class AppTest {

	private final String WORD_FILE_PATH = Constants.DEFAULT_FOLDER_TMP + "/test.doc";
	private final String HTML_FILE_PATH = Constants.DEFAULT_FOLDER_TMP + "/test.html";
	private final String EXCEL_FILE_PATH = Constants.DEFAULT_FOLDER_TMP + "/test.xlsx";
	private final String MD_FILE_PATH = Constants.DEFAULT_FOLDER_TMP + "/test.md";
	private final String PDF_FILE_PATH = Constants.DEFAULT_FOLDER_TMP + "/test.pdf";

	@Test
	public void testData() throws Exception {
		// load the file to be converted
		Document wpd = new Document(WORD_FILE_PATH);
		// convert doc to docx, PDF , HTML , PNG , JPEG
		wpd.save(Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test.docx", SaveFormat.DOCX);
		wpd.save(Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test.pdf", SaveFormat.PDF);
		wpd.save(Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test.html", SaveFormat.HTML);
		wpd.save(Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test.png", SaveFormat.PNG);
		wpd.save(Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test.jpg", SaveFormat.JPEG);
	}

	// ================================ ↓↓↓↓↓↓ 下面为自定义封装过的api ↓↓↓↓↓↓ ===================================

	@Test
	public void testWord2Html() throws Exception {
		File htmlFile = FileConvertUtil.wordBytes2HtmlFile(MyFileUtil.readBytes(WORD_FILE_PATH),
			Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test.html");
		System.out.println(htmlFile);
	}

	@Test
	public void testWord2Pdf() throws Exception {
		File pdfFile = FileConvertUtil.wordBytes2PdfFile(MyFileUtil.readBytes(WORD_FILE_PATH),
			Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test-word.pdf");
		System.out.println(pdfFile);
	}

	@Test
	public void testWord2Jpg() throws Exception {
		byte[] wordFileBytes = MyFileUtil.readBytes(WORD_FILE_PATH);
		List<File> jpgFileList =
			FileConvertUtil.wordBytes2JpegFileList(wordFileBytes, Constants.DEFAULT_FOLDER_TMP_GENERATE);
		System.out.println(jpgFileList);
	}

	@Test
	public void testWord2Png() throws Exception {
		byte[] wordFileBytes = MyFileUtil.readBytes(WORD_FILE_PATH);
		List<File> pngFileList =
			FileConvertUtil.wordBytes2PngFileList(wordFileBytes, Constants.DEFAULT_FOLDER_TMP_GENERATE);
		System.out.println(pngFileList);
	}

	@Test
	public void testHtml2Word() throws Exception {
		File wordFile = FileConvertUtil.htmlBytes2WordFile(MyFileUtil.readBytes(HTML_FILE_PATH),
			Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test.doc");
		System.out.println(wordFile);
	}

	@Test
	public void testHtml2Pdf() throws Exception {
		File pdfFile = FileConvertUtil.htmlBytes2PdfFile(MyFileUtil.readBytes(HTML_FILE_PATH),
			Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test-html.pdf");
		System.out.println(pdfFile);
	}

	@Test
	public void testHtml2Jpg() throws Exception {
		byte[] jpgBytes = FileConvertUtil.htmlBytes2JpgBytes(MyFileUtil.readBytes(HTML_FILE_PATH));
		File file = FileUtil.writeBytes(jpgBytes, Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test.jpg");
		System.out.println(file.getName());
	}

	@Test
	public void testHtml2PngBy2() throws Exception {
		HtmlParser htmlParser = new HtmlParserImpl();
		htmlParser.loadHtml(MyFileUtil.readFileContent(HTML_FILE_PATH));
		ImageRenderer imageRenderer = new ImageRendererImpl(htmlParser);
		String imgPath = Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test.png";
		MyFileUtil.touch(imgPath);
		imageRenderer.saveImage(imgPath);
	}

	@Test
	public void testDoc2Docx() throws Exception {
		File docxFile = FileConvertUtil.docBytes2DocxFile(MyFileUtil.readBytes(WORD_FILE_PATH),
			Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test.docx");
		System.out.println(docxFile);
	}

	@Test
	public void testExcel2Pdf() throws Exception {
		File pdfFile = FileConvertUtil.excelBytes2PdfFile(MyFileUtil.readBytes(EXCEL_FILE_PATH),
			Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test-excel.pdf");
		System.out.println(pdfFile);
	}

	@Test
	public void testHtml2Png() throws Exception {
		byte[] htmlFileBytes = MyFileUtil.readBytes(HTML_FILE_PATH);
		List<File> pngFileList =
			FileConvertUtil.wordBytes2PngFileList(htmlFileBytes, Constants.DEFAULT_FOLDER_TMP_GENERATE);
		System.out.println(pngFileList);
	}

	@Test // 【 https://gitee.com/cevin15/MD2File 】 【 注：转换格式不是太完善，存在一定问题！ 】
	public void testMarkdown2Html() throws Exception {
		FileFactory.produce(new File(MD_FILE_PATH), Constants.DEFAULT_FOLDER_TMP_GENERATE + "/test-md.html");
	}

	@Test
	public void testPdf2Word() throws Exception {
		FileConvertUtil.pdf2Word(PDF_FILE_PATH, Constants.DEFAULT_FOLDER_TMP_GENERATE + "/pdf2word.docx");
	}

	@Test
	public void testPdf2Png() throws Exception {
		byte[] pngBytes = MyFileUtil.readBytes(PDF_FILE_PATH);
		FileConvertUtil.pdfBytes2PngFileList(pngBytes, Constants.DEFAULT_FOLDER_TMP_GENERATE + "/pdf2img");
	}

}
