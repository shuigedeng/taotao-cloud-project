package com.taotao.cloud.office.convert.html2pdf;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * <p>
 * Html 转 Pdf 工具类
 * </p>
 *
 * @description
 * @since 2020/11/24 11:23
 */
public class Html2PdfUtil {

	/**
	 * `html` 转 `pdf`
	 *
	 * @param htmlBytes: html字节码
	 * @return 生成的`pdf`字节码
	 * @since 2020/11/24 11:26
	 */
	public static byte[] htmlBytes2PdfBytes(byte[] htmlBytes) throws Exception {
		Document document = new Document(new ByteArrayInputStream(htmlBytes));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		document.save(outputStream, SaveFormat.PDF);
		// 返回生成的`pdf`字节码
		return outputStream.toByteArray();
	}

	/**
	 * `html` 转 `pdf`
	 *
	 * @param htmlBytes:   html字节码
	 * @param pdfFilePath: 需转换的`pdf`文件路径
	 * @return 生成的`pdf`文件数据
	 * @since 2020/11/24 11:26
	 */
	public static File htmlBytes2PdfFile(byte[] htmlBytes, String pdfFilePath) throws Exception {
		Document document = new Document(new ByteArrayInputStream(htmlBytes));
		document.save(pdfFilePath, SaveFormat.PDF);
		return new File(pdfFilePath);
	}

}
