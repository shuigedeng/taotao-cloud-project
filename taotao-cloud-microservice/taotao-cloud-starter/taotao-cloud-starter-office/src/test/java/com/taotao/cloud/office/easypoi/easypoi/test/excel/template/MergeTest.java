package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
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
 * @author jueyue on 20-4-6.
 */
public class MergeTest {

    /**
     * 插入含有合并单元格的问题
     */
    @Test
    public void test() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
                "doc/foreach_insert_merge.xlsx");
        Map<String, Object> map  = new HashMap();
        List<Map>           list = new ArrayList<>();
        Map aa = new HashMap() {
            {
                put("id", "id" + 0);
                put("name", "name" + 0);
                put("age", "age" + 0);
                put("sex", "sex" + 0);
            }
        };
        for (int i = 0; i < 100000; i++) {
            final int index = i;
            list.add(aa);
        }
        map.put("nums", "你是一只小小鸟");
        map.put("list1", list);
        map.put("list2", list);
        //本来导出是专业那个
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        PoiMergeCellUtil.mergeCells(workbook.getSheetAt(0), 1, 0, 4);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/MergeTest.test.xlsx");
        workbook.write(fos);
        fos.close();
    }
}
