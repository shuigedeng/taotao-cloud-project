package com.taotao.cloud.common.image.combiner.painter;


import com.taotao.cloud.common.image.combiner.element.CombineElement;
import com.taotao.cloud.common.image.combiner.element.ImageElement;
import com.taotao.cloud.common.image.combiner.enums.Direction;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * 图片绘制器
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:15:03
 */
public class ImagePainter implements IPainter {

    @Override
    public void draw(Graphics2D g, CombineElement element, int canvasWidth) throws Exception {

        //强制转成子类
        ImageElement imageElement = (ImageElement) element;

        //读取元素图
        BufferedImage image = imageElement.getImage();

        //计算宽高
        int width = 0;
        int height = 0;

        switch (imageElement.getZoomMode()) {
            case Origin:
                width = image.getWidth();
                height = image.getHeight();
                break;
            case Width:
                width = imageElement.getWidth();
                height = image.getHeight() * width / image.getWidth();
                break;
            case Height:
                height = imageElement.getHeight();
                width = image.getWidth() * height / image.getHeight();
                break;
            case WidthHeight:
                height = imageElement.getHeight();
                width = imageElement.getWidth();
                break;
        }

        //设置圆角
        if (imageElement.getRoundCorner() != null) {
            image = this.makeRoundCorner(image, width, height, imageElement.getRoundCorner());
        }

        //高斯模糊
        if (imageElement.getBlur() != null) {
            image = this.makeBlur(image, imageElement.getBlur());
        }

        //是否居中（优先）和绘制方向
        if (imageElement.isCenter()) {
            int centerX = (canvasWidth - width) / 2;
            imageElement.setX(centerX);
        } else if (imageElement.getDirection() == Direction.RightLeft) {
            imageElement.setX(imageElement.getX() - width);
        } else if (imageElement.getDirection() == Direction.CenterLeftRight) {
            imageElement.setX(imageElement.getX() - width / 2);
        }

        //旋转
        if (imageElement.getRotate() != null) {
            g.rotate(Math.toRadians(imageElement.getRotate()), imageElement.getX() + imageElement.getWidth() / 2, imageElement.getY() + imageElement.getHeight() / 2);
        }

        //设置透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, imageElement.getAlpha()));

        //将元素图绘制到画布
        g.drawImage(image, imageElement.getX(), imageElement.getY(), width, height, null);

        //绘制完后反向旋转，以免影响后续元素
        if (imageElement.getRotate() != null) {
            g.rotate(-Math.toRadians(imageElement.getRotate()), imageElement.getX() + imageElement.getWidth() / 2, imageElement.getY() + imageElement.getHeight() / 2);
        }
    }

    /**
     * 高斯模糊（毛玻璃效果）
     *
     * @param srcImage
     * @param radius
     * @return
     */
    private BufferedImage makeBlur(BufferedImage srcImage, int radius) {

        if (radius < 1) {
            return srcImage;
        }

        int w = srcImage.getWidth();
        int h = srcImage.getHeight();

        int[] pix = new int[w * h];
        srcImage.getRGB(0, 0, w, h, pix, 0, w);

        int wm = w - 1;
        int hm = h - 1;
        int wh = w * h;
        int div = radius + radius + 1;

        int r[] = new int[wh];
        int g[] = new int[wh];
        int b[] = new int[wh];
        int rsum, gsum, bsum, x, y, i, p, yp, yi, yw;
        int vmin[] = new int[Math.max(w, h)];

        int divsum = (div + 1) >> 1;
        divsum *= divsum;
        int dv[] = new int[256 * divsum];
        for (i = 0; i < 256 * divsum; i++) {
            dv[i] = (i / divsum);
        }

        yw = yi = 0;

        int[][] stack = new int[div][3];
        int stackpointer;
        int stackstart;
        int[] sir;
        int rbs;
        int r1 = radius + 1;
        int routsum, goutsum, boutsum;
        int rinsum, ginsum, binsum;

        for (y = 0; y < h; y++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            for (i = -radius; i <= radius; i++) {
                p = pix[yi + Math.min(wm, Math.max(i, 0))];
                sir = stack[i + radius];
                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);
                rbs = r1 - Math.abs(i);
                rsum += sir[0] * rbs;
                gsum += sir[1] * rbs;
                bsum += sir[2] * rbs;
                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }
            }
            stackpointer = radius;

            for (x = 0; x < w; x++) {

                r[yi] = dv[rsum];
                g[yi] = dv[gsum];
                b[yi] = dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (y == 0) {
                    vmin[x] = Math.min(x + radius + 1, wm);
                }
                p = pix[yw + vmin[x]];

                sir[0] = (p & 0xff0000) >> 16;
                sir[1] = (p & 0x00ff00) >> 8;
                sir[2] = (p & 0x0000ff);

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[(stackpointer) % div];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi++;
            }
            yw += w;
        }
        for (x = 0; x < w; x++) {
            rinsum = ginsum = binsum = routsum = goutsum = boutsum = rsum = gsum = bsum = 0;
            yp = -radius * w;
            for (i = -radius; i <= radius; i++) {
                yi = Math.max(0, yp) + x;

                sir = stack[i + radius];

                sir[0] = r[yi];
                sir[1] = g[yi];
                sir[2] = b[yi];

                rbs = r1 - Math.abs(i);

                rsum += r[yi] * rbs;
                gsum += g[yi] * rbs;
                bsum += b[yi] * rbs;

                if (i > 0) {
                    rinsum += sir[0];
                    ginsum += sir[1];
                    binsum += sir[2];
                } else {
                    routsum += sir[0];
                    goutsum += sir[1];
                    boutsum += sir[2];
                }

                if (i < hm) {
                    yp += w;
                }
            }
            yi = x;
            stackpointer = radius;
            for (y = 0; y < h; y++) {
                pix[yi] = (0xff000000 & pix[yi]) | (dv[rsum] << 16) | (dv[gsum] << 8) | dv[bsum];

                rsum -= routsum;
                gsum -= goutsum;
                bsum -= boutsum;

                stackstart = stackpointer - radius + div;
                sir = stack[stackstart % div];

                routsum -= sir[0];
                goutsum -= sir[1];
                boutsum -= sir[2];

                if (x == 0) {
                    vmin[y] = Math.min(y + r1, hm) * w;
                }
                p = x + vmin[y];

                sir[0] = r[p];
                sir[1] = g[p];
                sir[2] = b[p];

                rinsum += sir[0];
                ginsum += sir[1];
                binsum += sir[2];

                rsum += rinsum;
                gsum += ginsum;
                bsum += binsum;

                stackpointer = (stackpointer + 1) % div;
                sir = stack[stackpointer];

                routsum += sir[0];
                goutsum += sir[1];
                boutsum += sir[2];

                rinsum -= sir[0];
                ginsum -= sir[1];
                binsum -= sir[2];

                yi += w;
            }
        }

        srcImage.setRGB(0, 0, w, h, pix, 0, w);
        return srcImage;
    }

    /**
     * 圆角
     *
     * @param srcImage
     * @param width
     * @param height
     * @param radius
     * @return
     */
    private BufferedImage makeRoundCorner(BufferedImage srcImage, int width, int height, int radius) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRoundRect(0, 0, width, height, radius, radius);
        g.setComposite(AlphaComposite.SrcIn);
        g.drawImage(srcImage, 0, 0, width, height, null);
        g.dispose();
        return image;
    }
}

