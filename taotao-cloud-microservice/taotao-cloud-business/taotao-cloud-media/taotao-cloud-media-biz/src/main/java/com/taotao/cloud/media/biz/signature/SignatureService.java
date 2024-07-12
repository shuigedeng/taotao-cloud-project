package com.taotao.cloud.media.biz.signature;

import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.util.UUID;

@Service
public class SignatureService {
	//为了实现防伪特征，我们将在生成电子印章时，通过图像处理技术添加独特的特征标示。
    // 生成带防伪特征的电子印章
    public void generateSeal(String text) throws IOException {
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
        addAntiForgeryFeature(g2d, width, height);

        // 释放资源
        g2d.dispose();

        // 保存图片到本地
        ImageIO.write(bufferedImage, "png", new File("seal.png"));
    }

    // 添加防伪特征
    private void addAntiForgeryFeature1(Graphics2D g2d, int width, int height) {
        // 生成唯一标示符
        String uniqueID = UUID.randomUUID().toString();

        // 将标示符转化为二维码或随机图形
        g2d.setColor(Color.BLACK);
        g2d.drawString(uniqueID, 20, height - 20);

        // 更复杂的图形处理逻辑可在此处添加
    }
	//使用唯一标示符添加防伪特征
	//给每个生成的电子印章添加一个唯一的标示符，如UUID。这是一个全局唯一的标示符，可以通过相应的算法生成。添加标示符的具体步骤如下：
	//
	//生成唯一标示符：使用Java中的UUID类生成一个唯一的标示符。
	//
	//标示符的图形化：我们可以将这个标示符直接以文本形式绘制到印章图像上，也可以将其转换为二维码图案并添加到印章中。
	//
	//位置选择：选择一个合适的位置绘制标示符，使得标示符不影响印章的整体视觉效果，同时不容易被故意覆盖或删除。
	private void addAntiForgeryFeature(Graphics2D g2d, int width, int height) {
		// 1. 生成唯一标示符
		String uniqueID = UUID.randomUUID().toString();

		// 2. 生成标示符的二维码图案（这里简单示例为字符串）
		// 实际应用中可以集成二维码生成库，如ZXing
		int fontSize = 10;
		g2d.setFont(new Font("Serif", Font.PLAIN, fontSize));
		int textWidth = g2d.getFontMetrics().stringWidth(uniqueID);

		// 3. 设定绘图位置，比如印章的右下角
		int x = width - textWidth - 10;
		int y = height - 10;

		// 设置防伪特征的颜色（如黑色）
		g2d.setColor(Color.BLACK);
		g2d.drawString(uniqueID, x, y);
	}
}
