package com.taotao.cloud.common.image.combiner.painter;


import com.taotao.cloud.common.image.combiner.element.CombineElement;
import java.awt.*;
import java.io.IOException;

/**
 * IPainter 
 *
 * @author shuigedeng
 * @version 2021.10
 * @since 2022-02-24 09:48:03
 */
public interface IPainter {
    void draw(Graphics2D g, CombineElement element, int canvasWidth) throws IOException, Exception;
}
