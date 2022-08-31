package com.taotao.cloud.office.xpdf.component;

import lombok.*;
import lombok.experimental.Accessors;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.junit.Before;
import org.junit.Test;
import wiki.xsx.core.pdf.component.XEasyPdfComponent;
import wiki.xsx.core.pdf.component.table.XEasyPdfCell;
import wiki.xsx.core.pdf.component.table.XEasyPdfRow;
import wiki.xsx.core.pdf.component.table.XEasyPdfTable;
import wiki.xsx.core.pdf.doc.*;
import wiki.xsx.core.pdf.handler.XEasyPdfHandler;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
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
public class XEasyPdfTableTest {

    private static final String FONT_PATH = "C:\\Windows\\Fonts\\simfang.ttf";
    private static final String FONT_PATH2 = "C:\\Windows\\Fonts\\msyh.ttf";
    private static final String OUTPUT_PATH = "E:\\pdf\\test\\component\\table\\";

    @Before
    public void setup() {
        File dir = new File(OUTPUT_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Test
    public void testTable1() throws IOException {
        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "testTable1.pdf";
        List<XEasyPdfRow> rowList = new ArrayList<>(50);
        List<XEasyPdfCell> cellList;
        for (int i = 0; i < 100; i++) {
            cellList = new ArrayList<>(5);
            for (int j = 0; j < 5; j++) {
                cellList.add(
                        i % 2 == 0 ?
                                XEasyPdfHandler.Table.Row.Cell.build(100F, 90F).addContent(
                                        XEasyPdfHandler.Text.build("row" + i + "-cell" + j + "中文中文中文中文中文中文中文中文中文中文中文中文")
                                ) :
                                XEasyPdfHandler.Table.Row.Cell.build(100F, 90F).addContent(
                                        XEasyPdfHandler.Text.build("row" + i + "-cell" + j + "中文中文中文中文中文中文中文中文中文中文中文中文")
                                ).setBackgroundColor(new Color(0, 191, 255))
                );
            }
            rowList.add(XEasyPdfHandler.Table.Row.build(cellList));
        }

        XEasyPdfHandler.Document.build().setGlobalHeader(
                XEasyPdfHandler.Header.build(
                        XEasyPdfHandler.Text.build(
                                "页眉第" + XEasyPdfHandler.Page.getCurrentPagePlaceholder() + "页，共13页"
                        ).setFontSize(20F).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                )
        ).addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("title"),
                        XEasyPdfHandler.Table.build(rowList).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginLeft(50F).setMarginBottom(50F)
                )
        ).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("finish，耗时：" + (end - begin) + " ms");
    }

    @Test
    public void testTable11() throws IOException {
        Map<String, String> replace = new HashMap<>();
        replace.put("\r", "r");
        replace.put("\n", "n");
        replace.put("\t", "t");
        replace.put("中", "zhong");

        long begin = System.currentTimeMillis();
        String filePath = OUTPUT_PATH + "testTable11.pdf";
        List<XEasyPdfRow> rowList = new ArrayList<>(50);
        List<XEasyPdfCell> cellList;
        for (int i = 0; i < 100; i++) {
            cellList = new ArrayList<>(5);
            for (int j = 0; j < 5; j++) {
                cellList.add(
                        i % 2 == 0 ?
                                XEasyPdfHandler.Table.Row.Cell.build(100F, 90F).addContent(
                                        XEasyPdfHandler.Text.build("row" + i + "-cell" + j + "中文中文中文中文中文中文中 \r \n \t  文中文中文中文中文中文")
                                ) :
                                XEasyPdfHandler.Table.Row.Cell.build(100F, 90F).addContent(
                                        XEasyPdfHandler.Text.build(10F, "row" + i + "-cell" + j + "中文中文中文中文中文中文中 \r \n \t 文中文中文中文中文中文")
                                ).setBackgroundColor(new Color(0, 191, 255))
                );
            }
            rowList.add(XEasyPdfHandler.Table.Row.build(cellList));
        }

        XEasyPdfHandler.Document.build().setGlobalHeader(
                XEasyPdfHandler.Header.build(
                        XEasyPdfHandler.Text.build(
                                "页眉第" + XEasyPdfHandler.Page.getCurrentPagePlaceholder() + "页，共13页"
                        ).setFontSize(20F).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                )
        ).addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Text.build("title 我无特殊字符"),
                        XEasyPdfHandler.Text.build("title \r \n \t 我有特殊字符"),
                        XEasyPdfHandler.Text.build("title \r \n \t 我有特殊字符"),
                        XEasyPdfHandler.Text.build(Arrays.asList("title \r \n \t 我有特殊字符", "中国")),
                        XEasyPdfHandler.Text.build(Arrays.asList("title \r \n \t 我有特殊字符", "中国")),
                        XEasyPdfHandler.Text.build(10, "title \r \n \t 我有特殊字符"),
                        XEasyPdfHandler.Text.build(10, "title \r \n \t 我有特殊字符"),
                        XEasyPdfHandler.Text.build(10, Arrays.asList("title \r \n \t 我有特殊字符", "中国")),
                        XEasyPdfHandler.Text.build(10, Arrays.asList("title \r \n \t 我有特殊字符", "中国")),
                        XEasyPdfHandler.Table.build(rowList).setHorizontalStyle(XEasyPdfPositionStyle.CENTER).setMarginLeft(50F).setMarginBottom(50F)
                )
        ).save(filePath).close();
        long end = System.currentTimeMillis();
        System.out.println("finish，耗时：" + (end - begin) + " ms");
    }

    @SneakyThrows
    @Test
    public void testTable2() throws IOException {
        String filePath = OUTPUT_PATH + "testTable2.pdf";
        for (int i = 1; i < 11; i++) {
            XEasyPdfHandler.Document.build().addPage(
                    XEasyPdfHandler.Page.build(
                            XEasyPdfHandler.Table.build(
                                    XEasyPdfHandler.Table.Row.build(
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build("1-1")
                                            ),
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build("1-2")
                                            ),
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build("1-3")
                                            ),
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build("1-4")
                                            )
                                    ).setHorizontalStyle(XEasyPdfPositionStyle.LEFT),
                                    XEasyPdfHandler.Table.Row.build(
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build("2-1")
                                            ),
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build("2-2")
                                            ),
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build("2-3")
                                            ),
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build("2-4")
                                            )
                                    ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                                    XEasyPdfHandler.Table.Row.build(
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build(Arrays.asList("3-1")).enableSelfStyle().setVerticalStyle(XEasyPdfPositionStyle.CENTER)
                                            ),
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build("3-2")
                                            ),
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build("3-3")
                                            ),
                                            XEasyPdfHandler.Table.Row.Cell.build(100F, 15F).addContent(
                                                    XEasyPdfHandler.Text.build("3-4")
                                            )
                                    ).setHorizontalStyle(XEasyPdfPositionStyle.RIGHT)
                            ).setMarginLeft(100F).setBorderLineLength(i).setBorderLineSpace(i)
                    )
            ).setFontPath(FONT_PATH).save(filePath).close();
            Thread.sleep(5000L);
        }
        System.out.println("finish");
    }

    @Test
    public void testTable3() throws IOException {
        String imagePath = OUTPUT_PATH + "222.jpg";
        String filePath = OUTPUT_PATH + "testTable3.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Table.build(
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 150F).addContent(
                                                XEasyPdfHandler.Image.build(new File(imagePath)).setWidth(50F).setHeight(50F).setMarginBottom(20F)
                                        ).setBackgroundColor(Color.BLUE).setVerticalStyle(XEasyPdfPositionStyle.BOTTOM),
                                        XEasyPdfHandler.Table.Row.Cell.build(200F, 50F).addContent(
                                                XEasyPdfHandler.Image.build(new File(imagePath))
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 150F).addContent(
                                                XEasyPdfHandler.Text.build(Arrays.asList("3-4-1", "3-4-2", "3-4-3"))
                                        )
                                ).setHeight(50F),
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).enableVerticalMerge(),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).addContent(
                                                XEasyPdfHandler.Text.build("2-1").setMarginLeft(20F)
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).addContent(
                                                XEasyPdfHandler.Text.build("2-2")
                                        )
                                ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                                , XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).enableVerticalMerge(),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).addContent(
                                                XEasyPdfHandler.Text.build("3-2")
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).addContent(
                                                XEasyPdfHandler.Text.build("3-3")
                                        )
                                ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                        ).setMarginLeft(100F).setMarginTop(100F).setVerticalStyle(XEasyPdfPositionStyle.CENTER).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                )
        ).setFontPath(FONT_PATH).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testTable4() throws IOException {
        String filePath = OUTPUT_PATH + "testTable4.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Table.build(
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("1")
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("2")
                                        ).setMarginLeft(100F)
                                ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("3")
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("4")
                                        ).setMarginLeft(100F)
                                ).setMarginTop(100F).setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("5")
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("6")
                                        ).setMarginLeft(100F)
                                ).setMarginTop(100F).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                        ).setMarginLeft(150F)
                )
        ).setFontPath(FONT_PATH).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testTable5() throws IOException {
        String filePath = OUTPUT_PATH + "testTable5.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Table.build(
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("1")
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("2")
                                        ).setMarginLeft(100F)
                                ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(101F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("5")
                                        ).setMarginLeft(99F)
                                ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("3")
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("4")
                                        ).setMarginLeft(100F)
                                ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                        ).setMarginLeft(150F)
                )
        ).setFontPath(FONT_PATH).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testTable6() throws IOException {
        String filePath = OUTPUT_PATH + "testTable6.pdf";
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Table.build(
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("1")
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("2")
                                        ).setMarginLeft(50F)
                                ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(101F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("3")
                                        ).setMarginLeft(74F)
                                ).setMarginTop(-50F).setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("4")
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("5")
                                        ).setMarginLeft(50F)
                                ).setMarginTop(-50F).setHorizontalStyle(XEasyPdfPositionStyle.CENTER),
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(101F, 100F).addContent(
                                                XEasyPdfHandler.Text.build("6")
                                        ).setMarginLeft(74F)
                                ).setMarginTop(-50F).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                        ).setMarginLeft(150F)
                )
        ).setFontPath(FONT_PATH).save(filePath).close();
        System.out.println("finish");
    }

    @Test
    public void testTable7() {
        String filePath = OUTPUT_PATH + "testTable7.pdf";
        List<XEasyPdfRow> rows = new ArrayList<>(10);
        rows.add(
                XEasyPdfHandler.Table.Row.build(
                        XEasyPdfHandler.Table.Row.Cell.build(100F, 16F).addContent(
                                XEasyPdfHandler.Text.build("部门")
                        ),
                        XEasyPdfHandler.Table.Row.Cell.build(99F, 16F).addContent(
                                XEasyPdfHandler.Text.build("XXXXXX")
                        ), XEasyPdfHandler.Table.Row.Cell.build(99F, 16F).addContent(
                                XEasyPdfHandler.Text.build("申请时间")
                        ), XEasyPdfHandler.Table.Row.Cell.build(199F, 16F).addContent(
                                XEasyPdfHandler.Text.build("2020-01-01 00:00:00")
                        )
                ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
        );
        rows.add(
                XEasyPdfHandler.Table.Row.build(
                        XEasyPdfHandler.Table.Row.Cell.build(100F, 32F).addContent(
                                XEasyPdfHandler.Text.build("报修内容").setMarginTop(10F)
                        ), XEasyPdfHandler.Table.Row.Cell.build(395F, 32F).addContent(
                                XEasyPdfHandler.Text.build("XXXXXXXXXXXXXXXXXXXXXX").setHorizontalStyle(XEasyPdfPositionStyle.LEFT).enableSelfStyle()
                        )
                ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
        );
        for (int i = 0; i < 3; i++) {
            rows.add(
                    XEasyPdfHandler.Table.Row.build(
                            XEasyPdfHandler.Table.Row.Cell.build(100F, 16F).addContent(
                                    XEasyPdfHandler.Text.build("派单人")
                            ),
                            XEasyPdfHandler.Table.Row.Cell.build(99F, 16F).addContent(
                                    XEasyPdfHandler.Text.build("XXXXXX")
                            ), XEasyPdfHandler.Table.Row.Cell.build(99F, 16F).addContent(
                                    XEasyPdfHandler.Text.build("派单")
                            ), XEasyPdfHandler.Table.Row.Cell.build(199F, 16F).addContent(
                                    XEasyPdfHandler.Text.build("2020-01-01 00:00:00")
                            )
                    ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
            );
        }
        rows.add(
                XEasyPdfHandler.Table.Row.build(
                        XEasyPdfHandler.Table.Row.Cell.build(100F, 16F).addContent(
                                XEasyPdfHandler.Text.build("分管签字")
                        ),
                        XEasyPdfHandler.Table.Row.Cell.build(99F, 16F).addContent(
                                XEasyPdfHandler.Text.build("")
                        ), XEasyPdfHandler.Table.Row.Cell.build(99F, 16F).addContent(
                                XEasyPdfHandler.Text.build("用户签字")
                        ), XEasyPdfHandler.Table.Row.Cell.build(199F, 16F).addContent(
                                XEasyPdfHandler.Text.build("")
                        )
                ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
        );
        XEasyPdfHandler.Document.build().addPage(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Table.build(rows).setMarginLeft(50F)
                )
        ).save(filePath).close();
    }

    @Test
    public void testTable8() {
        String filePath = OUTPUT_PATH + "testTable8.pdf";
        List<XEasyPdfComponent> tables = new ArrayList<>(5);
        for (int x = 0; x < 5; x++) {
            List<XEasyPdfRow> rows = new ArrayList<>(2);
            List<XEasyPdfCell> cells;
            for (int i = 0; i < 5; i++) {
                cells = new ArrayList<>(5);
                for (int j = 0; j < 5; j++) {
                    cells.add(
                            XEasyPdfHandler.Table.Row.Cell.build(100F, 100F).addContent(
                                    XEasyPdfHandler.Text.build(i + "" + j + "-" + x + "table")
                            ).setBackgroundColor(new Color(0, 200, 200))
                    );
                }
                rows.add(XEasyPdfHandler.Table.Row.build(cells));
            }
            XEasyPdfTable table = XEasyPdfHandler.Table.build(rows);
            table.setMarginLeft(50F).setMarginTop(50F);
            tables.add(table);
        }
        XEasyPdfHandler.Document.build().addPage(
                        XEasyPdfHandler.Page.build().addComponent(tables)
                ).setGlobalHeader(
                        XEasyPdfHandler.Header.build(
//                        XEasyPdfHandler.Image.build(new File("C:\\Users\\Administrator\\Desktop\\QQ截图20211010155457.png")).setWidth(700F).setHeight(40F).disableSelfAdaption()
                                XEasyPdfHandler.Text.build(
                                        "第" + XEasyPdfHandler.Page.getCurrentPagePlaceholder() + "页, " +
                                                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" +
                                                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX" +
                                                "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX"
                                ).setHorizontalStyle(XEasyPdfPositionStyle.CENTER)
                        )
                ).bookmark()
                .setBookMark(0, "第1页")
                .setBookMark(2, "第3页")
                .finish().save(filePath).close();
    }

    @Test
    public void testTable9() {
        String filePath = OUTPUT_PATH + "testTable9.pdf";
        // 构建文档
        XEasyPdfHandler.Document.build(
                // 构建页面
                XEasyPdfHandler.Page.build(
                        // 构建表格
                        XEasyPdfHandler.Table.build(
                                // 第一行
                                XEasyPdfHandler.Table.Row.build(
                                        // 第一列
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("1")
                                        ),
                                        // 第二列
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("2")
                                        ),
                                        // 第三列
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("3")
                                        ),
                                        // 第四列
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("4")
                                        ),
                                        // 第五列
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("5")
                                        )
                                ),
                                // 第二行
                                XEasyPdfHandler.Table.Row.build(
                                        // 第一列
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("1")
                                        ),
                                        // 第二列，第三列合并，直接设置宽高
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 50F).addContent(
                                                XEasyPdfHandler.Text.build("合并两行两列")
                                        ),
                                        // 第四列
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("4")
                                        ),
                                        // 第五列合并，直接设置合并高度
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 75F).addContent(
                                                XEasyPdfHandler.Text.build("合并三行")
                                        )
                                        // 合并行，设置平均行高
                                ).setHeight(25F),
                                // 第三行
                                XEasyPdfHandler.Table.Row.build(
                                        // 第一列
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("1")
                                        ),
                                        // 第二列，第三列合并，并设置垂直合并（占位）
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 25F).enableVerticalMerge(),
                                        // 第四列
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("4")
                                        ),
                                        // 第五列设置垂直合并
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).enableVerticalMerge()
                                ),
                                // 第四行
                                XEasyPdfHandler.Table.Row.build(
                                        // 第一列，第二列合并，直接设置合并宽度
                                        XEasyPdfHandler.Table.Row.Cell.build(100F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("合并两列")
                                        ),
                                        // 第三列
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("3")
                                        ),
                                        // 第四列
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).addContent(
                                                XEasyPdfHandler.Text.build("4")
                                        ),
                                        // 第五列设置垂直合并
                                        XEasyPdfHandler.Table.Row.Cell.build(50F, 25F).enableVerticalMerge()
                                )
                        )
                )
        ).save(filePath).close();
    }

    @Test
    public void testTable10() {
        String filePath = OUTPUT_PATH + "testTable10.pdf";
        XEasyPdfHandler.Document.build(
                XEasyPdfHandler.Page.build(
                        XEasyPdfHandler.Table.build(
                                XEasyPdfHandler.Table.Row.build(
                                        XEasyPdfHandler.Table.Row.Cell.build(100f, 30f).addContent(
                                                XEasyPdfHandler.Text.build("1")
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100f, 30f).addContent(
                                                XEasyPdfHandler.Text.build("2")
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100f, 30f).addContent(
                                                XEasyPdfHandler.Text.build("3")
                                        ),
                                        XEasyPdfHandler.Table.Row.Cell.build(100f, 30f).addContent(
                                                XEasyPdfHandler.Text.build("4")
                                        ).setLeftBorderColor(Color.RED).setRightBorderColor(Color.YELLOW).setTopBorderColor(Color.BLUE).setBottomBorderColor(Color.GREEN)
                                )
                        ).setTitle(
                                XEasyPdfHandler.Table.build(
                                        XEasyPdfHandler.Table.Row.build(
                                                XEasyPdfHandler.Table.Row.Cell.build(100.5f, 30f).addContent(
                                                        XEasyPdfHandler.Text.build("T1")
                                                ),
                                                XEasyPdfHandler.Table.Row.Cell.build(100f, 30f).addContent(
                                                        XEasyPdfHandler.Text.build("T2")
                                                ),
                                                XEasyPdfHandler.Table.Row.Cell.build(100f, 30f).addContent(
                                                        XEasyPdfHandler.Text.build("T3")
                                                ),
                                                XEasyPdfHandler.Table.Row.Cell.build(100.5f, 30f).addContent(
                                                        XEasyPdfHandler.Text.build("T4")
                                                )
                                        ).setBorderPolicy(XEasyPdfRow.BorderPolicy.NONE).setBackgroundColor(Color.GRAY).setMarginLeft(49.5F)
                                )
                        ).setMarginLeft(50F)
                )
        ).save(filePath).close();
    }

    @Test
    public void testTable12() {
        TestDemo.testWritePdf();
    }

    static class TestDemo {

        public static void testWritePdf() {
            List<PdfModel<?>> list = new ArrayList<>();
            PdfModel<WbDTO2> table = new PdfModel<>();
            table.setType(PdfContentType.TABLE);
            List<WbDTO2> dataList = new ArrayList<>();
            for (int i = 0; i < 120; i++) {
                dataList.add(new WbDTO2().setDeviceId("deviceId-" + i).setDeviceName("设备" + (i + 1))
                        .setLocalName("南山区金证大楼/" + (i + 1) + "楼").setType("虚拟设备").setProductKey("kt")
                        .setProductId("productId-" + i));
            }
            table.setTableData(dataList);
            list.add(table);
            write(list, PDRectangle.A4);
        }

        /**
         * 导出PDF
         *
         * @param dataList  数据
         * @param rectangle 纸张大小，默认A4
         * @author 杨昌海
         * @date 2022/5/19
         */
        public static void write(List<PdfModel<?>> dataList, PDRectangle rectangle) {
            //todo 支持指定换页
            rectangle = rectangle == null ? PDRectangle.A4 : rectangle;
            List<XEasyPdfComponent> components = new ArrayList<>();
            for (PdfModel<?> pdfModel : dataList) {
                XEasyPdfComponent component = null;
                switch (pdfModel.getType()) {
                    case TABLE:
                        component = createTable(pdfModel.getTableData(), rectangle);
                        break;
                    default:
                        break;
                }
                if (component != null) {
                    components.add(component);
                }
            }
            XEasyPdfPage page = XEasyPdfHandler.Page.build(XEasyPdfPageRectangle.create(rectangle.getWidth(), rectangle.getHeight()), components);
            XEasyPdfDocument document = XEasyPdfHandler.Document.build(page);
            document.save(OUTPUT_PATH + "/table10.pdf").close();
        }

        private static <T> XEasyPdfTable createTable(List<T> data, PDRectangle rectangle) {
            List<XEasyPdfRow> rows = new ArrayList<>();
            List<XEasyPdfCell> header = new ArrayList<>();
            int column = 0;
            float columnWith = 0;
            for (T datum : data) {
                Class<?> aClass = datum.getClass();
                Field[] fields = aClass.getDeclaredFields();
                if (column == 0) {
                    for (Field field : fields) {
                        field.setAccessible(true);
                        PdfTableProperty annotation = field.getAnnotation(PdfTableProperty.class);
                        if (annotation != null) {
                            column++;
                        }
                    }
                    columnWith = rectangle.getWidth() / column;
                    for (Field field : fields) {
                        field.setAccessible(true);
                        PdfTableProperty annotation = field.getAnnotation(PdfTableProperty.class);
                        if (annotation != null) {
                            String title = annotation.title();
                            header.add(
                                    XEasyPdfHandler.Table.Row.Cell.build(columnWith)
                                            .addContent(XEasyPdfHandler.Text.build(title))
                                            .setFontSize(16F)
                                            .setDefaultFontStyle(XEasyPdfDefaultFontStyle.BOLD)
                                            .setHorizontalStyle(XEasyPdfPositionStyle.LEFT)
                            );
                        }
                    }
                    //表头
                    rows.add(XEasyPdfHandler.Table.Row.build(header));
                }
                List<XEasyPdfCell> cells = new ArrayList<>();
                for (Field field : fields) {
                    field.setAccessible(true);
                    Object value = null;
                    try {
                        value = field.get(datum);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                    value = value == null ? "" : value;
                    cells.add(XEasyPdfHandler.Table.Row.Cell.build(columnWith)
                            .addContent(XEasyPdfHandler.Text.build(value.toString()))
                            .setHorizontalStyle(XEasyPdfPositionStyle.LEFT)
                    );
                }
                rows.add(XEasyPdfHandler.Table.Row.build(cells));
            }
            return XEasyPdfHandler.Table.build(rows);
        }

        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        @Builder
        static
        class PdfModel<T> {
            /**
             * 内容类型，默认文本
             */
            private PdfContentType type = PdfContentType.TEXT;

            /**
             * type为TABLE表格的业务数据
             */
            private List<T> tableData;
        }

        @Accessors(chain = true)
        @Data
        @AllArgsConstructor
        @NoArgsConstructor
        static class WbDTO2 {

            @PdfTableProperty(title = "设备名称")
            private String deviceName;

            @PdfTableProperty(title = "设备id")
            private String deviceId;

            @PdfTableProperty(title = "产品名称")
            private String productName;

            @PdfTableProperty(title = "产品标识")
            private String productKey;

            @PdfTableProperty(title = "产品ID")
            private String productId;

            @PdfTableProperty(title = "设备位置")
            private String localName;

            @PdfTableProperty(title = "描述")
            private String type;
        }

        /**
         * PDF table表格注解
         *
         * @author 杨昌海
         * @date 2022/5/18
         */
        @Target(ElementType.FIELD)
        @Retention(RetentionPolicy.RUNTIME)
        @interface PdfTableProperty {

            /**
             * 表头名称
             */
            String title();
        }

        enum PdfContentType {
            //内容格式
            //表格
            TABLE,
            //表单
            FORM,
            //文本
            TEXT,
            //图片
            IMAGE,
            //分隔符/横线
            LINE,
            //二维码
            BAR_CODE,
            //水印
            WATERMARK,
            ;
        }
    }
}
