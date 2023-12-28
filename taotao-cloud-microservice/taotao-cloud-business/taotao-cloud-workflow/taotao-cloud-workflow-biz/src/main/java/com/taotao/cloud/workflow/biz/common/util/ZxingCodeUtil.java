/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.workflow.biz.common.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import javax.imageio.ImageIO;

/** */
public class ZxingCodeUtil {

    // -----------------------------------条形码-----------------------------------
    /** 条形码的颜色 */
    private static final int BLACK = 0xff000000;
    /** 背景色 */
    private static final int WHITE = 0xFFFFFFFF;

    /**
     * 生成一维码（128）
     *
     * @param message 内容
     * @param width 宽度
     * @param height 高度
     * @return
     */
    public static BufferedImage getBarcode(String message, int width, int height) {
        try {
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "code");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(message, BarcodeFormat.CODE_128, width, height, hints);
            return toBufferedImage(bitMatrix);
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return null;
    }

    /**
     * 转换成图片
     *
     * @param matrix
     * @return
     */
    private static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    // --------------------------二维码-----------------------------------
    /** 二维码颜色 */
    private static final int QRCOLOR = 0xFF000000;
    /** 背景色 */
    private static final int BGCOLOR = 0xFFFFFFFF;

    /**
     * 生成普通的二维码
     *
     * @param message 二维码内容
     * @param width 宽度
     * @param height 高度
     * @return
     */
    public static BufferedImage createCode(String message, int width, int height) {
        MultiFormatWriter multiFormatWriter = null;
        BitMatrix bm = null;
        BufferedImage image = null;
        Map<EncodeHintType, Object> hints = getDecodeHintType();
        try {
            multiFormatWriter = new MultiFormatWriter();
            // 参数顺序分别为：编码内容，编码类型，生成图片宽度，生成图片高度，设置参数
            bm = multiFormatWriter.encode(message, BarcodeFormat.QR_CODE, width, height, hints);
            int w = bm.getWidth();
            int h = bm.getHeight();
            image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
            // 开始利用二维码数据创建Bitmap图片，分别设为黑（0xFFFFFFFF）白（0xFF000000）两色
            for (int x = 0; x < w; x++) {
                for (int y = 0; y < h; y++) {
                    image.setRGB(x, y, bm.get(x, y) ? QRCOLOR : BGCOLOR);
                }
            }
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return image;
    }

    /**
     * 生成带logo的二维码
     *
     * @param message 二维码内容
     * @param logoPath log路径
     * @param width 宽度
     * @param height 高度
     * @return
     */
    public static BufferedImage createCodeWithLogo(String message, String logoPath, int width, int height) {
        BufferedImage bim = createCode(message, width, height);
        try {
            // 读取二维码图片，并构建绘图对象
            BufferedImage image = bim;
            Graphics2D g = image.createGraphics();
            // 读取Logo图片
            BufferedImage logo = ImageIO.read(new File(logoPath));
            // 设置logo的大小,这里设置为二维码图片的20%,过大会盖掉二维码
            int
                    widthLogo =
                            logo.getWidth(null) > image.getWidth() * 3 / 10
                                    ? (image.getWidth() * 3 / 10)
                                    : logo.getWidth(null),
                    heightLogo =
                            logo.getHeight(null) > image.getHeight() * 3 / 10
                                    ? (image.getHeight() * 3 / 10)
                                    : logo.getWidth(null);
            // logo放在中心
            int x = (image.getWidth() - widthLogo) / 2;
            int y = (image.getHeight() - heightLogo) / 2;
            // 开始绘制图片
            g.drawImage(logo, x, y, widthLogo, heightLogo, null);
            g.dispose();
            logo.flush();
            image.flush();
            return image;
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return null;
    }

    /**
     * 生成带logo和文字的二维码
     *
     * @param message 二维码内容
     * @param logoPath log路径
     * @param text 文字
     * @param width 宽度
     * @param height 高度
     * @return
     */
    public static BufferedImage createCodeWithLogoAndText(
            String message, String logoPath, String text, int width, int height) {
        BufferedImage image = createCodeWithLogo(message, logoPath, width, height);
        try {
            if (text != null && !"".equals(text)) {
                // 新的图片，把带logo的二维码下面加上文字
                BufferedImage outImage = new BufferedImage(400, 445, BufferedImage.TYPE_4BYTE_ABGR);
                Graphics2D outg = outImage.createGraphics();
                // 画二维码到新的面板
                outg.drawImage(image, 0, 0, image.getWidth(), image.getHeight(), null);
                // 画文字到新的面板
                outg.setColor(Color.BLACK);
                // 字体、字型、字号
                outg.setFont(new Font("宋体", Font.BOLD, 30));
                int strWidth = outg.getFontMetrics().stringWidth(text);
                if (strWidth > 399) {
                    String productName1 = text.substring(0, text.length() / 2);
                    String productName2 = text.substring(text.length() / 2, text.length());
                    int strWidth1 = outg.getFontMetrics().stringWidth(productName1);
                    int strWidth2 = outg.getFontMetrics().stringWidth(productName2);
                    outg.drawString(
                            productName1,
                            200 - strWidth1 / 2,
                            image.getHeight() + (outImage.getHeight() - image.getHeight()) / 2 + 12);
                    BufferedImage outImage2 = new BufferedImage(400, 485, BufferedImage.TYPE_4BYTE_ABGR);
                    Graphics2D outg2 = outImage2.createGraphics();
                    outg2.drawImage(outImage, 0, 0, outImage.getWidth(), outImage.getHeight(), null);
                    outg2.setColor(Color.BLACK);
                    // 字体、字型、字号
                    outg2.setFont(new Font("宋体", Font.BOLD, 30));
                    outg2.drawString(
                            productName2,
                            200 - strWidth2 / 2,
                            outImage.getHeight() + (outImage2.getHeight() - outImage.getHeight()) / 2 + 5);
                    outg2.dispose();
                    outImage2.flush();
                    outImage = outImage2;
                } else {
                    // 画文字
                    outg.drawString(
                            text,
                            200 - strWidth / 2,
                            image.getHeight() + (outImage.getHeight() - image.getHeight()) / 2 + 12);
                }
                outg.dispose();
                outImage.flush();
                image = outImage;
                image.flush();
            }
        } catch (Exception e) {
            LogUtils.error(e);
        }
        return image;
    }

    /**
     * 设置二维码的格式参数
     *
     * @return
     */
    private static Map<EncodeHintType, Object> getDecodeHintType() {
        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>(16);
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
        hints.put(EncodeHintType.CHARACTER_SET, Constants.UTF_8);
        hints.put(EncodeHintType.MARGIN, 0);
        hints.put(EncodeHintType.MAX_SIZE, 350);
        hints.put(EncodeHintType.MIN_SIZE, 100);
        return hints;
    }
}
