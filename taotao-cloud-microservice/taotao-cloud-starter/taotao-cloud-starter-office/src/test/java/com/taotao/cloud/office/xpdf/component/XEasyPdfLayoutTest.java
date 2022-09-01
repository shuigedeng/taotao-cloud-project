package com.taotao.cloud.office.xpdf.component;

import org.junit.jupiter.api.Test;
import wiki.xsx.core.pdf.component.XEasyPdfComponent;
import wiki.xsx.core.pdf.component.barcode.XEasyPdfBarCode;
import wiki.xsx.core.pdf.component.layout.XEasyPdfLayoutComponent;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * @author xsx
 * @date 2022/3/26
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
public class XEasyPdfLayoutTest {

    private static final String OUTPUT_PATH = "E:\\pdf\\test\\component\\layout\\";

    @Test
    public void testHorizontalLayout(){
        String filePath = OUTPUT_PATH + "testHorizontal.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build().addComponent(
                        XEasyPdfHandler.Layout.Horizontal.build().addLayoutComponent(
                                XEasyPdfHandler.Layout.Component.build(150F, 200F).setComponent(
                                        XEasyPdfHandler.Text.build("LEFT").enableCenterStyle().setContentMode(XEasyPdfComponent.ContentMode.APPEND)
                                )
                        )
                        .addLayoutComponent(
                                XEasyPdfHandler.Layout.Component.build(150F, 200F).setComponent(
                                        XEasyPdfHandler.BarCode.build(XEasyPdfBarCode.CodeType.QR_CODE, "https://www.baidu.com")
                                )
                        )
                        .addLayoutComponent(
                                XEasyPdfHandler.Layout.Component.build(150F, 200F).setComponent(
                                        XEasyPdfHandler.Text.build("RIGHT").enableCenterStyle().setContentMode(XEasyPdfComponent.ContentMode.APPEND)
                                )
                        )
                        .setMarginLeft(20F).setMarginTop(10F).enableBorder()
                )
        ).save(filePath).close();
    }

    @Test
    public void testVerticalLayout(){
        String filePath = OUTPUT_PATH + "testVertical.pdf";
        String imagePath = OUTPUT_PATH + "test.jpg";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build().addComponent(
                        XEasyPdfHandler.Layout.Vertical.build().addLayoutComponent(
                                new XEasyPdfLayoutComponent(100F, 200F).setComponent(
                                        XEasyPdfHandler.Text.build("自从上次v2.6.11稳定版发布以来，社区使用反馈相对比较平稳。这次的升级是小版本升级。修复了社区用户在使用中遇到的一些bug。").enableCenterStyle()
                                )
                        ).enableBorder()
                                .addLayoutComponent(
                                        XEasyPdfHandler.Layout.Component.build(100F, 200F).setComponent(
                                                XEasyPdfHandler.Image.build(new File(imagePath))
                                        )
                                ).enableBorder()
                                .addLayoutComponent(
                                        XEasyPdfHandler.Layout.Component.build(100F, 200F).setComponent(
                                                XEasyPdfHandler.Text.build("RIGHT").enableCenterStyle()
                                        )
                                ).enableBorder()
                )
        ).save(filePath).close();
    }

    @Test
    public void testLayout(){
        String filePath = OUTPUT_PATH + "testLayout.pdf";
        String imagePath = OUTPUT_PATH + "test.jpg";
        List<String> leftList = new ArrayList<>();
        List<String> rightList = new ArrayList<>();
        String content = "经过实际测试，目前在访问上述提及的四款广告拦截扩展的页面时，确实是会弹出以下提示";
        boolean isLeft = true;
        // 拆分文本
        while (content.length()>5) {
            if (isLeft) {
                leftList.add(content.substring(0, 5));
                isLeft = false;
            }else {
                rightList.add(content.substring(0, 5));
                isLeft = true;
            }
            content = content.substring(5);
        }
        rightList.add(content);
        // 添加页面
        XEasyPdfHandler.Document.build().addPage(
                // 添加组件
                XEasyPdfHandler.Page.build().addComponent(
                        // 构建垂直布局(包含三行)
                        XEasyPdfHandler.Layout.Vertical.build().setMarginLeft(150F).setMarginTop(200F)
                                // 添加布局组件(第一行)
                                .addLayoutComponent(
                                        // 构建布局组件
                                        XEasyPdfHandler.Layout.Component.build(300F, 20F).setComponent(
                                                // 构建水平布局
                                                XEasyPdfHandler.Layout.Horizontal.build()
                                                        // 添加布局组件
                                                        .addLayoutComponent(
                                                                // 构建布局组件
                                                                XEasyPdfHandler.Layout.Component.build(300F, 20F).setComponent(
                                                                        // 设置文本组件
                                                                        XEasyPdfHandler.Text.build("贵阳贵阳贵阳贵阳贵阳贵阳贵阳贵").setFontSize(20F)
                                                                )
                                                        )
                                        )
                                )
                                // 添加布局组件(第二行)
                                .addLayoutComponent(
                                        // 构建布局组件
                                        XEasyPdfHandler.Layout.Component.build(100F, 100F).setComponent(
                                                // 构建水平布局(包含三列)
                                                XEasyPdfHandler.Layout.Horizontal.build()
                                                        // 添加布局组件
                                                        .addLayoutComponent(
                                                                // 构建布局组件
                                                                XEasyPdfHandler.Layout.Component.build(100F, 100F).setComponent(
                                                                        // 设置文本组件
                                                                        XEasyPdfHandler.Text.build(leftList).setFontSize(20F)
                                                                )
                                                        )
                                                        // 添加布局组件
                                                        .addLayoutComponent(
                                                                // 构建布局组件
                                                                XEasyPdfHandler.Layout.Component.build(100F, 100F).setComponent(
                                                                        // 设置图片组件
                                                                        XEasyPdfHandler.Image.build(new File(imagePath)).disableSelfAdaption()
                                                                )
                                                        )
                                                        // 添加布局组件
                                                        .addLayoutComponent(
                                                                // 构建布局组件
                                                                XEasyPdfHandler.Layout.Component.build(100F, 100F).setComponent(
                                                                        // 设置文本组件
                                                                        XEasyPdfHandler.Text.build(rightList).setFontSize(20F)
                                                                )
                                                        )
                                        )
                                )
                                // 添加布局组件(第三行)
                                .addLayoutComponent(
                                        // 构建布局组件
                                        XEasyPdfHandler.Layout.Component.build(300F, 20F).setComponent(
                                                // 构建水平布局
                                                XEasyPdfHandler.Layout.Horizontal.build()
                                                        // 添加布局组件
                                                        .addLayoutComponent(
                                                                // 构建布局组件
                                                                XEasyPdfHandler.Layout.Component.build(300F, 20F).setComponent(
                                                                        // 设置文本组件
                                                                        XEasyPdfHandler.Text.build("贵阳贵阳贵阳贵阳贵阳贵阳贵阳贵").setFontSize(20F)
                                                                )
                                                        )
                                        )
                                )
                )
                // 保存并关闭
        ).save(filePath).close();
    }
}
