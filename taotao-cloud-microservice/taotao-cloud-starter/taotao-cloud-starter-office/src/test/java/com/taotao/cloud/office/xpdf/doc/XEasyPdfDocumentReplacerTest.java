package com.taotao.cloud.office.xpdf.doc;

import org.junit.Before;
import org.junit.Test;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;
import wiki.xsx.core.pdf.util.XEasyPdfTextUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * @author xsx
 * @date 2022/4/4
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
public class XEasyPdfDocumentReplacerTest {

    private static final String OUTPUT_PATH = "E:\\pdf\\test\\doc\\replace\\";

    @Before
    public void setup(){
        System.setProperty("org.apache.commons.logging.log", "org.apache.commons.logging.impl.SimpleLog");
        System.setProperty("org.apache.commons.logging.simplelog.defaultlog", "debug");
    }

    @Test
    public void testFill(){
        String sourcePath = OUTPUT_PATH+"temp.pdf";
        String filePath = OUTPUT_PATH+"testFill.pdf";
        String fontPath = "C:\\Windows\\Fonts\\simsun.ttc,0";
        Map<String, String> map = new HashMap<>(9);
        map.put("名称1", "测试报告");
        map.put(XEasyPdfTextUtil.escapeForRegex("{xxx-xx-xx}"), "2022-04-10");
        map.put("采购合同", "呼吸外科");
        map.put("no", "0001");
        map.put("name", "张三");
        map.put("sex", "男");
        map.put("age", "10");
        map.put("sign", "李某某");
        map.put("signTime", "2022-04-10 12:00:00");
        XEasyPdfHandler.Document
                .load(sourcePath)
                .replacer()
                .setFontPath(fontPath)
                .enableReplaceCOSArray()
                .replaceText(map)
                .finish(filePath);
    }

    /**
     * absolute
     */
    @Test
    public void testImage(){
        String sourcePath = OUTPUT_PATH+"temp.pdf";
        String filePath = OUTPUT_PATH+"testFill.pdf";
        XEasyPdfHandler.Document
                .load(sourcePath)
                .replacer()
                .replaceImage(null)
                .finish(filePath);
    }

}
