package com.taotao.cloud.office.easypoi.easypoi.i18n;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.taotao.cloud.office.easypoi.test.entity.temp.NumEntity;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 国际化 注解里面的修改应用
 */
public class AnnI18nTest {

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
        params.setI18nHandler(name -> {
            switch (name) {
                case "str":
                    // 这里可以环境选择对应的数据
                    return "字符串";
            }
            return name;
        });
        Workbook workbook = ExcelExportUtil.exportExcel(params, NumEntity.class, list);
        System.out.println(new Date().getTime() - start.getTime());
        File savefile = new File("D:/home/excel/");
        if (!savefile.exists()) {
            savefile.mkdirs();
        }
        FileOutputStream fos = new FileOutputStream("D:/home/excel/AnnI18nTest.test.xlsx");
        workbook.write(fos);
        fos.close();
    }
}
