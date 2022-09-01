package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;

public class ExcelExportForMap {
    /**
     * Map 测试
     */
    @Test
    public void test() {
        try {
            List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
            ExcelExportEntity excelentity = new ExcelExportEntity("姓名", "name");
            excelentity.setNeedMerge(true);
            entity.add(excelentity);
            entity.add(new ExcelExportEntity("性别", "sex"));
            excelentity = new ExcelExportEntity(null, "students");
            List<ExcelExportEntity> temp = new ArrayList<ExcelExportEntity>();
            temp.add(new ExcelExportEntity("姓名", "name"));
            temp.add(new ExcelExportEntity("性别", "sex"));
            excelentity.setList(temp);
            entity.add(excelentity);

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Map<String, Object> map;
            for (int i = 0; i < 10; i++) {
                map = new HashMap<String, Object>();
                map.put("name", "1" + i);
                map.put("sex", "2" + i);

                List<Map<String, Object>> tempList = new ArrayList<Map<String, Object>>();
                tempList.add(map);
                tempList.add(map);
                map.put("students", tempList);

                list.add(map);
            }

            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("测试", "测试"), entity,
                list);
            FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportForMap.tt.xls");
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 合并同类项
     */
    @Test
    public void testMerge() {
        try {
            List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
            ExcelExportEntity excelentity = new ExcelExportEntity("部门", "depart");
            excelentity.setMergeVertical(true);
            entity.add(excelentity);
            excelentity = new ExcelExportEntity("姓名", "name");
            excelentity.setMergeVertical(true);
            excelentity.setMergeRely(new int[]{0});
            entity.add(excelentity);
            excelentity = new ExcelExportEntity("电话", "phone");
            excelentity.setMergeVertical(true);
            excelentity.setMergeRely(new int[] { 1 });
            entity.add(excelentity);

            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
            Map<String, Object> map;
            for (int i = 0; i < 10; i++) {
                map = new HashMap<String, Object>();
                map.put("depart", "设计部");
                map.put("name", "小明" + i / 3);
                map.put("phone", "1311234567" + i / 2);
                list.add(map);
            }
            for (int i = 0; i < 10; i++) {
                map = new HashMap<String, Object>();
                map.put("depart", "开发部");
                map.put("name", "小蓝" + i / 3);
                map.put("phone", "1871234567" + i / 2);
                list.add(map);
            }

            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams("员工通讯录", "通讯录"),
                entity, list);
            FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportForMap.testMerge.xls");
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testMany() {
        try {
            List<ExcelExportEntity> entity = new ArrayList<ExcelExportEntity>();
            for (int i = 0; i < 500; i++) {
                entity.add(new ExcelExportEntity("姓名" + i, "name" + i));
            }

            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            Map<String, String> map;
            for (int i = 0; i < 10; i++) {
                map = new HashMap<String, String>();
                for (int j = 0; j < 500; j++) {
                    map.put("name" + j, j + "_" + i);
                }
                list.add(map);
            }
            ExportParams params = new ExportParams("测试", "测试", ExcelType.XSSF);
            params.setFreezeCol(5);
            Workbook workbook = ExcelExportUtil.exportExcel(params, entity, list);
            FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportForMap.testMany.xlsx");
            workbook.write(fos);
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
