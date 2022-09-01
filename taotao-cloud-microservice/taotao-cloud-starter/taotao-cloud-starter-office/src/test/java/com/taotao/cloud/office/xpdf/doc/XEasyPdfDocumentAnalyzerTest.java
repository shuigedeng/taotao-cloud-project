package com.taotao.cloud.office.xpdf.doc;

import org.junit.Before;
import org.junit.jupiter.api.Test;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.io.IOException;

/**
 * @author xsx
 * @date 2022/4/19
 * @since 1.8
 * <p>
 * Copyright (c) 2022 xsx All Rights Reserved.
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
public class XEasyPdfDocumentAnalyzerTest {

    private static final String FILE_PATH = "E:\\pdf\\test\\test\\allTest.pdf";

    @Before
    public void setup() {
        // 初始化日志实现
        System.setProperty("org.apache.commons.logging.log", "org.apache.commons.logging.impl.SimpleLog");
        // 初始化日志级别
        System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "debug");
    }

    @Test
    public void analyzeText() throws IOException {
        long begin = System.currentTimeMillis();
        XEasyPdfHandler.Document.load(FILE_PATH).analyzer().analyzeText().finish();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end-begin));
    }

    @Test
    public void analyzeImage() throws IOException {
        long begin = System.currentTimeMillis();
        XEasyPdfHandler.Document.load(FILE_PATH).analyzer().analyzeImage().finish();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end-begin));
    }

    @Test
    public void analyzeBookmark() throws IOException {
        long begin = System.currentTimeMillis();
        XEasyPdfHandler.Document.load(FILE_PATH).analyzer().analyzeBookmark().finish();
        long end = System.currentTimeMillis();
        System.out.println("完成，耗时： " + (end-begin));
    }
}
