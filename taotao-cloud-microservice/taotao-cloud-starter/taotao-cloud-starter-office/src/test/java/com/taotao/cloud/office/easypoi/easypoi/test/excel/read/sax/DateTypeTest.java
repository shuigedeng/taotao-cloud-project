package com.taotao.cloud.office.easypoi.easypoi.test.excel.read.sax;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.afterturn.easypoi.handler.inter.IReadHandler;
import cn.afterturn.easypoi.test.excel.read.FileUtilTest;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Map;

/**
 * @author by jueyue on 19-6-21.
 */
public class DateTypeTest {

    @Test
    public void test() {
        try {
            ImportParams params = new ImportParams();
            params.setTitleRows(1);
            params.setStartSheetIndex(1);
            long start = new Date().getTime();
            ExcelImportUtil.importExcelBySax(
                    new FileInputStream(
                            new File(FileUtilTest.getWebRootPath("import/saxtest.xlsx"))),
                    Map.class, params, new IReadHandler<Map>() {
                        @Override
                        public void handler(Map o) {
                            System.out.println(o);
                        }

                        @Override
                        public void doAfterAll() {
                            System.out.println("全部执行完毕了--------------------------------");
                        }
                    });
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
