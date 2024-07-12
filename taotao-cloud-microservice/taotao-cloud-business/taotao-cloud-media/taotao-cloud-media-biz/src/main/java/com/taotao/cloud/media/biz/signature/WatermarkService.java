package com.taotao.cloud.media.biz.signature;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

//数字水印技术添加防伪特征
//数字水印通过在图像中嵌入难以察觉的特征标识来增强图像的防伪能力。数字水印可以包括文本、标识符甚至是公司的徽标，嵌入方式不会明显影响图像的视觉效果，但在验证时可通过特定算法提取出特征信息。
//
//数字水印的原理
//数字水印技术的一种常见实现方法是通过小波变换（DWT）、离散余弦变换（DCT）或快速傅里叶变换（FFT）等算法，将水印信息嵌入到图像的频域或时域之中。如下是一个简单的图像像素修改示例，以增加水印：
public class WatermarkService {

    // 添加数字水印
    public BufferedImage addWatermark(BufferedImage sourceImage, String watermarkText) {
        Graphics2D g2d = sourceImage.createGraphics();

        // 获取图像宽度和高度
        int width = sourceImage.getWidth();
        int height = sourceImage.getHeight();
        
        // 设置水印属性
        g2d.setFont(new Font("Serif", Font.BOLD, 20));
        g2d.setColor(new Color(255, 0, 0, 40)); // 红色，透明度为40%
        
        // 计算水印文本的宽度和高度
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int stringWidth = fontMetrics.stringWidth(watermarkText);
        int stringHeight = fontMetrics.getHeight();

        // 设置水印位置（比如在底部居中）
        int x = width / 2 - stringWidth / 2;
        int y = height - stringHeight;

        // 绘制水印
        g2d.drawString(watermarkText, x, y);

        g2d.dispose();

        return sourceImage;
    }
}
