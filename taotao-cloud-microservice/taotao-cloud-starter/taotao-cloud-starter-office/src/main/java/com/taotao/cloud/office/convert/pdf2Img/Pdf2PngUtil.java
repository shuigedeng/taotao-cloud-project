package com.taotao.cloud.office.convert.pdf2Img;

import com.google.common.collect.Lists;
import com.taotao.cloud.office.convert.util.MyFileUtil;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * <p>
 * Pdf 转 PNG 工具类
 * </p>
 * <p>
 * ya
 *
 * @description
 * @since 2021/3/19 16:14
 */
public class Pdf2PngUtil {

	/**
	 * dpi越大转换后越清晰，相对转换速度越慢
	 */
	private static final Integer DPI = 100;

	/**
	 * 转换后的图片类型
	 */
	private static final String IMG_TYPE = "png";

	/**
	 * `pdf` 转 `png`
	 *
	 * @param pdfBytes: pdf字节码
	 * @return 转换后的png字节码
	 * ya
	 * @since 2021/3/22 9:42
	 */
	public List<byte[]> pdf2Png(byte[] pdfBytes) throws IOException {
		List<byte[]> result = new ArrayList<>();
		try (PDDocument document = PDDocument.load(pdfBytes)) {
			PDFRenderer renderer = new PDFRenderer(document);
			for (int i = 0; i < document.getNumberOfPages(); ++i) {
				BufferedImage bufferedImage = renderer.renderImageWithDPI(i, DPI);
				ByteArrayOutputStream out = new ByteArrayOutputStream();
				ImageIO.write(bufferedImage, IMG_TYPE, out);
				result.add(out.toByteArray());
			}
		}
		return result;
	}

	/**
	 * `pdf` 转 `png`
	 *
	 * @param pdfBytes:    pdf字节码
	 * @param imgRootPath: 需转换的`png`文件路径
	 * @return 图片文件数据列表
	 * ya
	 * @since 2021/3/19 16:16
	 */
	public List<File> pdf2Png(byte[] pdfBytes, String imgRootPath) throws IOException {
		List<byte[]> pngBytesList = this.pdf2Png(pdfBytes);
		List<File> pngFileList = Lists.newArrayList();
		for (int i = 0; i < pngBytesList.size(); i++) {
			String imgPath = imgRootPath + "/" + (i + 1) + ".png";
			File pngFile = MyFileUtil.writeFileContent(pngBytesList.get(i), imgPath);
			pngFileList.add(pngFile);
		}
		return pngFileList;
	}

}
