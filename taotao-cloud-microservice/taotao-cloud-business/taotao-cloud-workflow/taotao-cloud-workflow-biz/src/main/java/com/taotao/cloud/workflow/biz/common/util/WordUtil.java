/*
 * Copyright (c) 2020-2030, Shuigedeng (981376577@qq.com & https://blog.taotaocloud.top/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.taotao.cloud.workflow.biz.common.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.Cleanup;
import org.apache.poi.ooxml.POIXMLDocument;

/** */
public class WordUtil {

    /**
     * 根据模板生成新word文档 判断表格是需要替换还是需要插入，判断逻辑有$为替换，表格无$为插入
     *
     * @param inputUrl 模板存放地址
     * @param outputUrl 新文档存放地址
     * @param textMap 需要替换的信息集合
     * @param tableList 需要插入的表格信息集合
     * @return 成功返回true,失败返回false
     */
    public static void changWord(
            String inputUrl, String outputUrl, Map<String, String> textMap, List<String[]> tableList) {
        boolean changeFlag = true;
        try {
            @Cleanup XWPFDocument document = new XWPFDocument(POIXMLDocument.openPackage(inputUrl));
            changeText(document, textMap);
            changeTable(document, textMap, tableList);
            File file = new File(outputUrl);
            @Cleanup FileOutputStream stream = new FileOutputStream(file);
            document.write(stream);
            stream.close();
        } catch (IOException e) {
            LogUtils.error(e);
        }
    }

    /**
     * 替换段落文本
     *
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合
     */
    public static void changeText(XWPFDocument document, Map<String, String> textMap) {
        List<XWPFParagraph> paragraphs = document.getParagraphs();
        for (XWPFParagraph paragraph : paragraphs) {
            String text = paragraph.getText();
            if (checkText(text)) {
                List<XWPFRun> runs = paragraph.getRuns();
                for (XWPFRun run : runs) {
                    run.setText(changeValue(run.toString(), textMap), 0);
                }
            }
        }
    }

    /**
     * 替换表格对象方法
     *
     * @param document docx解析对象
     * @param textMap 需要替换的信息集合
     * @param tableList 需要插入的表格信息集合
     */
    public static void changeTable(XWPFDocument document, Map<String, String> textMap, List<String[]> tableList) {
        List<XWPFTable> tables = document.getTables();
        for (int i = 0; i < tables.size(); i++) {
            XWPFTable table = tables.get(i);
            if (table.getRows().size() > 1) {
                if (checkText(table.getText())) {
                    List<XWPFTableRow> rows = table.getRows();
                    eachTable(rows, textMap);
                } else {
                    insertTable(table, tableList);
                }
            }
        }
    }

    /**
     * 遍历表格
     *
     * @param rows 表格行对象
     * @param textMap 需要替换的信息集合
     */
    public static void eachTable(List<XWPFTableRow> rows, Map<String, String> textMap) {
        for (XWPFTableRow row : rows) {
            List<XWPFTableCell> cells = row.getTableCells();
            for (XWPFTableCell cell : cells) {
                if (checkText(cell.getText())) {
                    List<XWPFParagraph> paragraphs = cell.getParagraphs();
                    for (XWPFParagraph paragraph : paragraphs) {
                        List<XWPFRun> runs = paragraph.getRuns();
                        for (XWPFRun run : runs) {
                            run.setText(changeValue(run.toString(), textMap), 0);
                        }
                    }
                }
            }
        }
    }

    /**
     * 为表格插入数据，行数不够添加新行
     *
     * @param table 需要插入数据的表格
     * @param tableList 插入数据集合
     */
    public static void insertTable(XWPFTable table, List<String[]> tableList) {
        for (int i = 1; i < tableList.size(); i++) {
            XWPFTableRow row = table.createRow();
        }
        List<XWPFTableRow> rows = table.getRows();
        for (int i = 1; i < rows.size(); i++) {
            XWPFTableRow newRow = table.getRow(i);
            List<XWPFTableCell> cells = newRow.getTableCells();
            for (int j = 0; j < cells.size(); j++) {
                XWPFTableCell cell = cells.get(j);
                cell.setText(tableList.get(i - 1)[j]);
            }
        }
    }

    /**
     * 判断文本中时候包含$
     *
     * @param text 文本
     * @return 包含返回true,不包含返回false
     */
    public static boolean checkText(String text) {
        boolean check = false;
        if (text.indexOf("$") != -1) {
            check = true;
        }
        return check;
    }

    /**
     * 匹配传入信息集合与模板
     *
     * @param value 模板需要替换的区域
     * @param textMap 传入信息集合
     * @return 模板需要替换区域信息集合对应值
     */
    public static String changeValue(String value, Map<String, String> textMap) {
        Set<Map.Entry<String, String>> textSets = textMap.entrySet();
        for (Map.Entry<String, String> textSet : textSets) {
            String key = "${" + textSet.getKey() + "}";
            if (value.indexOf(key) != -1) {
                value = textSet.getValue();
            }
        }
        if (checkText(value)) {
            value = "";
        }
        return value;
    }
}
