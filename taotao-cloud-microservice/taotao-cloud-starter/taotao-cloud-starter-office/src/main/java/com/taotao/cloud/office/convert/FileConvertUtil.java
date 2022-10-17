package com.taotao.cloud.office.convert;

import com.taotao.cloud.office.convert.config.MatchLicense;
import com.taotao.cloud.office.convert.doc2docx.Doc2DocxUtil;
import com.taotao.cloud.office.convert.excel2pdf.Excel2PdfUtil;
import com.taotao.cloud.office.convert.html2img.Html2ImgUtil;
import com.taotao.cloud.office.convert.html2img.Html2PngUtil;
import com.taotao.cloud.office.convert.html2pdf.Html2PdfUtil;
import com.taotao.cloud.office.convert.html2word.Htm2WordUtil;
import com.taotao.cloud.office.convert.pdf2Img.Pdf2PngUtil;
import com.taotao.cloud.office.convert.pdf2word.Pdf2WordUtil;
import com.taotao.cloud.office.convert.word2html.Word2HtmlUtil;
import com.taotao.cloud.office.convert.word2img.Word2JpegUtil;
import com.taotao.cloud.office.convert.word2img.Word2PngUtil;
import com.taotao.cloud.office.convert.word2pdf.Word2PdfUtil;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * <p>
 * 文件转换工具类$
 * </p>
 *
 * @description
 * @since 2020/11/23$ 16:19$
 */
public class FileConvertUtil {

	/**
	 * `word` 转 `html`
	 *
	 * @param wordBytes: word字节码
	 * @return html文件字节码数据
	 * @since 2020/11/23 16:21
	 */
	public static byte[] wordBytes2HtmlBytes(byte[] wordBytes) throws Exception {
		MatchLicense.init();
		return Word2HtmlUtil.wordBytes2HtmlBytes(wordBytes);
	}

	/**
	 * `word` 转 `html`
	 *
	 * @param wordBytes: word字节码
	 * @return html文件内容
	 * @since 2020/11/23 16:21
	 */
	public static String wordBytes2HtmlStr(byte[] wordBytes) throws Exception {
		MatchLicense.init();
		byte[] htmlBytes = Word2HtmlUtil.wordBytes2HtmlBytes(wordBytes);
		return new String(htmlBytes);
	}

	/**
	 * `word` 转 `html`
	 *
	 * @param wordBytes:    word字节码
	 * @param htmlFilePath: html文件路径
	 * @return html文件数据
	 * @since 2020/11/23 16:21
	 */
	public static File wordBytes2HtmlFile(byte[] wordBytes, String htmlFilePath) throws Exception {
		MatchLicense.init();
		return Word2HtmlUtil.wordBytes2HtmlFile(wordBytes, htmlFilePath);
	}

	/**
	 * `word` 转 `pdf`
	 *
	 * @param wordBytes: word字节码
	 * @return 生成的`pdf`字节码
	 * @since 2020/11/26 13:39
	 */
	public static byte[] wordBytes2PdfBytes(byte[] wordBytes) throws Exception {
		MatchLicense.init();
		return Word2PdfUtil.wordBytes2PdfBytes(wordBytes);
	}

	/**
	 * `word` 转 `pdf`
	 *
	 * @param wordBytes:   word字节码
	 * @param pdfFilePath: 需转换的`pdf`文件路径
	 * @return 生成的`pdf`文件数据
	 * @since 2020/11/26 13:39
	 */
	public static File wordBytes2PdfFile(byte[] wordBytes, String pdfFilePath) throws Exception {
		MatchLicense.init();
		return Word2PdfUtil.wordBytes2PdfFile(wordBytes, pdfFilePath);
	}

	/**
	 * `doc` 转 `docx`
	 *
	 * @param docBytes: doc文件字节码
	 * @return 生成的`docx`文件字节码
	 * @since 2020/11/24 11:26
	 */
	public static byte[] docBytes2DocxBytes(byte[] docBytes) throws Exception {
		MatchLicense.init();
		return Doc2DocxUtil.docBytes2DocxBytes(docBytes);
	}

	/**
	 * `doc` 转 `docx`
	 *
	 * @param docBytes:     doc文件字节码
	 * @param docxFilePath: 待生成的的`docx`文件路径
	 * @return 生成的`docx`文件数据
	 * @since 2020/11/24 11:26
	 */
	public static File docBytes2DocxFile(byte[] docBytes, String docxFilePath) throws Exception {
		MatchLicense.init();
		return Doc2DocxUtil.docBytes2DocxFile(docBytes, docxFilePath);
	}

	/**
	 * `html` 转 `word` 【 注：本地图片不支持显示！！！ 需转换成在线图片 】
	 *
	 * @param htmlBytes: html字节码
	 * @return word文件字节码
	 * @since 2020/11/24 11:52
	 */
	public static byte[] htmlBytes2WordBytes(byte[] htmlBytes) throws Exception {
		MatchLicense.init();
		return Htm2WordUtil.htmlBytes2WordBytes(htmlBytes);
	}

	/**
	 * `html` 转 `word` 【 注：本地图片不支持显示！！！ 需转换成在线图片 】
	 *
	 * @param html: html内容
	 * @return word文件字节码
	 * @since 2020/11/24 11:52
	 */
	public static byte[] html2WordBytes(String html) throws Exception {
		MatchLicense.init();
		return Htm2WordUtil.htmlBytes2WordBytes(html.getBytes());
	}

	/**
	 * `html` 转 `word` 【 注：本地图片不支持显示！！！ 需转换成在线图片 】
	 *
	 * @param htmlBytes:    html字节码
	 * @param wordFilePath: 待生成的word文件路径
	 * @return word文件信息
	 * @since 2020/11/23 16:04
	 */
	public static File htmlBytes2WordFile(byte[] htmlBytes, String wordFilePath) throws Exception {
		MatchLicense.init();
		return Htm2WordUtil.htmlBytes2WordFile(htmlBytes, wordFilePath);
	}

	/**
	 * `html` 转 `pdf`
	 *
	 * @param htmlBytes: html字节码
	 * @return 生成的`pdf`字节码
	 * @since 2020/11/24 11:26
	 */
	public static byte[] htmlBytes2PdfBytes(byte[] htmlBytes) throws Exception {
		MatchLicense.init();
		return Html2PdfUtil.htmlBytes2PdfBytes(htmlBytes);
	}

	/**
	 * `html` 转 `pdf`
	 *
	 * @param htmlBytes:   html文件数据
	 * @param pdfFilePath: 待生成的`pdf`文件路径
	 * @return 生成的`pdf`文件数据
	 * @since 2020/11/24 11:26
	 */
	public static File htmlBytes2PdfFile(byte[] htmlBytes, String pdfFilePath) throws Exception {
		MatchLicense.init();
		return Html2PdfUtil.htmlBytes2PdfFile(htmlBytes, pdfFilePath);
	}

	/**
	 * `excel` 转 `pdf`
	 *
	 * @param excelBytes: html字节码
	 * @return 生成的`pdf`文件流
	 * @since 2020/11/24 11:26
	 */
	public static byte[] excelBytes2PdfBytes(byte[] excelBytes) throws Exception {
		MatchLicense.init();
		return Excel2PdfUtil.excelBytes2PdfBytes(excelBytes);
	}

	/**
	 * `excel` 转 `pdf`
	 *
	 * @param excelBytes:  excel文件字节码
	 * @param pdfFilePath: 待生成的`pdf`文件路径
	 * @return 生成的`Pdf`文件数据
	 * @since 2020/11/24 11:26
	 */
	public static File excelBytes2PdfFile(byte[] excelBytes, String pdfFilePath) throws Exception {
		MatchLicense.init();
		return Excel2PdfUtil.excelBytes2PdfFile(excelBytes, pdfFilePath);
	}

	/**
	 * `word` 转 `jpeg`
	 *
	 * @param wordBytes: word字节码数据
	 * @return 图片字节码数据列表
	 * @since 2020/11/24 11:52
	 */
	public static List<byte[]> wordBytes2JpegBytes(byte[] wordBytes) throws Exception {
		MatchLicense.init();
		return Word2JpegUtil.wordBytes2JpegBytes(wordBytes);
	}

	/**
	 * `word` 转 `jpeg`
	 *
	 * @param wordBytes:   word字节码数据
	 * @param imgRootPath: 生成图片根路径
	 * @return 图片字节码数据列表
	 * @since 2020/11/24 11:52
	 */
	public static List<File> wordBytes2JpegFileList(byte[] wordBytes, String imgRootPath) throws Exception {
		MatchLicense.init();
		return Word2JpegUtil.wordBytes2JpegFileList(wordBytes, imgRootPath);
	}

	/**
	 * `word` 转 `png`
	 *
	 * @param wordBytes: word字节码数据
	 * @return 图片字节码数据列表
	 * @since 2020/11/24 11:52
	 */
	public static List<byte[]> wordBytes2PngBytes(byte[] wordBytes) throws Exception {
		MatchLicense.init();
		return Word2PngUtil.wordBytes2PngBytes(wordBytes);
	}

	/**
	 * `word` 转 `png`
	 *
	 * @param wordBytes:   word字节码数据
	 * @param imgRootPath: 生成图片路径
	 * @return 图片字节码数据列表
	 * @since 2020/11/24 11:52
	 */
	public static List<File> wordBytes2PngFileList(byte[] wordBytes, String imgRootPath) throws Exception {
		MatchLicense.init();
		return Word2PngUtil.wordBytes2PngFileList(wordBytes, imgRootPath);
	}

	/**
	 * `html` 转 `png`
	 *
	 * @param htmlBytes: html字节码
	 * @return 图片字节码数据列表
	 * @since 2020/11/25 17:17
	 */
	public static List<byte[]> htmlBytes2PngBytes(byte[] htmlBytes) throws Exception {
		MatchLicense.init();
		return Html2PngUtil.htmlBytes2PngBytes(htmlBytes);
	}

	/**
	 * `html` 转 `png`
	 *
	 * @param htmlBytes:   html字节码
	 * @param imgRootPath: 需转换的`png`文件路径
	 * @return 图片文件数据列表
	 * @since 2020/11/25 17:17
	 */
	public static List<File> htmlBytes2PngFileList(byte[] htmlBytes, String imgRootPath) throws Exception {
		MatchLicense.init();
		return Html2PngUtil.htmlBytes2PngFileList(htmlBytes, imgRootPath);
	}

	/**
	 * `html` 转 `Jpg`
	 *
	 * @param htmlBytes html字节码
	 * @return 图片字节码数据
	 * ya
	 * @since 2021/8/11 9:28
	 */
	public static byte[] htmlBytes2JpgBytes(byte[] htmlBytes) throws ParserConfigurationException, IOException, SAXException {
		return Html2ImgUtil.htmlBytes2JpgBytes(htmlBytes);
	}

	/**
	 * `pdf` 转 `word`
	 *
	 * @param pdfPath:  pdf文件路径
	 * @param wordPath: word输出路径
	 * @return void
	 * @since 2021/1/28 9:56
	 */
	public static void pdf2Word(String pdfPath, String wordPath) {
		new Pdf2WordUtil().pdf2Word(pdfPath, wordPath);
	}

	/**
	 * `pdf` 转 `png`
	 *
	 * @param pdfBytes: pdf字节码
	 * @return 转换后的png字节码
	 * @since 2021/1/28 9:56
	 */
	public static List<byte[]> pdfBytes2PngBytes(byte[] pdfBytes) throws IOException {
		return new Pdf2PngUtil().pdf2Png(pdfBytes);
	}

	/**
	 * `pdf` 转 `png`
	 *
	 * @param pdfBytes:    pdf字节码
	 * @param imgRootPath: 需转换的`png`文件路径
	 * @return png文件列表
	 * @since 2021/1/28 9:56
	 */
	public static List<File> pdfBytes2PngFileList(byte[] pdfBytes, String imgRootPath) throws IOException {
		return new Pdf2PngUtil().pdf2Png(pdfBytes, imgRootPath);
	}

}
