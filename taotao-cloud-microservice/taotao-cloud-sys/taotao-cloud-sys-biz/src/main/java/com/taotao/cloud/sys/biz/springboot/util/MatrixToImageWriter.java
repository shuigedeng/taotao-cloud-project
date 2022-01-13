package com.taotao.cloud.sys.biz.springboot.util;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Hashtable;

import javax.imageio.ImageIO;

import org.springframework.core.io.ClassPathResource;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/*** 二维码生成工具@author https://gitee.com/YYDeament/88ybg
 * 
 * @date 2016/10/1 */
public class MatrixToImageWriter {
	
	private static final int	BLACK	= 0xFF000000;
	private static final int	WHITE	= 0xFFFFFFFF;
	/*** 边框 **/
	private static final int	MARGIN	= 1;
	private static final String	FORMAT	= "png";
	
	private MatrixToImageWriter() {
	}
	
	public static void createRqCode(String textOrUrl, int width, int height, OutputStream toStream) throws WriterException, IOException {
		Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
		// 内容所使用字符集编码
		hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
		hints.put(EncodeHintType.MARGIN, new Integer(MARGIN));
		BitMatrix bitMatrix = new MultiFormatWriter().encode(textOrUrl, BarcodeFormat.QR_CODE, width, height, hints);
		BufferedImage image = toBufferedImage(bitMatrix);
		// 应用LOGO
		applyLogo(image);
		writeToStream(image, FORMAT, toStream);
	}
	
	private static void applyLogo(BufferedImage image) throws IOException {
		Graphics2D gs = image.createGraphics();
		// logo图片
		ClassPathResource resource = new ClassPathResource("logo.png");
		// 载入logo
		Image img = ImageIO.read(resource.getFile());
		int left = image.getWidth() / 2 - img.getWidth(null) / 2;
		int top = image.getHeight() / 2 - img.getHeight(null) / 2;
		gs.drawImage(img, left, top, null);
		gs.dispose();
		img.flush();
	}
	
	private static BufferedImage toBufferedImage(BitMatrix matrix) {
		int width = matrix.getWidth();
		int height = matrix.getHeight();
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
			}
		}
		return image;
	}
	
	public static void writeToFile(BufferedImage image, String format, File file) throws IOException {
		if (!ImageIO.write(image, format, file)) {
			throw new IOException("Could not write an image of format " + format + " to " + file);
		}
	}
	
	public static void writeToStream(BufferedImage image, String format, OutputStream stream) throws IOException {
		if (!ImageIO.write(image, format, stream)) {
			throw new IOException("Could not write an image of format " + format);
		}
	}
}
