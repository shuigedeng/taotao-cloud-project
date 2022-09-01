package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.excel.entity.params.ExcelExportEntity;
import cn.afterturn.easypoi.excel.export.ExcelExportService;
import cn.afterturn.easypoi.excel.export.template.ExcelExportOfTemplateUtil;

public class TemplateExcelSheetTest {

    @Test
    public void test() throws Exception {
        ExcelExportService util = new ExcelExportService();
        Workbook workbook = new HSSFWorkbook();
        ExportParams entity = new ExportParams();
        entity.setCreateHeadRows(false);
        entity.setStyle(ManySheetOneSyler.class);
        List<ExcelExportEntity> entityList = new ArrayList<ExcelExportEntity>();
        ExcelExportEntity e = new ExcelExportEntity();
        e.setHeight(40);
        e.setWidth(40);
        e.setWrap(true);
        e.setName("one");
        e.setKey("one");
        entityList.add(e);
        e = new ExcelExportEntity();
        e.setHeight(40);
        e.setWidth(40);
        e.setWrap(true);
        e.setName("two");
        e.setKey("two");
        entityList.add(e);
        e = new ExcelExportEntity();
        e.setHeight(40);
        e.setWidth(40);
        e.setWrap(true);
        e.setName("three");
        e.setKey("three");
        entityList.add(e);
        e = new ExcelExportEntity();
        e.setHeight(40);
        e.setWidth(40);
        e.setWrap(true);
        e.setName("four");
        e.setKey("four");
        entityList.add(e);
        e = new ExcelExportEntity();
        e.setHeight(40);
        e.setWidth(40);
        e.setWrap(true);
        e.setName("five");
        e.setKey("five");
        entityList.add(e);
        e = new ExcelExportEntity();
        e.setHeight(40);
        e.setWidth(40);
        e.setWrap(true);
        e.setName("six");
        e.setKey("six");
        entityList.add(e);
        List<String> list = new ArrayList<String>();
        for (int i = 0; i < 80; i++) {
            list.add("卡片编号：2228\n资产名称：办公器\n开始使用日期：20090910\n使用状况：在用\n使用科室：财务科\n管理科室：总务科\n市妇幼2015年6月");
        }

        //拼装成6个
        List<Map<String, Object>> dataList = new ArrayList<Map<String, Object>>();
        for (int l = 0; l < list.size(); l += 6) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("one", l + 0 >= list.size() ? "" : list.get(l + 0));
            map.put("two", l + 1 >= list.size() ? "" : list.get(l + 1));
            map.put("three", l + 2 >= list.size() ? "" : list.get(l + 2));
            map.put("four", l + 3 >= list.size() ? "" : list.get(l + 3));
            map.put("five", l + 4 >= list.size() ? "" : list.get(l + 4));
            map.put("six", l + 5 >= list.size() ? "" : list.get(l + 5));
            dataList.add(map);
        }

        //补全数据好看一点
        for (int l = 0, le = (dataList.size() % 5) == 0 ? 0 : 5 - dataList.size() % 5; l < le; l++) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("one", "");
            map.put("two", "");
            map.put("three", "");
            map.put("five", "");
            map.put("four", "");
            dataList.add(map);
        }

        for (int i = 0, le = dataList.size() / 5; i < le; i++) {
            util.createSheetForMap(workbook, entity, entityList, dataList.subList(0, 5));
        }
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/foreach.xls");
        workbook.write(fos);
        fos.close();
    }

    public void createSheetForMap(Workbook workbook, ExportParams entity,
                                  List<ExcelExportEntity> entityList,
                                  Collection<? extends Map<?, ?>> dataSet) {

    }

}
