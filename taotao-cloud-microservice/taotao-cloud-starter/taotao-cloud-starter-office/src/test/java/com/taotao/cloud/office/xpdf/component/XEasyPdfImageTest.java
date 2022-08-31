package com.taotao.cloud.office.xpdf.component;

import org.junit.Before;
import org.junit.Test;
import wiki.xsx.core.pdf.component.XEasyPdfComponent;
import wiki.xsx.core.pdf.component.image.XEasyPdfImageType;
import wiki.xsx.core.pdf.doc.XEasyPdfDocument;
import wiki.xsx.core.pdf.doc.XEasyPdfPage;
import wiki.xsx.core.pdf.doc.XEasyPdfPageRectangle;
import wiki.xsx.core.pdf.doc.XEasyPdfPositionStyle;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;
import wiki.xsx.core.pdf.util.XEasyPdfImageUtil;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
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
public class XEasyPdfImageTest {

    private static final String FONT_PATH = "C:\\Windows\\Fonts\\simfang.ttf";
    private static final String OUTPUT_PATH = "E:\\pdf\\test\\component\\image\\";
    private static final String IMAGE_PATH = OUTPUT_PATH + "test.png";

    @Before
    public void setup() {
        File dir = new File(OUTPUT_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void test() throws IOException {
        long begin = System.currentTimeMillis();
        String sourcePath = OUTPUT_PATH + "testImage2.pdf";
        String filePath = OUTPUT_PATH + "testImageWatermark.pdf";
        XEasyPdfDocument document = XEasyPdfHandler.Document.load(sourcePath);
        document.setGlobalWatermark(
                XEasyPdfHandler.Watermark.build("贵阳")
                        .setRadians(0D)
                        .setWordCount(1)
                        .setWordLine(1)
                        .setCharacterSpacing(100)
                        .setPosition(595.27563F/2-100, 841.8898F/2+25)
        ).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("finish：" + (end-begin) + "ms");
    }

    @Test
    public void testImage() throws IOException {
        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "testFlush.pdf";
        XEasyPdfDocument document = XEasyPdfHandler.Document.build().setTempDir("E:\\pdf\\test\\component\\image");
        for (int i = 0; i < 1000; i++) {
            document.setFontPath(FONT_PATH).setGlobalFooter(
                    XEasyPdfHandler.Footer.build(XEasyPdfHandler.Text.build("this is my footer").enableCenterStyle())
            ).addPage(
                    XEasyPdfHandler.Page.build(
                            XEasyPdfHandler.Image.build(new File(IMAGE_PATH)).enableCenterStyle().setHeight(800)
                    ).setHeader(
                            XEasyPdfHandler.Header.build(XEasyPdfHandler.Text.build("My Header，当前第"+XEasyPdfHandler.Page.getCurrentPagePlaceholder()+"页"))
                    )
            );
            if (i>0&&i%25==0) {
                document.flush();
            }
        }
        document.save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("finish：" + (end-begin) + "ms");
    }

    @Test
    public void testImage2() throws IOException {
        String filePath = OUTPUT_PATH + "testImage2.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Image.build(new File(IMAGE_PATH)).setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                        XEasyPdfHandler.Image.build(new File(IMAGE_PATH))
                                .setWidth(100F)
                                .setHeight(100F)
                                .setHorizontalStyle(XEasyPdfPositionStyle.LEFT),
                        XEasyPdfHandler.Image.build(new File(IMAGE_PATH))
                                .setWidth(100F)
                                .setHeight(100F)
                                .setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                        XEasyPdfHandler.Image.build(new File(IMAGE_PATH))
                                .setWidth(100F)
                                .setHeight(100F)
                                .setHorizontalStyle(XEasyPdfPositionStyle.RIGHT),
                        XEasyPdfHandler.Image.build(new File(IMAGE_PATH))
                                .setWidth(300F)
                                .setHeight(300F)
                ).setHeader(
                        XEasyPdfHandler.Header.build(XEasyPdfHandler.Text.build("My Header"))
                )
        ).setFontPath(FONT_PATH).setGlobalFooter(
                XEasyPdfHandler.Footer.build(XEasyPdfHandler.Text.build("this is my footer"))
        ).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testImage3() throws IOException {
        String sourcePath = OUTPUT_PATH + "testImage2.pdf";
        String filePath = OUTPUT_PATH + "testImage3.pdf";
        XEasyPdfDocument document = XEasyPdfHandler.Document.load(sourcePath);
        List<XEasyPdfPage> pageList = document.getPageList();
        XEasyPdfPage xEasyPdfPage = pageList.get(pageList.size() - 1);
        xEasyPdfPage.addComponent(
                XEasyPdfHandler.Image.build(new File(IMAGE_PATH))
                        .setHeight(12)
                        .setWidth(12)
                        .setPosition(12F, 12F)
        );
        document.save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testImage4() throws IOException {
        String filePath = OUTPUT_PATH + "testImage4.pdf";
        try (InputStream inputStream = new URL("https://images.gitee.com/uploads/images/2021/0527/104627_bc14225e_668748.png").openStream()) {
            XEasyPdfHandler.Document.build().addPage(
                    XEasyPdfHandler.Page.build(
                            XEasyPdfPageRectangle.A4,
                            XEasyPdfHandler.Image.build(inputStream, XEasyPdfImageType.PNG)
                    )
            ).save(filePath).close();
        }
        System.out.println("finish");
    }

    @Test
    public void testImage5() throws IOException {
        String filePath = OUTPUT_PATH + "testImage5.pdf";
        InputStream inputStream = new URL("https://i0.hdslb.com/bfs/article/f3141e4a8f2852365535a28b846d08317af656a2.jpg").openStream();
        BufferedImage image = XEasyPdfImageUtil.read(inputStream);
        inputStream.close();
        List<XEasyPdfComponent> list = new ArrayList<>();
        inputStream = XEasyPdfImageUtil.toInputStream(image, XEasyPdfImageType.PNG.name());
        list.add(
                XEasyPdfHandler.Image.build(inputStream, XEasyPdfImageType.PNG).setWidth(100).setHeight(100)
        );
        float marginLeft = 100;
        for (int i = 0; i < 6; i++) {
            inputStream = XEasyPdfImageUtil.toInputStream(image, XEasyPdfImageType.PNG.name());
            list.add(
                    XEasyPdfHandler.Image.build(inputStream, XEasyPdfImageType.PNG)
                            .setWidth(100)
                            .setHeight(100)
                            .setMarginTop(-100)
                            .setMarginLeft(marginLeft)
                            .disableSelfAdaption()
            );
            marginLeft += 100;
            inputStream.close();
        }
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(XEasyPdfPageRectangle.A4, list)
        ).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testImage6() throws IOException {
        String sourcePath = OUTPUT_PATH + "testImage5.pdf";
        String filePath = OUTPUT_PATH + "testImage6.pdf";
        String imagePath = OUTPUT_PATH + "91h687506p0.jpg";
        InputStream inputStream = Files.newInputStream(Paths.get(imagePath));
        XEasyPdfHandler.Document.load(sourcePath)
                .replacer()
                .replaceImage(XEasyPdfImageUtil.read(new File(imagePath)), Arrays.asList(1, 0), 0)
                .finish(filePath);
        inputStream.close();
        System.out.println("finish");
    }

    @Test
    public void testImage7() throws IOException {
        String filePath = OUTPUT_PATH + "testImage7.pdf";
        String imagePath = OUTPUT_PATH + "222.jpg";
        File image = new File(imagePath);
        XEasyPdfHandler.Document.build(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Table.build(
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100, 100)
                                                .addContent(
                                                        XEasyPdfHandler.Image.build(image)
                                                ),
                                        XEasyPdfHandler.Table.Row.Cell.build(50, 50).setMarginTop(50)
                                                .addContent(
                                                        XEasyPdfHandler.Image.build(image)
                                                ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100, 100)
                                                .addContent(
                                                        XEasyPdfHandler.Image.build(image)
                                                        .setWidth(50).setHeight(2000)
                                                )
                                )
                        ).setMarginLeft(100f).enableCenterStyle()
//                                .disableBorder()
                )
        ).save(filePath).close();
        System.out.println("finish");
    }
}
