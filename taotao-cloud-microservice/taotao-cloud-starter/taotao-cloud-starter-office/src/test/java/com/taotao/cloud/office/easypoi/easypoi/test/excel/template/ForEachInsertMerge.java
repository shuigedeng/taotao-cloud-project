package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.*;

/**
 * 插入合并这块
 * @author jueyue on 20-4-9.
 */
public class ForEachInsertMerge {

    @Test
    public void sendGoods() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
                "doc/foreachInsertMerge.goods_send.xlsx");
        Map<String, Object>       map     = new HashMap<>();
        map.put("nowTime",new Date());
        map.put("unitName","悟耘信息");
        map.put("order",new Date().getTime());
        List<Map<String, Object>> mapList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Map<String, Object> testMap = new HashMap<>();
            testMap.put("name", "小明" + i);
            testMap.put("nums",i);
            testMap.put("type","食品");
            testMap.put("remark","甜食");
            mapList.add(testMap);
        }
        map.put("list", mapList);
        //本来导出是专业那个
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File     savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ForEachInsertMerge.sendGoods.xlsx");
        workbook.write(fos);
        fos.close();
    }
}
