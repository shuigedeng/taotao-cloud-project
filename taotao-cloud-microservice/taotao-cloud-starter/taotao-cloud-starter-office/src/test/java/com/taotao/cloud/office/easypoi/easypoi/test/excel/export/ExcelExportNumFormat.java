package com.taotao.cloud.office.easypoi.easypoi.test.excel.export;

import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.test.entity.temp.NumEntity;

/**
 * Created by Think on 2017/6/17.
 */
public class ExcelExportNumFormat {

    @Test
    public void test() throws Exception {

        List<NumEntity> list = new ArrayList<NumEntity>();
        for (int i = 0; i < 20; i++) {
            NumEntity client = new NumEntity();
            client.setDouTest(i % 3 == 0 ? i + 0.0D : null);
            client.setIntTest((int) (Math.random() * 1000000000));
            client.setLongTest((long) (Math.random() * 100000000000000000L));
            client.setStrTest(Math.random() * 100000000000000000D + "");
            list.add(client);
        }
        Date start = new Date();
        ExportParams params = new ExportParams("2412312", "测试", ExcelType.XSSF);
        Workbook workbook = ExcelExportUtil.exportExcel(params, NumEntity.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelExportNumFormat.xlsx");
        workbook.write(fos);
        fos.close();
    }
}
