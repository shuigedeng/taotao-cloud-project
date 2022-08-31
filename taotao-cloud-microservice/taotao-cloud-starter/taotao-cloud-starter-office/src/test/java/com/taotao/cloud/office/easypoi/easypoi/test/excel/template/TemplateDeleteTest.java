package com.taotao.cloud.office.easypoi.easypoi.test.excel.template;

import com.google.common.collect.Lists;

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

/**
 * 删除列,样式确认
 *
 * @author JueYue on 2017/11/17.
 */
public class TemplateDeleteTest {

    @Test
    public void test() throws Exception {
        TemplateExportParams params = new TemplateExportParams(
                "doc/delete-test.xls");
        params.setHeadingStartRow(3);
        params.setHeadingRows(2);
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("containerTotalSize", 200);
        map.put("truckFee", 2000000.00);
        map.put("feeSumTotal", 2000000.00);
        map.put("buyerName", "执笔潜行科技有限公司");
        List<Map<String, Object>> mapList = new ArrayList<Map<String, Object>>();
        for (int i = 1; i <= 50; i++) {
            map.put("feeDisplay" + i, i % 2 == 0);
            map.put("feeSum" + i, i);
            map.put("fee" + i, i);
            Map<String, Object> temp = new HashMap<String, Object>();
            mapList.add(temp);
            temp.put("seqNo", i);
            temp.put("businessNo", i);
            temp.put("number", i);
            temp.put("planArrivalTime", "2017-02-02");
            temp.put("doorAddress", "山东曲阜孔子家");

        }
        map.put("mapList", mapList);
        Workbook workbook = ExcelExportUtil.exportExcel(params, map);
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/delete-test-gen.xls");
        workbook.write(fos);
        fos.close();
    }
}
