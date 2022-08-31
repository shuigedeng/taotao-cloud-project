package com.taotao.cloud.office.xpdf.component;

import org.junit.Before;
import org.junit.Test;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @author xsx
 * @date 2020/6/15
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
public class XEasyPdfBaseLineTest {

    private static final String FONT_PATH = "C:\\Windows\\Fonts\\simfang.ttf";
    private static final String OUTPUT_PATH = "C:\\Users\\xsx\\Desktop\\pdf\\test\\component\\line\\";

    @Before
    public void setup() {
        File dir = new File(OUTPUT_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void testLine() throws IOException {
        String filePath = OUTPUT_PATH + "testLine.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Line.build(50F, 50F, 100F, 100F).setLineWidth(20F).setColor(Color.PINK),
                        XEasyPdfHandler.Line.build(100F, 100F, 150F, 50F).setLineWidth(20F).setColor(Color.ORANGE),
                        XEasyPdfHandler.Line.build(150F, 50F, 50F, 50F).setLineWidth(20F).setColor(Color.CYAN)
                ).setHeader(
                        XEasyPdfHandler.Header.build(XEasyPdfHandler.Text.build("My Header"))
                )
        ).setFontPath(FONT_PATH).setGlobalFooter(
                XEasyPdfHandler.Footer.build(XEasyPdfHandler.Text.build("this is my footer"))
        ).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testSolidSplitLine() throws IOException {
        String filePath = OUTPUT_PATH + "testSolidSplitLine.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.SplitLine.SolidLine.build().setLineWidth(20F).setColor(Color.YELLOW),
                        XEasyPdfHandler.SplitLine.SolidLine.build().setLineWidth(10F).setColor(Color.RED),
                        XEasyPdfHandler.SplitLine.SolidLine.build()
                ).setHeader(
                        XEasyPdfHandler.Header.build(XEasyPdfHandler.Text.build("My Header"))
                )
        ).setFontPath(FONT_PATH).setGlobalFooter(
                XEasyPdfHandler.Footer.build(XEasyPdfHandler.Text.build("this is my footer"))
        ).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testDottedSplitLine() throws IOException {
        String filePath = OUTPUT_PATH + "testDottedSplitLine.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.SplitLine.DottedLine.build().setMarginTop(10F).setColor(Color.YELLOW),
                        XEasyPdfHandler.SplitLine.DottedLine.build().setMarginTop(10F).setColor(Color.RED),
                        XEasyPdfHandler.SplitLine.DottedLine.build().setMarginTop(10F).setColor(Color.BLUE)
                ).setHeader(
                        XEasyPdfHandler.Header.build(XEasyPdfHandler.Text.build("My Header"))
                )
        ).setFontPath(FONT_PATH).setGlobalFooter(
                XEasyPdfHandler.Footer.build(XEasyPdfHandler.Text.build("this is my footer"))
        ).save(filePath).close();
        System.out.println("finish");
    }
}
