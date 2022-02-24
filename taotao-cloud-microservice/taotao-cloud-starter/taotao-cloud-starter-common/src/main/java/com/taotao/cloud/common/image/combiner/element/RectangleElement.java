package com.taotao.cloud.common.image.combiner.element;


import com.taotao.cloud.common.image.combiner.enums.GradientDirection;
import java.awt.*;

/**
 * RectangleElement 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:16:25
 */
public class RectangleElement extends CombineElement<RectangleElement> {
    private Integer width;                  //绘制宽度
    private Integer height;                 //绘制高度
    private Integer roundCorner = 0;        //圆角大小
    private Color color = new Color(255, 255, 255);   //颜色，默认白色

    //渐变相关属性
    private Color fromColor;                        //开始颜色
    private Color toColor;                          //结束颜色
    private Integer fromExtend = 0;                 //开始位置延长（反向，影响渐变效果）
    private Integer toExtend = 0;                   //结束位置延长（正向，影响渐变效果）
    private GradientDirection gradientDirection;    //渐变方向

    /**
     * @param x      x坐标
     * @param y      y坐标
     * @param width  宽度
     * @param height 高度
     */
    public RectangleElement(int x, int y, int width, int height) {
        this.width = width;
        this.height = height;
        super.setX(x);
        super.setY(y);
    }

    public Integer getWidth() {
        return width;
    }

    public RectangleElement setWidth(Integer width) {
        this.width = width;
        return this;
    }

    public Integer getHeight() {
        return height;
    }

    public RectangleElement setHeight(Integer height) {
        this.height = height;
        return this;
    }

    public Integer getRoundCorner() {
        return roundCorner;
    }

    public RectangleElement setRoundCorner(Integer roundCorner) {
        this.roundCorner = roundCorner;
        return this;
    }

    public Color getColor() {
        return color;
    }

    public RectangleElement setColor(Color color) {
        this.color = color;
        return this;
    }

    public RectangleElement setColor(int r, int g, int b) {
        return setColor(new Color(r, g, b));
    }

    /**
     * 设置渐变
     *
     * @param fromColor         开始颜色
     * @param toColor           结束颜色
     * @param gradientDirection 渐变方向
     * @return
     */
    public RectangleElement setGradient(Color fromColor, Color toColor, GradientDirection gradientDirection) {
        this.fromColor = fromColor;
        this.toColor = toColor;
        this.gradientDirection = gradientDirection;
        return this;
    }

    /**
     * 设置渐变
     *
     * @param fromColor         开始颜色
     * @param toColor           结束颜色
     * @param fromExtend        开始位置延长（影响渐变效果）
     * @param toExtend          结束位置延长（影响渐变效果）
     * @param gradientDirection 渐变方向
     * @return
     */
    public RectangleElement setGradient(Color fromColor, Color toColor, int fromExtend,int toExtend, GradientDirection gradientDirection) {
        this.fromColor = fromColor;
        this.toColor = toColor;
        this.fromExtend = fromExtend;
        this.toExtend = toExtend;
        this.gradientDirection = gradientDirection;
        return this;
    }

    public Color getFromColor() {
        return fromColor;
    }

    public Color getToColor() {
        return toColor;
    }

    public Integer getFromExtend() {
        return fromExtend;
    }

    public Integer getToExtend() {
        return toExtend;
    }

    public GradientDirection getGradientDirection() {
        return gradientDirection;
    }
}
