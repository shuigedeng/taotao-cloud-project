package com.taotao.cloud.office.convert.html2img;

import org.w3c.dom.Document;
import org.xhtmlrenderer.swing.Java2DRenderer;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * <p>
 * html 转 图片 工具类 （core-renderer-R8）
 * </p>
 * <p>
 * ya
 *
 * @description 可参考： https://flyingsaucerproject.github.io/flyingsaucer/r8/guide/users-guide-R8.html
 * @since 2021/8/11 9:15
 */
public class Html2ImgUtil {

	/**
	 * `html` 转 `jpg` (注：宽高注意设置，否则图片显示内容不完整)
	 *
	 * @param htmlBytes: html字节码
	 * @return 图片字节码数据
	 * @since 2020/11/25 17:17
	 */
	public static byte[] htmlBytes2JpgBytes(byte[] htmlBytes) throws ParserConfigurationException, IOException, SAXException {
		ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(htmlBytes);
		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
		Document document = documentBuilder.parse(byteArrayInputStream);
		Java2DRenderer renderer = new Java2DRenderer(document, 800, 1000);
		BufferedImage bufferedImage = renderer.getImage();
		ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		ImageIO.write(bufferedImage, "jpg", byteArrayOutputStream);
		return byteArrayOutputStream.toByteArray();
	}

}
