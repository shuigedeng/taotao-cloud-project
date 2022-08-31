package com.taotao.cloud.office.xpdf.component;

import org.junit.Test;
import wiki.xsx.core.pdf.component.barcode.XEasyPdfBarCode;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.awt.*;
import java.io.IOException;

/**
 * @author xsx
 * @date 2021/12/31
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
public class XEasyPdfBarCodeTest {

    private static final String FONT_PATH = "C:\\Windows\\Fonts\\simfang.ttf";
    private static final String OUTPUT_PATH = "E:\\pdf\\test\\component\\barcode\\";

    @Test
    public void testBarcode() throws IOException {
        String filePath = OUTPUT_PATH + "testbarcode.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.BarCode.build(
                                XEasyPdfBarCode.CodeType.EAN_13,
                                "6922454332930"
                        ).setWords("EAN-13").enableShowWords().setMarginLeft(200F).setMarginTop(100F),
                        XEasyPdfHandler.BarCode.build(
                                XEasyPdfBarCode.CodeType.QR_CODE,
                                "https://www.xsx.wiki"
                        ).setWords("QR-CODE")
                                .setCodeMargin(0)
                                .enableShowWords()
                                .setWordsSize(30).setWordsColor(Color.BLUE).setWidth(200).setHeight(200).setMaxWidth(150).setMaxHeight(150).setMarginLeft(230F).setMarginTop(50F)
                ).setBackgroundColor(Color.GRAY)
        ).setFontPath(FONT_PATH).save(filePath).close();
        System.out.println("finish");
    }
}
