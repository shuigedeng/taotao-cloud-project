package com.taotao.cloud.office.xpdf.component;

import org.junit.Before;
import org.junit.Test;
import wiki.xsx.core.pdf.component.XEasyPdfPagingCondition;
import wiki.xsx.core.pdf.doc.XEasyPdfDocument;
import wiki.xsx.core.pdf.doc.XEasyPdfPage;
import wiki.xsx.core.pdf.doc.XEasyPdfPageRectangle;
import wiki.xsx.core.pdf.doc.XEasyPdfPositionStyle;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
public class XEasyPdfTextTest {

    private static final String FONT_PATH = "E:\\pdf\\msyhl.ttc,0";
    private static final String OUTPUT_PATH = "E:\\pdf\\test\\component\\text\\";

    @Before
    public void setup() {
        File dir = new File(OUTPUT_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void testText() throws IOException {
        String filePath = OUTPUT_PATH + "testText.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfPageRectangle.A4,
                        XEasyPdfHandler.Text.build(20F, "贵阳（贵州省省会）").setCharacterSpacing(10F).setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                        XEasyPdfHandler.Text.build(
                                "贵阳，简称“筑”，别称林城、筑城，是贵州省省会，国务院批复确定的中国西南地区重要的区域创新中心、中国重要的生态休闲度假旅游城市 [1]  。" +
                                        "截至2018年，全市下辖6个区、3个县、代管1个县级市，总面积8034平方千米，" +
                                        "建成区面积360平方千米，常住人口488.19万人，城镇人口368.24万人，城镇化率75.43%。"
                        ).setMarginLeft(10).setMarginRight(10).setAutoIndent(2).setCharacterSpacing(12F),
                        XEasyPdfHandler.Text.build(
                                "贵阳地处中国西南地区、贵州中部，是西南地区重要的中心城市之一 [3]  ，" +
                                        "贵州省的政治、经济、文化、科教、交通中心，西南地区重要的交通、通信枢纽、工业基地及商贸旅游服务中心 [4-5]  ，" +
                                        "全国综合性铁路枢纽 [6]  ，也是国家级大数据产业发展集聚区 [7]  、呼叫中心与服务外包集聚区 [8]  、大数据交易中心、数据中心集聚区。"
                        ).setMarginLeft(10).setMarginRight(10).setAutoIndent(2).setCharacterSpacing(6F),
                        XEasyPdfHandler.Text.build(
                                "贵阳之名较早见于明（弘治）《贵州图经新志》，因境内贵山之南而得名，元代始建顺元城，明永乐年间，" +
                                        "贵州建省，贵阳成为贵州省的政治、军事、经济、文化中心。境内有30多种少数民族，" +
                                        "有山地、河流、峡谷、湖泊、岩溶、洞穴、瀑布、原始森林、人文、古城楼阁等32种旅游景点 [10]  ，" +
                                        "是首个国家森林城市 [11]  、国家循环经济试点城市 [12]  、中国避暑之都 [13]  ，荣登“中国十大避暑旅游城市”榜首。 [14] "
                        ).setMarginLeft(10).setMarginRight(10).setAutoIndent(4),
                        XEasyPdfHandler.Text.build(
                                "2017年，复查确认保留全国文明城市称号。 [15]  2018年度《中国国家旅游》最佳优质旅游城市。 [16]  " +
                                        "2018年重新确认国家卫生城市。2019年1月12日，中国开放发展与合作高峰论坛暨第八届环球总评榜，" +
                                        "贵阳市荣获“2018中国国际营商环境标杆城市”“2018绿色发展和生态文明建设十佳城市”两项大奖。"
                        ).setMarginLeft(10).setMarginRight(10).setAutoIndent(4),
                        XEasyPdfHandler.Text.build("-- 摘自百度百科").setHorizontalStyle(XEasyPdfPositionStyle.RIGHT).setMarginRight(10F)
                ).setWatermark(
                        XEasyPdfHandler.Watermark.build("贵阳").setFontColor(new Color(51, 153, 255))
                )
        ).setVersion(1.7F).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testText2() throws IOException {
        String filePath = OUTPUT_PATH + "testText2.pdf";
        StringBuilder textBuild = new StringBuilder();
        for (int i = 0; i < 300; i++) {
            textBuild.append("分页分页分页分页分页分页分页分页分页分页分页");
        }
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build(
                                20F,
                                textBuild.toString()
                        ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                ).setHeader(
                        XEasyPdfHandler.Header.build(XEasyPdfHandler.Text.build("页眉").setHorizontalStyle(XEasyPdfPositionStyle.RIGHT))
                )
        ).setFontPath(FONT_PATH).setGlobalHeader(
                XEasyPdfHandler.Header.build(XEasyPdfHandler.Text.build("页眉"))
        ).setGlobalFooter(
                XEasyPdfHandler.Footer.build(XEasyPdfHandler.Text.build("页脚"))
        ).setGlobalWatermark(
                XEasyPdfHandler.Watermark.build("贵阳")
        ).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testText3() throws IOException {
        String filePath = OUTPUT_PATH + "testText3.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build(
                                20F,
                                "贵阳"
                        ).setMarginRight(50F).setMarginLeft(50F)
                )
        ).setFontPath(FONT_PATH).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testText4() throws IOException {
        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "testText4.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build(
                                14F,
                                "爽爽的贵阳"
                        ).setFontColor(Color.GREEN),
                        XEasyPdfHandler.Text.build(
                                14F,
                                "，"
                        ).enableTextAppend(),
                        XEasyPdfHandler.Text.build(
                                14F,
                                "避暑的天堂"
                        ).enableTextAppend().setFontColor(Color.cyan),
                        XEasyPdfHandler.Text.build(
                                14F,
                                "。"
                        ).enableTextAppend(),
                        XEasyPdfHandler.Text.build(
                                14F,
                                "贵阳，简称“筑”，别称林城、筑城，是贵州省省会，" +
                                        "国务院批复确定的中国西南地区重要的区域创新中心、中国重要的生态休闲度假旅游城市 [1]  ；" +
                                        "截至2020年11月，贵阳全市下辖6个区、3个县、代管1个县级市，" +
                                        "总面积8034平方公里，建成区面积360平方公里，常住人口497.14万人，城镇人口378.47万人，城镇化率76.13%。" +
                                        "777777777777777777777777777777"
                        ).enableTextAppend().setFontColor(Color.MAGENTA).enableUnderline().setUnderlineColor(Color.RED),
                        XEasyPdfHandler.Text.build(
                                30F,
                                "新的段落"
                        )
                ).setHeader(
                        XEasyPdfHandler.Header.build(
                                XEasyPdfHandler.Text.build("贵阳")
                        )
                )
        ).setFontPath(FONT_PATH).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end - begin));
    }

    @Test
    public void testText5() {
        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "testText5.pdf";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 1000; i++) {
            builder.append("爽爽的贵阳，避暑的天堂。");
        }
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfPageRectangle.A4,
                        XEasyPdfHandler.Text.build(builder.toString()).setAutoIndent(9)
                )
        ).setGlobalWatermark(
                XEasyPdfHandler.Watermark.build("爽爽的贵阳")
        ).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end - begin));
    }

    @Test
    public void testText6() {
        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "testText6.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfPageRectangle.A4,
                        XEasyPdfHandler.Text.build("2022心想事成10")
                                .setRadians(10D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成20")
                                .setRadians(20D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成30")
                                .setRadians(30D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成40")
                                .setRadians(40D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成45")
                                .setRadians(45D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成60")
                                .setRadians(60D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成70")
                                .setRadians(70D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成80")
                                .setRadians(80D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成90")
                                .setRadians(90D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成-10")
                                .setRadians(-10D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成-20")
                                .setRadians(-20D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成-30")
                                .setRadians(-30D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成-40")
                                .setRadians(-40D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成-45")
                                .setRadians(-45D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成-60")
                                .setRadians(-60D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成-70")
                                .setRadians(-70D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成-80")
                                .setRadians(-80D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline(),
                        XEasyPdfHandler.Text.build("2022心想事成-90")
                                .setRadians(-90D).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(30).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(2F).enableUnderline()
                )
        ).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end - begin));
    }

    @Test
    public void testText7() {
        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "testText7.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfPageRectangle.A4,
                        XEasyPdfHandler.Text.build("2022心想事成")
                                .setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginTop(10F)
                                .setFontSize(12F).setFontColor(Color.BLACK).setUnderlineColor(Color.RED)
                                .setUnderlineWidth(1F).enableUnderline()
                                .setHighlightColor(new Color(0x5EB7F0)).enableHighlight(),
                        XEasyPdfHandler.Text.build("爽爽的贵阳，避暑的天堂，也是堵车的天堂")
                                .setHorizontalStyle(XEasyPdfPositionStyle.RIGHT)
                                .setVerticalStyle(XEasyPdfPositionStyle.CENTER)
                                .setFontSize(12F).setFontColor(Color.BLACK)
                                .setDeleteLineWidth(1F).enableDeleteLine(),
                        XEasyPdfHandler.Text.build("112233")
                                .setVerticalStyle(XEasyPdfPositionStyle.BOTTOM)
                                .setHorizontalStyle(XEasyPdfPositionStyle.RIGHT)
                )
        ).setGlobalFooter(
                XEasyPdfHandler.Footer.build(
                        XEasyPdfHandler.Text.build(Arrays.asList("第一行", "第二行"))
                ).setMarginLeft(200F)
        ).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end - begin));
    }

    @Test
    public void testText10() {
        long begin = System.currentTimeMillis();
        String sourcePath = OUTPUT_PATH + "testText9.pdf";
        String filePath = OUTPUT_PATH + "testText10.pdf";
        XEasyPdfHandler.Document.load(sourcePath).setGlobalWatermark(
                XEasyPdfHandler.Watermark.build("爽爽的贵阳")
        ).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end - begin));
    }

    @Test
    public void testText11() {
        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "testText11.pdf";
        XEasyPdfHandler.Document.build(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("test")
                                .setPosition(0, 12F)
                                .setPagingCondition(new XEasyPdfPagingCondition() {
                                    @Override
                                    public boolean isPaging(XEasyPdfDocument document, XEasyPdfPage page, Float currentY) {
                                        float footerHeight = 0F;
                                        if (page.getFooter() != null) {
                                            footerHeight = page.getFooter().getHeight(document, page);
                                        }
                                        return currentY < footerHeight + 13;
                                    }
                                })
                )
        ).setGlobalWatermark(
                XEasyPdfHandler.Watermark.build("爽爽的贵阳")
        ).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end - begin));
    }

    @Test
    public void testTextLink() {
        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "testText12.pdf";
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            builder.append("hello world");
        }
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build(builder.toString())
                                .setLink("https://www.baidu.com")
                                .setComment("转跳百度地址")
                                .setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                )
        ).setGlobalWatermark(
                XEasyPdfHandler.Watermark.build("爽爽的贵阳")
        ).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end - begin));
    }

    @Test
    public void testRotateLine() {
        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "testText13.pdf";
        XEasyPdfPage page = XEasyPdfHandler.Page.build();
        for (int i = 0; i < 8; i++) {
            page.addComponent(
                    XEasyPdfHandler.Text.build("test-test-test")
                            .setRadians(i * 45D)
            );
        }
        for (int i = 0; i < 8; i++) {
            page.addComponent(
                    XEasyPdfHandler.Text.build("test-test-test")
                            .setPosition(300F, 700F)
                            .setRadians(i * 45D)
                            .enableRotateLine()
            );
        }
        XEasyPdfHandler.Document.build().addPage(page).setGlobalWatermark(
                XEasyPdfHandler.Watermark.build("爽爽的贵阳")
        ).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end - begin));
    }

    @Test
    public void testExtractorForm() {
        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "fillform.pdf";
        Map<String, String> data = new HashMap<>(2);
        XEasyPdfHandler.Document.load(filePath).extractor().extractForm(data).finish().close();
        System.out.println("data = " + data);
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end - begin));
    }

    @Test
    public void test() {
        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "test.pdf";
        XEasyPdfHandler.Document.build(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("LEFT").setWidth(200F).setMarginLeft(100F).setHorizontalStyle(XEasyPdfPositionStyle.LEFT),
                        XEasyPdfHandler.Text.build("MIDDLE").setWidth(200F).setMarginLeft(100F).setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                        XEasyPdfHandler.Text.build("RIGHT").setWidth(200F).setMarginLeft(100F).setHorizontalStyle(XEasyPdfPositionStyle.RIGHT)
                )
        ).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end - begin));
    }
}
