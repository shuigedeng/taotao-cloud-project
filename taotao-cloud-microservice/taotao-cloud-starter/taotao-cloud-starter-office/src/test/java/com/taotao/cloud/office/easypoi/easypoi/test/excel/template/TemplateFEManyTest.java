package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.util.PoiMergeCellUtil;

/**
 * Created by JueYue on 2017/8/25.
 */
public class TemplateFEManyTest {


    @Test
    public void test()  throws Exception {
        List<Map<String, String>> onelist = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 100; i++) {
            onelist.add(getMap("第一组", i));
        }

        List<Map<String, String>> twolist = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 1; i++) {
            twolist.add(getMap("第二组", i));
        }
        List<Map<String, String>> threelist = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 1; i++) {
            threelist.add(getMap("第三组", i));
        }
        List<Map<String, String>> fourlist = new ArrayList<Map<String, String>>();
        for (int i = 0; i < 1; i++) {
            fourlist.add(getMap("第四组", i));
        }
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("onelist", onelist);
        map.put("twolist", twolist);
        map.put("threelist", threelist);
        map.put("fourlist", fourlist);

        TemplateExportParams params = new TemplateExportParams(
                "doc/foreach_insert_many.xlsx");
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        PoiMergeCellUtil.mergeCells(workbook.getSheetAt(0), 1, 0, 4);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/TemplateFEManyTest_test.xlsx");
        workbook.write(fos);
        fos.close();
    }

    private Map<String, String> getMap(String name, int i) {

        Map<String, String> map = new HashMap<String, String>();
        map.put("name", name + i);
        map.put("sex", "猿人");
        map.put("addr", "中国孔子家附近");
        map.put("email", "jn@email.com");
        return map;
    }
}
