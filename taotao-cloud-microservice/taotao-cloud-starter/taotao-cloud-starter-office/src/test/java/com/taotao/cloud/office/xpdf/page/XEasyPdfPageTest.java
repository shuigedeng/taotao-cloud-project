package com.taotao.cloud.office.xpdf.page;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import wiki.xsx.core.pdf.component.XEasyPdfComponent;
import wiki.xsx.core.pdf.doc.*;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.io.File;
import java.io.IOException;
import java.util.List;

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
public class XEasyPdfPageTest {

    private static final String FONT_PATH = "C:\\Windows\\Fonts\\simfang.ttf";
    private static final String OUTPUT_PATH = "E:\\pdf\\test\\page\\";

    @Before
    public void setup() {
        File dir = new File(OUTPUT_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void testAddComponent() throws IOException {
        String filePath = OUTPUT_PATH + "testAddComponent.pdf";
        XEasyPdfHandler.Document.build(
                XEasyPdfHandler.Page.build().setRotation(XEasyPdfPage.Rotation.FORWARD_0).setFontPath(FONT_PATH).addComponent(
                        XEasyPdfHandler.Text.build("Hello World").setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                        XEasyPdfHandler.Text.build("你好，世界！").setDefaultFontStyle(XEasyPdfDefaultFontStyle.LIGHT),
                        XEasyPdfHandler.Text.build("我是第一页").setDefaultFontStyle(XEasyPdfDefaultFontStyle.BOLD)
                )
        ).save(filePath).setFontPath(FONT_PATH).close();
        System.out.println("finish");

        XEasyPdfHandler.Document
                .load(OUTPUT_PATH + "testAddComponent.pdf")
                .modifyPageSize(XEasyPdfPageRectangle.create(0, 300, 0, 900))
                .save(OUTPUT_PATH + "testAddComponent2.pdf")
                .close();
        System.out.println("finish");
    }

    @Test
    public void testAddComponent2() throws IOException {
        String sourcePath = OUTPUT_PATH + "testAddComponent.pdf";
        String filePath = OUTPUT_PATH + "testAddComponent2.pdf";
        String imagePath = "C:\\Users\\Administrator\\Desktop\\testImage9.jpg";
        XEasyPdfDocument document = XEasyPdfHandler.Document.load(sourcePath);
        List<XEasyPdfPage> pageList = document.getPageList();
        XEasyPdfPage xEasyPdfPage = pageList.get(pageList.size() - 1);
        xEasyPdfPage.addComponent(
                XEasyPdfHandler.Image.build(new File(imagePath)).setContentMode(XEasyPdfComponent.ContentMode.PREPEND),
                XEasyPdfHandler.Text.build("xxxx")
        ).setDefaultFontStyle(XEasyPdfDefaultFontStyle.BOLD);
        document.save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testMargin() throws IOException {
        String filePath = OUTPUT_PATH + "testMargin.pdf";
        XEasyPdfDocument document = XEasyPdfHandler.Document.build();
        XEasyPdfPage xEasyPdfPage = XEasyPdfHandler.Page.build();
        xEasyPdfPage.addComponent(
                XEasyPdfHandler.Text.build("" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx" +
                        ""
                ).setMargin(200F)
        ).setDefaultFontStyle(XEasyPdfDefaultFontStyle.BOLD);
        XEasyPdfPage xEasyPdfPage2 = XEasyPdfHandler.Page.build();
        xEasyPdfPage2.addComponent(XEasyPdfHandler.Text.build("222"));
        document.addPage(xEasyPdfPage, xEasyPdfPage2).save(filePath).close();
    }
}
