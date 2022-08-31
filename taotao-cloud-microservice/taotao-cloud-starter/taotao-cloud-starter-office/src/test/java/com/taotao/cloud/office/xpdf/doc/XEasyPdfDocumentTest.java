package com.taotao.cloud.office.xpdf.doc;

import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import wiki.xsx.core.pdf.component.image.XEasyPdfImageType;
import wiki.xsx.core.pdf.component.text.XEasypdfTextRenderingMode;
import wiki.xsx.core.pdf.doc.XEasyPdfDocument;
import wiki.xsx.core.pdf.doc.XEasyPdfDocumentBookmark;
import wiki.xsx.core.pdf.doc.XEasyPdfPositionStyle;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.*;

/**
 * @author xsx
 * @date 2020/6/8
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
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class XEasyPdfDocumentTest {

    private static final String FONT_PATH = "C:\\Windows\\Fonts\\msyh.ttf";
    private static final String OUTPUT_PATH = "E:\\pdf\\test\\doc\\";
    private static final String IMAGE_PATH = OUTPUT_PATH + "test.png";

    @Before
    public void setup() {
        File dir = new File(OUTPUT_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void test01AddPage() throws IOException {
        String filePath = OUTPUT_PATH + "testAddPage.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("Hello World").setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                        XEasyPdfHandler.Text.build("可以，世界！"),
                        XEasyPdfHandler.Text.build("我是第一页")
                ),
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("你好，世界！").setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                        XEasyPdfHandler.Text.build("我是第二页").setHorizontalStyle(XEasyPdfPositionStyle.RIGHT)
                )
        ).setFontPath(FONT_PATH).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void test02Save() throws IOException {
        String sourcePath = OUTPUT_PATH + "testAddPage.pdf";
        String filePath = OUTPUT_PATH + "doc1.pdf";
        XEasyPdfHandler.Document.load(sourcePath).addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("Hello World").setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                        XEasyPdfHandler.Text.build("你好，世界！")
                ).setFontPath(FONT_PATH)
        ).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void test03SetInfo() throws IOException {
        String filePath = OUTPUT_PATH + "info.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build()
        ).information()
            .setTitle("test info")
            .setAuthor("xsx")
            .setSubject("info")
            .setCreator("my-creator")
            .setKeywords("pdf,xsx")
            .setCreateTime(Calendar.getInstance())
            .setUpdateTime(Calendar.getInstance())
            .finish()
            .save(filePath)
            .close();
        System.out.println("finish");
    }

    @Test
    public void test04SetPermission() throws IOException {
        String filePath = OUTPUT_PATH + "permission.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build()
        ).permission()
                .setCanPrintDegraded(false)
                .setCanPrint(false)
                .setCanAssembleDocument(false)
                .setCanExtractContent(false)
                .setCanExtractForAccessibility(false)
                .setCanFillInForm(false)
                .setCanModify(false)
                .setCanModifyAnnotations(false)
                .finish()
                .save(filePath)
                .close();
        System.out.println("finish");
    }

    @Test
    public void test05SetBackgroundColor() throws IOException {
        String filePath = OUTPUT_PATH + "backgroundColor.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(),
                XEasyPdfHandler.Page.build()
        ).setGlobalBackgroundColor(Color.YELLOW)
                .save(filePath)
                .close();
        System.out.println("finish");
    }

    @Test
    public void test06SetBackgroundImage() throws IOException {
        String filePath = OUTPUT_PATH + "backgroundImage.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build().addComponent(
                        XEasyPdfHandler.Text.build(
                                "hello world!!!" + "hello world!!!" + "hello world!!!"
                                        + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!"
                                        + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!"
                                        + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!"
                                        + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!"
                                        + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!"
                                        + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!"
                                        + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!"
                                        + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!"
                                        + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!" + "hello world!!!"
                        ).setFontSize(20)
                ).setBackgroundColor(Color.WHITE),
                XEasyPdfHandler.Page.build()
        ).setFontPath(FONT_PATH)
                .setGlobalBackgroundColor(new Color(0,191,255))
                .setGlobalBackgroundImage(XEasyPdfHandler.Image.build(new File(IMAGE_PATH)).setHorizontalStyle(XEasyPdfPositionStyle.CENTER))
                .save(filePath)
                .close();
        System.out.println("finish");
    }

    @Test
    public void test07InsertPage() throws IOException {
        String sourcePath = OUTPUT_PATH + "backgroundColor.pdf";
        String filePath = OUTPUT_PATH + "insertPage.pdf";
        XEasyPdfHandler.Document.load(sourcePath).insertPage(
                -100,
                XEasyPdfHandler.Page.build(XEasyPdfHandler.Text.build("插入首页"))
        ).insertPage(
                100,
                XEasyPdfHandler.Page.build(XEasyPdfHandler.Text.build("插入尾页"))
        ).setFontPath(FONT_PATH)
                .setGlobalBackgroundColor(new Color(0,191,255))
                .save(filePath)
                .close();
        System.out.println("finish");
    }

    @Test
    public void test08Merge() throws IOException {
        String sourcePath = OUTPUT_PATH + "backgroundColor.pdf";
        String mergePath1 = OUTPUT_PATH + "insertPage.pdf";
        String mergePath2 = OUTPUT_PATH + "doc1.pdf";
        String filePath = OUTPUT_PATH + "merge.pdf";
        XEasyPdfHandler.Document.load(sourcePath).merge(
                XEasyPdfHandler.Document.load(mergePath1),
                XEasyPdfHandler.Document.load(mergePath2)
        ).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void test09Image1() throws IOException {
        String sourcePath = OUTPUT_PATH + "doc1.pdf";
        XEasyPdfHandler.Document.load(sourcePath).imager().image(OUTPUT_PATH, XEasyPdfImageType.PNG).finish().close();
        System.out.println("finish");
    }

    @Test
    public void test10Image2() throws IOException {
        String sourcePath = OUTPUT_PATH + "insertPage.pdf";
        String prefix = "x-easypdf";
        XEasyPdfHandler.Document.load(sourcePath).imager().image(OUTPUT_PATH, XEasyPdfImageType.JPEG, prefix).finish().close();
        System.out.println("finish");
    }

    @Test
    public void test11Image3() throws IOException {
        String sourcePath = OUTPUT_PATH + "merge.pdf";
        String filePath1 = OUTPUT_PATH + "merge0.jpg";
        String filePath2 = OUTPUT_PATH + "merge6.jpg";
        try(
                OutputStream outputStream1 = Files.newOutputStream(Paths.get(filePath1));
                OutputStream outputStream2 = Files.newOutputStream(Paths.get(filePath2))
        ) {
            XEasyPdfHandler.Document.load(sourcePath)
                    .imager()
                    .image(outputStream1, XEasyPdfImageType.JPEG, 0)
                    .image(outputStream2, XEasyPdfImageType.PNG, 6)
                    .finish()
                    .close();
        }
        System.out.println("finish");
    }

    @Test
    public void test12Split1() throws IOException {
        String sourcePath = OUTPUT_PATH + "doc1.pdf";
        XEasyPdfHandler.Document.load(sourcePath).splitter().split(OUTPUT_PATH).finish().close();
        System.out.println("finish");
    }

    @Test
    public void test13Split2() throws IOException {
        String sourcePath = OUTPUT_PATH + "testAddPage.pdf";
        XEasyPdfHandler.Document.load(sourcePath)
                .splitter()
                .addDocument(1)
                .addDocument(1, 0)
                .split(OUTPUT_PATH, "mypdf")
                .finish()
                .close();
        System.out.println("finish");
    }

    @Test
    public void test14Split3() throws IOException {
        String sourcePath = OUTPUT_PATH + "doc1.pdf";
        String filePath = OUTPUT_PATH + "testSplit3.pdf";
        try(OutputStream outputStream = Files.newOutputStream(Paths.get(filePath))) {
            XEasyPdfHandler.Document.load(sourcePath).splitter().split(outputStream, 1).finish().close();
        }
        System.out.println("finish");
    }

    @Test
    public void test15StripText() throws IOException {
        long begin = System.currentTimeMillis();
        final String sourcePath = OUTPUT_PATH + "doc1.pdf";
        List<String> list = new ArrayList<>(1024);
        XEasyPdfHandler.Document.load(sourcePath).extractor().extractText(list, ".*第.*").finish().close();
        for (String s : list) {
            System.out.println("s = " + s);
        }
        long end = System.currentTimeMillis();
        System.out.println("finish("+(end-begin)+"ms)");
    }

    @Test
    public void test16StripText1() throws IOException {
        long begin = System.currentTimeMillis();
        final String sourcePath = OUTPUT_PATH + "模板.pdf";
        List<String> list = new ArrayList<>(1024);
        XEasyPdfHandler.Document.load(sourcePath).extractor().extractText(list, 0).finish().close();
        for (String s : list) {
            System.out.println("s = " + s);
        }
        long end = System.currentTimeMillis();
        System.out.println("finish("+(end-begin)+"ms)");
    }

    @Test
    public void test17StripText2() throws IOException {
        long begin = System.currentTimeMillis();
        final String sourcePath = OUTPUT_PATH + "doc1.pdf";
        List<Map<String, String>> dataList = new ArrayList<>();
        XEasyPdfHandler.Document.load(sourcePath).extractor().addRegion("test1", new Rectangle(600,2000)).extractTextByRegions(dataList, 0).finish().close();
        System.out.println("dataList = " + dataList);
        long end = System.currentTimeMillis();
        System.out.println("finish("+(end-begin)+"ms)");
    }

    @Test
    public void test18StripTable() throws IOException {
        long begin = System.currentTimeMillis();
        final String sourcePath = OUTPUT_PATH +"testAddPage.pdf";
        List<List<String>> list = new ArrayList<>(1024);
        XEasyPdfHandler.Document.load(sourcePath).extractor().extractTextForSimpleTable(list, 0).finish().close();
        for (List<String> s : list) {
            System.out.println("s = " + s);
        }
        long end = System.currentTimeMillis();
        System.out.println("finish("+(end-begin)+"ms)");
    }

    @Test
    public void test19StripTable2() throws IOException {
        long begin = System.currentTimeMillis();
        final String sourcePath = OUTPUT_PATH +"testAddPage.pdf";
        List<List<String>> list = new ArrayList<>(1024);
        XEasyPdfHandler.Document.load(sourcePath).extractor().extractTextByRegionsForSimpleTable(list, new Rectangle(0,0, 800, 170),1).finish().close();
        for (List<String> s : list) {
            System.out.println("s = " + s);
        }
        long end = System.currentTimeMillis();
        System.out.println("finish("+(end-begin)+"ms)");
    }

    @Test
    public void test20StripTable3() throws IOException {
        long begin = System.currentTimeMillis();
        final String sourcePath = OUTPUT_PATH +"testAddPage.pdf";
        List<Map<String, String>> dataList = new ArrayList<>();
        XEasyPdfHandler.Document.load(sourcePath).extractor().addRegion("test1", new Rectangle(0,320,800,540)).extractTextByRegions(dataList, 0).finish().close();
        System.out.println("dataList = " + dataList);
        long end = System.currentTimeMillis();
        System.out.println("finish("+(end-begin)+"ms)");
    }

    @Test
    public void test21FillForm() throws IOException {
        long begin = System.currentTimeMillis();
        final String sourcePath = "E:\\pdf\\hi.pdf";
        final String outputPath = OUTPUT_PATH + "test_fill2.pdf";
        Map<String, String> form = new HashMap<>(2);
        form.put("test1", "爽爽的贵阳");
        form.put("test2", "堵车的天堂");
        XEasyPdfHandler.Document.load(sourcePath).formFiller().fill(form).finish(outputPath);
        long end = System.currentTimeMillis();
        System.out.println("finish("+(end-begin)+"ms)");
    }

    @Test
    public void test22() throws IOException {
        final String outputPath = OUTPUT_PATH+"text.pdf";

        // 设置背景图片
        XEasyPdfHandler.Document.build().setGlobalBackgroundImage(
                // 构建图片
                XEasyPdfHandler.Image.build(new File(IMAGE_PATH)).setHeight(800F).enableCenterStyle()
                // 设置全局页眉
        ).setGlobalHeader(
                // 构建页眉
                XEasyPdfHandler.Header.build(
                        // 构建页眉文本，并居中显示
                        XEasyPdfHandler.Text.build("这是页眉").setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                )
                // 设置全局页脚
        ).setGlobalFooter(
                // 构建页脚
                XEasyPdfHandler.Footer.build(
                        // 构建页脚文本
                        XEasyPdfHandler.Text.build("这是页脚")
                )
                // 添加页面
        ).addPage(
                // 构建页面
                XEasyPdfHandler.Page.build(
                        // 构建文本
                        XEasyPdfHandler.Text.build(
                                "Hello World(这是一个DEMO)"
                        ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setFontSize(20F).setMargin(10F)
                        // 构建文本
                        ,XEasyPdfHandler.Text.build(
                                "这里是正文（这是一个基于PDFBOX开源工具，专注于PDF文件导出功能，" +
                                        "以组件形式进行拼接，简单、方便，上手及其容易，" +
                                        "目前有TEXT(文本)、LINE(分割线)等组件，后续还会补充更多组件，满足各种需求）。"
                        ).setHorizontalStyle(XEasyPdfPositionStyle.LEFT).setFontSize(14F).setMargin(10F).setAutoIndent(9)
                        // 构建文本
                        ,XEasyPdfHandler.Text.build(
                                "-- by xsx"
                        ).setHorizontalStyle(XEasyPdfPositionStyle.RIGHT).setFontSize(12F).setMarginTop(10F).setMarginRight(10F)
                        // 构建文本
                        ,XEasyPdfHandler.Text.build(
                                "2020.03.12"
                        ).setHorizontalStyle(XEasyPdfPositionStyle.RIGHT).setFontSize(12F).setMarginTop(10F).setMarginRight(10F)
                        // 构建实线分割线
                        ,XEasyPdfHandler.SplitLine.SolidLine.build().setMarginTop(10F)
                        // 构建虚线分割线
                        ,XEasyPdfHandler.SplitLine.DottedLine.build().setLineLength(10F).setMarginTop(10F).setLineWidth(10F)
                        // 构建实线分割线
                        ,XEasyPdfHandler.SplitLine.SolidLine.build().setMarginTop(10F)
                        // 构建文本
                        ,XEasyPdfHandler.Text.build( "完结").setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                )
                // 设置字体路径，并保存
        ).save(outputPath).close();
    }

    @Test
    public void test23() throws IOException {
        long begin = System.currentTimeMillis();
        final String sourcePath = OUTPUT_PATH+"hi.pdf";
        final String outputPath = OUTPUT_PATH+"test_fill4.pdf";
        Map<String, String> form = new HashMap<>(5);
        form.put("test1", "爽爽的贵阳");
        form.put("test2", "堵车的天堂");
        form.put("text1", "xxx");
        form.put("text2", "sss");
        form.put("hi", "我是xsx");
        XEasyPdfHandler.Document.load(sourcePath).formFiller().fill(form).finish(outputPath);;
        long end = System.currentTimeMillis();
        System.out.println("finish("+(end-begin)+"ms)");
    }


    @Test
    public void test24() throws IOException {
        long begin = System.currentTimeMillis();
        final String sourcePath = OUTPUT_PATH+"hi.pdf";
        final String outputPath = OUTPUT_PATH+"ZZZ.pdf";
        Map<String, String> form = new HashMap<>(2);
        form.put("hi", "静静");
        form.put("test1", "7");
        form.put("test2", "xxx");
        XEasyPdfHandler.Document.load(sourcePath).setFontPath(FONT_PATH).addPage(
                XEasyPdfHandler.Page.build(XEasyPdfHandler.Text.build("哈哈"))
        ).formFiller().fill(form).finish(outputPath);
        long end = System.currentTimeMillis();
        System.out.println("finish("+(end-begin)+"ms)");
    }

    @Test
    public void test26() throws IOException {
        long begin = System.currentTimeMillis();
        // 定义保存路径
        final String outputPath = OUTPUT_PATH + "merge.pdf";
        XEasyPdfDocument document = XEasyPdfHandler.Document.build();
        document.merge(
                XEasyPdfHandler.Document.build().addPage(
                        XEasyPdfHandler.Page.build(
                                XEasyPdfHandler.Text.build("第一个文件")
                        )
                ),
                XEasyPdfHandler.Document.build().addPage(
                        XEasyPdfHandler.Page.build(
                                XEasyPdfHandler.Text.build("第二个文件")
                        )
                )
        ).setGlobalHeader(
                XEasyPdfHandler.Header.build(
                        XEasyPdfHandler.Text.build("当前页码："+XEasyPdfHandler.Page.getCurrentPagePlaceholder()+"" +
                                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" +
                                "SSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSS" +
                                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
                        )
                )
        ).setGlobalFooter(
                XEasyPdfHandler.Footer.build(
                        XEasyPdfHandler.Text.build("当前页码："+XEasyPdfHandler.Page.getCurrentPagePlaceholder())
                )
        ).bookmark()
                .setBookMark(0, "第1个文件")
                .setBookMark(
                        XEasyPdfDocumentBookmark.BookmarkNode.build()
                                .setTitle("第二个文件")
                                .setPage(1)
                                .setTitleColor(new Color(255, 0, 153))
                                .addChild(
                                        XEasyPdfDocumentBookmark.BookmarkNode.build()
                                                .setTitle("第二个文件子节点")
                                                .setPage(1)
                                                .setTop(300)
                                                .setTitleColor(new Color(255,50,100))
                                                .addChild(
                                                        XEasyPdfDocumentBookmark.BookmarkNode.build()
                                                                .setTitle("第二个文件子节点的子节点")
                                                                .setPage(1)
                                                )
                                )
                )
                .finish()
                .save(outputPath)
                .close();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end-begin));
    }

    @Test
    public void test27() throws IOException {
        long begin = System.currentTimeMillis();
        // 定义保存路径
        final String outputPath = OUTPUT_PATH + "mutilPage.pdf";
        XEasyPdfDocument document = XEasyPdfHandler.Document.build();
        document.addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("第一个页面文件第一行"),
                        XEasyPdfHandler.Text.build("第一个页面文件第二行"),
                        XEasyPdfHandler.Text.build("第一个页面文件第三行")
                ),
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("第二个页面文件")
                ),
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("第三个页面文件第一行"),
                        XEasyPdfHandler.Text.build("第三个页面文件第二行")
                )
        ).setGlobalHeader(
                XEasyPdfHandler.Header.build(
                        XEasyPdfHandler.Text.build(Arrays.asList("当前页码："+XEasyPdfHandler.Page.getCurrentPagePlaceholder(), "页眉第二行", "页眉XXXXXX")).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                )
        ).setGlobalFooter(
                XEasyPdfHandler.Footer.build(
                        XEasyPdfHandler.Text.build("当前页码："+XEasyPdfHandler.Page.getCurrentPagePlaceholder())
                )
        ).save(outputPath).close();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end-begin));
    }

    @Test
    public void test29() throws IOException {
        long begin = System.currentTimeMillis();
        final String fontPath = "C:\\Windows\\Fonts\\simsun.ttc,0";
        // 定义源路径
        final String sourcePath = OUTPUT_PATH + "nodisplayText.pdf";
        // 定义保存路径
        final String outputPath = OUTPUT_PATH + "replaceText.pdf";
        Map<String, String> replaceMap = new HashMap<>(9);
        replaceMap.put("模板", "标题");
        replaceMap.put("test1", "联想供应商");
        replaceMap.put("test2", "2022-04-04");
        replaceMap.put("test3", "0001");
        replaceMap.put("test4", "2022-04-10 10:00:00");
        replaceMap.put("\\$\\{name1\\}", "商品XXX");
        replaceMap.put("sku1", "规格-大");
        replaceMap.put("num1", "10");
        replaceMap.put("remark1", "单位：ml");
        XEasyPdfHandler.Document.load(sourcePath).replacer().setFontPath(fontPath).replaceText(replaceMap).finish(outputPath);
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end-begin));
    }

    @Test
    public void test30() throws IOException {
        long begin = System.currentTimeMillis();
        // 定义保存路径
        final String outputPath = OUTPUT_PATH + "flush.pdf";
        XEasyPdfHandler.Document.build(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("你好")
                )
        ).flush().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("贵阳")
                )
        ).save(outputPath);
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end-begin));
    }

    @Test
    public void test31() {
        // 定义保存路径
        final String outputPath = OUTPUT_PATH + "fontStyle.pdf";
        XEasyPdfHandler.Document.build(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("TEST").setFontSize(20F),
                        XEasyPdfHandler.Text.build("NORMAL").setFontSize(20F).setRenderingMode(XEasypdfTextRenderingMode.NORMAL),
                        XEasyPdfHandler.Text.build("STROKE").setFontSize(20F).setRenderingMode(XEasypdfTextRenderingMode.STROKE),
                        XEasyPdfHandler.Text.build("BOLD").setFontSize(20F).setRenderingMode(XEasypdfTextRenderingMode.BOLD),
                        XEasyPdfHandler.Text.build("LIGHT").setFontSize(20F).setRenderingMode(XEasypdfTextRenderingMode.LIGHT),
                        XEasyPdfHandler.Text.build("ITALIC").setFontSize(20F).setRenderingMode(XEasypdfTextRenderingMode.ITALIC),
                        XEasyPdfHandler.Text.build("ITALIC_STROKE").setFontSize(20F).setRenderingMode(XEasypdfTextRenderingMode.ITALIC_STROKE),
                        XEasyPdfHandler.Text.build("ITALIC_BOLD").setFontSize(20F).setRenderingMode(XEasypdfTextRenderingMode.ITALIC_BOLD),
                        XEasyPdfHandler.Text.build("ITALIC_LIGHT").setFontSize(20F).setRenderingMode(XEasypdfTextRenderingMode.ITALIC_LIGHT),
                        XEasyPdfHandler.Text.build("HIDDEN").setFontSize(20F).setRenderingMode(XEasypdfTextRenderingMode.HIDDEN)
                )
        ).save(outputPath).close();
    }

    @Test
    public void test32() {
        final String sourcePath = OUTPUT_PATH + "doc3.pdf";
        XEasyPdfHandler.Document.load(sourcePath)
                .imager()
                .enableGray()
                .enableHorizontalMerge()
                .setDpi(144F)
                .image(OUTPUT_PATH, XEasyPdfImageType.JPEG)
                .finish()
                .close();
    }

    @Test
    public void test33() {
        final String sourcePath = OUTPUT_PATH + "doc3.pdf";
        XEasyPdfHandler.Document.load(sourcePath).print(1).close();
    }
}
