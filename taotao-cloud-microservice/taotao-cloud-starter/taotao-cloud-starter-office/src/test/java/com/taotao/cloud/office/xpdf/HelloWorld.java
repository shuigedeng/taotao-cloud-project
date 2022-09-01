package com.taotao.cloud.office.xpdf;

import org.junit.jupiter.api.Test;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

/**
 * @author xsx
 * @date 2022/6/26
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
public class HelloWorld {

    @Test
    public void test() {
        // 设置加载系统字体
        System.setProperty(XEasyPdfHandler.FontMappingPolicy.key(), XEasyPdfHandler.FontMappingPolicy.ALL.name());
        // 构建pdf
        XEasyPdfHandler.Document.build(XEasyPdfHandler.Page.build(XEasyPdfHandler.Text.build("Hello World"))).save("E:\\pdf\\hello-world.pdf").close();
    }
}
