package com.taotao.cloud.sys.biz.support.image;


import com.taotao.cloud.sys.biz.support.image.element.CombineElement;
import com.taotao.cloud.sys.biz.support.image.element.ImageElement;
import com.taotao.cloud.sys.biz.support.image.element.RectangleElement;
import com.taotao.cloud.sys.biz.support.image.element.TextElement;
import com.taotao.cloud.sys.biz.support.image.enums.OutputFormat;
import com.taotao.cloud.sys.biz.support.image.enums.ZoomMode;
import com.taotao.cloud.sys.biz.support.image.painter.IPainter;
import com.taotao.cloud.sys.biz.support.image.painter.PainterFactory;
import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;

/**
 * ImageCombiner
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:14:12
 */
public class ImageCombiner {
    private List<CombineElement> combineElements = new ArrayList<>();   //待绘制的元素集合
    private BufferedImage combinedImage;                                //合成后的图片对象
    private int canvasWidth;                                            //画布宽度
    private int canvasHeight;                                           //画布高度
    private OutputFormat outputFormat;                                  //输出图片格式
    private Integer roundCorner;                                        //画布圆角（针对整图）

    /***************** 构造函数 *******************/

    /**
     * @param canvasWidth  画布宽
     * @param canvasHeight 画布高
     * @param outputFormat 输出图片格式
     */
    public ImageCombiner(int canvasWidth, int canvasHeight, OutputFormat outputFormat) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.outputFormat = outputFormat;
    }

    /**
     * @param canvasWidth  画布宽
     * @param canvasHeight 画布高
     * @param bgColor      画布颜色（如果需要透明背景，不要设这个参数，比方图片边缘是圆角的场景）
     * @param outputFormat 输出图片格式
     */
    public ImageCombiner(int canvasWidth, int canvasHeight, Color bgColor, OutputFormat outputFormat) {
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.outputFormat = outputFormat;
        RectangleElement bgElement = new RectangleElement(0, 0, canvasWidth, canvasHeight);
        bgElement.setColor(bgColor);
        this.combineElements.add(bgElement);
    }

    /**
     * @param bgImageUrl   背景图片地址（画布以背景图宽高为基准）
     * @param outputFormat 输出图片格式
     * @throws Exception
     */
    public ImageCombiner(String bgImageUrl, OutputFormat outputFormat) throws Exception {
        this(ImageIO.read(new URL(bgImageUrl)), outputFormat);
    }

    /**
     * @param bgImage      背景图片对象（画布以背景图宽高为基准）
     * @param outputFormat 输出图片格式
     * @throws Exception
     */
    public ImageCombiner(BufferedImage bgImage, OutputFormat outputFormat) {
        ImageElement bgImageElement = new ImageElement(bgImage, 0, 0);
        this.combineElements.add(bgImageElement);
        this.canvasWidth = bgImage.getWidth();
        this.canvasHeight = bgImage.getHeight();
        this.outputFormat = outputFormat;
    }

    /**
     * @param bgImageUrl   背景图片地址
     * @param width        背景图宽度
     * @param height       背景图高度
     * @param zoomMode     缩放模式
     * @param outputFormat 输出图片格式
     * @throws Exception
     */
    public ImageCombiner(String bgImageUrl, int width, int height, ZoomMode zoomMode, OutputFormat outputFormat) throws Exception {
        this(ImageIO.read(new URL(bgImageUrl)), width, height, zoomMode, outputFormat);
    }

    /**
     * @param bgImage      背景图片对象
     * @param width        背景图宽度
     * @param height       背景图高度
     * @param zoomMode     缩放模式
     * @param outputFormat 输出图片格式
     */
    public ImageCombiner(BufferedImage bgImage, int width, int height, ZoomMode zoomMode, OutputFormat outputFormat) {
        ImageElement bgImageElement = new ImageElement(bgImage, 0, 0, width, height, zoomMode);

        //计算画布新宽高
        int canvasWidth = 0;
        int canvasHeight = 0;

        switch (zoomMode) {
            case Origin:
                canvasWidth = bgImage.getWidth();
                canvasHeight = bgImage.getHeight();
                break;
            case Width:
                canvasWidth = width;
                canvasHeight = bgImage.getHeight() * canvasWidth / bgImage.getWidth();
                break;
            case Height:
                canvasHeight = height;
                canvasWidth = bgImage.getWidth() * canvasHeight / bgImage.getHeight();
                break;
            case WidthHeight:
                canvasHeight = width;
                canvasWidth = height;
                break;
        }

        this.combineElements.add(bgImageElement);
        this.canvasWidth = canvasWidth;
        this.canvasHeight = canvasHeight;
        this.outputFormat = outputFormat;
    }


    /***************** 公共方法 *******************/

    /**
     * 合成图片，返回图片对象
     *
     * @throws Exception
     */
    public BufferedImage combine() throws Exception {
        combinedImage = new BufferedImage(canvasWidth, canvasHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = combinedImage.createGraphics();

        //PNG要做透明度处理，否则背景图透明部分会变黑
        if (outputFormat == OutputFormat.PNG) {
            combinedImage = g.getDeviceConfiguration().createCompatibleImage(canvasWidth, canvasHeight, Transparency.TRANSLUCENT);
            g = combinedImage.createGraphics();
        }
        //抗锯齿
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.white);

        //循环绘制
        for (CombineElement element : combineElements) {
            IPainter painter = PainterFactory.createInstance(element);
            painter.draw(g, element, canvasWidth);
        }
        g.dispose();

        //处理整图圆角
        if (roundCorner != null) {
            combinedImage = this.makeRoundCorner(combinedImage, canvasWidth, canvasHeight, roundCorner);
        }

        return combinedImage;
    }

    /**
     * 设置背景高斯模糊（毛玻璃效果）
     *
     * @return
     */
    public void setBackgroundBlur(int blur) {
        ImageElement bgElement = (ImageElement) combineElements.get(0);
        bgElement.setBlur(blur);
    }

    /**
     * 设置画布圆角（针对整图）
     * @param roundCorner
     */
    public void setCanvasRoundCorner(Integer roundCorner) throws Exception {
        if (outputFormat != OutputFormat.PNG){
            throw new Exception("整图圆角，输出格式必须设置为PNG");
        }
        this.roundCorner = roundCorner;
    }

    /**
     * 获取画布宽度
     * @return
     */
    public int getCanvasWidth() {
        return canvasWidth;
    }

    /**
     * 获取画布高度
     * @return
     */
    public int getCanvasHeight() {
        return canvasHeight;
    }

    /***************** 对象输出相关方法 *******************/

    /**
     * 获取合成后的图片对象
     *
     * @return
     */
    public BufferedImage getCombinedImage() {
        return combinedImage;
    }

    /**
     * 获取合成后的图片流
     *
     * @return
     * @throws Exception
     */
    public InputStream getCombinedImageStream() throws Exception {
        if (combinedImage != null) {
            try (ByteArrayOutputStream os = new ByteArrayOutputStream()) {
                ImageIO.write(combinedImage, outputFormat.getName(), os);
                return new ByteArrayInputStream(os.toByteArray());
            } catch (Exception e) {
                throw new Exception("执行图片合成失败，无法输出文件流");
            }
        } else {
            throw new Exception("尚未执行图片合成，无法输出文件流");
        }
    }

    /**
     * 保存合成后的图片
     *
     * @param filePath 完整保存路径，如 “d://123.jpg”
     * @throws IOException
     */
    public void save(String filePath) throws Exception {
        if (combinedImage != null) {
            ImageIO.write(combinedImage, outputFormat.getName(), new File(filePath));
        } else {
            throw new Exception("尚未执行图片合成，无法保存文件");
        }
    }


    /***************** 创建和添加元素的方法 *******************/

    /**
     * 添加元素（图片或文本）
     *
     * @param element 图片或文本元素
     */
    public void addElement(CombineElement element) {
        this.combineElements.add(element);
    }

    /**
     * 添加图片元素
     *
     * @param imgUrl 图片url
     * @param x      x坐标
     * @param y      y坐标
     * @return
     */
    public ImageElement addImageElement(String imgUrl, int x, int y) throws Exception {
        ImageElement imageElement = new ImageElement(imgUrl, x, y);
        this.combineElements.add(imageElement);
        return imageElement;
    }

    /**
     * 添加图片元素
     *
     * @param image 图片对象
     * @param x     x坐标
     * @param y     y坐标
     * @return
     */
    public ImageElement addImageElement(BufferedImage image, int x, int y) {
        ImageElement imageElement = new ImageElement(image, x, y);
        this.combineElements.add(imageElement);
        return imageElement;
    }

    /**
     * 添加图片元素
     *
     * @param imgUrl   图片rul
     * @param x        x坐标
     * @param y        y坐标
     * @param width    宽度
     * @param height   高度
     * @param zoomMode 缩放模式
     * @return
     */
    public ImageElement addImageElement(String imgUrl, int x, int y, int width, int height, ZoomMode zoomMode) {
        ImageElement imageElement = new ImageElement(imgUrl, x, y, width, height, zoomMode);
        this.combineElements.add(imageElement);
        return imageElement;
    }

    /**
     * 添加图片元素
     *
     * @param image    图片对象
     * @param x        x坐标
     * @param y        y坐标
     * @param width    宽度
     * @param height   高度
     * @param zoomMode 缩放模式
     * @return
     */
    public ImageElement addImageElement(BufferedImage image, int x, int y, int width, int height, ZoomMode zoomMode) {
        ImageElement imageElement = new ImageElement(image, x, y, width, height, zoomMode);
        this.combineElements.add(imageElement);
        return imageElement;
    }

    /**
     * 添加文本元素
     *
     * @param text 文本
     * @param font Font对象
     * @param x    x坐标
     * @param y    y坐标
     * @return
     */
    public TextElement addTextElement(String text, Font font, int x, int y) {
        TextElement textElement = new TextElement(text, font, x, y);
        this.combineElements.add(textElement);
        return textElement;
    }

    /**
     * 添加文本元素
     *
     * @param text     文本
     * @param fontSize 字体大小
     * @param x        x坐标
     * @param y        y坐标
     * @return
     */
    public TextElement addTextElement(String text, int fontSize, int x, int y) {
        TextElement textElement = new TextElement(text, fontSize, x, y);
        this.combineElements.add(textElement);
        return textElement;
    }

    /**
     * 添加文本元素
     *
     * @param text     文本
     * @param fontName 字体名称
     * @param fontSize 字体大小
     * @param x        x坐标
     * @param y        y坐标
     * @return
     */
    public TextElement addTextElement(String text, String fontName, int fontSize, int x, int y) {
        TextElement textElement = new TextElement(text, fontName, fontSize, x, y);
        this.combineElements.add(textElement);
        return textElement;
    }

    /**
     * 添加矩形元素
     *
     * @param x      x坐标
     * @param y      y坐标
     * @param width  宽度
     * @param height 高度
     * @return
     */
    public RectangleElement addRectangleElement(int x, int y, int width, int height) {
        RectangleElement rectangleElement = new RectangleElement(x, y, width, height);
        this.combineElements.add(rectangleElement);
        return rectangleElement;
    }

    /***************** 私有方法 *******************/
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
