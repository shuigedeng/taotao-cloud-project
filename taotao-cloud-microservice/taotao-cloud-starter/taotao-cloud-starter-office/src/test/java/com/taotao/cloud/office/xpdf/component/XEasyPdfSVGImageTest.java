package com.taotao.cloud.office.xpdf.component;

import org.junit.jupiter.api.Test;
import wiki.xsx.core.pdf.component.image.XEasyPdfImageType;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.io.File;

/**
 * @author xsx
 * @date 2022/7/20
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
public class XEasyPdfSVGImageTest {

    private static final String OUTPUT_PATH = "E:\\pdf\\test\\component\\image\\svg\\";

    @Test
    public void test() {
        long begin = System.currentTimeMillis();
        String svgFilePath = "E:\\pdf\\test\\component\\image\\svg\\asf-logo.svg";
        String outputPath = OUTPUT_PATH+"test.pdf";
        XEasyPdfHandler.Document.build(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.SVGImage.build()
                                .setImageType(XEasyPdfImageType.PNG)
                                .setWidth(100F)
                                .setHeight(30F)
                                .setImage(new File(svgFilePath))
                )
        ).save(outputPath).close();
        long end = System.currentTimeMillis();
        System.out.printf("finish: %s", (end-begin));
    }
}
