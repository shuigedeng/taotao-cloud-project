package com.taotao.cloud.office.xpdf.template;

import org.junit.Test;
import wiki.xsx.core.pdf.template.XEasyPdfTemplatePositionStyle;
import wiki.xsx.core.pdf.template.enums.XEasyPdfTemplatePositionStyle;
import wiki.xsx.core.pdf.template.handler.XEasyPdfTemplateHandler;

/**
 * @author xsx
 * @date 2022/8/6
 * @since 1.8
 * <p>
 * Copyright (c) 2020-2022 xsx All Rights Reserved.
 * x-easypdf is licensed under the Mulan PSL v2.
 * You can use this software according to the terms and conditions of the Mulan PSL v2.
 * You may obtain a copy of Mulan PSL v2 at:
 * http://license.coscl.org.cn/MulanPSL2
 * THIS SOFTWARE IS PROVIDED ON AN "AS IS" BASIS, WITHOUT WARRANTIES OF ANY KIND,
 * EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO NON-INFRINGEMENT,
 * MERCHANTABILITY OR FIT FOR A PARTICULAR PURPOSE.
 * See the Mulan PSL v2 for more details.
 * </p>
 */
public class XEasyPdfTemplateDocumentTest {

    @Test
    public void helloWorld() {
        XEasyPdfTemplateHandler.Document.build().addPage(XEasyPdfTemplateHandler.Page.build().addBodyComponent(XEasyPdfTemplateHandler.Text.build().setText("hello world").setHorizontalStyle(XEasyPdfTemplatePositionStyle.HORIZONTAL_CENTER))).transform("E:\\pdf\\test\\fo\\document.pdf");
    }

    @Test
    public void testXmlContent() {
        System.out.println(XEasyPdfTemplateHandler.Document.build().addPage(XEasyPdfTemplateHandler.Page.build()).getContent());
    }

    @Test
    public void testBlankPage() {
        // 定义fop配置文件路径
        String configPath = "E:\\pdf\\test\\fo\\fop.xconf";
        // 定义输出路径
        String outputPath = "E:\\pdf\\test\\fo\\template-document.pdf";
        // 转换pdf
        XEasyPdfTemplateHandler.Document.build().setConfigPath(configPath).addPage(
                XEasyPdfTemplateHandler.Page.build()
        ).transform(outputPath);
    }

    @Test
    public void testChinese() {
        XEasyPdfTemplateHandler.Document.build().addPage(
                XEasyPdfTemplateHandler.Page.build().addBodyComponent(
                        XEasyPdfTemplateHandler.Text.build().setText("你好贵阳")
                                .setHorizontalStyle(XEasyPdfTemplatePositionStyle.HORIZONTAL_CENTER)
                                .setFontFamily("微软雅黑")
                )
        ).transform("E:\\pdf\\test\\fo\\document.pdf");
    }
}
