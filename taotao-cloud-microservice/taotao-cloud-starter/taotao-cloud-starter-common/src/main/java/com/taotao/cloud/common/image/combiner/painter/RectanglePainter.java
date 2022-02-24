package com.taotao.cloud.common.image.combiner.painter;


import com.taotao.cloud.common.image.combiner.element.CombineElement;
import com.taotao.cloud.common.image.combiner.element.RectangleElement;
import com.taotao.cloud.common.image.combiner.enums.Direction;
import java.awt.*;

/**
 * RectanglePainter 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:15:25
 */
public class RectanglePainter implements IPainter {

    @Override
    public void draw(Graphics2D g, CombineElement element, int canvasWidth) {

        //强制转成子类
        RectangleElement rectangleElement = (RectangleElement) element;

        //设置颜色
        g.setColor(rectangleElement.getColor());

        //设置居中（优先）和绘制方向
        if (rectangleElement.isCenter()) {
            int centerX = (canvasWidth - rectangleElement.getWidth()) / 2;
            rectangleElement.setX(centerX);
        } else if (rectangleElement.getDirection() == Direction.RightLeft) {
            rectangleElement.setX(rectangleElement.getX() - rectangleElement.getWidth());
        } else if (rectangleElement.getDirection() == Direction.CenterLeftRight) {
            rectangleElement.setX(rectangleElement.getX() - rectangleElement.getWidth() / 2);
        }

        //设置渐变
        if (rectangleElement.getFromColor() != null) {
            float fromX = 0, fromY = 0, toX = 0, toY = 0;
            switch (rectangleElement.getGradientDirection()) {
                case TopBottom:
                    fromX = rectangleElement.getX() + rectangleElement.getWidth() / 2;
                    fromY = rectangleElement.getY() - rectangleElement.getFromExtend();
                    toX = fromX;
                    toY = rectangleElement.getY() + rectangleElement.getHeight() + rectangleElement.getToExtend();
                    break;
                case LeftRight:
                    fromX = rectangleElement.getX() - rectangleElement.getFromExtend();
                    fromY = rectangleElement.getY() + rectangleElement.getHeight() / 2;
                    toX = rectangleElement.getX() + rectangleElement.getWidth() + rectangleElement.getToExtend();
                    toY = fromY;
                    break;
                case LeftTopRightBottom:
                    fromX = rectangleElement.getX() - (float) Math.sqrt(rectangleElement.getFromExtend());
                    fromY = rectangleElement.getY() - (float) Math.sqrt(rectangleElement.getFromExtend());
                    toX = rectangleElement.getX() + rectangleElement.getWidth() + (float) Math.sqrt(rectangleElement.getToExtend());
                    toY = rectangleElement.getY() + rectangleElement.getHeight() + (float) Math.sqrt(rectangleElement.getToExtend());
                    break;
                case RightTopLeftBottom:
                    fromX = rectangleElement.getX() + rectangleElement.getWidth() + (float) Math.sqrt(rectangleElement.getFromExtend());
                    fromY = rectangleElement.getY() - (float) Math.sqrt(rectangleElement.getFromExtend());
                    toX = rectangleElement.getX() - (float) Math.sqrt(rectangleElement.getToExtend());
                    toY = rectangleElement.getY() + rectangleElement.getHeight() + (float) Math.sqrt(rectangleElement.getToExtend());
                    break;
            }
            g.setPaint(new GradientPaint(fromX, fromY, rectangleElement.getFromColor(), toX, toY, rectangleElement.getToColor()));
        } else {
            g.setPaint(null);
        }

        //设置透明度
        g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, rectangleElement.getAlpha()));

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRoundRect(rectangleElement.getX(), rectangleElement.getY(), rectangleElement.getWidth(), rectangleElement.getHeight(), rectangleElement.getRoundCorner(), rectangleElement.getRoundCorner());
    }
}

