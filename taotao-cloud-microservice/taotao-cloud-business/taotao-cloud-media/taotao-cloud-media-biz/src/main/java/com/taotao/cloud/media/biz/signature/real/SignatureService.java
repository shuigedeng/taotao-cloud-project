package com.taotao.cloud.media.biz.signature.real;

import com.taotao.cloud.media.biz.signature.DigitalSignatureService;
import com.taotao.cloud.media.biz.signature.WatermarkService;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import javax.imageio.ImageIO;

public class SignatureService {

	private final WatermarkService watermarkService;
	private final DigitalSignatureService digitalSignatureService;

	public SignatureService(WatermarkService watermarkService,
		DigitalSignatureService digitalSignatureService) {
		this.watermarkService = watermarkService;
		this.digitalSignatureService = digitalSignatureService;
	}

	// 生成带防伪特征的电子印章并数字签名
	public void generateSeal(String text) throws IOException, NoSuchAlgorithmException, Exception {
		int width = 200;
		int height = 200;

		// 创建一个空白的BufferedImage对象
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		// 设置白色背景
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);

		// 设置印章的基本样式
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(5));
		g2d.drawOval(10, 10, width - 20, height - 20);

		// 添加印章文本
		g2d.setFont(new Font("Serif", Font.BOLD, 20));
		g2d.drawString(text, width / 2 - g2d.getFontMetrics().stringWidth(text) / 2, height / 2);

		// 调用方法添加防伪特征
		addAntiForgeryFeature(g2d, width, height, bufferedImage);

		// 数字签名
		byte[] imageData = ((DataBufferByte) bufferedImage.getData().getDataBuffer()).getData();
		String signature = digitalSignatureService.signData(imageData);
	}

	public void generateSeal(String text) throws IOException, NoSuchAlgorithmException, Exception {
		int width = 200;
		int height = 200;

		// 创建一个空白的BufferedImage对象
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g2d = bufferedImage.createGraphics();

		// 设置白色背景
		g2d.setColor(Color.WHITE);
		g2d.fillRect(0, 0, width, height);

		// 设置印章的基本样式
		g2d.setColor(Color.RED);
		g2d.setStroke(new BasicStroke(5));
		g2d.drawOval(10, 10, width - 20, height - 20);

		// 添加印章文本
		g2d.setFont(new Font("Serif", Font.BOLD, 20));
		g2d.drawString(text, width / 2 - g2d.getFontMetrics().stringWidth(text) / 2, height / 2);

		// 调用方法添加防伪特征
		addAntiForgeryFeature(g2d, width, height, bufferedImage);

		// 释放资源
		g2d.dispose();

		// 数字签名
		// 将BufferedImage转换为字节数组进行签名
		byte[] imageData = convertBufferedImageToByteArray(bufferedImage);
		String signature = digitalSignatureService.signData(imageData);

		// 保存图片到本地
		ImageIO.write(bufferedImage, "png", new File("seal.png"));

		// 保存签名到本地（与图像文件同名但后缀为.sig）
		try (OutputStream os = new FileOutputStream("seal.sig")) {
			os.write(signature.getBytes(StandardCharsets.UTF_8));
		}

		System.out.println("印章生成完毕，并已进行数字签名，签名数据如下：");
		System.out.println(signature);
	}

	// 转换BufferedImage为字节数组方法
	private byte[] convertBufferedImageToByteArray(BufferedImage image) throws IOException {
		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			ImageIO.write(image, "png", baos);
			return baos.toByteArray();
		}
	}
}
