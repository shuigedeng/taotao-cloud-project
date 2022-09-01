package com.taotao.cloud.office.xpdf.component;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author xsx
 * @date 2022/1/26
 * @since 1.8
 * <p>
 * Copyright (c) 2020-2022 xsx All Rights Reserved.
 * x-easypdf is licensed under Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 * </p>
 */
public class XEasyPdfCircleTest {

    private static final String FONT_PATH = "C:\\Windows\\Fonts\\simfang.ttf";
    private static final String OUTPUT_PATH = "E:\\pdf\\test\\component\\circle\\";

    @Before
    public void setup() {
        File dir = new File(OUTPUT_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void testCircle() throws IOException {
        String filePath = OUTPUT_PATH + "testCircle.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Circle.build(50F, 250F, 600F)
                                .setBackgroundColor(Color.BLUE),
                        XEasyPdfHandler.Circle.build(50F, 250F, 400F)
                                .setHasBorder(true),
                        XEasyPdfHandler.Circle.build(50F, 250F, 200F)
                                .setBackgroundColor(Color.GREEN)
                ).setHeader(
                        XEasyPdfHandler.Header.build(XEasyPdfHandler.Text.build("My Header"))
                )
        ).setFontPath(FONT_PATH).setGlobalFooter(
                XEasyPdfHandler.Footer.build(XEasyPdfHandler.Text.build("this is my footer"))
        ).save(filePath).close();
        System.out.println("finish");
    }
}
