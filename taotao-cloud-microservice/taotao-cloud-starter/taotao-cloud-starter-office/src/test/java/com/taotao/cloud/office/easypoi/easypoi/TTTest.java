package com.taotao.cloud.office.easypoi.easypoi;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.TemplateExportParams;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 专治疑难杂症
 *
 * @author by jueyue on 19-10-16.
 */
public class TTTest {

    private static final String DESKTOP = "C:\\Users\\jueyue\\Desktop\\";

    @Test
    public void img03() throws Exception {

        try {
            TemplateExportParams params = new TemplateExportParams(
                    DESKTOP + "hc.xlsx");
            List<Map>           list = new ArrayList<>();
            Map<String, Object> data = new HashMap<>();
            data.put("base", 1);
            data.put("one", 1);
            data.put("two", 2);
            list.add(data);
            list.add(data);
            list.add(data);
            list.add(data);
            list.add(data);
            list.add(data);
            Map map = new HashMap();
            map.put("entitylist", list);
            Workbook         workbook = ExcelExportUtil.exportExcel(params, map);
            FileOutputStream fos      = new FileOutputStream(DESKTOP + "hc12.xlsx");
            workbook.write(fos);
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
