package com.taotao.cloud.office.xpdf.component;

import org.junit.Before;
import org.junit.Test;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * @author xsx
 * @date 2020/6/12
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
public class XEasyPdfFooterTest {

    private static final String FONT_PATH = "C:\\Windows\\Fonts\\simfang.ttf";
    private static final String OUTPUT_PATH = "C:\\Users\\xsx\\Desktop\\pdf\\test\\component\\footer\\";

    @Before
    public void setup() {
        File dir = new File(OUTPUT_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void testGlobalFooter() throws IOException {
        String filePath = OUTPUT_PATH + "testGlobalFooter.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build()
        ).setFontPath(FONT_PATH).setGlobalFooter(
                XEasyPdfHandler.Footer.build(XEasyPdfHandler.Text.build("Hello World"))
        ).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testPageFooter() throws IOException {
        String filePath = OUTPUT_PATH + "testPageFooter.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build().setFooter(
                        XEasyPdfHandler.Footer.build(
                                XEasyPdfHandler.Text.build(
                                        Arrays.asList(
                                                "Hello XSX",
                                                "Hello World",
                                                "HELLO X-EASYPDF"
                                        )
                                )
                        )
                )
        ).setFontPath(FONT_PATH).setGlobalFooter(
                XEasyPdfHandler.Footer.build(XEasyPdfHandler.Text.build("Hello World"))
        ).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testPageFooter2() throws IOException {
        String filePath = OUTPUT_PATH + "testPageFooter2.pdf";
        String imagePath = "C:\\Users\\Administrator\\Desktop\\testImage9.jpg";
        String backgroundImagePath = "C:\\Users\\Administrator\\Desktop\\testImage9.jpg";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build().setFooter(
                        XEasyPdfHandler.Footer.build(
                                XEasyPdfHandler.Image.build(new File(imagePath)).setHeight(200).disableSelfAdaption(),
                                XEasyPdfHandler.Text.build(
                                        Arrays.asList(
                                                "Hello XSX",
                                                "Hello World",
                                                "HELLO X-EASYPDF"
                                        )
                                ).setFontColor(Color.RED)
                        )
                )
        ).setGlobalBackgroundImage(
                XEasyPdfHandler.Image.build(new File(backgroundImagePath)).setPosition(0, 0)
        ).setFontPath(FONT_PATH).setGlobalFooter(
                XEasyPdfHandler.Footer.build(
                        XEasyPdfHandler.Text.build("Hello World")
                )
        ).setGlobalBackgroundColor(new Color(0,191,255)).save(filePath).close();
        System.out.println("finish");
    }
}
