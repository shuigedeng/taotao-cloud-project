package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import cn.afterturn.easypoi.test.entity.temp.TemplateExcelExportEntity;
import cn.afterturn.easypoi.util.PoiMergeCellUtil;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by jueyue on 18-7-17.
 */
public class ForEachOfInsert {

    @Test
    public void test() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
                "doc/foreach.xlsx",1);
        Map<String, Object> map = new HashMap<String, Object>();
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < 4; i++) {
            Map<String, Object> testMap = new HashMap<String, Object>();

            testMap.put("id", "xman");
            testMap.put("name", "小明" + i);
            testMap.put("sex", "1");
            mapList.add(testMap);
        }
        map.put("maplist", mapList);
        //本来导出是专业那个
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ForEachOfInsert.foreach.xlsx");
        workbook.write(fos);
        fos.close();
    }
}
