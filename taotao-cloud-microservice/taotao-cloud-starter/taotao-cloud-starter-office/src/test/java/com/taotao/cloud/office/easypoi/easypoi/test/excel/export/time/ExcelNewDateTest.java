package com.taotao.cloud.office.easypoi.easypoi.test.excel.export.time;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import cn.afterturn.easypoi.test.entity.NewDateEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author by jueyue on 19-6-23.
 */
public class ExcelNewDateTest {

    @Test
    public void test() throws Exception {

        List<NewDateEntity> list = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            NewDateEntity newDate = new NewDateEntity();
            newDate.setInstant(Instant.now().plus(i, ChronoUnit.DAYS));
            newDate.setLocalDate(LocalDate.now().plusWeeks(i));
            newDate.setLocalDateTime(LocalDateTime.now().plusMonths(i));
            newDate.setZoneId(ZoneId.systemDefault());
            list.add(newDate);
        }
        Date         start  = new Date();
        ExportParams params = new ExportParams("JKD8时间测试", "测试", ExcelType.XSSF);
        params.setFreezeCol(2);
        Workbook workbook = ExcelExportUtil.exportExcel(params, NewDateEntity.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/ExcelNewDateTest.test.xlsx");
        workbook.write(fos);
        fos.close();
    }
}
