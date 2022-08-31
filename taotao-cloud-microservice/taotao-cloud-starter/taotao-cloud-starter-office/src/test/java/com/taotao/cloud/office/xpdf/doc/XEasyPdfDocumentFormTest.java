package com.taotao.cloud.office.xpdf.doc;

import org.junit.Test;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

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
public class XEasyPdfDocumentFormTest {

    private static final String OUTPUT_PATH = "E:\\pdf\\test\\doc\\form\\";

    @Test
    public void testCreate(){
        String filePath = OUTPUT_PATH + "testCreate.pdf";
        XEasyPdfHandler.Document
                // 创建文档
                .build()
                // 添加页面
                .addPage(
                        // 创建空白页
                        XEasyPdfHandler.Page.build()
                )
                // 获取表单填写器
                .formFiller()
                // 创建表单
                .create()
                // 创建第一个文本属性
                .createTextField()
                // 设置映射名称
                .setMappingName("property1")
                // 设置位置坐标
                .setPosition(50F,700F)
                // 开启打印
                .enablePrint()
                // 完成文本属性创建
                .finish()
                // 创建第二个文本属性
                .createTextField()
                // 设置映射名称
                .setMappingName("property2")
                // 设置位置坐标
                .setPosition(200F,700F)
                // 设置默认值
                .setDefaultValue("test")
                // 设置最大字符数
                .setMaxLength(11)
                // 完成文本属性创建
                .finish()
                // 完成表单操作
                .finish()
                // 完成填写器操作
                .finish(filePath);
    }

    @Test
    public void testFill(){
        long begin = System.currentTimeMillis();
        String sourcePath = OUTPUT_PATH + "template.pdf";
        String filePath = OUTPUT_PATH + "testFill.pdf";
        Map<String, String> map = new HashMap<>(5);
        map.put("title", "贵阳市");
        map.put("Text1", "贵州省南明区花果园");
        map.put("Text2", "贵阳2");
        map.put("Text3", "可以的 2022-04-06");
        map.put("Text4", "2022-04-06 10:00:00");
        XEasyPdfHandler.Document
                .load(sourcePath)
//                .setDefaultFontStyle(XEasyPdfDefaultFontStyle.BOLD)
                .formFiller()
//                .setDefaultFontStyle(XEasyPdfDefaultFontStyle.LIGHT)
                .enableReadOnly()
                .enableAppearance()
//                .enableCompress()
                .fill(map)
                .finish(filePath);
        long end = System.currentTimeMillis();
        System.out.printf("finish: %sms", (end-begin));
    }
}
