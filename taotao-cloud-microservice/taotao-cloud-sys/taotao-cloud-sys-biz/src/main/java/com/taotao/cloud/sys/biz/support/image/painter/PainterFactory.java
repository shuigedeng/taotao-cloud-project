package com.taotao.cloud.sys.biz.support.image.painter;


import com.taotao.cloud.sys.biz.support.image.element.CombineElement;
import com.taotao.cloud.sys.biz.support.image.element.ImageElement;
import com.taotao.cloud.sys.biz.support.image.element.RectangleElement;
import com.taotao.cloud.sys.biz.support.image.element.TextElement;

/**
 * PainterFactory
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:15:12
 */
public class PainterFactory {
    private static ImagePainter imagePainter;               //图片绘制器
    private static TextPainter textPainter;                 //文本绘制器
    private static RectanglePainter rectanglePainter;       //矩形绘制器

    public static IPainter createInstance(CombineElement element) throws Exception {

        //考虑到性能，这里用单件，先不lock了
        if (element instanceof ImageElement) {
            if (imagePainter == null) {
                imagePainter = new ImagePainter();
            }
            return imagePainter;
        } else if (element instanceof TextElement) {
            if (textPainter == null) {
                textPainter = new TextPainter();
            }
            return textPainter;
        } else if (element instanceof RectangleElement) {
            if (rectanglePainter == null) {
                rectanglePainter = new RectanglePainter();
            }
            return rectanglePainter;
        } else {
            throw new Exception("不支持的Painter类型");
        }
    }
}
