package com.taotao.cloud.office.easypoi.easypoi.test.excel.export.desensitization;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.test.entity.temp.DesensitizationEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 脱敏数据测试
 *
 * @author jueyue on 21-2-8.
 */
public class ExcelDesensitizationTest {


    @Test
    public void test() throws Exception {

        List<DesensitizationEntity> list = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            DesensitizationEntity entity = new DesensitizationEntity();
            entity.setCard("37010119900101123" + i % 10);
            entity.setName("张三");
            entity.setPhone("1311234567" + i % 10);
            entity.setEmail(i % 10 + "ttttt@afterturn.com");
            list.add(entity);
        }
        Date         start    = new Date();
        ExportParams params   = new ExportParams("脱敏测试", "脱敏测试", ExcelType.XSSF);
        Workbook     workbook = ExcelExportUtil.exportExcel(params, DesensitizationEntity.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelDesensitizationTest.xlsx");
        workbook.write(fos);
        fos.close();
    }
}
