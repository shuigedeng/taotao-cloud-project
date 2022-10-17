package com.taotao.cloud.office.convert.word2pdf;

import com.aspose.words.Document;
import com.aspose.words.SaveFormat;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * <p>
 * word 转 pdf 工具类
 * </p>
 *
 * @description
 * @since 2020/11/26 13:37
 */
public class Word2PdfUtil {

	/**
	 * `word` 转 `pdf`
	 *
	 * @param wordBytes: word字节码
	 * @return 生成的`pdf`字节码
	 * @since 2020/11/26 13:39
	 */
	public static byte[] wordBytes2PdfBytes(byte[] wordBytes) throws Exception {
		Document document = new Document(new ByteArrayInputStream(wordBytes));
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		document.save(outputStream, SaveFormat.PDF);
		// 返回生成的`pdf`字节码
		return outputStream.toByteArray();
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
		Document document = new Document(new ByteArrayInputStream(wordBytes));
		document.save(pdfFilePath, SaveFormat.PDF);
		return new File(pdfFilePath);
	}

}
