package com.taotao.cloud.sys.biz.support.image.element;


import com.taotao.cloud.sys.biz.support.image.enums.ZoomMode;
import java.awt.image.BufferedImage;
import java.net.URL;
import javax.imageio.ImageIO;

/**
 * ImageElement 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:37:46
 */
public class ImageElement extends CombineElement<ImageElement> {

    private BufferedImage image;            //图片对象
    private String imgUrl;                  //图片地址
    private Integer width;                  //绘制宽度
    private Integer height;                 //绘制高度
    private Integer roundCorner;            //圆角大小
    private ZoomMode zoomMode;              //缩放模式
    private Integer rotate;                 //旋转角度
    private Integer blur;                   //高斯模糊（毛玻璃）

    /**
     * @param imgUrl 图片url
     * @param x      x坐标
     * @param y      y坐标
     */
    public ImageElement(String imgUrl, int x, int y) throws Exception {
        this.imgUrl = imgUrl;
        this.width = getImage().getWidth();     //事先获得宽高，后面计算要用
        this.height = getImage().getHeight();
        this.zoomMode = ZoomMode.Origin;
        super.setX(x);
        super.setY(y);
    }

    /**
     * @param image 图片对象
     * @param x     x坐标
     * @param y     y坐标
     */
    public ImageElement(BufferedImage image, int x, int y) {
        this.image = image;
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.zoomMode = ZoomMode.Origin;
        super.setX(x);
        super.setY(y);
    }

    /**
     * @param imgUrl   图片url
     * @param x        x坐标
     * @param y        y坐标
     * @param width    宽度
     * @param height   高度
     * @param zoomMode 缩放模式
     */
    public ImageElement(String imgUrl, int x, int y, int width, int height, ZoomMode zoomMode) {
        this.imgUrl = imgUrl;
        this.width = width;
        this.height = height;
        this.zoomMode = zoomMode;
        super.setX(x);
        super.setY(y);
    }

    /**
     * @param image    图片对象
     * @param x        x坐标
     * @param y        y坐标
     * @param width    宽度
     * @param height   高度
     * @param zoomMode 缩放模式
     */
    public ImageElement(BufferedImage image, int x, int y, int width, int height, ZoomMode zoomMode) {
        this.image = image;
        this.width = width;
        this.height = height;
        this.zoomMode = zoomMode;
        super.setX(x);
        super.setY(y);
    }


    public BufferedImage getImage() throws Exception {
        if (this.image == null) {
            try {
                this.image = ImageIO.read(new URL(this.imgUrl));
            } catch (Exception e) {
                throw e;
            }
        }
        return image;
    }

    public ImageElement setImage(BufferedImage image) {
        this.image = image;
        return this;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public ImageElement setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
        return this;
    }

    public Integer getWidth() {
        return width;
    }

    public ImageElement setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public ImageElement setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Integer getRoundCorner() {
        return roundCorner;
    }

    public ImageElement setRoundCorner(Integer roundCorner) {
        this.roundCorner = roundCorner;
        return this;
    }

    public ZoomMode getZoomMode() {
        return zoomMode;
    }

    public ImageElement setZoomMode(ZoomMode zoomMode) {
        this.zoomMode = zoomMode;
        return this;
    }

    public Integer getRotate() {
        return rotate;
    }

    public ImageElement setRotate(Integer rotate) {
        this.rotate = rotate;
        return this;
    }

    public Integer getBlur() {
        return blur;
    }

    public void setBlur(Integer blur) {
        this.blur = blur;
    }
}
