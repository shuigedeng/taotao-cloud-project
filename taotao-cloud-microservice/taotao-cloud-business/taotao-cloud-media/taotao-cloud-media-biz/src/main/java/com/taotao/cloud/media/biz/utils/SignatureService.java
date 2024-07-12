package com.taotao.cloud.media.biz.utils;

import com.taotao.cloud.media.biz.signature.WatermarkService;
import org.springframework.stereotype.Service;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
public class SignatureService {

    private final WatermarkService watermarkService;

    public SignatureService(WatermarkService watermarkService) {
        this.watermarkService = watermarkService;
    }

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
        addAntiForgeryFeature(g2d, width, height, bufferedImage);

        // 释放资源
        g2d.dispose();

        // 保存图片到本地
        ImageIO.write(bufferedImage, "png", new File("seal.png"));
    }

    // 添加防伪特征
    private void addAntiForgeryFeature(Graphics2D g2d, int width, int height, BufferedImage bufferedImage) {
        // 生成唯一标示符并添加为水印
        String uniqueID = UUID.randomUUID().toString();

        // 使用 WatermarkService 添加水印
        BufferedImage watermarkedImage = watermarkService.addWatermark(bufferedImage, uniqueID);

        // 将处理后的图像替换原图
        g2d.drawImage(watermarkedImage, 0, 0, null);
    }
}
