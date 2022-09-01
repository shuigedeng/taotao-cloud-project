package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;

import cn.afterturn.easypoi.test.entity.temp.BudgetAccountsEntity;
import cn.afterturn.easypoi.test.entity.temp.PayeeEntity;
import cn.afterturn.easypoi.test.entity.temp.TemplateExcelExportEntity;
import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.export.styler.ExcelExportStylerColorImpl;
import cn.afterturn.easypoi.util.PoiMergeCellUtil;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Lists;

public class TemplateForEachTest {

    @Test
    public void test() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
            "doc/foreach.xlsx");
        Map<String, Object> map = new HashMap<String, Object>();
        List<TemplateExcelExportEntity> list = new ArrayList<TemplateExcelExportEntity>();

        for (int i = 0; i < 4; i++) {
            TemplateExcelExportEntity entity = new TemplateExcelExportEntity();
            entity.setIndex(i + 1 + "");
            entity.setAccountType("开源项目");
            entity.setProjectName("EasyPoi " + i + "期");
            entity.setAmountApplied(i * 10000 + "");
            entity.setApprovedAmount((i + 1) * 10000 - 100 + "");
            list.add(entity);
        }
        map.put("entitylist", list);
        map.put("manmark", "1");
        map.put("letest", "12345678");
        map.put("fntest", "12345678.2341234");
        map.put("fdtest", null);
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 4; i++) {
            Map<String, Object> testMap = new HashMap<String, Object>();

            testMap.put("id", "xman");
            testMap.put("name", "小明" + i);
            testMap.put("sex", "1");
            mapList.add(testMap);
        }
        map.put("maplist", mapList);

        mapList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 6; i++) {
            Map<String, Object> testMap = new HashMap<String, Object>();

            testMap.put("si", "xman");
            mapList.add(testMap);
        }
        map.put("sitest", mapList);
        //本来导出是专业那个 
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        PoiMergeCellUtil.mergeCells(workbook.getSheetAt(0), 1, 0, 4);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/foreach.xlsx");
        workbook.write(fos);
        fos.close();
    }

}
