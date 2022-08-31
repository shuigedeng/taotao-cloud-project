package com.taotao.cloud.office.easypoi.easypoi;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;

import java.io.File;
import java.util.List;
import java.util.Map;

/**
 * @author by jueyue on 19-3-29.
 */
public class GongHeTiShu {

    public static void main(String[] args) {
        List<Map<String, String>> list = ExcelImportUtil.importExcel(new File(""), Map.class, new ImportParams());

        for (int i = 0; i < list.size(); i++) {

        }
    }
}
