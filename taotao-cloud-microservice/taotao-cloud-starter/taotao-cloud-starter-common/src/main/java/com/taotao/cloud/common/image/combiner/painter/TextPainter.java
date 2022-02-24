package com.taotao.cloud.common.image.combiner.painter;


import com.taotao.cloud.common.image.combiner.element.CombineElement;
import com.taotao.cloud.common.image.combiner.element.TextElement;
import com.taotao.cloud.common.image.combiner.enums.Direction;
import java.awt.*;
import java.awt.font.TextAttribute;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

/**
 * TextPainter
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:15:33
 */
public class TextPainter implements IPainter {

    @Override
    public void draw(Graphics2D g, CombineElement element, int canvasWidth) {

        //强制转成子类
        TextElement textElement = (TextElement) element;

        //首先计算是否要换行（由于拆行计算比较耗资源，不设置换行则直接用原始对象绘制）
        List<TextElement> textLineElements = new ArrayList<>();
        textLineElements.add(textElement);

        if (textElement.isAutoBreakLine()) {
            textLineElements = textElement.getBreakLineElements();
        }

        for (int i = 0; i < textLineElements.size(); i++) {
            TextElement firstLineElement = textLineElements.get(0);
            TextElement currentLineElement = textLineElements.get(i);

            //设置字体、颜色
            g.setFont(currentLineElement.getFont());
            g.setColor(currentLineElement.getColor());

            //设置居中（多行的时候，第一行居中，后续行按对齐方式计算）
            if (currentLineElement.isCenter()) {
                if (i == 0) {
                    currentLineElement.setX((canvasWidth - currentLineElement.getWidth()) / 2);
                } else {
                    switch (textElement.getLineAlign()) {
                        case Left:
                            currentLineElement.setX(firstLineElement.getX());
                            break;
                        case Center:
                            currentLineElement.setX((canvasWidth - currentLineElement.getWidth()) / 2);
                            break;
                        case Right:
                            currentLineElement.setX(firstLineElement.getX() + firstLineElement.getWidth() - currentLineElement.getWidth());
                            break;
                    }
                }
            } else {
                if (i == 0) {
                    //绘制方向（只处理第一个元素即可，后续元素会参照第一个元素的坐标来计算）
                    if (currentLineElement.getDirection() == Direction.RightLeft) {
                        currentLineElement.setX(currentLineElement.getX() - currentLineElement.getWidth());
                    } else if (currentLineElement.getDirection() == Direction.CenterLeftRight) {
                        currentLineElement.setX(currentLineElement.getX() - currentLineElement.getWidth() / 2);
                    }
                } else {
                    switch (textElement.getLineAlign()) {
                        case Left:
                            currentLineElement.setX(firstLineElement.getX());
                            break;
                        case Center: {
                            currentLineElement.setX(firstLineElement.getX() + (firstLineElement.getWidth() - currentLineElement.getWidth()) / 2);
                            break;
                        }
                        case Right: {
                            currentLineElement.setX(firstLineElement.getX() + firstLineElement.getWidth() - currentLineElement.getWidth());
                            break;
                        }
                    }
                }
            }

            //旋转
            if (currentLineElement.getRotate() != null) {
                g.rotate(Math.toRadians(currentLineElement.getRotate()), currentLineElement.getX() + currentLineElement.getWidth() / 2, currentLineElement.getDrawY());
            }

            //设置透明度
            g.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, currentLineElement.getAlpha()));

            //带删除线样式的文字要特殊处理
            if (currentLineElement.isStrikeThrough() == true) {
                AttributedString as = new AttributedString(currentLineElement.getText());
                as.addAttribute(TextAttribute.FONT, currentLineElement.getFont());
                as.addAttribute(TextAttribute.STRIKETHROUGH, TextAttribute.STRIKETHROUGH_ON, 0, currentLineElement.getText().length());
                g.drawString(as.getIterator(), currentLineElement.getX(), currentLineElement.getDrawY());
            } else {
                g.drawString(currentLineElement.getText(), currentLineElement.getX(), currentLineElement.getDrawY());
            }

            //绘制完后反向旋转，以免影响后续元素
            if (currentLineElement.getRotate() != null) {
                g.rotate(-Math.toRadians(currentLineElement.getRotate()), currentLineElement.getX() + currentLineElement.getWidth() / 2, currentLineElement.getDrawY());
            }
        }
    }
}
